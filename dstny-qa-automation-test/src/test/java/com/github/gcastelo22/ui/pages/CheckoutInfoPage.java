package com.github.gcastelo22.ui.pages;

import com.github.gcastelo22.ui.core.BasePage;
import com.microsoft.playwright.Page;

/**
 * Page Object Model representation of the initial Checkout Step (Customer Information Page).
 *
 * Handles form interaction for entering customer details (first name, last name, postal code)
 * and controls page transition to the Checkout Overview screen.
 */
public class CheckoutInfoPage extends BasePage {

    // =========================================================================================
    // Locators (CSS Selectors)
    // =========================================================================================

    /** CSS selector for the customer's first name input field. */
    private final String firstNameField = "#first-name";

    /** CSS selector for the customer's last name input field. */
    private final String lastNameField = "#last-name";

    /** CSS selector for the postal/zip code input field. */
    private final String zipCodeField = "#postal-code";

    /** CSS selector for the "Continue" navigation button. */
    private final String continueButton = "#continue";

    /**
     * Constructs the CheckoutInfoPage instance with the thread's active Playwright Page context.
     *
     * @param page Active {@link Page} reference passed from the preceding Page Object in the workflow.
     */
    public CheckoutInfoPage(Page page) {
        super(page);
    }

    /**
     * Fills out the complete customer information form and submits it to proceed to the checkout overview.
     *
     * Utilizes inherited {@link BasePage#write(String, String)} helper methods, which leverage
     * Playwright's atomic {@code page.fill()} behavior to ensure input fields are cleared and
     * DOM state change events are correctly dispatched.
     *
     * @param first The customer's first name string.
     * @param last  The customer's last name string.
     * @param zip   The postal or ZIP code string.
     * @return A new {@link CheckoutOverviewPage} instance representing the next step in the purchasing flow.
     */
    public CheckoutOverviewPage fillInformation(String first, String last, String zip) {
        LOG.info("Entering checkout information for: " + first + " " + last);
        write(firstNameField, first);
        write(lastNameField, last);
        write(zipCodeField, zip);

        LOG.info("Clicking continue to go to Overview.");
        click(continueButton);

        return new CheckoutOverviewPage(page);
    }
}