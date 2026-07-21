package pages.elementMapper;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePageElementMapper {

    @FindBy(css = "[data-testid='search-form-input']")
    public WebElement campoDestino;

    @FindBy(css = "[data-testid='search-button-with-loader']")
    public WebElement botaoPesquisa;

    @FindBy(css = "button[name='sorting-filter']")
    public WebElement botaoOrdenarPor;

    @FindBy(css = "[data-testid='accommodation-list-element']")
    public WebElement primeiroItemDaLista;

    @FindBy(css = "[data-testid='accommodation-list-element'] [data-testid='item-name-link']")
    public WebElement nomeLocalPrimeiroItemDaLista;

    @FindBy(css = "[data-testid='accommodation-list-element'] [data-testid='expected-price']")
    public WebElement valorLocalPrimeiroItemDaLista;

}
