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
import java.util.List;

public class HomePagePageObjects extends HomePageElementMapper {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final By ITEM_LISTA = By.cssSelector("[data-testid='accommodation-list-element']");
    private static final By LISTA_SUGESTOES = By.cssSelector("[data-testid='suggested-item']");

    public HomePagePageObjects() {
        this.driver = Browser.getCurrentDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        PageFactory.initElements(driver, this);
    }

    // ==================== BUSCA ====================

    public void pesquisaSimples(String destino) {
        selecionarDestino(destino);
        confirmarPesquisa();
    }

    // seleciona a sugestao do autocomplete correspondente ao destino digitado
    public void selecionarDestino(String destino) {
        By sugestaoDestino = sugestaoPor(destino);
        WebDriverWait waitSugestao = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean selecionou = false;

        // A API de autocomplete do Trivago as vezes retorna vazio de forma intermitente
        // (anti-bot); nesse caso recarregamos a pagina (reseta a sessao) e tentamos de novo.
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
                waitSugestao.until(ExpectedConditions.invisibilityOfElementLocated(LISTA_SUGESTOES));
                selecionou = true;
            } catch (TimeoutException semSugestao) {
                recarregar(tentativa); // sugestao nao veio; recarrega (com backoff) e tenta de novo
            }
        }

        if (!selecionou) {
            throw new IllegalStateException(
                    "Não foi possível selecionar o destino \"" + destino + "\" no autocomplete do Trivago.");
        }
    }

    public void confirmarPesquisa() {
        dormir(500);
        botaoPesquisa.click();
        new WebDriverWait(driver, Duration.ofSeconds(40))
                .until(ExpectedConditions.presenceOfElementLocated(ITEM_LISTA));
    }

    // digita no campo sem selecionar sugestao (para cenarios de autocomplete/negativos)
    public void digitarNoCampoDestino(String texto) {
        wait.until(ExpectedConditions.elementToBeClickable(campoDestino));
        limparCampo(campoDestino);
        digitarDevagar(campoDestino, texto);
        dormir(2500); // aguarda o autocomplete responder
    }

    // clica numa sugestao ja exibida; se nao estiver disponivel, refaz a busca (com retry)
    public void clicarSugestao(String destino) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(8))
                    .until(ExpectedConditions.elementToBeClickable(sugestaoPor(destino))).click();
            new WebDriverWait(driver, Duration.ofSeconds(8))
                    .until(ExpectedConditions.invisibilityOfElementLocated(LISTA_SUGESTOES));
        } catch (TimeoutException e) {
            selecionarDestino(destino);
        }
    }

    public boolean existeSugestaoDeDestino(String texto) {
        // o autocomplete e intermitente; se nao aparecer, recarrega e redigita
        for (int tentativa = 0; tentativa < 3; tentativa++) {
            try {
                new WebDriverWait(driver, Duration.ofSeconds(8))
                        .until(ExpectedConditions.visibilityOfElementLocated(sugestaoPor(texto)));
                return true;
            } catch (TimeoutException e) {
                if (tentativa == 2) return false;
                recarregar(tentativa);
                wait.until(ExpectedConditions.elementToBeClickable(campoDestino));
                limparCampo(campoDestino);
                digitarDevagar(campoDestino, texto);
                dormir(1500);
            }
        }
        return false;
    }

    public boolean existeOpcaoDeBuscaLivre() {
        return !driver.findElements(By.cssSelector("[data-testid='ssg-element-free-search']")).isEmpty();
    }

    public boolean existeSugestaoDeCidade() {
        for (WebElement e : driver.findElements(By.cssSelector("[data-testid='suggestion-subtitle']"))) {
            if (e.getText().toLowerCase().contains("cidade")) {
                return true;
            }
        }
        return false;
    }

    public void pesquisarSemDestino() {
        wait.until(ExpectedConditions.elementToBeClickable(campoDestino));
        limparCampo(campoDestino);
        dormir(300);
        botaoPesquisa.click();
        dormir(1500);
    }

    public boolean campoDestinoEstaFocado() {
        WebElement ativo = driver.switchTo().activeElement();
        return "search-form-input".equals(ativo.getAttribute("data-testid"));
    }

    public boolean estaNaPaginaInicial() {
        String url = driver.getCurrentUrl();
        return !url.contains("/srl/") && !url.contains("/lm/");
    }

    // ==================== ORDENAÇÃO ====================

    public void selectComboBoxOrdenarPor(String opcao) {
        wait.until(ExpectedConditions.visibilityOf(botaoOrdenarPor));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", botaoOrdenarPor);
        dormir(300);
        // clique via JS: o botao pode ficar sob barras fixas que interceptam o clique nativo
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botaoOrdenarPor);

        // o dialog de ordenacao expoe cada opcao como um <label> com o texto visivel
        By opcaoOrdenacao = By.xpath("//label[normalize-space()='" + opcao + "']");
        wait.until(ExpectedConditions.elementToBeClickable(opcaoOrdenacao)).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(ITEM_LISTA));
    }

    // ==================== FILTROS ====================

    public void filtrarPorEstrelas(int quantidade) {
        String urlAntes = driver.getCurrentUrl();
        clicarViaJs(By.cssSelector("[data-testid='rating-checkbox-" + quantidade + "']"));
        aguardarMudancaDeUrl(urlAntes);
    }

    // rotulo esperado: "8,0+" (o filtro no site aparece como "Avaliação: 8,0+")
    public void filtrarPorAvaliacaoMinima(String rotulo) {
        String urlAntes = driver.getCurrentUrl();
        clicarViaJs(By.xpath("//*[@data-testid='refinement-row-pills']//label[normalize-space()='Avaliação: " + rotulo + "']"));
        aguardarMudancaDeUrl(urlAntes);
    }

    public void removerFiltrosDeEstrelas() {
        for (int q = 1; q <= 5; q++) {
            if (filtroEstrelasEstaAtivo(q)) {
                String urlAntes = driver.getCurrentUrl();
                clicarViaJs(By.cssSelector("[data-testid='rating-checkbox-" + q + "']"));
                aguardarMudancaDeUrl(urlAntes);
            }
        }
    }

    public boolean filtroEstrelasEstaAtivo(int quantidade) {
        List<WebElement> inputs = driver.findElements(
                By.cssSelector("[data-testid='rating-checkbox-" + quantidade + "-input']"));
        return !inputs.isEmpty() && inputs.get(0).isSelected();
    }

    public boolean todosOsHoteisPossuemEstrelas(int quantidade) {
        int verificados = 0;
        for (WebElement card : primeirosCards(12)) {
            int estrelas = estrelasDoCard(card);
            if (estrelas == 0) continue; // card sem classificacao por estrelas; ignora
            verificados++;
            if (estrelas != quantidade) return false;
        }
        return verificados >= 3;
    }

    public boolean todosOsHoteisPossuemAvaliacaoMinima(double minimo) {
        int verificados = 0;
        for (WebElement card : primeirosCards(12)) {
            Double nota = notaDoCard(card);
            if (nota == null) continue;
            verificados++;
            if (nota < minimo) return false;
        }
        return verificados >= 3;
    }

    public boolean cadaHotelExibeNomeAvaliacaoEPreco() {
        List<WebElement> cards = primeirosCards(6);
        if (cards.isEmpty()) return false;
        for (WebElement card : cards) {
            String nome = textoDoCard(card, "[data-testid='item-name-link']");
            String preco = textoDoCard(card, "[data-testid='expected-price']");
            Double nota = notaDoCard(card);
            if (nome.isEmpty() || !preco.contains("R$") || nota == null) return false;
        }
        return true;
    }

    // ==================== RESULTADOS ====================

    public int getQuantidadeResultados() {
        return driver.findElements(ITEM_LISTA).size();
    }

    public String getTextNomeLocalPrimeiroItemDaLista() {
        return wait.until(ExpectedConditions.visibilityOf(nomeLocalPrimeiroItemDaLista)).getText().trim();
    }

    public int getQuantidadeEstrelasPrimeiroItemDaLista() {
        return estrelasDoCard(primeiroItemDaLista);
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

    // ==================== HELPERS ====================

    private By sugestaoPor(String texto) {
        return By.xpath("//*[@data-testid='suggested-item']" +
                "[.//*[@data-testid='suggestion-title'][contains(normalize-space(),'" + texto + "')]]");
    }

    private List<WebElement> primeirosCards(int limite) {
        List<WebElement> cards = driver.findElements(ITEM_LISTA);
        return cards.subList(0, Math.min(limite, cards.size()));
    }

    private int estrelasDoCard(WebElement card) {
        return card.findElements(By.cssSelector("[data-testid='star-rating'] [data-testid='star']")).size();
    }

    // le a nota (ex.: "8,2 Muito boa (2.513)") e devolve 8.2; null se o card nao tiver nota
    private Double notaDoCard(WebElement card) {
        List<WebElement> ratings = card.findElements(By.cssSelector("[data-testid='aggregate-rating']"));
        if (ratings.isEmpty()) return null;
        String texto = ratings.get(0).getText().trim();
        if (texto.isEmpty()) return null;
        try {
            return Double.parseDouble(texto.split("\\s+")[0].replace(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String textoDoCard(WebElement card, String css) {
        List<WebElement> els = card.findElements(By.cssSelector(css));
        return els.isEmpty() ? "" : els.get(0).getText().trim();
    }

    private void clicarViaJs(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        dormir(300);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    private void aguardarMudancaDeUrl(String urlAnterior) {
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(d -> !d.getCurrentUrl().equals(urlAnterior));
        wait.until(ExpectedConditions.presenceOfElementLocated(ITEM_LISTA));
        dormir(2500); // deixa a lista re-renderizar apos o filtro
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
}
