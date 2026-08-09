# Playwright Test Automation Framework (Java)

This repository contains a hybrid End-to-End (E2E) Test Automation Framework for **Web UI** and **REST API** testing, built using **Java**, **Playwright** and **JUnit**. 

The framework is structured using the **Page Object Model (POM)** pattern with a **Fluent Interface** approach for UI testing, along with a clean **DTO-driven (Data Transfer Object)** design using **Jackson** for API testing. Configuration management is handled dynamically via **Typesafe Config (HOCON)**.

---

## 🏗️ Architecture & Features

* **UI Automation:** Automated E2E testing for the SauceDemo e-commerce web application.
* **API Automation:** E2E testing covering full CRUD lifecycle validation against the Petstore API.
* **Page Object Model (POM):** Clean separation of test logic from UI locators and actions.
* **Fluent Interface Pattern:** Method chaining within Page Objects for enhanced test readability.
* **Auto-Waiting & Resilience:** Leverages Playwright's native auto-waiting capabilities to eliminate flaky tests.
* **Dynamic Multi-Browser Support:** Easily switch execution between **Chromium**, **Firefox** and **WebKit**.
* **Environment Configuration:** Centralized HOCON config files (api-config.conf, ui-config.conf) separating test parameters from source code.
* **Automated Evidence Capture:** Takes viewport screenshots on test teardown for debugging.

---

## 🛠️ Prerequisites

Before getting started, ensure you have the following installed on your machine:

1. **Java Development Kit (JDK 17 or higher)**
2. **Apache Maven (3.8+)**
3. **Git**

---

## 🚀 Setup & Installation

1. **Clone the repository:**
   git clone https://github.com/gcastelo22/dstny-qa-automation-test.git
   cd dstny-qa-automation-test

2. **Install project dependencies and build the project:**
   mvn clean compile

3. **Install Playwright Browsers:**
   Playwright requires dedicated browser binaries to execute tests. Run the following command to download them:
   mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"

---

## ⚙️ Configuration Management

Framework settings are centrally managed using HOCON configuration files located in src/test/resources/config/:

* **ui-config.conf**: Controls target URL, browser engine (CHROMIUM, FIREFOX, WEBKIT), headless execution mode (true/false) and default action timeouts.
* **api-config.conf**: Controls base URL endpoints, HTTP timeouts, authentication keys and global headers (Content-Type, Accept).

---

## 🧪 Running Tests

You can execute tests directly from the command line using Maven.

### 1. Run the Entire Test Suite (UI & API)
mvn test

### 2. Run Only UI Automated Tests
To run all UI test suites:
mvn test "-Dtest=com.github.gcastelo22.ui.tests.**"

### 3. Run Only API Automated Tests
To run all API test suites:
mvn test "-Dtest=com.github.gcastelo22.api.tests.**"

### 4. Run a Specific Test Class
mvn test "-Dtest="PetAPITest"

---

## 📁 Project Structure

```text
src
├── test
│   ├── java
│   │   └── com
│   │       └── github
│   │           └── gcastelo22
│   │               ├── api
│   │               │   ├── core       # BaseAPITest setup and lifecycle management
│   │               │   ├── models     # DTOs (Pet, Category, Tag) for payload mapping
│   │               │   └── tests      # API E2E CRUD test suites
│   │               └── ui
│   │                   ├── core       # BaseUITest setup and BasePage Playwright wrapper
│   │                   ├── pages      # Page Objects (LoginPage, ProductsPage, CartPage, etc.)
│   │                   └── tests      # UI E2E test suites
│   └── resources
│       └── config
│           ├── api-config.conf        # API environment settings (HOCON)
│           └── ui-config.conf         # UI environment settings (HOCON)
pom.xml                                # Project dependencies and Maven build config
README.md                              # Project documentation
```

---

## 📸 Test Artifacts & Evidence

When UI tests run, execution evidence is automatically captured:
* **Screenshots:** On test completion/teardown (@After), full-page/viewport screenshots are automatically saved to target/screenshots/ named after the executing test method.

---

**Developed by Guilherme Castelo**
*Senior Quality Engineer | SDET | Data Integrity Specialist*

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/guilhermecastelo/)
[![Email](https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:lguilherme.castelo@gmail.com)	