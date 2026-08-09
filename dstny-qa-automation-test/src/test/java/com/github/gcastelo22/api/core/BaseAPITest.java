package com.github.gcastelo22.api.core;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Abstract Base Class for API Test Automation.
 *
 * Manages the lifecycle of Playwright's {@link Playwright} engine and {@link APIRequestContext},
 * loads centralized environmental configurations via HOCON (Typesafe Config), handles global HTTP headers
 * and provides reusable logging capabilities across all API test suites.
 *
 * All API test classes must extend this class to inherit setup, teardown and request context configurations.
 */
public abstract class BaseAPITest {

    /**
     * Core Playwright process instance required to spawn API request contexts.
     */
    protected Playwright playwright;

    /**
     * Isolated HTTP request context pre-configured with Base URL, default headers and timeouts.
     */
    protected APIRequestContext request;

    /**
     * Logger instance for tracing test lifecycle events and execution steps.
     */
    protected static final Logger LOG = Logger.getLogger(BaseAPITest.class.getName());

    // =========================================================================================
    // Configuration Properties (Loaded from HOCON file: src/test/resources/config/api-config.conf)
    // =========================================================================================

    /**
     * Loads application configurations dynamically from the HOCON file "config/api-config.conf".
     */
    protected static final Config APP_CONFIG = ConfigFactory.load("config/api-config.conf");

    /**
     * Target Base URL for API endpoints under test.
     */
    protected static final String BASE_URL = APP_CONFIG.getString("api.base_url");

    /**
     * HTTP request timeout limit in milliseconds.
     */
    protected static final double TIMEOUT = APP_CONFIG.getDouble("api.timeout");

    /**
     * Global "Content-Type" header value (e.g., "application/json").
     */
    protected static final String CONTENT_TYPE = APP_CONFIG.getString("api.headers.content_type");

    /**
     * Global "Accept" header value (e.g., "application/json").
     */
    protected static final String ACCEPT = APP_CONFIG.getString("api.headers.accept");

    /**
     * JUnit Rule that provides access to the currently executing test method name at runtime.
     * Useful for dynamic logging and reporting within individual test executions.
     */
    @Rule
    public TestName testName = new TestName();

    /**
     * Pre-test setup method executed before each JUnit test method (@Test).
     *    - Initializes the core Playwright driver engine.
     *    - Constructs global HTTP headers (Content-Type, Accept, optional API Key).
     *    - Spawns a new {@link APIRequestContext} with Base URL and timeout rules.
     *
     */
    @Before
    public void setUp() {
        LOG.info("Initializing Playwright API engine...");
        playwright = Playwright.create();

        // Assemble default HTTP request headers
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", CONTENT_TYPE);
        headers.put("Accept", ACCEPT);

        // Dynamically append API key authentication header if configured and non-empty
        if (APP_CONFIG.hasPath("auth.api_key") && !APP_CONFIG.getString("auth.api_key").isEmpty()) {
            headers.put("api_key", APP_CONFIG.getString("auth.api_key"));
        }

        LOG.info("Creating API request context for base URL: " + BASE_URL);

        // Instantiate the request context with isolated options
        request = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL(BASE_URL)
                .setTimeout(TIMEOUT)
                .setExtraHTTPHeaders(headers));
    }

    /**
     * Post-test cleanup method executed after each JUnit test method (@Test).
     * Disposes of the active {@link APIRequestContext} and closes the {@link Playwright} process
     * to prevent socket leaks and free system memory.
     */
    @After
    public void tearDown() {
        if (request != null) {
            LOG.info("Closing Playwright API resources...");
            request.dispose();  // Disposes the context and closes pending connections
            playwright.close(); // Terminates the underlying Playwright engine process
        }
    }

    /**
     * Utility method to format and output API response details to standard output.
     * Helpful for debugging payload responses and HTTP status codes during test runs.
     *
     * @param testName   The name of the test executing the call (can use {@code testName.getMethodName()}).
     * @param httpMethod The HTTP verb executed (e.g., "GET", "POST", "PUT", "DELETE").
     * @param response   The {@link APIResponse} object returned by the Playwright request.
     */
    protected void logResponse(String testName, String httpMethod, APIResponse response) {
        System.out.println("==================================================");
        System.out.printf("[%s] %s %s%n", testName, httpMethod, response.url());
        System.out.printf("Status Code: %d (%s)%n", response.status(), response.statusText());

        // Only attempt to print the body if a non-empty response payload is returned
        if (response.text() != null && !response.text().isEmpty()) {
            System.out.println("Response Body: " + response.text());
        }
        System.out.println("==================================================\n");
    }
}