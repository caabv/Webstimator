package webtestimator.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.*;
import java.util.Properties;

/**
 * Controller for the settings view of the application
 */
public class SettingsLayoutController {

    private MainLayoutController mainController;
    private Properties prop;

    /* CMS */
    @FXML
    private TextField txtFoundCMS;

    @FXML
    private TextField txtNoCMS;

    @FXML
    private Button btnSaveCMSSettings;

    /* Functionality */
    @FXML
    private TextField txtHasLogin;

    @FXML
    private TextField txtNoLogin;

    @FXML
    private TextField txtFewFuncAmount;

    @FXML
    private TextField txtMediumFuncAmount;

    @FXML
    private TextField txtManyFuncAmount;

    @FXML
    private TextField txtFewFuncWeight;

    @FXML
    private TextField txtMediumFuncWeight;

    @FXML
    private TextField txtManyFuncWeight;

    @FXML
    private Button btnSaveFuncSettings;

    /* Size */
    @FXML
    private TextField txtSmallSizeAmount;

    @FXML
    private TextField txtMediumSizeAmount;

    @FXML
    private TextField txtLargeSizeAmount;

    @FXML
    private TextField txtSmallSizeWeight;

    @FXML
    private TextField txtMediumSizeWeight;

    @FXML
    private TextField txtLargeSizeWeight;

    @FXML
    private Button btnSaveSizeSettings;

    /* Platform */
    @FXML
    private TextField txtKnownPlatform;

    @FXML
    private TextField txtUnknownPlatform;

    @FXML
    private Button btnSavePlatformSettings;

    /**
     * Initialization method
     * @param mainController The MainLayoutController currently running.
     * */
    public void init(MainLayoutController mainController) {
        this.mainController = mainController;

        // create a Properties object and read the config file to it
        prop = new Properties();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("configs/config.properties");

        try {
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("No config found");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if(prop == null) {
            // TODO: do some proper error handling
            System.out.println("Failed to load config");
        } else {
            // fill out settings gui from config
            txtFoundCMS.setText(prop.getProperty("FoundCMS"));
            txtNoCMS.setText(prop.getProperty("NoCMS"));

            txtHasLogin.setText(prop.getProperty("HasLogin"));
            txtNoLogin.setText(prop.getProperty("NoLogin"));

            txtFewFuncAmount.setText(prop.getProperty("FewFuncAmount"));
            txtMediumFuncAmount.setText(prop.getProperty("MediumFuncAmount"));
            txtManyFuncAmount.setText(prop.getProperty("ManyFuncAmount"));

            txtFewFuncWeight.setText(prop.getProperty("FewFuncWeight"));
            txtMediumFuncWeight.setText(prop.getProperty("MediumFuncWeight"));
            txtManyFuncWeight.setText(prop.getProperty("ManyFuncWeight"));

            txtSmallSizeAmount.setText(prop.getProperty("SmallSizeAmount"));
            txtMediumSizeAmount.setText(prop.getProperty("MediumSizeAmount"));
            txtLargeSizeAmount.setText(prop.getProperty("LargeSizeAmount"));

            txtSmallSizeWeight.setText(prop.getProperty("SmallSizeWeight"));
            txtMediumSizeWeight.setText(prop.getProperty("MediumSizeWeight"));
            txtLargeSizeWeight.setText(prop.getProperty("LargeSizeWeight"));

            txtKnownPlatform.setText(prop.getProperty("KnownPlatform"));
            txtUnknownPlatform.setText(prop.getProperty("UnknownPlatform"));
        }
    }

    /**
     * Handles event when save button is clicked. Validates all values, saves them if they are valid.
     * */
    @FXML
    private void saveSettings() {
        //TODO make settings window close when saving

        if(validateWeight(txtFoundCMS.getText())){
            prop.setProperty("FoundCMS",txtFoundCMS.getText());
        }

        if(validateWeight(txtNoCMS.getText())) {
            prop.setProperty("NoCMS", txtNoCMS.getText());
        }

        if(validateWeight(txtHasLogin.getText())) {
            prop.setProperty("HasLogin", txtHasLogin.getText());
        }

        if(validateWeight(txtNoLogin.getText())) {
            prop.setProperty("NoLogin", txtNoLogin.getText());
        }

        if(validateAmount(txtFewFuncAmount.getText(),txtMediumFuncAmount.getText(),txtManyFuncAmount.getText())) {
            prop.setProperty("FewFuncAmount", txtFewFuncAmount.getText());
            prop.setProperty("MediumFuncAmount", txtMediumFuncAmount.getText());
            prop.setProperty("ManyFuncAmount", txtManyFuncAmount.getText());
        }

        if(validateWeight(txtFewFuncWeight.getText())){
            prop.setProperty("FewFuncWeight", txtFewFuncWeight.getText());
        }

        if(validateWeight(txtMediumFuncWeight.getText())) {
            prop.setProperty("MediumFuncWeight", txtMediumFuncWeight.getText());
        }

        if(validateWeight(txtManyFuncWeight.getText())) {
            prop.setProperty("ManyFuncWeight", txtManyFuncWeight.getText());
        }

        if(validateAmount(txtSmallSizeAmount.getText(), txtMediumSizeAmount.getText(), txtLargeSizeAmount.getText())) {
            prop.setProperty("SmallSizeAmount", txtSmallSizeAmount.getText());
            prop.setProperty("MediumSizeAmount", txtMediumSizeAmount.getText());
            prop.setProperty("LargeSizeAmount", txtLargeSizeAmount.getText());
        }

        if(validateWeight(txtSmallSizeWeight.getText())) {
            prop.setProperty("SmallSizeWeight", txtSmallSizeWeight.getText());
        }

        if(validateWeight(txtMediumSizeWeight.getText())) {
            prop.setProperty("MediumSizeWeight", txtMediumSizeWeight.getText());
        }

        if(validateWeight(txtLargeSizeWeight.getText())) {
            prop.setProperty("LargeSizeWeight", txtLargeSizeWeight.getText());
        }

        if(validateWeight(txtKnownPlatform.getText())) {
            prop.setProperty("KnownPlatform", txtKnownPlatform.getText());
        }

        if(validateWeight(txtUnknownPlatform.getText())) {
            prop.setProperty("UnknownPlatform", txtUnknownPlatform.getText());
        }

        try {
            prop.store(new FileOutputStream("resources/config.properties"), null);
            mainController.getPageCrawler().updatedConfig();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Handles event when cancel button is clicked.
     * */
    @FXML
    private void cancelSettings() {
        mainController.closeSettingsWindow();
    }

    /**
     * Method for validating weight variables.
     * @param weight The weight to be validated. Should be between 0.0 and 2.0
     * @return true or false depending on validity
     * */
    private boolean validateWeight(String weight) {
        try {
            Double number = Double.parseDouble(weight);
            if(number > 0 && number < 2.00) {
                return true;
            }
            else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Method for validating amount variables. They always come in threes, the lower boundary, the medium size boundary and the large size boundary
     * @param small The lower boundary value
     * @param medium The medium boundary value
     * @param large The high boundary value
     * @return true or false depending on validity
     * */
    private boolean validateAmount(String small, String medium, String large) {
        try {
            int smallInt = Integer.parseInt(small);
            int mediumInt = Integer.parseInt(medium);
            int largeInt = Integer.parseInt(large);
            if(largeInt > mediumInt && mediumInt > smallInt && smallInt > 0) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
