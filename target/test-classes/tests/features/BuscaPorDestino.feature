#language: pt
Funcionalidade: Buscar por um local
  Como um usuário
  Desejo consultar um local
  Para que eu possa visualizar os dados completos do local que estou buscando

  Cenário: Buscar por hotéis em Manaus com uma boa avaliação e preço
      Dado que eu acesse o site da Trivago
      Quando eu solicito pesquisar pelo destino "Manaus"
      E ordeno a pesquisa por "Avaliação e sugestões"
      Então eu vejo os dados do primeiro item da lista
        | Nome             | Avaliação | Valor |
        | Sleep Inn Manaus | 3         | R$138 |