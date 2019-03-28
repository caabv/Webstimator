package webtestimator.modules;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import webtestimator.model.enums.CMS;

/**
 * Module designed to find cms on a page.
 */
public class CMSFinder {

    private Document doc;
    private Document robots;

    public CMSFinder(Document doc, Document robots) {
        this.doc = doc;
        this.robots = robots;
    }

    /**
     * Method that tries to identify a cms from meta tags in the head of the html of a site.
     * @return a String with the name of the found cms, or null if none is found.
     * */
    public String getByMetaTag() {
        // Try to find cms with meta tag
        Elements metas = doc.select("meta[name]");
        for(Element e : metas) {
            if(e.attr("name").equals("generator")) {
                for(CMS c : CMS.values()) {
                    if(e.attr("content").toUpperCase().contains(c.name().toUpperCase())){
                        System.out.println("Setting cms to (from meta tag): " + e.attr("content"));
                        return e.attr("content");
                    }
                }
            }
        }
        return null;
    }

    /**
     * Method that tries to identify a cms from the robots.txt page of a site.
     * @return a String with the name of the found cms, or null if none is found.
     * */
    public String getByRobots() {
        for(Node node : robots.childNodes()) {

            for (CMS c : CMS.values()) {
                if(node.toString().toUpperCase().contains(c.name().toUpperCase())) {
                    System.out.println("Found cms from robot.txt: " + c.name()
                    );
                    return c.name();
                }
            }
        }
        return null;
    }

    /**
     * Goes through all html, and checks if names of any known cms is found.
     * Currently not being used, as it is too inconsistent.
     * @return a String with the name of the found cms, or null if none is found.
     * */
    public String getByHtml() {

        for(Node node : doc.childNodes()) {

                for (CMS c : CMS.values()) {
                    if(node.toString().toUpperCase().contains(c.name().toUpperCase())) {
                        System.out.println("Found cms from html: " + c.name()
                        );
                        return c.name();
                    }
                }
        }
        return null;
    }
}
