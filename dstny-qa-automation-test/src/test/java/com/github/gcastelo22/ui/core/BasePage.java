package com.github.gcastelo22.ui.core;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import java.util.logging.Logger;

/**
 * Abstract Base Page Object class under the Page Object Model (POM) pattern.
 *
 * Centralizes common Playwright page interactions (clicking, filling forms, reading text, scrolling)
 * and provides unified logging across all UI page objects. It abstracts direct DOM manipulation from test classes,
 * leveraging Playwright's native auto-waiting mechanisms for actionability checks.
 */
public abstract class BasePage extends BaseUITest {

    /**
     * Active Playwright {@link Page} context assigned to this Page Object.
     */
    protected Page page;

    /**
     * Logger instance for tracking user actions and DOM interactions during UI test execution.
     */
    protected static final Logger LOG = Logger.getLogger(BasePage.class.getName());

    /**
     * Constructor that binds the active Playwright Page instance to this Page Object.
     *
     * @param page The Playwright {@link Page} object passed from the test context or previous page transition.
     */
    public BasePage(Page page) {
        this.page = page;
    }

    /**
     * Explicitly waits for an element matching the given selector to become visible in the DOM.
     *
     * Note: While Playwright automatically waits for actionability before performable actions (like click or fill),
     * this explicit wait is useful for conditional assertions or waiting for asynchronous UI state changes.
     *
     * @param selector The target element locator string (CSS selector, XPath, text, etc.).
     */
    protected void waitForElementVisible(String selector) {
        LOG.info("Waiting for visibility of element: " + selector);
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
    }

    /**
     * Performs a mouse click on the specified element locator.
     * Playwright natively auto-waits for the element to be present, visible, enabled, stable and clickable before firing the event.
     *
     * @param selector The target element locator string.
     */
    protected void click(String selector) {
        LOG.info("Clicking on element: " + selector);
        page.click(selector);
    }

    /**
     * Fills an input field or textarea with the specified string value.
     *
     * The {@code page.fill()} method is preferred over legacy keystroke typing as it automatically clears
     * existing text, inputs the new value atomically and dispatches change/input DOM events to ensure React/Angular state synchronization.
     *
     * @param selector The target input locator string.
     * @param text     The string value to enter into the field.
     */
    protected void write(String selector, String text) {
        LOG.info("Writing text into element: " + selector);
        page.fill(selector, text);
    }

    /**
     * Retrieves and returns the inner text content of an element, stripped of leading/trailing whitespace.
     *
     * @param selector The target element locator string.
     * @return Trimmed inner text content of the located element.
     */
    protected String getText(String selector) {
        String text = page.innerText(selector).trim();
        LOG.info("Text retrieved from [" + selector + "]: " + text);
        return text;
    }

    /**
     * Scrolls the browser viewport until the specified element becomes visible.
     * Useful for elements lazy-loaded upon scrolling into view.
     *
     * @param selector The target element locator string.
     */
    protected void scrollToElement(String selector) {
        LOG.info("Scrolling to element: " + selector);
        page.locator(selector).scrollIntoViewIfNeeded();
    }

    /**
     * Checks if an element is currently visible on the page without throwing an exception if it is missing.
     *
     * @param selector The target element locator string.
     * @return {@code true} if the element is present and visible; {@code false} if absent, hidden or on DOM evaluation error.
     */
    protected boolean isElementPresent(String selector) {
        try {
            return page.isVisible(selector);
        } catch (Exception e) {
            LOG.warning("Element not present or error during check: " + selector);
            return false;
        }
    }
}