package com.github.gcastelo22.ui.tests;

import com.github.gcastelo22.ui.core.BaseUITest;
import com.github.gcastelo22.ui.pages.LoginPage;
import org.junit.Assert;
import org.junit.Test;

/**
 * Functional Test Suite focused on Authentication scenarios.
 *
 * Verifies both valid user authentication flows and negative test cases,
 * such as error banner handling for restricted/locked-out accounts on SauceDemo.
 */
public class LoginTest extends BaseUITest {

    /**
     * Positive Test Case: Verifies that a valid standard user can authenticate successfully
     * and transition to the Products/Inventory Catalog page.
     *
     * Demonstrates seamless cross-page transition using the Fluent Interface pattern.
     */
    @Test
    public void shouldLoginSuccessfully() {
        LOG.info("Starting test: shouldLoginSuccessfully");

        // Executes authentication chain and validates the landed page header
        new LoginPage(page)
                .enterUsername("standard_user")
                .enterPassword("secret_sauce")
                .clickLogin()
                .verifyPageTitle("Products");

        LOG.info("Login successful test passed.");
    }

    /**
     * Negative Test Case: Verifies that attempting to authenticate with a locked-out user
     * prevents navigation and displays the appropriate error notification message.
     *
     * Demonstrates how the Page Object remains on the current page to extract error state
     * without breaking execution flow.
     */
    @Test
    public void shouldShowErrorForLockedOutUser() {
        LOG.info("Starting test: shouldShowErrorForLockedOutUser");

        LoginPage loginPage = new LoginPage(page);

        // Submit credentials for a known locked-out user account
        loginPage.enterUsername("locked_out_user")
                .enterPassword("secret_sauce")
                .clickLogin();

        // Validation of the specific error message displayed by SauceDemo
        String expectedError = "Sorry, this user has been locked out.";
        String actualError = loginPage.getErrorMessageText();

        Assert.assertTrue("Error message mismatch! Expected it to contain: " + expectedError,
                actualError.contains(expectedError));

        LOG.info("Locked out user test passed.");
    }
}