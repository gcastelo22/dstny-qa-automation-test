package com.github.gcastelo22.ui.core;

import com.microsoft.playwright.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Abstract Base Test Class for Web UI Test Automation.
 *
 * Manages the entire Playwright execution lifecycle, including driver initialization, dynamic browser launching
 * (Chromium, Firefox, WebKit), creation of isolated {@link BrowserContext} sessions, configuration loading from
 * HOCON files and automated screenshot capture during post-test teardown.
 *
 * All UI Test Suites must extend this class to inherit browser instance lifecycle management and evidence capture.
 */
public abstract class BaseUITest {

    /** Core Playwright process instance responsible for launching browser engines. */
    protected Playwright playwright;

    /** Global browser engine instance (Chromium, Firefox or WebKit). */
    protected Browser browser;

    /** Isolated incognito-like browser session ensuring complete state clean-up between tests. */
    protected BrowserContext context;

    /** Active tab/page instance where UI interactions occur. */
    protected Page page;

    /** Logger instance for tracing test lifecycle stages and browser actions. */
    protected static final Logger LOG = Logger.getLogger(BaseUITest.class.getName());

    // =========================================================================================
    // UI Environment Configurations (Loaded from HOCON file: src/test/resources/config/ui-config.conf)
    // =========================================================================================

    /** Loads application configurations dynamically from the HOCON file "config/ui-config.conf". */
    protected static final Config APP_CONFIG = ConfigFactory.load("config/ui-config.conf");

    /** Target browser type specified in the configuration (e.g., "CHROMIUM", "FIREFOX", "WEBKIT"). */
    protected static final String BROWSER_TYPE = APP_CONFIG.getString("conf.browser");

    /** Application target base URL for web testing. */
    protected static final String URL = APP_CONFIG.getString("conf.url");

    /** Flag determining whether the browser runs in headless mode (true) or headed mode (false). */
    protected static final boolean HEADLESS = APP_CONFIG.getBoolean("conf.headless");

    /**
     * JUnit Rule that captures the executing test method name at runtime.
     * Utilized for naming generated evidence screenshots dynamically.
     */
    @Rule
    public TestName testName = new TestName();

    /**
     * Pre-test setup method executed before each JUnit test method (@Test).
     *   - Initializes the Playwright driver process.
     *   - Launches the configured browser type in headless or headed mode.
     *   - Creates a clean, isolated {@link BrowserContext} to prevent cross-test cookies/session leakage.
     *   - Configures default execution timeouts and navigates the primary tab to the target URL.
     */
    @Before
    public void setUp() {
        LOG.info("Initializing Playwright engine...");
        playwright = Playwright.create();

        LOG.info("Launching browser: " + BROWSER_TYPE);
        initializeBrowser();

        // Create an isolated browser context (equivalent to an incognito window)
        context = browser.newContext();

        // Configure default action and navigation timeout from config settings
        context.setDefaultTimeout(APP_CONFIG.getDouble("conf.timeout"));

        page = context.newPage();
        LOG.info("Navigating to: " + URL);
        page.navigate(URL);
    }

    /**
     * Configures launch options and instantiates the specific {@link Browser} instance
     * based on the "conf.browser" setting in "ui-config.conf".
     *
     * @throws IllegalArgumentException If an unsupported browser type string is provided.
     */
    private void initializeBrowser() {
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(HEADLESS);

        switch (BROWSER_TYPE.toUpperCase()) {
            case "CHROMIUM" -> browser = playwright.chromium().launch(options);
            case "FIREFOX" -> browser = playwright.firefox().launch(options);
            case "WEBKIT" -> browser = playwright.webkit().launch(options);
            default -> throw new IllegalArgumentException("Browser type not supported: " + BROWSER_TYPE);
        }
    }

    /**
     * Post-test teardown method executed after each JUnit test method (@Test).
     * Captures a screenshot of the final browser page state for execution reporting/evidence
     * and disposes of all Playwright resources in reverse order of creation.
     */
    @After
    public void tearDown() {
        if (page != null) {
            takeScreenshot();
            LOG.info("Closing Playwright resources...");
            context.close();   // Closes the active session and clears cookies/cache
            browser.close();   // Terminates the browser instance
            playwright.close(); // Kills the underlying Playwright process
        }
    }

    /**
     * Captures a full/viewport PNG screenshot of the current active page state
     * and saves it into the "target/screenshots" directory using the test method name.
     */
    private void takeScreenshot() {
        String path = "target/screenshots/" + testName.getMethodName() + ".png";
        try {
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)));
            LOG.info("Screenshot successfully saved to: " + path);
        } catch (Exception e) {
            LOG.warning("Failed to save screenshot: " + e.getMessage());
        }
    }
}