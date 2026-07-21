#language: pt
Funcionalidade: Buscar por um local
  Como um usuário
  Desejo consultar um local
  Para que eu possa visualizar os dados completos do local que estou buscando

  Cenário: Buscar por hotéis em Manaus ordenados por avaliação
      Dado que eu acesse o site da Trivago
      Quando eu solicito pesquisar pelo destino "Manaus"
      E ordeno a pesquisa por "Avaliação e sugestões"
      Então eu vejo os resultados da busca pelo destino "Manaus"
      E o primeiro item da lista possui nome, quantidade de estrelas e preço
