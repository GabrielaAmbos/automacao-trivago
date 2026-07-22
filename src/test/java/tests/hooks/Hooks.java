package tests.hooks;

import io.cucumber.java.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import utils.Browser;

import java.io.File;
import java.io.FileOutputStream;

public class Hooks {

    @Before
    public void setUp() {
        // pequena pausa entre cenários: o autocomplete do Trivago limita (throttle)
        // requisições automatizadas em sequência; espaçar as buscas reduz falhas.
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Browser.setCurrentBrowser(navegador(), isHeadless());
    }

    // navegador via -Dbrowser=<chrome|firefox|edge> ou BROWSER=<...>; padrão: chrome
    private Browser.Type navegador() {
        String valor = System.getProperty("browser");
        if (valor == null) valor = System.getenv("BROWSER");
        if (valor == null) return Browser.Type.CHROME;
        try {
            return Browser.Type.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Navegador inválido: \"" + valor + "\". Use chrome, firefox ou edge.");
        }
    }

    // headless por padrão; desative com HEADLESS=false (env) ou -Dheadless=false (system property)
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
                File dir = new File("target/screenshots");
                dir.mkdirs();
                String nome = scenario.getName().replaceAll("[^a-zA-Z0-9-_]+", "_");
                File destino = new File(dir, nome + ".png");
                try (FileOutputStream fos = new FileOutputStream(destino)) {
                    fos.write(png);
                }
                System.out.println("[DEBUG] Screenshot salvo em: " + destino.getPath());
            } catch (Exception e) {
                System.out.println("[DEBUG] Falha ao capturar screenshot: " + e.getMessage());
            }
        }
        Browser.quitBrowser();
    }
}
