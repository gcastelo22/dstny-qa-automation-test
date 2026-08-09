package com.github.gcastelo22.ui.tests;

import com.github.gcastelo22.ui.core.BaseUITest;
import com.github.gcastelo22.ui.pages.LoginPage;
import org.junit.Test;

/**
 * Functional Test Suite dedicated to Product Catalog and Inventory interactions.
 *
 * Focuses on validating item selection, shopping cart badge counter updates
 * and verification of product entries inside the cart view.
 * Unlike full E2E journeys, this suite isolates catalog functionality to provide fast feedback.
 */
public class InventoryTest extends BaseUITest {

    /**
     * Test Case: Verifies that a user can successfully add a specific item to the cart
     * and confirm its presence in the shopping cart page.
     *
     * Leverages the thread's inherited {@link #page} context from {@link BaseUITest}
     * and executes a chain of Page Object actions using the Fluent Interface pattern.
     */
    @Test
    public void shouldAddBackpackToCartSuccessfully() {
        // =========================================================================================
        // Test Data Definition
        // =========================================================================================
        String product = "Sauce Labs Backpack";

        LOG.info("Starting Inventory test: Adding " + product + " to cart.");

        // =========================================================================================
        // Fluent Execution Flow across Page Objects
        // =========================================================================================
        new LoginPage(page)
                // 1. Log in with standard user credentials -> returns ProductsPage
                .loginAsStandardUser()

                // 2. Validate inventory header title -> returns ProductsPage
                .verifyPageTitle("Products")

                // 3. Add target product to cart using dynamic selector -> returns ProductsPage
                .addProductToCart(product)

                // 4. Assert shopping cart badge counter is updated to '1' -> returns ProductsPage
                .verifyCartBadgeCount("1")

                // 5. Navigate to Shopping Cart -> returns CartPage
                .goToCart()

                // 6. Assert cart page title header -> returns CartPage
                .verifyPageTitle("Your Cart")

                // 7. Assert selected product is visible in the cart list -> returns CartPage
                .verifyProductInCart(product);

        LOG.info("Inventory test completed successfully.");
    }
}