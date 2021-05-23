package pages.pageObjects;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import pages.elementMapper.HomePageElementMapper;
import utils.Browser;

public class HomePagePageObjects extends HomePageElementMapper {

    public HomePagePageObjects() {
        PageFactory.initElements(Browser.getCurrentDriver(), this);
    }

    public void sendKeysCampoDestino(String nomeDestino) {
        JavascriptExecutor executor = (JavascriptExecutor)Browser.getCurrentDriver();
        executor.executeScript("document.getElementById('querytext').value='"+nomeDestino+"';");
    }

    public void clickBotaoPesquisa() {
        botaoPesquisa.submit();
    }

    public void clickButtonFecharCalendario() {
        buttonFecharCalendario.click();
    }

    public void clickButtonFecharMapa() {
        buttonFecharMapa.click();
    }

    public void selectComboBoxOrdenarPor(String opcoes) {
       Select dropDown = new Select(comboBoxOrdenarPor);
       dropDown.selectByVisibleText(opcoes);
    }

    public String getTextNomeLocalPrimeiroItemDaLista() {
        return nomeLocalPrimeiroItemDaLista.getText();
    }

    public String getTextQuantidadeEstrelasPrimeiroItemDaLista() {
        return quantidadeEstrelasPrimeiroItemDaLista.getAttribute("content");
    }

    public String getTextValorLocalPrimeiroItemDaLista() {
        return valorLocalPrimeiroItemDaLista.getText();
    }

    public void pesquisaSimples(String destino) {
        sendKeysCampoDestino(destino);
        clickBotaoPesquisa();
        clickButtonFecharCalendario();
        clickButtonFecharMapa();
    }
}
