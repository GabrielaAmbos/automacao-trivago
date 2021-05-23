package pages.elementMapper;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePageElementMapper {

    @FindBy(id = "querytext")
    public WebElement campoDestino;

    @FindBy(css = ".search-button")
    public WebElement botaoPesquisa;

    @FindBy(css = "#mf-select-sortby")
    public WebElement comboBoxOrdenarPor;

    @FindBy(xpath = "//button[@class='df_overlay_close_wrap overlay__close']")
    public WebElement buttonFecharCalendario;

    @FindBy(xpath = "//button[@data-qa='close-map-button']")
    public WebElement buttonFecharMapa;

    @FindBy(xpath = "(//div[@class='pos-relative item__wrapper']//span[@class='item-link name__copytext'])[1]")
    public WebElement nomeLocalPrimeiroItemDaLista;

    @FindBy(xpath = "(//div[@class='stars-wrp']//meta[@itemprop='ratingValue'])[1]")
    public WebElement quantidadeEstrelasPrimeiroItemDaLista;

    @FindBy(xpath = "(//strong[@data-qa='recommended-price'])[1]")
    public WebElement valorLocalPrimeiroItemDaLista;

}
