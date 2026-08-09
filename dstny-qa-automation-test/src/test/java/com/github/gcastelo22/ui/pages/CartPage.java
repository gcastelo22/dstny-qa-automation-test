package com.github.gcastelo22.ui.pages;

import com.github.gcastelo22.ui.core.BasePage;
import com.microsoft.playwright.Page;
import org.junit.Assert;

/**
 * Page Object Model representation of the Shopping Cart screen.
 *
 * Encapsulates DOM element locators, validation assertions for cart items
 * and page transitions toward the checkout workflow.
 * Implements the Fluent Interface pattern by returning "this" for assertion methods
 * to enable method chaining within test scenarios.
 */
public class CartPage extends BasePage {

    // =========================================================================================
    // Locators (CSS & Playwright Selectors)
    // =========================================================================================

    /** CSS selector for the page header title label (e.g., "Your Cart"). */
    private final String titleSpan = ".title";

    /** CSS selector for the primary checkout navigation button. */
    private final String checkoutButton = "#checkout";

    /**
     * Constructs the CartPage instance with the thread's active Playwright Page context.
     *
     * @param page Active {@link Page} reference passed from the test or preceding Page Object.
     */
    public CartPage(Page page) {
        super(page);
    }

    /**
     * Asserts that the current page header title matches the expected text value.
     *
     * @param expectedTitle Expected string content of the header (e.g., "Your Cart").
     * @return Current {@link CartPage} instance for fluent method chaining.
     */
    public CartPage verifyPageTitle(String expectedTitle) {
        Assert.assertEquals("Cart page title mismatch!", expectedTitle, getText(titleSpan));
        return this;
    }

    /**
     * Verifies whether a specific item exists and is visible inside the cart list.
     *
     * Utilizes Playwright's combined engine selector syntax (CSS + text filtering)
     * to locate inventory item names matching the exact product name dynamically.
     *
     * @param productName Name of the target product to locate in the cart.
     * @return Current {@link CartPage} instance for fluent method chaining.
     */
    public CartPage verifyProductInCart(String productName) {
        LOG.info("Verifying product in cart: " + productName);

        // Playwright dynamic selector engine combining CSS class with exact text matching
        boolean isVisible = page.isVisible(".inventory_item_name >> text='" + productName + "'");

        Assert.assertTrue("Product " + productName + " was not found in the cart!", isVisible);
        return this;
    }

    /**
     * Clicks the checkout button to proceed with the purchasing workflow.
     *
     * @return A new {@link CheckoutInfoPage} instance representing the next step in the checkout flow.
     */
    public CheckoutInfoPage clickCheckout() {
        LOG.info("Proceeding to checkout information...");
        click(checkoutButton);
        return new CheckoutInfoPage(page);
    }
}