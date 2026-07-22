<div align="center">

**🇺🇸 English** · [🇧🇷 Português](README.pt-BR.md)

</div>

[![Typing SVG](https://readme-typing-svg.demolab.com?font=Fira+Code&size=18&pause=1000&color=DF62F7&width=435&lines=Trivago+Automation+Project)](https://git.io/typing-svg)

End-to-end (E2E) test automation project for [trivago.com.br](https://www.trivago.com.br/), covering the **hotel search** feature. The tests simulate a real user browsing the site: they search for a destination, sort the results, apply filters and validate the data of the listed hotels.

## 🛠️ Tech stack

| Tool | Version | Role |
| ---- | ------- | ---- |
| **Java** | 17+ | Language |
| **Maven** | 3.9+ | Build & dependencies |
| **Selenium WebDriver** | 4.27.0 | Browser automation |
| **Selenium Manager** | (bundled with Selenium) | Downloads the browser driver automatically |
| **Cucumber (BDD)** | 6.10.4 | Scenarios in natural language (Gherkin/PT) |
| **JUnit** | 4.13.2 | Test runner |

> 💡 **No need to download ChromeDriver manually.** Since Selenium 4.6, **Selenium Manager** detects the installed Chrome version and downloads the matching driver on its own.

## ✅ Prerequisites

- **JDK 17 or higher**
- **Maven 3.9 or higher**
- **Google Chrome** installed

### macOS setup (via [Homebrew](https://brew.sh))

```bash
brew install openjdk@21 maven
```

Homebrew's OpenJDK is *keg-only*, so add it to your `~/.zshrc` to make `java`/`mvn` available in the terminal:

```bash
echo 'export JAVA_HOME="/usr/local/opt/openjdk@21"' >> ~/.zshrc
echo 'export PATH="$JAVA_HOME/bin:/usr/local/opt/maven/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

> On Apple Silicon (M1/M2/M3), Homebrew installs under `/opt/homebrew` — adjust the paths to `/opt/homebrew/opt/openjdk@21`.

Verify the installation:

```bash
java -version   # should show 21.x
mvn -version    # should show 3.9.x
```

## ▶️ How to run

From the project root:

```bash
mvn test
```

This downloads the dependencies (on the first run), launches Chrome, runs the scenario and closes the browser at the end.

### Headless mode (with or without a window)

By default the tests run in **headless mode** (no browser window). To **watch** the browser during execution:

```bash
HEADLESS=false mvn test
# or
mvn test -Dheadless=false
```

| Command | Behavior |
| ------- | -------- |
| `mvn test` | Headless (no window) — **default** |
| `HEADLESS=false mvn test` | Opens the browser window |
| `mvn test -Dheadless=false` | Opens the browser window |

### Choosing the browser

By default the tests run on **Chrome**. To use another browser:

| Command | Browser |
| ------- | ------- |
| `mvn test` | Chrome (default) |
| `mvn test -Dbrowser=firefox` | Firefox |
| `mvn test -Dbrowser=edge` | Edge |

You can also use an environment variable: `BROWSER=firefox mvn test`. The chosen browser must be **installed**; Selenium Manager downloads the matching driver automatically. The options combine: `mvn test -Dbrowser=firefox -Dheadless=false`.

Execution reports are written to `target/surefire-reports/`. On failure, a screenshot of the moment of the error is saved to `target/screenshots/<scenario>.png` to help with debugging.

## 📋 Covered scenarios

**Feature:** Hotel search
_As a user, I want to search, sort and filter hotels by destination so that I can see the complete data of the options I am looking for._

The scenarios live in [`BuscaDeHoteis.feature`](src/test/java/tests/features/BuscaDeHoteis.feature), grouped by theme:

### Sorting
- Search hotels in Manaus **sorted by rating** and validate the first item's data;
- Sort results by **different criteria** (Featured stays, Rating & suggestions, Price & suggestions, Best guest reviews).

### Autocomplete
- The autocomplete **suggests destinations** as you type;
- **Select a destination** from the suggestions list and search.

### Search across different destinations
- Search hotels in **several destinations** (São Paulo, Gramado, Fortaleza) and confirm there are results.

### Filters
- Filter results by **star rating**;
- Filter results by **minimum guest rating** (8.0+);
- **Remove** the star filter while keeping the results list.

### Negative flows
- Searching a term **with no matching destination** only offers the free (AI) search;
- Trying to search **without providing a destination** keeps the user on the home page.

### Result details
- The first result shows a **price in Brazilian currency** (R$);
- **Every hotel** in the list shows name, rating and price.

**Example (main scenario):**

```gherkin
Cenário: Buscar hotéis ordenados por avaliação
    Dado que eu esteja na Trivago
    Quando eu busco hotéis em "Manaus" ordenados por "Avaliação e sugestões"
    Então vejo hotéis disponíveis em "Manaus"
    E o primeiro hotel apresenta nome, classificação e preço
```

> 📝 The scenarios are written in **Portuguese Gherkin** and follow a **declarative** style (they describe intent — _"I search for hotels in Manaus"_) rather than imperative (_"I type in the field", "I click the button"_). UI mechanics are encapsulated in the step definitions and Page Objects.

> ℹ️ Assertions are **structural** (presence and format of the data), not fixed hotel/price values. Since Trivago is a live site, the top hotel and prices change daily — asserting exact values would make the test flaky.

### Backlog scenarios (`@backlog`)
Some scenarios have **no step definitions yet** because they are inherently fragile against the live site (ascending-price comparison, guests/rooms and date selection). They are tagged `@backlog`. To run **only the implemented scenarios**:

```bash
mvn test -Dcucumber.filter.tags="not @backlog"
```

> ⚠️ Since the tests run against the real Trivago (with anti-bot protection), running **many scenarios in a row** may trigger throttling of the autocomplete API and cause intermittent failures. Running by groups (`-Dcucumber.filter.name="..."`) or waiting between large runs helps avoid this.

## 📁 Project structure

```
src/test/java/
├── pages/
│   ├── elementMapper/
│   │   └── HomePageElementMapper.java   # Element mapping (selectors)
│   └── pageObjects/
│       └── HomePagePageObjects.java     # Page actions (Page Object Model)
├── tests/
│   ├── features/
│   │   └── BuscaDeHoteis.feature        # Gherkin scenarios (Portuguese)
│   ├── steps/
│   │   └── BuscaDeHoteisSteps.java      # Gherkin ↔ code glue
│   ├── hooks/
│   │   └── Hooks.java                   # Browser setup/teardown
│   └── RunTests.java                    # Cucumber runner
└── utils/
    ├── Browser.java                     # WebDriver management (Chrome/Firefox/Edge)
    └── providers/
        └── UrlProvider.java             # Application base URL
```

The project follows the **Page Object Model (POM)** pattern, separating element mapping, page actions and test steps.

## ⚠️ Notes

- The tests run against the **real Trivago site**, which has anti-bot protection. On many repeated back-to-back runs, the site's autocomplete API may throttle requests and return empty results. The project already handles this with *retry* and page reload, but if an isolated failure happens, wait about 1 minute and run again.
- The default browser is **Chrome**. Firefox and Edge are also supported via `-Dbrowser` (see [Choosing the browser](#choosing-the-browser)).
