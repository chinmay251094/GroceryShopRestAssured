package com.grocery.tests;

import com.github.javafaker.Faker;
import com.grocery.annotations.GroceryShopTeam;
import com.grocery.base.BaseTest;
import com.grocery.constants.FrameworkConstants;
import com.grocery.enums.TestCategory;
import com.grocery.enums.Tester;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static com.grocery.reports.GroceryShopReportLogger.logRequestInformation;
import static com.grocery.reports.GroceryShopReportLogger.logResponse;
import static com.grocery.utils.Utilities.saveResponseToProperties;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.matchesPattern;

public final class AuthenticationTests extends BaseTest {
    private AuthenticationTests() {
    }

    @Test
    @GroceryShopTeam(author = Tester.CHINMAY, category = {TestCategory.REGRESSION, TestCategory.SANITY})
    void testGenerateAccessToken(Map<String, String> map) {
        // Create a Faker instance
        Faker faker = new Faker();
        // Generate random data for 'clientName' and 'clientEmail'
        String clientName = faker.name().firstName();
        String clientEmail = clientName.toLowerCase().concat("@groceryshop.in");

        // Build a JSON object
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("clientName", clientName);
        jsonObject.put("clientEmail", clientEmail);

        // Convert the JSON object to a string
        String payload = jsonObject.toString();

        RequestSpecification requestSpecification = given();
        Response response = requestSpecification.body(payload).post("/api-clients");

        response.then().log().all();

        response.then().assertThat().body("accessToken", matchesPattern("^[a-z0-9]{64}$"));
        response.then().assertThat().body("accessToken", hasLength(64));

        if (response.getStatusCode() == 201) {
            // Extract access token directly from the response
            //String accessToken = response.jsonPath().getString("accessToken");
            String accessToken = response.path("accessToken");

            // Save access token to properties file
            saveResponseToProperties("bearertoken", accessToken, FrameworkConstants.getRESOURCEPATH() + "/config/token.properties");
        } else {
            Assert.fail("Invalid request");
        }

        logRequestInformation(requestSpecification);
        logResponse(response);
    }
}
