#language: pt
Funcionalidade: Busca de hotéis
  Como um usuário
  Desejo pesquisar, ordenar e filtrar hotéis por destino
  Para que eu possa visualizar os dados completos das opções que estou buscando

  # ---------- Ordenação ----------

  Cenário: Buscar por hotéis em Manaus ordenados por avaliação
      Dado que eu acesse o site da Trivago
      Quando eu solicito pesquisar pelo destino "Manaus"
      E ordeno a pesquisa por "Avaliação e sugestões"
      Então eu vejo os resultados da busca pelo destino "Manaus"
      E o primeiro item da lista possui nome, quantidade de estrelas e preço

  Esquema do Cenário: Ordenar os resultados por diferentes critérios
    Dado que eu acesse o site da Trivago
    Quando eu solicito pesquisar pelo destino "Manaus"
    E ordeno a pesquisa por "<opcao>"
    Então eu vejo os resultados da busca pelo destino "Manaus"
    E o primeiro item da lista possui nome, quantidade de estrelas e preço

    Exemplos:
      | opcao                           |
      | Estadias em destaque            |
      | Avaliação e sugestões           |
      | Preço e sugestões               |
      | Melhores avaliações de hóspedes |

  # ---------- Autocomplete ----------

  Cenário: O autocomplete sugere destinos ao digitar
    Dado que eu acesse o site da Trivago
    Quando eu digito "Manaus" no campo de destino
    Então vejo uma sugestão de destino contendo "Manaus"

  Cenário: Selecionar um destino a partir da lista de sugestões
    Dado que eu acesse o site da Trivago
    Quando eu digito "Rio de Janeiro" no campo de destino
    E seleciono a sugestão "Rio de Janeiro"
    E confirmo a pesquisa
    Então eu vejo os resultados da busca pelo destino "Rio de Janeiro"

  # ---------- Busca por diferentes destinos ----------

  Esquema do Cenário: Buscar hotéis em diferentes destinos
    Dado que eu acesse o site da Trivago
    Quando eu solicito pesquisar pelo destino "<destino>"
    Então eu vejo os resultados da busca pelo destino "<destino>"
    E a lista de resultados possui pelo menos um hotel

    Exemplos:
      | destino       |
      | São Paulo     |
      | Gramado       |
      | Fortaleza     |

  # ---------- Filtros ----------

  Cenário: Filtrar resultados por classificação de estrelas
    Dado que eu acesse o site da Trivago
    Quando eu solicito pesquisar pelo destino "Manaus"
    E filtro os resultados por "4 estrelas"
    Então todos os hotéis exibidos possuem 4 estrelas

  Cenário: Filtrar resultados por avaliação mínima dos hóspedes
    Dado que eu acesse o site da Trivago
    Quando eu solicito pesquisar pelo destino "Manaus"
    E filtro os resultados por avaliação "8,0+"
    Então todos os hotéis exibidos possuem avaliação igual ou superior a 8,0

  Cenário: Remover o filtro de estrelas mantém a lista de resultados
    Dado que eu acesse o site da Trivago
    Quando eu solicito pesquisar pelo destino "Manaus"
    E filtro os resultados por "4 estrelas"
    E removo todos os filtros aplicados
    Então o filtro de estrelas não está mais ativo
    E a lista de resultados continua sendo exibida

  # ---------- Fluxos negativos ----------

  Cenário: Buscar por um texto sem destino correspondente exibe apenas a busca livre
    Dado que eu acesse o site da Trivago
    Quando eu digito "xyzqwk123" no campo de destino
    Então vejo a opção de busca livre
    E não vejo nenhuma sugestão de cidade

  Cenário: Tentar pesquisar sem informar um destino
    Dado que eu acesse o site da Trivago
    Quando eu aciono o botão de pesquisa sem informar um destino
    Então permaneço na página inicial com o campo de destino em destaque

  # ---------- Detalhe do resultado ----------

  Cenário: O primeiro resultado exibe preço no formato de moeda brasileira
    Dado que eu acesse o site da Trivago
    Quando eu solicito pesquisar pelo destino "Manaus"
    Então o preço do primeiro item está no formato "R$"

  Cenário: Cada hotel da lista exibe nome, avaliação e preço
    Dado que eu acesse o site da Trivago
    Quando eu solicito pesquisar pelo destino "Manaus"
    Então cada hotel exibido possui nome, avaliação e preço

  # ==========================================================================
  # Cenários de backlog (ainda sem step definitions — naturalmente frágeis
  # contra o site ao vivo). Para rodar apenas os implementados:
  #   mvn test -Dcucumber.filter.tags="not @backlog"
  # ==========================================================================

  @backlog
  Cenário: Ordenar por preço crescente coloca o menor valor primeiro
    Dado que eu acesse o site da Trivago
    Quando eu solicito pesquisar pelo destino "Manaus"
    E ordeno a pesquisa por "Preço em ordem crescente"
    Então o preço do primeiro item é menor ou igual ao preço do segundo item

  @backlog
  Cenário: Pesquisar informando quantidade de hóspedes e quartos
    Dado que eu acesse o site da Trivago
    Quando eu solicito pesquisar pelo destino "Manaus"
    E informo "3" hóspedes e "2" quartos
    Então eu vejo os resultados da busca pelo destino "Manaus"
    E o resumo da pesquisa exibe "3 hóspedes" e "2 quartos"

  @backlog
  Cenário: Pesquisar informando datas de entrada e saída
    Dado que eu acesse o site da Trivago
    Quando eu solicito pesquisar pelo destino "Manaus"
    E seleciono a data de entrada para amanhã e saída para depois de amanhã
    Então eu vejo os resultados da busca pelo destino "Manaus"
    E os preços exibidos correspondem ao período selecionado
