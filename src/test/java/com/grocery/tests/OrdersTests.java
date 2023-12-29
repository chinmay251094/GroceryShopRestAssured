package com.grocery.tests;

import com.grocery.annotations.GroceryShopTeam;
import com.grocery.base.BaseTest;
import com.grocery.constants.FrameworkConstants;
import com.grocery.enums.TestCategory;
import com.grocery.enums.Tester;
import com.grocery.reports.GroceryShopReportLogger;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.Map;

import static com.grocery.utils.Utilities.readJsonFileAsString;
import static io.restassured.RestAssured.given;
import static io.restassured.config.LogConfig.logConfig;
import static io.restassured.config.RestAssuredConfig.config;

public final class OrdersTests extends BaseTest {
    private OrdersTests() {
    }

    @Test
    @GroceryShopTeam(author = Tester.CHINMAY, category = {TestCategory.REGRESSION, TestCategory.SANITY})
    void testCreateNewOrder(Map<String, String> map) {
        RequestSpecification requestSpecification = given()
                .config(config().logConfig(logConfig().blacklistHeader("Authorization")))
                .log().all()
                .header("Authorization", map.get("Token"))
                .body(readJsonFileAsString(FrameworkConstants.getRESOURCEPATH() + "/files/OrderCreator.json"));

        Response response = requestSpecification.post("/orders");

        GroceryShopReportLogger.logRequestInformation(requestSpecification);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(201);
    }
}
