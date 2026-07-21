package utils;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.edge.*;
import org.openqa.selenium.firefox.*;

import java.time.Duration;

public class Browser {
    private static WebDriver driver;
    private static Type currentType;
    private static Boolean isHeadless = false;

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
                        if(isHeadless) {
                            chromeOptions.addArguments("--headless=new");
                            // garante layout desktop no headless (senao abre em resolucao pequena)
                            chromeOptions.addArguments("--window-size=1920,1080");
                        }
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
                        firefoxOptions.setCapability("browser.download.folderList", 2);
                        firefoxOptions.setCapability("browser.download.useDownloadDir", true);
                        firefoxOptions.setCapability("browser.download.viewableInternally.enabledTypes", "");
                        firefoxOptions.setCapability("browser.helperApps.neverAsk.saveToDisk", "application/json;application/pdf;text/plain;application/text;text/xml;application/xml;application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        firefoxOptions.setCapability("pdfjs.disabled", true);
                        driver = new FirefoxDriver(firefoxOptions);
                        break;
                }
                // Em headless o maximize() encolhe a janela e esconde a barra lateral de
                // filtros (layout mobile); por isso fixamos uma resolucao desktop.
                if (isHeadless) {
                    driver.manage().window().setSize(new Dimension(1920, 1080));
                } else {
                    driver.manage().window().maximize();
                }
                // Sem implicit wait: os Page Objects usam explicit waits (WebDriverWait).
                // Misturar implicit + explicit wait quebra o polling do explicit wait.
                driver.manage().timeouts().implicitlyWait(Duration.ZERO);
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
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
