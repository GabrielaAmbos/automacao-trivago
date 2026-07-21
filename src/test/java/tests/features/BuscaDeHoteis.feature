#language: pt
Funcionalidade: Busca de hotéis
  Como um usuário
  Desejo pesquisar, ordenar e filtrar hotéis por destino
  Para que eu possa visualizar os dados completos das opções que estou buscando

  # ---------- Ordenação ----------

  Cenário: Buscar hotéis ordenados por avaliação
    Dado que eu esteja na Trivago
    Quando eu busco hotéis em "Manaus" ordenados por "Avaliação e sugestões"
    Então vejo hotéis disponíveis em "Manaus"
    E o primeiro hotel apresenta nome, classificação e preço

  Esquema do Cenário: Ordenar hotéis por diferentes critérios
    Dado que eu esteja na Trivago
    Quando eu busco hotéis em "Manaus" ordenados por "<critério>"
    Então vejo hotéis disponíveis em "Manaus"
    E o primeiro hotel apresenta nome, classificação e preço

    Exemplos:
      | critério                        |
      | Estadias em destaque            |
      | Avaliação e sugestões           |
      | Preço e sugestões               |
      | Melhores avaliações de hóspedes |

  # ---------- Autocomplete ----------

  Cenário: Receber sugestões de destino ao informar um termo
    Dado que eu esteja na Trivago
    Quando eu informo o destino "Manaus"
    Então recebo "Manaus" entre as sugestões de destino

  Cenário: Buscar hotéis em um destino informado
    Dado que eu esteja na Trivago
    Quando eu busco hotéis em "Rio de Janeiro"
    Então vejo hotéis disponíveis em "Rio de Janeiro"

  # ---------- Busca por diferentes destinos ----------

  Esquema do Cenário: Buscar hotéis em diferentes destinos
    Dado que eu esteja na Trivago
    Quando eu busco hotéis em "<destino>"
    Então vejo hotéis disponíveis em "<destino>"

    Exemplos:
      | destino       |
      | São Paulo     |
      | Gramado       |
      | Fortaleza     |

  # ---------- Filtros ----------

  Cenário: Filtrar hotéis por classificação de estrelas
    Dado que eu tenha buscado hotéis em "Manaus"
    Quando eu filtro por hotéis de "4 estrelas"
    Então todos os hotéis exibidos possuem 4 estrelas

  Cenário: Filtrar hotéis por avaliação mínima dos hóspedes
    Dado que eu tenha buscado hotéis em "Manaus"
    Quando eu filtro por avaliação mínima "8,0+"
    Então todos os hotéis exibidos têm avaliação a partir de 8,0

  Cenário: Remover o filtro aplicado mantém a lista de hotéis
    Dado que eu tenha buscado hotéis em "Manaus"
    E que eu tenha filtrado por hotéis de "4 estrelas"
    Quando eu removo os filtros aplicados
    Então nenhum filtro de estrelas permanece ativo
    E continuo vendo hotéis disponíveis

  # ---------- Fluxos negativos ----------

  Cenário: Termo sem destino correspondente oferece busca livre
    Dado que eu esteja na Trivago
    Quando eu informo o destino "xyzqwk123"
    Então recebo a opção de busca livre
    E não recebo sugestões de cidade

  Cenário: Não é possível buscar sem informar um destino
    Dado que eu esteja na Trivago
    Quando eu tento buscar sem informar um destino
    Então permaneço na página inicial para informar o destino

  # ---------- Detalhe do resultado ----------

  Cenário: Preço exibido em moeda brasileira
    Dado que eu tenha buscado hotéis em "Manaus"
    Então o preço do primeiro hotel é exibido em reais

  Cenário: Todos os hotéis apresentam informações essenciais
    Dado que eu tenha buscado hotéis em "Manaus"
    Então cada hotel exibido apresenta nome, avaliação e preço

  # ==========================================================================
  # Cenários de backlog (ainda sem step definitions — naturalmente frágeis
  # contra o site ao vivo). Para rodar apenas os implementados:
  #   mvn test -Dcucumber.filter.tags="not @backlog"
  # ==========================================================================

  @backlog
  Cenário: Ordenar hotéis do menor para o maior preço
    Dado que eu tenha buscado hotéis em "Manaus"
    Quando eu ordeno os hotéis por "Preço em ordem crescente"
    Então os hotéis são exibidos em ordem crescente de preço

  @backlog
  Cenário: Buscar hotéis para um número específico de hóspedes e quartos
    Dado que eu esteja na Trivago
    Quando eu busco hotéis em "Manaus" para 3 hóspedes e 2 quartos
    Então vejo hotéis disponíveis em "Manaus"
    E a busca considera 3 hóspedes e 2 quartos

  @backlog
  Cenário: Buscar hotéis para um período de datas
    Dado que eu esteja na Trivago
    Quando eu busco hotéis em "Manaus" para as próximas datas disponíveis
    Então vejo hotéis disponíveis em "Manaus"
    E os preços correspondem ao período selecionado
