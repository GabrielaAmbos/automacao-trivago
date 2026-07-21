package tests.steps;

import io.cucumber.java.pt.*;
import pages.pageObjects.HomePagePageObjects;
import utils.Browser;
import utils.providers.UrlProvider;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BuscaDeHoteisSteps {

    HomePagePageObjects homePagePage = new HomePagePageObjects();

    // ==================== Dado ====================

    @Dado("que eu acesse o site da Trivago")
    public void queEuAcesseOSiteDaTrivago() {
        Browser.loadApplication(UrlProvider.getBaseUrl());
    }

    // ==================== Quando ====================

    @Quando("eu solicito pesquisar pelo destino {string}")
    public void euSolicitoPesquisarPeloDestino(String nomeDestino) {
        homePagePage.pesquisaSimples(nomeDestino);
    }

    @Quando("eu digito {string} no campo de destino")
    public void euDigitoNoCampoDeDestino(String texto) {
        homePagePage.digitarNoCampoDestino(texto);
    }

    @Quando("eu aciono o botão de pesquisa sem informar um destino")
    public void euAcionoOBotaoDePesquisaSemInformarUmDestino() {
        homePagePage.pesquisarSemDestino();
    }

    @E("ordeno a pesquisa por {string}")
    public void ordenoAPesquisaPor(String opcao) {
        homePagePage.selectComboBoxOrdenarPor(opcao);
    }

    @E("seleciono a sugestão {string}")
    public void selecionoASugestao(String destino) {
        homePagePage.clicarSugestao(destino);
    }

    @E("confirmo a pesquisa")
    public void confirmoAPesquisa() {
        homePagePage.confirmarPesquisa();
    }

    @E("filtro os resultados por {string}")
    public void filtroOsResultadosPor(String filtro) {
        int estrelas = Integer.parseInt(filtro.replaceAll("\\D+", ""));
        homePagePage.filtrarPorEstrelas(estrelas);
    }

    @E("filtro os resultados por avaliação {string}")
    public void filtroOsResultadosPorAvaliacao(String rotulo) {
        homePagePage.filtrarPorAvaliacaoMinima(rotulo);
    }

    @E("removo todos os filtros aplicados")
    public void removoTodosOsFiltrosAplicados() {
        homePagePage.removerFiltrosDeEstrelas();
    }

    // ==================== Então ====================

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

    @Então("vejo uma sugestão de destino contendo {string}")
    public void vejoUmaSugestaoDeDestinoContendo(String texto) {
        assertTrue("Nenhuma sugestão de destino contendo \"" + texto + "\" foi exibida",
                homePagePage.existeSugestaoDeDestino(texto));
    }

    @Então("vejo a opção de busca livre")
    public void vejoAOpcaoDeBuscaLivre() {
        assertTrue("A opção de busca livre não foi exibida", homePagePage.existeOpcaoDeBuscaLivre());
    }

    @E("não vejo nenhuma sugestão de cidade")
    public void naoVejoNenhumaSugestaoDeCidade() {
        assertFalse("Não era esperada nenhuma sugestão de cidade", homePagePage.existeSugestaoDeCidade());
    }

    @Então("permaneço na página inicial com o campo de destino em destaque")
    public void permanecoNaPaginaInicialComOCampoDeDestinoEmDestaque() {
        assertTrue("Esperava permanecer na página inicial", homePagePage.estaNaPaginaInicial());
        assertTrue("Esperava o campo de destino focado", homePagePage.campoDestinoEstaFocado());
    }

    @Então("todos os hotéis exibidos possuem {int} estrelas")
    public void todosOsHoteisExibidosPossuemEstrelas(int quantidade) {
        assertTrue("Nem todos os hotéis exibidos possuem " + quantidade + " estrelas",
                homePagePage.todosOsHoteisPossuemEstrelas(quantidade));
    }

    @Então("todos os hotéis exibidos possuem avaliação igual ou superior a 8,0")
    public void todosOsHoteisExibidosPossuemAvaliacaoMinima() {
        assertTrue("Nem todos os hotéis exibidos possuem avaliação >= 8,0",
                homePagePage.todosOsHoteisPossuemAvaliacaoMinima(8.0));
    }

    @Então("o filtro de estrelas não está mais ativo")
    public void oFiltroDeEstrelasNaoEstaMaisAtivo() {
        for (int q = 1; q <= 5; q++) {
            assertFalse("O filtro de " + q + " estrelas ainda está ativo",
                    homePagePage.filtroEstrelasEstaAtivo(q));
        }
    }

    @E("a lista de resultados continua sendo exibida")
    public void aListaDeResultadosContinuaSendoExibida() {
        assertTrue("A lista de resultados não está mais sendo exibida",
                homePagePage.getQuantidadeResultados() >= 1);
    }

    @E("a lista de resultados possui pelo menos um hotel")
    public void aListaDeResultadosPossuiPeloMenosUmHotel() {
        assertTrue("A lista de resultados está vazia", homePagePage.getQuantidadeResultados() >= 1);
    }

    @Então("o preço do primeiro item está no formato {string}")
    public void oPrecoDoPrimeiroItemEstaNoFormato(String formato) {
        String valor = homePagePage.getTextValorLocalPrimeiroItemDaLista();
        assertTrue("O preço \"" + valor + "\" não contém \"" + formato + "\"", valor.contains(formato));
    }

    @Então("cada hotel exibido possui nome, avaliação e preço")
    public void cadaHotelExibidoPossuiNomeAvaliacaoEPreco() {
        assertTrue("Nem todos os hotéis exibidos possuem nome, avaliação e preço",
                homePagePage.cadaHotelExibeNomeAvaliacaoEPreco());
    }
}
