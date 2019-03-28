package webtestimator.modules;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * Module for finding specific functionality on a page
 */
public class FunctionalityFinder {
    private boolean hasLogin = false;
    private int searchFields = 0;
    private int loginFields = 0;
    private int fileUploads = 0;
    private int mailFields = 0;

    /**
     * Calls all the find methods to count all different functionality for the page.
     * @param doc The document object for the page to be checked.
     * */
    public void findFunctionality(Document doc) {
        Elements inputs = doc.select("input[type]");

        for(Element input : inputs) {
            findSearchFields(input);
            findLoginFields(input);
            findFileUploads(input);
            findMailFields(input);
        }
    }

    /**
     * This method checks an input field to check if it is a search field.
     * @param input The input element to be checked.
     * */
    private void findSearchFields(Element input) {
        if(input.attr("type").equals("search") || input.attr("name").toUpperCase().contains("SEARCH")
                || input.className().toUpperCase().contains("SEARCH")) {
            System.out.println("Found search field");
            searchFields++;
        }
    }

    /**
     * This method checks an input field to check if it is a login field.
     * @param input The input element to be checked.
     * */
    private void findLoginFields(Element input) {
        if(input.attr("type").equals("password")) {
            System.out.println("Found password field");
            loginFields++;
            hasLogin = true;
        }
    }

    /**
     * This method checks an input field to check if it is a file upload.
     * @param input The input element to be checked.
     * */
    private void findFileUploads(Element input) {
        if(input.attr("type").equals("file")) {
            System.out.println("Found file upload");
            fileUploads++;
        }
    }

    /**
     * This method checks an input field to check if it is a email field.
     * @param input The input element to be checked.
     * */
    private void findMailFields(Element input) {
        if(input.attr("type").equals("email") || input.attr("name").toUpperCase().contains("MAIL")
                || input.className().toUpperCase().contains("MAIL")) {
            System.out.println("Found mail field");
            mailFields++;
        }
    }

    public boolean getHasLogin() { return hasLogin; }

    public int getSearchFields() {
        return searchFields;
    }

    public int getLoginFields() {
        return loginFields;
    }

    public int getFileUploads() {
        return fileUploads;
    }

    public int getMailFields() {
        return mailFields;
    }

    public int getTotalFunctionality() {
        return searchFields + loginFields + fileUploads + mailFields;
    }
}
