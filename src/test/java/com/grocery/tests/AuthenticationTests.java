package com.grocery.tests;

import com.grocery.annotations.GroceryShopTeam;
import com.grocery.constants.FrameworkConstants;
import com.grocery.enums.TestCategory;
import com.grocery.enums.Tester;
import com.grocery.utils.Utilities;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static com.grocery.builder.RequestBuilder.initiateRequest;
import static com.grocery.reports.GroceryShopReportLogger.logRequestInformation;
import static com.grocery.reports.GroceryShopReportLogger.logResponse;

public final class AuthenticationTests {
    private AuthenticationTests() {
    }

    @GroceryShopTeam(author = Tester.CHINMAY, category = {TestCategory.REGRESSION, TestCategory.SANITY})
    @Test
    void testGenerateAccessToken(Map<String, String> map) {
        String data = Utilities.readJsonFileAsString(FrameworkConstants.getRESOURCEPATH() + "/files/TokenGenerator.json");

        RequestSpecification requestSpecification = initiateRequest().buildRequestForPostCalls();
        Response response = requestSpecification.body(data).post("/api-clients");

        if (response.getStatusCode() == 201) {
            // Extract access token directly from the response
            //String accessToken = response.jsonPath().getString("accessToken");
            String accessToken = response.path("accessToken");

            // Save access token to properties file
            Utilities.saveDataToProperties("bearertoken", accessToken, FrameworkConstants.getRESOURCEPATH() + "/config/token.properties");
        } else {
            Assert.fail("Invalid request");
        }

        logRequestInformation(requestSpecification);
        logResponse(response);
    }
}
