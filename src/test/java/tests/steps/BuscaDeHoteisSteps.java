package tests.steps;

import io.cucumber.java.pt.*;
import pages.pageObjects.HomePagePageObjects;
import utils.Browser;
import utils.providers.UrlProvider;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BuscaDeHoteisSteps {

    HomePagePageObjects homePagePage = new HomePagePageObjects();

    // ==================== Contexto (Dado) ====================

    @Dado("que eu esteja na Trivago")
    public void queEuEstejaNaTrivago() {
        Browser.loadApplication(UrlProvider.getBaseUrl());
    }

    @Dado("que eu tenha buscado hotéis em {string}")
    public void queEuTenhaBuscadoHoteisEm(String destino) {
        Browser.loadApplication(UrlProvider.getBaseUrl());
        homePagePage.pesquisaSimples(destino);
    }

    @Dado("que eu tenha filtrado por hotéis de {string}")
    public void queEuTenhaFiltradoPorHoteisDe(String filtro) {
        homePagePage.filtrarPorEstrelas(quantidadeDeEstrelas(filtro));
    }

    // ==================== Ações (Quando) ====================

    @Quando("eu busco hotéis em {string} ordenados por {string}")
    public void euBuscoHoteisEmOrdenadosPor(String destino, String criterio) {
        homePagePage.pesquisaSimples(destino);
        homePagePage.selectComboBoxOrdenarPor(criterio);
    }

    @Quando("eu busco hotéis em {string}")
    public void euBuscoHoteisEm(String destino) {
        homePagePage.pesquisaSimples(destino);
    }

    @Quando("eu informo o destino {string}")
    public void euInformoODestino(String destino) {
        homePagePage.digitarNoCampoDestino(destino);
    }

    @Quando("eu filtro por hotéis de {string}")
    public void euFiltroPorHoteisDe(String filtro) {
        homePagePage.filtrarPorEstrelas(quantidadeDeEstrelas(filtro));
    }

    @Quando("eu filtro por avaliação mínima {string}")
    public void euFiltroPorAvaliacaoMinima(String rotulo) {
        homePagePage.filtrarPorAvaliacaoMinima(rotulo);
    }

    @Quando("eu removo os filtros aplicados")
    public void euRemovoOsFiltrosAplicados() {
        homePagePage.removerFiltrosDeEstrelas();
    }

    @Quando("eu tento buscar sem informar um destino")
    public void euTentoBuscarSemInformarUmDestino() {
        homePagePage.pesquisarSemDestino();
    }

    // ==================== Verificações (Então) ====================

    @Então("vejo hotéis disponíveis em {string}")
    public void vejoHoteisDisponiveisEm(String destino) {
        String url = homePagePage.getUrlAtual().toLowerCase();
        String titulo = homePagePage.getTituloAtual().toLowerCase();
        String esperado = destino.toLowerCase();

        assertTrue(
                "Esperava a página de resultados de \"" + destino + "\". URL: " + url + " | título: " + titulo,
                url.contains(esperado) || titulo.contains(esperado));
    }

    @Então("o primeiro hotel apresenta nome, classificação e preço")
    public void oPrimeiroHotelApresentaNomeClassificacaoEPreco() {
        String nome = homePagePage.getTextNomeLocalPrimeiroItemDaLista();
        int estrelas = homePagePage.getQuantidadeEstrelasPrimeiroItemDaLista();
        String valor = homePagePage.getTextValorLocalPrimeiroItemDaLista();

        assertFalse("O nome do primeiro hotel está vazio", nome.isEmpty());
        assertTrue("Classificação em estrelas fora do intervalo esperado (1 a 5): " + estrelas,
                estrelas >= 1 && estrelas <= 5);
        assertTrue("O preço do primeiro hotel não contém \"R$\": " + valor, valor.contains("R$"));
    }

    @Então("recebo {string} entre as sugestões de destino")
    public void receboEntreAsSugestoesDeDestino(String texto) {
        assertTrue("Nenhuma sugestão de destino contendo \"" + texto + "\" foi exibida",
                homePagePage.existeSugestaoDeDestino(texto));
    }

    @Então("recebo a opção de busca livre")
    public void receboAOpcaoDeBuscaLivre() {
        assertTrue("A opção de busca livre não foi exibida", homePagePage.existeOpcaoDeBuscaLivre());
    }

    @Então("não recebo sugestões de cidade")
    public void naoReceboSugestoesDeCidade() {
        assertFalse("Não era esperada nenhuma sugestão de cidade", homePagePage.existeSugestaoDeCidade());
    }

    @Então("permaneço na página inicial para informar o destino")
    public void permanecoNaPaginaInicialParaInformarODestino() {
        assertTrue("Esperava permanecer na página inicial", homePagePage.estaNaPaginaInicial());
        assertTrue("Esperava o campo de destino em foco para informar o destino",
                homePagePage.campoDestinoEstaFocado());
    }

    @Então("todos os hotéis exibidos possuem {int} estrelas")
    public void todosOsHoteisExibidosPossuemEstrelas(int quantidade) {
        assertTrue("Nem todos os hotéis exibidos possuem " + quantidade + " estrelas",
                homePagePage.todosOsHoteisPossuemEstrelas(quantidade));
    }

    @Então("todos os hotéis exibidos têm avaliação a partir de 8,0")
    public void todosOsHoteisExibidosTemAvaliacaoAPartirDe() {
        assertTrue("Nem todos os hotéis exibidos têm avaliação a partir de 8,0",
                homePagePage.todosOsHoteisPossuemAvaliacaoMinima(8.0));
    }

    @Então("nenhum filtro de estrelas permanece ativo")
    public void nenhumFiltroDeEstrelasPermaneceAtivo() {
        for (int q = 1; q <= 5; q++) {
            assertFalse("O filtro de " + q + " estrelas ainda está ativo",
                    homePagePage.filtroEstrelasEstaAtivo(q));
        }
    }

    @Então("continuo vendo hotéis disponíveis")
    public void continuoVendoHoteisDisponiveis() {
        assertTrue("A lista de hotéis não está mais sendo exibida",
                homePagePage.getQuantidadeResultados() >= 1);
    }

    @Então("o preço do primeiro hotel é exibido em reais")
    public void oPrecoDoPrimeiroHotelEExibidoEmReais() {
        String valor = homePagePage.getTextValorLocalPrimeiroItemDaLista();
        assertTrue("O preço \"" + valor + "\" não está em reais (R$)", valor.contains("R$"));
    }

    @Então("cada hotel exibido apresenta nome, avaliação e preço")
    public void cadaHotelExibidoApresentaNomeAvaliacaoEPreco() {
        assertTrue("Nem todos os hotéis exibidos apresentam nome, avaliação e preço",
                homePagePage.cadaHotelExibeNomeAvaliacaoEPreco());
    }

    // ==================== Helpers ====================

    private int quantidadeDeEstrelas(String filtro) {
        return Integer.parseInt(filtro.replaceAll("\\D+", ""));
    }
}
