package pages.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.elementMapper.HomePageElementMapper;
import utils.Browser;

import java.time.Duration;

public class HomePagePageObjects extends HomePageElementMapper {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final By ITEM_LISTA = By.cssSelector("[data-testid='accommodation-list-element']");

    public HomePagePageObjects() {
        this.driver = Browser.getCurrentDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        PageFactory.initElements(driver, this);
    }

    public void pesquisaSimples(String destino) {
        // seleciona a sugestao do autocomplete correspondente ao destino digitado
        By sugestaoDestino = By.xpath(
                "//*[@data-testid='suggested-item']" +
                "[.//*[@data-testid='suggestion-title'][contains(normalize-space(),'" + destino + "')]]");

        By listaSugestoes = By.cssSelector("[data-testid='suggested-item']");
        WebDriverWait waitSugestao = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean selecionou = false;

        // A API de autocomplete do Trivago as vezes retorna "Nenhum resultado" de forma
        // intermitente; nesse caso recarregamos a pagina (reseta a sessao) e tentamos de novo.
        for (int tentativa = 0; tentativa < 4 && !selecionou; tentativa++) {
            wait.until(ExpectedConditions.elementToBeClickable(campoDestino));
            limparCampo(campoDestino);
            digitarDevagar(campoDestino, destino);

            // garante que o campo contem exatamente o destino (evita texto concatenado)
            String conteudo = campoDestino.getAttribute("value");
            if (conteudo == null || !conteudo.equalsIgnoreCase(destino)) {
                recarregar(tentativa);
                continue;
            }
            try {
                waitSugestao.until(ExpectedConditions.elementToBeClickable(sugestaoDestino)).click();
                // confirma a selecao: o dropdown de sugestoes deve fechar
                waitSugestao.until(ExpectedConditions.invisibilityOfElementLocated(listaSugestoes));
                selecionou = true;
            } catch (TimeoutException semSugestao) {
                recarregar(tentativa); // sugestao nao veio; recarrega (com backoff) e tenta de novo
            }
        }

        if (!selecionou) {
            throw new IllegalStateException(
                    "Não foi possível selecionar o destino \"" + destino + "\" no autocomplete do Trivago.");
        }

        dormir(500);
        botaoPesquisa.click();

        // aguarda a pagina de resultados carregar
        new WebDriverWait(driver, Duration.ofSeconds(40))
                .until(ExpectedConditions.presenceOfElementLocated(ITEM_LISTA));
    }

    private void recarregar(int tentativa) {
        driver.navigate().refresh();
        // backoff incremental: da tempo do throttle do autocomplete do Trivago aliviar
        dormir(2000L + tentativa * 1500L);
    }

    private void digitarDevagar(WebElement campo, String texto) {
        for (char c : texto.toCharArray()) {
            campo.sendKeys(String.valueOf(c));
            dormir(120);
        }
    }

    // limpa o campo com BACKSPACE (dispara os eventos de input que o React precisa;
    // Ctrl/Cmd+A e .clear() nao sao confiaveis nesse autocomplete)
    private void limparCampo(WebElement campo) {
        campo.click();
        String atual = campo.getAttribute("value");
        int qtd = (atual == null ? 0 : atual.length());
        campo.sendKeys(Keys.END);
        for (int i = 0; i < qtd + 3; i++) {
            campo.sendKeys(Keys.BACK_SPACE);
        }
    }

    private void dormir(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void selectComboBoxOrdenarPor(String opcao) {
        wait.until(ExpectedConditions.visibilityOf(botaoOrdenarPor));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", botaoOrdenarPor);
        dormir(300);
        // clique via JS: o botao pode ficar sob barras fixas que interceptam o clique nativo
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botaoOrdenarPor);

        // o dialog de ordenacao expoe cada opcao como um <label> com o texto visivel
        By opcaoOrdenacao = By.xpath("//label[normalize-space()='" + opcao + "']");
        wait.until(ExpectedConditions.elementToBeClickable(opcaoOrdenacao)).click();

        // aguarda a lista reordenar
        wait.until(ExpectedConditions.presenceOfElementLocated(ITEM_LISTA));
    }

    public String getTextNomeLocalPrimeiroItemDaLista() {
        return wait.until(ExpectedConditions.visibilityOf(nomeLocalPrimeiroItemDaLista)).getText().trim();
    }

    public int getQuantidadeEstrelasPrimeiroItemDaLista() {
        return primeiroItemDaLista
                .findElements(By.cssSelector("[data-testid='star-rating'] [data-testid='star']"))
                .size();
    }

    public String getTextValorLocalPrimeiroItemDaLista() {
        return wait.until(ExpectedConditions.visibilityOf(valorLocalPrimeiroItemDaLista)).getText().trim();
    }

    public String getUrlAtual() {
        return driver.getCurrentUrl();
    }

    public String getTituloAtual() {
        return driver.getTitle();
    }
}
