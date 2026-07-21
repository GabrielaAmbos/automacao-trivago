package tests.hooks;

import io.cucumber.java.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import utils.Browser;

import java.io.FileOutputStream;

public class Hooks {

    @Before
    public void setUp() {
        Browser.setCurrentBrowser(Browser.Type.CHROME, isHeadless());
    }

    // headless por padrao; desative com HEADLESS=false (env) ou -Dheadless=false (system property)
    private boolean isHeadless() {
        String valor = System.getProperty("headless");
        if (valor == null) valor = System.getenv("HEADLESS");
        if (valor == null) return true;
        return !valor.equalsIgnoreCase("false") && !valor.equals("0");
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            try {
                System.out.println("[DEBUG] URL no momento da falha: " + Browser.getCurrentDriver().getCurrentUrl());
                byte[] png = ((TakesScreenshot) Browser.getCurrentDriver()).getScreenshotAs(OutputType.BYTES);
                String out = "/private/tmp/claude-501/-Users-gabrielaambos-Documents-GitHub-automacao-trivago/e793d859-3b70-4a5e-bc24-64bf05373f7e/scratchpad/falha.png";
                try (FileOutputStream fos = new FileOutputStream(out)) { fos.write(png); }
                System.out.println("[DEBUG] Screenshot salvo em: " + out);
            } catch (Exception e) {
                System.out.println("[DEBUG] Falha ao capturar screenshot: " + e.getMessage());
            }
        }
        Browser.quitBrowser();
    }
}
