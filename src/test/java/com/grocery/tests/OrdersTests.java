package com.grocery.tests;

import com.grocery.annotations.GroceryShopTeam;
import com.grocery.constants.FrameworkConstants;
import com.grocery.enums.TestCategory;
import com.grocery.enums.Tester;
import com.grocery.reports.GroceryShopReportLogger;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.Map;

import static com.grocery.builder.RequestBuilder.initiateRequest;
import static com.grocery.utils.Utilities.readJsonFileAsString;

public final class OrdersTests {
    private OrdersTests() {
    }

    @Test
    @GroceryShopTeam(author = Tester.CHINMAY, category = {TestCategory.REGRESSION, TestCategory.SANITY})
    void testCreateNewOrder(Map<String, String> map) {
        String orderData = readJsonFileAsString(FrameworkConstants.getRESOURCEPATH() + "/files/OrderCreator.json");

        RequestSpecification requestSpecification = initiateRequest().
                buildRequestForPostCallsWithAuthorization().
                header("Authorization", map.get("Token"));

        Response response = requestSpecification.body(orderData).post("/orders");

        GroceryShopReportLogger.logRequestInformation(requestSpecification);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(201);
    }
}
