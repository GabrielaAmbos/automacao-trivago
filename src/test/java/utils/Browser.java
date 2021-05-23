package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.edge.*;
import org.openqa.selenium.firefox.*;

import java.util.concurrent.TimeUnit;

public class Browser {
    private static WebDriver driver;
    private static Type currentType;
    private static Boolean isHeadless = false;
    private static String userDir = System.getProperty("user.dir");

    public enum Type {
        FIREFOX,
        CHROME,
        EDGE;
    }

    public static void setCurrentBrowser(Type type, boolean enableHeadless) {
        currentType = type;
        isHeadless = enableHeadless;
    }

    public static Type getCurrentBrowser()
    {
        return currentType;
    }


    public static WebDriver getCurrentDriver() {
        if(driver == null) {
            try {
                switch (getCurrentBrowser()) {
                    case CHROME:
                        ChromeOptions chromeOptions = new ChromeOptions();
                        if(isHeadless) chromeOptions.addArguments("--headless");
                        chromeOptions.setCapability("download.prompt_for_download", false);
                        chromeOptions.setCapability("disable-popup-blocking", "true");
                        System.setProperty("webdriver.chrome.driver", userDir+"\\chromedriver.exe");
                        driver = new ChromeDriver(chromeOptions);
                        break;
                    case EDGE:
                        EdgeOptions edgeOptions = new EdgeOptions();
                        if(isHeadless) edgeOptions.addArguments("headless");
                        driver = new EdgeDriver(edgeOptions);
                        break;
                    case FIREFOX:
                        FirefoxOptions firefoxOptions = new FirefoxOptions();
                        if(isHeadless) firefoxOptions.addArguments("--headless");
                        System.setProperty("webdriver.gecko.driver", userDir+"\\geckodriver.exe");
                        firefoxOptions.setCapability("browser.download.folderList", 2);
                        firefoxOptions.setCapability("browser.download.useDownloadDir", true);
                        firefoxOptions.setCapability("browser.download.viewableInternally.enabledTypes", "");
                        firefoxOptions.setCapability("browser.helperApps.neverAsk.saveToDisk", "application/json;application/pdf;text/plain;application/text;text/xml;application/xml;application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        firefoxOptions.setCapability("pdfjs.disabled", true);
                        driver = new FirefoxDriver(firefoxOptions);
                        break;
                }
                driver.manage().window().maximize();
                driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
                driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return driver;
    }

    public static void loadApplication(String url) {
        getCurrentDriver().get(url);
    }

    public static void quitBrowser() {
        getCurrentDriver().quit();
        driver = null;
    }
}
