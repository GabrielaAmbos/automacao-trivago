package tests.steps;

import io.cucumber.java.pt.*;
import pages.pageObjects.HomePagePageObjects;
import utils.Browser;
import utils.providers.UrlProvider;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

    @Então("eu vejo os resultados da busca pelo destino {string}")
    public void euVejoOsResultadosDaBuscaPeloDestino(String destino) {
        String url = homePagePage.getUrlAtual().toLowerCase();
        String titulo = homePagePage.getTituloAtual().toLowerCase();
        String esperado = destino.toLowerCase();

        assertTrue(
                "Esperava a página de resultados de \"" + destino + "\". URL: " + url + " | título: " + titulo,
                url.contains(esperado) || titulo.contains(esperado));
    }

    @E("o primeiro item da lista possui nome, quantidade de estrelas e preço")
    public void oPrimeiroItemDaListaPossuiNomeQuantidadeDeEstrelasEPreco() {
        String nome = homePagePage.getTextNomeLocalPrimeiroItemDaLista();
        int estrelas = homePagePage.getQuantidadeEstrelasPrimeiroItemDaLista();
        String valor = homePagePage.getTextValorLocalPrimeiroItemDaLista();

        assertFalse("O nome do primeiro item está vazio", nome.isEmpty());
        assertTrue("Quantidade de estrelas fora do intervalo esperado (1 a 5): " + estrelas,
                estrelas >= 1 && estrelas <= 5);
        assertTrue("O preço do primeiro item não contém \"R$\": " + valor, valor.contains("R$"));
    }
}
