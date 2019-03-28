package webtestimator.modules;

import javafx.application.Platform;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import webtestimator.model.Page;
import webtestimator.modules.CMSFinder;
import webtestimator.modules.ComplexityManager;
import webtestimator.modules.FunctionalityFinder;
import webtestimator.view.MainLayoutController;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Page crawler class. Contains all the logic for traversing a web site and gathering pages.
 * The class implements Runnable, and should be used as an additional thread.
 */
public class PageCrawler implements Runnable {

    private MainLayoutController controller;
    private FunctionalityFinder functionalityFinder;
    private ComplexityManager complexityManager;

    private String URL;
    private Set<String> visitedPages;
    private int count;
    private String baseDomain;
    private boolean stopCrawl;
    private int maxDepth;
    private int currentDepth;
    private String cms;
    private String server;
    private Document robots;

    // for page tree printing
    private int indent = 0;
    private volatile Page firstPage;

    /**
     * Constructor for PageCrawler module.
     * @param url the URL to start crawling from
     * @param controller the MainLayoutController
     * @param maxDepth the maximum depth that the crawler can go to
     * */
    public PageCrawler(String url, MainLayoutController controller, int maxDepth){
        this.controller = controller;
        this.functionalityFinder = new FunctionalityFinder();
        this.complexityManager = new ComplexityManager();
        this.URL = url;
        this.stopCrawl = false;
        this.maxDepth = maxDepth;
        this.robots = null;
        this.server = null;

        visitedPages = new HashSet<String>();
        count = 1;
        currentDepth = 0;
    }

    /**
     * Recursive method, for crawling through pages. An initial Page object is passed into the method
     * The method then uses jsoup to make a http GET request to the URL of the initial page object.
     * New page objects are made for each link found on the page, and operation is continued recursively.
     * @param  page The initial page, containing root url to crawl from.
     * @return      The root Page object. Contains all other pages as a graph.
     * */
    public Page crawl(Page page) {
        try {
            if(stopCrawl) {
                return null;
            }
            // Do these things on the first page of the crawling
            if(count == 1) {
                Connection con = Jsoup.connect(page.getUrl()).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com");
                Connection.Response res = con.execute();

                // Find server from head from the http response
                server = res.header("Server");

                Document doc = con.get();
                // Run this if this is the base domain to set the base domain URL.
                baseDomain = res.url().toString();

                try {
                    Connection robotCon = Jsoup.connect(page.getUrl() + "/robots.txt").userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                            .referrer("http://www.google.com");
                    robots = robotCon.get();
                } catch (HttpStatusException e) {

                }

                // Use the cmsfinder to try to identify cms
                CMSFinder cmsFinder = new CMSFinder(doc, robots);
                cms = cmsFinder.getByMetaTag();
                if(cms == null && robots != null) {
                    cms = cmsFinder.getByRobots();
                }
//                if(cms == null) {
//                    cms = cmsFinder.getByHtml();
//                }

            }
            // connect to url with user agent and referrer set to avoid detection of crawler
            Document doc = Jsoup.connect(page.getUrl()).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com").get();

            // Find functionality on page
            functionalityFinder.findFunctionality(doc);


            // Select all anchor tags on the html response
            Elements links = doc.select("a[href]");

            // loop through al the links on the page
            for(Element e : links) {
                String link = e.attr("abs:href");
                if(isValidURL(link) && maxDepth > currentDepth && !visitedPages.contains(link) && link.contains(baseDomain)) {

                    visitedPages.add(link);
                    count++;

                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            controller.setCurrentPage(link);
                        }
                    });

                    Page newPage = new Page(link);
                    currentDepth++;
                    crawl(newPage);
                    currentDepth--;
                    page.addPage(newPage);
                }
            }
        } catch (IOException e) {
            if(e instanceof SocketTimeoutException){
                crawl(page);
            }
            else {
                e.printStackTrace();
            }
        }
        return page;
    }

    /**
     * Method for testing whether a given URI is valid to crawl
     * @param url The url to be validated
     * @return    Boolean value showing if url is valid
     * */
    private boolean isValidURL(String url) {

        if(url.endsWith(".pdf") || url.endsWith(".exe") || url.endsWith(".dmg") || url.endsWith(".png") ||
                url.endsWith(".jpg") || url.endsWith(".gif")){
            return false;
        }
        // check if hashtag anchor link
        if(url.contains("#")){
            String str = url.substring(0, url.indexOf("#"));
            if(visitedPages.contains(str)){
                return false;
            }
            else {
                return true;
            }
        }
        return true;
    }

    public int getCount(){
        return count;
    }

    public String getCMS() { return cms; }

    public String getServer() { return server; }

    /**
     * Outputs a visual representation of the page tree in the console window. Recursive method.
     * @param page the root Page object.
     * */
    public void printPageTree(Page page) {
        List<Page> pageList = page.getPageList();

        indent++;
        for(int i = 0; i < indent; i++) {
            System.out.print("  ");
        }
        System.out.print(page.getUrl() + "\n");
        for (Page p: pageList) {

            printPageTree(p);

        }
        indent--;
    }

    /**
     * Exports the page tree to a json document.
     * @param page the root Page object.
     * @param parentArr The parent array. Should be passed null on first call.
     * @return A JSONObject containing the tree.
     * */
    public JSONObject exportPageTreeToJSON(Page page, JSONArray parentArr) {
        List<Page> pageList = page.getPageList();

        JSONObject obj = new JSONObject();
        obj.put("URL", page.getUrl());

        JSONArray arr = new JSONArray();

        for (Page p : pageList) {
            exportPageTreeToJSON(p, arr);
        }
        if(parentArr != null) {
            parentArr.add(obj);
        }
        if(arr.size() != 0) {
            obj.put("pages", arr);
        }
        return obj;
    }

    /**
     * The run method is called after instantiating the class as a thread.
     * When invoked, it calls the crawl method. When crawling is done, the controller is notified,
     * So that it might clean up.
     * */
    @Override
    public void run() {
        firstPage = crawl(new Page(URL));
        boolean hasCMS = (cms != null);
        boolean foundServer = (server != null);
        Double hours = complexityManager.calculateComplexity(count, functionalityFinder.getTotalFunctionality(),
                hasCMS, functionalityFinder.getHasLogin(), foundServer);
        controller.doneCrawling(hours);
    }

    public void stopCrawl(){
        this.stopCrawl = true;
    }

    public FunctionalityFinder getFunctionalityFinder() {
        return functionalityFinder;
    }

    public void updatedConfig() {
        complexityManager.updatedConfig();
    }
}
