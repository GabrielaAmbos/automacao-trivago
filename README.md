
[![Typing SVG](https://readme-typing-svg.demolab.com?font=Fira+Code&size=18&pause=1000&color=DF62F7&width=435&lines=Projeto+de+Automa%C3%A7%C3%A3o+Trivago)](https://git.io/typing-svg)

Projeto de automação de testes end-to-end (E2E) no site [trivago.com.br](https://www.trivago.com.br/), explorando a funcionalidade **"Busca por destino"**. Os testes simulam um usuário real navegando pelo site: pesquisam um destino, ordenam os resultados e validam os dados do primeiro hotel da lista.

## 🛠️ Tecnologias

| Ferramenta | Versão | Papel |
| ---------- | ------ | ----- |
| **Java** | 17+ | Linguagem |
| **Maven** | 3.9+ | Build e dependências |
| **Selenium WebDriver** | 4.27.0 | Automação do navegador |
| **Selenium Manager** | (embutido no Selenium) | Baixa o driver do navegador automaticamente |
| **Cucumber (BDD)** | 6.10.4 | Cenários em linguagem natural (Gherkin/PT) |
| **JUnit** | 4.13.2 | Runner dos testes |

> 💡 **Não é preciso baixar o ChromeDriver manualmente.** A partir do Selenium 4.6, o **Selenium Manager** detecta a versão do Chrome instalada e baixa o driver compatível sozinho.

## ✅ Pré-requisitos

- **JDK 17 ou superior**
- **Maven 3.9 ou superior**
- **Google Chrome** instalado

### Instalação no macOS (via [Homebrew](https://brew.sh))

```bash
brew install openjdk@21 maven
```

Como o OpenJDK do Homebrew é *keg-only*, adicione ao seu `~/.zshrc` para o `java`/`mvn` ficarem disponíveis no terminal:

```bash
echo 'export JAVA_HOME="/usr/local/opt/openjdk@21"' >> ~/.zshrc
echo 'export PATH="$JAVA_HOME/bin:/usr/local/opt/maven/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

> Em Apple Silicon (M1/M2/M3), o Homebrew instala em `/opt/homebrew` — ajuste os caminhos para `/opt/homebrew/opt/openjdk@21`.

Verifique a instalação:

```bash
java -version   # deve mostrar 21.x
mvn -version    # deve mostrar 3.9.x
```

## ▶️ Como executar

Na raiz do projeto:

```bash
mvn test
```

Isso baixa as dependências (na 1ª vez), inicia o Chrome, executa o cenário e fecha o navegador ao final.

### Modo headless (com ou sem janela)

Por padrão os testes rodam em **modo headless** (sem abrir janela do navegador). Para **ver** o navegador durante a execução:

```bash
HEADLESS=false mvn test
# ou
mvn test -Dheadless=false
```

| Comando | Comportamento |
| ------- | ------------- |
| `mvn test` | Headless (sem janela) — **padrão** |
| `HEADLESS=false mvn test` | Abre a janela do Chrome |
| `mvn test -Dheadless=false` | Abre a janela do Chrome |

Os relatórios de execução ficam em `target/surefire-reports/`. Em caso de falha, um screenshot do momento do erro é salvo automaticamente para facilitar o diagnóstico.

## 📋 Cenários atendidos

**Funcionalidade:** Busca de hotéis
_Como um usuário, desejo pesquisar, ordenar e filtrar hotéis por destino para visualizar os dados completos das opções que estou buscando._

Os cenários estão em [`BuscaDeHoteis.feature`](src/test/java/tests/features/BuscaDeHoteis.feature), agrupados por tema:

### Ordenação
- Buscar hotéis em Manaus **ordenados por avaliação** e validar os dados do primeiro item;
- Ordenar os resultados por **diferentes critérios** (Estadias em destaque, Avaliação e sugestões, Preço e sugestões, Melhores avaliações de hóspedes).

### Autocomplete
- O autocomplete **sugere destinos** ao digitar;
- **Selecionar um destino** a partir da lista de sugestões e pesquisar.

### Busca por diferentes destinos
- Buscar hotéis em **vários destinos** (São Paulo, Gramado, Fortaleza) e confirmar que há resultados.

### Filtros
- Filtrar resultados por **classificação de estrelas**;
- Filtrar resultados por **avaliação mínima dos hóspedes** (8,0+);
- **Remover** o filtro de estrelas mantendo a lista de resultados.

### Fluxos negativos
- Buscar por um texto **sem destino correspondente** exibe apenas a busca livre;
- Tentar pesquisar **sem informar um destino** mantém o usuário na página inicial.

### Detalhe do resultado
- O primeiro resultado exibe **preço no formato de moeda brasileira** (R$);
- **Cada hotel** da lista exibe nome, avaliação e preço.

**Exemplo (cenário principal):**

```gherkin
Cenário: Buscar hotéis ordenados por avaliação
    Dado que eu esteja na Trivago
    Quando eu busco hotéis em "Manaus" ordenados por "Avaliação e sugestões"
    Então vejo hotéis disponíveis em "Manaus"
    E o primeiro hotel apresenta nome, classificação e preço
```

> 📝 Os cenários seguem um estilo **declarativo** (descrevem a intenção — _"busco hotéis em Manaus"_) em vez de imperativo (_"digito no campo", "clico no botão"_). A mecânica de UI fica encapsulada nos step definitions e nos Page Objects.

> ℹ️ As asserções são **estruturais** (presença e formato dos dados), e não valores fixos de hotel/preço. Como o Trivago é um site ao vivo, o hotel em primeiro lugar e os preços mudam diariamente — validar valores exatos tornaria o teste instável.

### Cenários de backlog (`@backlog`)
Alguns cenários ainda **não têm step definitions** por serem naturalmente frágeis contra o site ao vivo (comparação de preço crescente, hóspedes/quartos e seleção de datas). Eles ficam marcados com a tag `@backlog`. Para rodar **apenas os cenários implementados**:

```bash
mvn test -Dcucumber.filter.tags="not @backlog"
```

> ⚠️ Como o teste roda contra o Trivago real (com proteção anti-bot), executar **muitos cenários em sequência** pode acionar limitação (throttling) da API de autocomplete e causar falhas intermitentes. Rodar por grupos (`-Dcucumber.filter.name="..."`) ou aguardar entre execuções grandes ajuda a evitar isso.

## 📁 Estrutura do projeto

```
src/test/java/
├── pages/
│   ├── elementMapper/
│   │   └── HomePageElementMapper.java   # Mapeamento dos elementos (seletores)
│   └── pageObjects/
│       └── HomePagePageObjects.java     # Ações na página (Page Object Model)
├── tests/
│   ├── features/
│   │   └── BuscaDeHoteis.feature        # Cenários em Gherkin (português)
│   ├── steps/
│   │   └── BuscaDeHoteisSteps.java      # Ligação Gherkin ↔ código
│   ├── hooks/
│   │   └── Hooks.java                   # Setup/teardown do navegador
│   └── RunTests.java                    # Runner do Cucumber
└── utils/
    ├── Browser.java                     # Gerência do WebDriver (Chrome/Firefox/Edge)
    └── providers/
        └── UrlProvider.java             # URL base da aplicação
```

O projeto segue o padrão **Page Object Model (POM)**, separando o mapeamento de elementos, as ações de página e os passos de teste.

## ⚠️ Observações

- O teste roda contra o **site real do Trivago**, que possui proteção anti-bot. Em execuções muito repetidas em sequência, a API de autocomplete do site pode limitar (throttle) as requisições e retornar resultados vazios. O projeto já trata isso com *retry* e recarga da página, mas se ocorrer uma falha isolada, aguarde cerca de 1 minuto e execute novamente.
- O navegador padrão é o **Chrome**. O suporte a Firefox e Edge também está implementado em [`Browser.java`](src/test/java/utils/Browser.java).
