
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

**Funcionalidade:** Buscar por um local
_Como um usuário, desejo consultar um local para visualizar os dados completos do local que estou buscando._

```gherkin
Cenário: Buscar por hotéis em Manaus ordenados por avaliação
    Dado que eu acesse o site da Trivago
    Quando eu solicito pesquisar pelo destino "Manaus"
    E ordeno a pesquisa por "Avaliação e sugestões"
    Então eu vejo os resultados da busca pelo destino "Manaus"
    E o primeiro item da lista possui nome, quantidade de estrelas e preço
```

**O que é validado:**
- A busca leva à página de resultados do destino pesquisado (Manaus);
- O primeiro hotel da lista possui **nome**, **classificação em estrelas** (entre 1 e 5) e **preço**.

> ℹ️ As asserções são **estruturais** (presença e formato dos dados), e não valores fixos de hotel/preço. Como o Trivago é um site ao vivo, o hotel em primeiro lugar e os preços mudam diariamente — validar valores exatos tornaria o teste instável.

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
