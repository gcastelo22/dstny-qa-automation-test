package com.github.gcastelo22.ui.pages;

import com.github.gcastelo22.ui.core.BasePage;
import com.microsoft.playwright.Page;
import org.junit.Assert;

/**
 * Page Object Model representation of the Checkout Overview screen (Final Review Step).
 *
 * Handles final purchase confirmation actions and validates the order success state,
 * serving as the terminal step in the E2E shopping workflow.
 */
public class CheckoutOverviewPage extends BasePage {

    // =========================================================================================
    // Locators (CSS Selectors)
    // =========================================================================================

    /** CSS selector for the "Finish" purchase completion button. */
    private final String finishButton = "#finish";

    /** CSS selector for the success header message container displayed after placing an order. */
    private final String completeHeader = ".complete-header";

    /**
     * Constructs the CheckoutOverviewPage instance with the thread's active Playwright Page context.
     *
     * @param page Active {@link Page} reference passed from the preceding Page Object in the workflow.
     */
    public CheckoutOverviewPage(Page page) {
        super(page);
    }

    /**
     * Completes the order process by executing a click on the "Finish" button.
     *
     * @return Current {@link CheckoutOverviewPage} instance to allow fluent assertion chaining on completion.
     */
    public CheckoutOverviewPage clickFinish() {
        LOG.info("Clicking the Finish button to complete the order.");
        click(finishButton);
        return this;
    }

    /**
     * Performs final assertion to verify order placement success.
     * Extracts text from the completion header element and validates it against the expected success message.
     *
     * @param expectedMessage Expected success banner message (e.g., "Thank you for your order!").
     */
    public void verifyOrderCompletion(String expectedMessage) {
        LOG.info("Verifying order completion message.");
        String actualMessage = getText(completeHeader);
        Assert.assertEquals("Order completion message mismatch!", expectedMessage, actualMessage);
    }
}