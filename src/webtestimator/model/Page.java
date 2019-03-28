package webtestimator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Page class. Is used as data storage for each actual page on a site. Contains a list of
 * all the pages that are linked to from the page itself, as well as the URL to it.
 */
public class Page {
    private String url;
    private List<Page> pageList;

    public Page(String url){
        this.url = url;
        pageList = new ArrayList<Page>();
    }

    public void addPage(Page page) {
        pageList.add(page);
    }

    public List getPageList(){
        return pageList;
    }

    public String getUrl(){
        return url;
    }
}
