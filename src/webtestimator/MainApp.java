package webtestimator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import webtestimator.view.MainLayoutController;

import java.io.IOException;

/**
 * Class that starts the application.
 */
public class MainApp extends Application{
    private Stage primaryStage;
    private BorderPane rootLayout;
    private MainLayoutController controller;


    /**
     * Starts the application
     * */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Webstimator");

        initRootLayout();
        showMainLayout();
    }

    /**
     * Initializes root layout
     * */
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane)loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the main layout
     * */
    public void showMainLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/MainLayout.fxml"));
            AnchorPane mainLayout = (AnchorPane) loader.load();

            rootLayout.setCenter(mainLayout);

            controller = loader.getController();
            controller.init(this);
        }
        catch(IOException e) {

        }
    }


    // This is bugged the fuck out, find another solution
    @Override
    public void stop(){
        controller.getPageCrawler().stopCrawl();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
