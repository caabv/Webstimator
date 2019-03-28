package webtestimator.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import webtestimator.MainApp;
import webtestimator.modules.PageCrawler;

import java.io.IOException;

/**
 * Controller for the main view of the application
 */
public class MainLayoutController {

    private MainApp app;
    private PageCrawler pageCrawler;
    private Thread crawlerThread;
    private boolean isCrawling;

    private Stage settingsStage;

    @FXML
    private MenuItem btnSettings;
    @FXML
    private TextField urlField;
    @FXML
    private ComboBox<String> comboType;
    @FXML
    private TextField depthField;
    @FXML
    private Button btnEstimate;
    @FXML
    private CheckBox checkboxCMS;
    @FXML
    private CheckBox checkboxWebServer;
    @FXML
    private CheckBox checkboxDatabase;
    @FXML
    private CheckBox checkboxSize;
    @FXML
    private CheckBox checkboxComplexity;
    @FXML
    private CheckBox checkboxFunctionality;
    @FXML
    private CheckBox checkboxQuickFindings;
    @FXML
    private TextArea txtFieldResult;
    @FXML
    private Label lblCurrentPage;

    /**
     * Initialization method
     * @param app The MainApp Object that started the application
     * */
    public void init(MainApp app) {
        this.app = app;
        this.isCrawling = false;

        // initialize combobox
        comboType.getItems().addAll("Web Test", "Pentest");
        comboType.setValue("Web Test");

        // initialize result textarea
        txtFieldResult.setEditable(false);

        // initialize checkboxes
        checkboxCMS.setSelected(true);
        checkboxComplexity.setSelected(true);
        checkboxFunctionality.setSelected(true);
        checkboxQuickFindings.setSelected(true);
        checkboxSize.setSelected(true);
        checkboxWebServer.setSelected(true);
    }

    /**
     * Handles the event when clicking on estimate/about button.
     * */
    @FXML
    private void estimateClicked(){
        if(!isCrawling) {
            // validate url input
            int depth = 5;
            System.out.println(depthField.getText().equals(""));
            if(!(depthField.getText().equals(""))){
                depth = Integer.parseInt(depthField.getText());
            }
            String url = urlField.getText();
            if(!url.startsWith("http")) {
                url = "http://" + url;
            }

            // clear old display data
            txtFieldResult.setText("");

            // change button text to "abort"
            btnEstimate.setText("Abort");
            isCrawling = true;

            // crawl
            pageCrawler = new PageCrawler(url, this, depth);
            crawlerThread = new Thread(pageCrawler);
            crawlerThread.start();
        }
        else {
            pageCrawler.stopCrawl();
//            crawlerThread.interrupt();
            pageCrawler = null;
            isCrawling = false;
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    btnEstimate.setText("Estimate");
                    lblCurrentPage.setText("");
                }
            });
        }

    }

    /**
     * Handles event when crawling is done.
     * @param hours The hour amount to be shown in the gui
     * */
    public void doneCrawling(double hours){
        if(isCrawling) {
            if(checkboxSize.isSelected()) {
                txtFieldResult.setText("Size: " + Integer.toString(pageCrawler.getCount()) + " pages");
            }

            if(checkboxWebServer.isSelected()) {
                String server = pageCrawler.getServer();
                txtFieldResult.appendText((server != null) ? "\nServer: " + server : "\nServer: Unknown server");
            }

            if(checkboxCMS.isSelected()) {
                String cms = pageCrawler.getCMS();
                if(cms != null) {
                    txtFieldResult.appendText("\nCMS: " + cms);
                }
            }

            // Check if we found different functionality on pages
            if(checkboxFunctionality.isSelected()) {
                boolean foundFileUpload = pageCrawler.getFunctionalityFinder().getFileUploads() > 0;
                boolean foundMailField = pageCrawler.getFunctionalityFinder().getMailFields() > 0;
                boolean foundSearchField = pageCrawler.getFunctionalityFinder().getSearchFields() > 0;
                boolean foundLoginField = pageCrawler.getFunctionalityFinder().getLoginFields() > 0;

                if(foundFileUpload) {
                    txtFieldResult.appendText("\nFound file upload functionality");
                }
                if(foundMailField) {
                    txtFieldResult.appendText("\nFound mail input field");
                }
                if(foundSearchField) {
                    txtFieldResult.appendText("\nFound search field");
                }
                if(foundLoginField) {
                    txtFieldResult.appendText("\nFound login field");
                }
            }

            pageCrawler = null;

            Platform.runLater(new Runnable() {
                @Override public void run() {
                    btnEstimate.setText("Estimate");
                    lblCurrentPage.setText(Math.round(hours) + " hours");
                }
            });
            isCrawling = false;
        }
        else {
            pageCrawler = null;

            Platform.runLater(new Runnable() {
                @Override public void run() {
                    btnEstimate.setText("Estimate");
                    lblCurrentPage.setText("");
                }
            });
            isCrawling = false;
        }
    }

    /**
     * Handles the event when settings button is clicked. Opens a new window with the setting layout.
     * */
    @FXML
    private void openSettings() {
        System.out.println("Pressed settings");

        try {
            // load fxml & create new stage
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/SettingsLayout.fxml"));
            settingsStage = new Stage();
            settingsStage.setTitle("Settings");
            AnchorPane settingsLayout = (AnchorPane)loader.load();

            // create new scene, and display it on stage
            Scene scene = new Scene(settingsLayout);
            settingsStage.setScene(scene);
            SettingsLayoutController controller = loader.getController();
            controller.init(this);
            settingsStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PageCrawler getPageCrawler() {
        return pageCrawler;
    }

    public void setCurrentPage(String link) {
        lblCurrentPage.setText(link);
    }

    public void closeSettingsWindow() {
        settingsStage.close();
    }
}
