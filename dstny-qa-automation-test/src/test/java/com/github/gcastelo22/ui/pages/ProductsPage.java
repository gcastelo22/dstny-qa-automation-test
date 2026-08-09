package com.github.gcastelo22.ui.pages;

import com.github.gcastelo22.ui.core.BasePage;
import com.microsoft.playwright.Page;
import org.junit.Assert;

/**
 * Page Object Model representation of the Products/Inventory Catalog screen.
 *
 * Provides capabilities to verify header titles, dynamically add items to the shopping cart,
 * validate the cart badge counter and navigate directly to the Shopping Cart page.
 * Implements the Fluent Interface pattern by returning "this" for action and assertion methods.
 */
public class ProductsPage extends BasePage {

    // =========================================================================================
    // Locators (CSS Selectors)
    // =========================================================================================

    /** CSS selector for the inventory page title header label (e.g., "Products"). */
    private final String titleSpan = ".title";

    /** CSS selector for the shopping cart counter badge overlaying the cart icon. */
    private final String shoppingCartBadge = ".shopping_cart_badge";

    /** CSS selector for the primary shopping cart navigation link/icon. */
    private final String cartLink = ".shopping_cart_link";

    /**
     * Constructs the ProductsPage instance with the thread's active Playwright Page context.
     *
     * @param page Active {@link Page} reference passed from the preceding Page Object or test setup.
     */
    public ProductsPage(Page page) {
        super(page);
    }

    /**
     * Asserts that the current inventory page header title matches the expected text value.
     *
     * @param expectedTitle Expected string content of the header (e.g., "Products").
     * @return Current {@link ProductsPage} instance for fluent method chaining.
     */
    public ProductsPage verifyPageTitle(String expectedTitle) {
        Assert.assertEquals("Page title mismatch!", expectedTitle, getText(titleSpan));
        return this;
    }

    /**
     * Dynamically constructs the HTML ID selector based on the provided product name
     * and triggers a click action to add the item to the shopping cart.
     *
     * Converts raw product titles (e.g., "Sauce Labs Backpack") into SauceDemo's DOM
     * attribute format (e.g., "#add-to-cart-sauce-labs-backpack").
     *
     * @param productName Display name of the product to add to the cart.
     * @return Current {@link ProductsPage} instance for fluent method chaining.
     */
    public ProductsPage addProductToCart(String productName) {
        LOG.info("Adding product to cart: " + productName);

        // Converts product name to the standard ID convention used by SauceDemo DOM
        String buttonId = "#add-to-cart-" + productName.toLowerCase().replace(" ", "-");
        click(buttonId);

        return this;
    }

    /**
     * Asserts that the shopping cart badge count matches the expected number of items.
     *
     * @param expectedCount Expected item count string displayed on the badge (e.g., "1", "2").
     * @return Current {@link ProductsPage} instance for fluent method chaining.
     */
    public ProductsPage verifyCartBadgeCount(String expectedCount) {
        Assert.assertEquals("Cart badge count mismatch!", expectedCount, getText(shoppingCartBadge));
        return this;
    }

    /**
     * Clicks the shopping cart icon to navigate to the Cart view.
     *
     * Leverages Playwright's built-in auto-waiting mechanism to handle navigation without requiring
     * explicit URL assertions or manual thread pauses.
     *
     * @return A new {@link CartPage} instance representing the shopping cart view.
     */
    public CartPage goToCart() {
        LOG.info("Navigating to the shopping cart.");
        click(cartLink);
        return new CartPage(page);
    }
}