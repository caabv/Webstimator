package webtestimator.modules;

import webtestimator.model.Page;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Module for finding the estimation for a certain page.
 */
public class ComplexityManager {

    //TODO temp variables, should be user-defined
    private int maxHours = 40;
    private double administrationTime = 1.1;

    private Properties prop;

    // vægtene skal lægges sammen
    private Double hasCMSWeight;
    private Double noCMSWeight;

    private Double hasPlatformWeight;
    private Double noPlatformWeight;

    private Double hasLoginWeight;
    private Double noLoginWeight;

    private int mediumSizeAmount;
    private int largeSizeAmount;

    private Double smallSizeWeight;
    private Double mediumSizeWeight;
    private Double largeSizeWeight;

    private int mediumFuncAmount;
    private int manyFuncAmount;

    private Double fewFuncWeight;
    private Double mediumFuncWeight;
    private Double manyFuncWeight;

    public ComplexityManager(){
        updatedConfig();
    }

    /**
     * The main method of the class. Calculates the estimated hours it takes to do a web test of a certain site.
     * It uses the weight that is specified in the config file to calculate the estimate
     *
     * @param size The amount of pages on a site as an integer.
     * @param functionality The amount of different vulnerable interfaces on a site
     * @param hasCMS A boolean value indicating whether a cms was found for the site
     * @param hasLogin A boolean value indicating whether a login field was found on the site
     * @param foundPlatform A boolean value indicating whether a platform (or server) was found for the site.
     * @return The amount of hours the web test of the page is estimated to as a Double.
     * */
    public Double calculateComplexity(int size, int functionality, boolean hasCMS, boolean hasLogin, boolean foundPlatform) {
        Double hours = 0.0;
        // size
        if(size < mediumSizeAmount) { hours += smallSizeWeight; }
        else if(size < largeSizeAmount) { hours += mediumSizeWeight; }
        else { hours += largeSizeWeight; }

        // functionality
        if(functionality < mediumFuncAmount) { hours += fewFuncWeight; }
        else if(functionality < manyFuncAmount) { hours += mediumFuncWeight; }
        else { hours += manyFuncWeight; }

        // CMS
        hours += (hasCMS) ? hasCMSWeight : noCMSWeight;

        // Login
        hours += (hasLogin) ? hasLoginWeight : noLoginWeight;

        // Found platform
        hours += (foundPlatform) ? hasPlatformWeight : noPlatformWeight;

        // Documentation time
        if(hours < 0.75) { hours += 0.5; }
        else if(hours < 1.75) { hours += 1; }
        else { hours += 2; }

        // administration time
        hours *= administrationTime;

        // Multiply all the added weights we have found with the scale constant to get the estimate in hours
        hours = hours * getScaleConstant();
        return hours;
    }

    /**
     * This method is called when changes have been made to the config. It updates local variables, so that
     * calculations will be up to date with newest settings.
     * */
    public void updatedConfig(){
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
            System.out.println("lol fail");
        } else {
            hasCMSWeight = Double.parseDouble(prop.getProperty("FoundCMS"));
            noCMSWeight = Double.parseDouble(prop.getProperty("NoCMS"));

            hasPlatformWeight = Double.parseDouble(prop.getProperty("KnownPlatform"));
            noPlatformWeight = Double.parseDouble(prop.getProperty("UnknownPlatform"));

            hasLoginWeight = Double.parseDouble(prop.getProperty("HasLogin"));
            noLoginWeight = Double.parseDouble(prop.getProperty("NoLogin"));

            mediumSizeAmount = Integer.parseInt(prop.getProperty("MediumSizeAmount"));
            largeSizeAmount = Integer.parseInt(prop.getProperty("LargeSizeAmount"));

            smallSizeWeight = Double.parseDouble(prop.getProperty("SmallSizeWeight"));
            mediumSizeWeight = Double.parseDouble(prop.getProperty("MediumSizeWeight"));
            largeSizeWeight = Double.parseDouble(prop.getProperty("LargeSizeWeight"));

            mediumFuncAmount = Integer.parseInt(prop.getProperty("MediumFuncAmount"));
            manyFuncAmount = Integer.parseInt(prop.getProperty("ManyFuncAmount"));

            fewFuncWeight = Double.parseDouble(prop.getProperty("FewFuncWeight"));
            mediumFuncWeight = Double.parseDouble(prop.getProperty("MediumFuncWeight"));
            manyFuncWeight = Double.parseDouble(prop.getProperty("ManyFuncWeight"));

        }
    }

    /**
     * Finds the scale constant, that the combined weight in the CalculateComplexity method is multiplied with to find amount of hours.
     * @return The scale constant as a Double
     * */
    public Double getScaleConstant(){
        Double maxWeight = noCMSWeight + noPlatformWeight + hasLoginWeight + largeSizeWeight + manyFuncWeight + 2;
        maxWeight *= 1.1;
        System.out.println("Max weight: " + maxWeight);
        System.out.println("Scale constant: " + 40/maxWeight);
        return 40/maxWeight;
    }
}
