
[![Typing SVG](https://readme-typing-svg.demolab.com?font=Fira+Code&pause=1000&width=435&lines=Projeto+de+Automa%C3%A7%C3%A3o+Trivago)](https://git.io/typing-svg)

Projeto de automação no site https://www.trivago.com.br/, utilizando a linguagem Java e o framework de automação Selenium WebDriver, explorando a funcionalidade 'Busca por destino'.

## Feature testada:

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


