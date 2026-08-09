package com.github.gcastelo22.ui.pages;

import com.github.gcastelo22.ui.core.BasePage;
import com.microsoft.playwright.Page;

/**
 * Page Object Model representation of the SauceDemo Login Page.
 *
 * Encapsulates locators and interactions for authentication controls.
 * Implements the Fluent Interface pattern to streamline test creation for both positive
 * and negative authentication workflows.
 */
public class LoginPage extends BasePage {

    // =========================================================================================
    // Locators (CSS Selectors)
    // =========================================================================================

    /** CSS selector for the username input field. */
    private final String usernameField = "#user-name";

    /** CSS selector for the password input field. */
    private final String passwordField = "#password";

    /** CSS selector for the primary login submission button. */
    private final String loginButton = "#login-button";

    /** CSS selector for the error message banner container (using custom data-test attribute). */
    private final String errorMessage = "[data-test='error']";

    /**
     * Constructs the LoginPage instance with the thread's active Playwright Page context.
     *
     * @param page Active {@link Page} reference passed from the test setup or preceding step.
     */
    public LoginPage(Page page) {
        super(page);
    }

    /**
     * Inputs the given username into the username field.
     *
     * @param user Username credential string.
     * @return Current {@link LoginPage} instance for fluent method chaining.
     */
    public LoginPage enterUsername(String user) {
        write(usernameField, user);
        return this;
    }

    /**
     * Inputs the given password into the password field.
     *
     * @param pass Password credential string.
     * @return Current {@link LoginPage} instance for fluent method chaining.
     */
    public LoginPage enterPassword(String pass) {
        write(passwordField, pass);
        return this;
    }

    /**
     * Executes a click action on the login button and transitions to the main products view.
     *
     * @return A new {@link ProductsPage} instance representing the authenticated landing page.
     */
    public ProductsPage clickLogin() {
        LOG.info("Attempting to login...");
        click(loginButton);
        return new ProductsPage(page);
    }

    /**
     * High-level helper method performing a complete authentication sequence using standard user credentials.
     *
     * Demonstrates method chaining within the Page Object to simplify pre-condition setup in dependent test suites.
     *
     * @return A new {@link ProductsPage} instance following successful submission.
     */
    public ProductsPage loginAsStandardUser() {
        return enterUsername("standard_user")
                .enterPassword("secret_sauce")
                .clickLogin();
    }

    /**
     * Extracts and returns the text content displayed in the authentication error banner.
     * Useful for negative test case assertions (e.g., locked out user, incorrect credentials).
     *
     * @return The error message string present in the DOM or an empty string if not found.
     */
    public String getErrorMessageText() {
        return getText(errorMessage);
    }
}