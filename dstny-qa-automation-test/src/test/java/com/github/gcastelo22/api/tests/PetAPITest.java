package com.github.gcastelo22.api.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gcastelo22.api.core.BaseAPITest;
import com.github.gcastelo22.api.models.Pet;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.Collections;

/**
 * End-to-End API Test Suite covering the complete CRUD lifecycle of the Pet resource.
 *
 * Extends {@link BaseAPITest} to leverage initialized Playwright request contexts, logging and configurations.
 * Uses JUnit's {@link FixMethodOrder} with lexicographical sorting to execute tests sequentially (Create -> Read -> Update -> Delete).
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PetAPITest extends BaseAPITest {

    /**
     * Dynamically generated unique identifier based on current system timestamp
     * to prevent primary key collision issues during test suite execution.
     */
    private static final long PET_ID = System.currentTimeMillis();

    /** Base test name used across creation and retrieval assertions. */
    private static final String PET_NAME = "Bobi";

    /**
     * Jackson ObjectMapper instance used for converting Java DTO objects to JSON strings
     * (serialization) and API JSON response strings back to Java DTOs (deserialization).
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Test Case 1: CREATE (POST)
     *
     * Sends a POST request to create a new Pet entity, validates successful 200 OK status
     * and deserializes the response payload to verify ID and property integrity.
     *
     * @throws IOException If JSON serialization or parsing fails.
     */
    @Test
    public void test1CreatePet() throws IOException {
        // Build request payload using DTO
        Pet newPet = new Pet(PET_ID, PET_NAME, "available");
        newPet.setCategory(new Pet.Category(1L, "Dogs"));
        newPet.setPhotoUrls(Collections.singletonList("https://example.com/dog.jpg"));

        // Execute HTTP POST request with serialized JSON body
        APIResponse response = request.post("pet", RequestOptions.create()
                .setData(objectMapper.writeValueAsString(newPet)));

        // Log execution summary to standard output
        logResponse("Create Pet", "POST", response);

        // Assertions
        Assert.assertEquals("Status code should be 200 OK", 200, response.status());
        Assert.assertTrue("Response should indicate success", response.ok());

        // Deserialize response body and validate returned fields
        Pet createdPet = objectMapper.readValue(response.text(), Pet.class);
        Assert.assertNotNull(createdPet);
        Assert.assertEquals(Long.valueOf(PET_ID), createdPet.getId());
        Assert.assertEquals(PET_NAME, createdPet.getName());
    }

    /**
     * Test Case 2: READ (GET)
     *
     * Fetches the previously created Pet entity by its unique ID, validates 200 OK HTTP status
     * and verifies that the retrieved entity details match expectations.
     *
     * @throws IOException If JSON deserialization fails.
     */
    @Test
    public void test2GetPetById() throws IOException {
        // Execute HTTP GET request targeting specific endpoint resource ID
        APIResponse response = request.get("pet/" + PET_ID);

        logResponse("Get Pet By ID", "GET", response);

        // Assertions
        Assert.assertEquals("Status code should be 200 OK", 200, response.status());

        // Deserialize and assert retrieved values
        Pet fetchedPet = objectMapper.readValue(response.text(), Pet.class);
        Assert.assertNotNull(fetchedPet);
        Assert.assertEquals(Long.valueOf(PET_ID), fetchedPet.getId());
        Assert.assertEquals(PET_NAME, fetchedPet.getName());
    }

    /**
     * Test Case 3: UPDATE (PUT)
     *
     * Modifies existing Pet attributes (name and status) via HTTP PUT,
     * validates 200 OK response status and confirms state mutation on the server side.
     *
     * @throws IOException If JSON serialization or deserialization fails.
     */
    @Test
    public void test3UpdatePet() throws IOException {
        String updatedName = "Bobi Updated";
        String updatedStatus = "sold";

        // Build updated payload maintaining identical PET_ID
        Pet updatedPetData = new Pet(PET_ID, updatedName, updatedStatus);
        updatedPetData.setCategory(new Pet.Category(1L, "Dogs"));

        // Execute HTTP PUT request
        APIResponse response = request.put("pet", RequestOptions.create()
                .setData(objectMapper.writeValueAsString(updatedPetData)));

        logResponse("Update Pet", "PUT", response);

        // Assertions
        Assert.assertEquals("Status code should be 200 OK", 200, response.status());

        // Deserialize and verify updated properties
        Pet responsePet = objectMapper.readValue(response.text(), Pet.class);
        Assert.assertEquals(Long.valueOf(PET_ID), responsePet.getId());
        Assert.assertEquals(updatedName, responsePet.getName());
        Assert.assertEquals(updatedStatus, responsePet.getStatus());
    }

    /**
     * Test Case 4: DELETE & VERIFY
     *
     * Sends an HTTP DELETE request to remove the Pet entity by ID, asserts 200 OK deletion
     * and executes a subsequent GET request to confirm the resource returns a 404 Not Found.
     */
    @Test
    public void test4DeletePet() {
        // Execute HTTP DELETE request
        APIResponse response = request.delete("pet/" + PET_ID);

        logResponse("Delete Pet", "DELETE", response);

        Assert.assertEquals("Status code should be 200 OK", 200, response.status());

        // Verify entity removal with a follow-up GET request expecting HTTP 404
        APIResponse getResponse = request.get("pet/" + PET_ID);
        logResponse("Verify Pet Deleted (404 Expected)", "GET", getResponse);

        Assert.assertEquals("Status code should be 404 Not Found", 404, getResponse.status());
    }
}