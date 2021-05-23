package tests.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.*;
import pages.pageObjects.HomePagePageObjects;
import utils.Browser;
import utils.providers.UrlProvider;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BuscaPorDestinoSteps {

    HomePagePageObjects homePagePage = new HomePagePageObjects();

    @Dado("que eu acesse o site da Trivago")
    public void queEuAcesseOSiteDaTrivago() {
        Browser.loadApplication(UrlProvider.getBaseUrl());
    }

    @Quando("eu solicito pesquisar pelo destino {string}")
    public void euSolicitoPesquisarPeloDestino(String nomeDestino) {
        homePagePage.pesquisaSimples(nomeDestino);
    }

    @E("ordeno a pesquisa por {string}")
    public void ordenoAPesquisaPor(String opcao) {
        homePagePage.selectComboBoxOrdenarPor(opcao);
    }

    @Então("eu vejo os dados do primeiro item da lista")
    public void euVejoOsDadosDoPrimeiroItemDaLista(DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);

        for(Map<String, String> columns : rows) {
            String nome = columns.get("Nome");
            String avaliacao = columns.get("Avaliação");
            String valor = columns.get("Valor");

            assertEquals(nome, homePagePage.getTextNomeLocalPrimeiroItemDaLista());
            assertEquals(avaliacao, homePagePage.getTextQuantidadeEstrelasPrimeiroItemDaLista());
            assertEquals(valor, homePagePage.getTextValorLocalPrimeiroItemDaLista());
        }
    }
}
