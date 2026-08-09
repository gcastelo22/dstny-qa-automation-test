package com.github.gcastelo22.ui.tests;

import com.github.gcastelo22.ui.core.BaseUITest;
import com.github.gcastelo22.ui.pages.LoginPage;
import org.junit.Test;

/**
 * End-to-End (E2E) Test Suite verifying the complete E-Commerce Purchase Journey on SauceDemo.
 *
 * Extends {@link BaseUITest} to inherit Playwright engine lifecycle management (browser launch,
 * isolated context creation, base URL navigation and automated teardown/screenshot generation).
 * Validates the primary critical path: User Authentication -> Product Catalog Selection ->
 * Shopping Cart Verification -> Customer Checkout Information -> Order Finalization.
 */
public class E2EPurchaseTest extends BaseUITest {

    /**
     * Test Case: Complete E2E Purchase Flow.
     *
     * Demonstrates the power and readability of the Fluent Interface pattern combined with
     * the Page Object Model (POM). Every method call either performs an action returning
     * the next Page Object or executes a fluent assertion returning {@code this}.
     */
    @Test
    public void shouldCompleteFullPurchaseFlow() {
        // =========================================================================================
        // Test Data Definition
        // =========================================================================================
        String productName = "Sauce Labs Backpack";
        String firstName = "Guilherme";
        String lastName = "Castelo";
        String zipCode = "1234-567";
        String successMessage = "Thank you for your order!";

        LOG.info("Starting E2E Purchase Flow for product: " + productName);

        // =========================================================================================
        // Fluent Execution Flow across Page Objects
        // =========================================================================================
        new LoginPage(page)
                // 1. Authenticate with standard user credentials -> returns ProductsPage
                .loginAsStandardUser()

                // 2. Validate inventory header title -> returns ProductsPage
                .verifyPageTitle("Products")

                // 3. Dynamically add target item to cart -> returns ProductsPage
                .addProductToCart(productName)

                // 4. Assert shopping cart badge updated to 1 item -> returns ProductsPage
                .verifyCartBadgeCount("1")

                // 5. Navigate to Shopping Cart -> returns CartPage
                .goToCart()

                // 6. Assert cart page header -> returns CartPage
                .verifyPageTitle("Your Cart")

                // 7. Assert selected product is listed in the cart -> returns CartPage
                .verifyProductInCart(productName)

                // 8. Proceed to checkout -> returns CheckoutInfoPage
                .clickCheckout()

                // 9. Fill customer form and continue -> returns CheckoutOverviewPage
                .fillInformation(firstName, lastName, zipCode)

                // 10. Click finish to confirm purchase -> returns CheckoutOverviewPage
                .clickFinish()

                // 11. Final Assertion: Verify success completion message -> returns void
                .verifyOrderCompletion(successMessage);

        LOG.info("E2E Purchase Flow completed successfully.");
    }
}