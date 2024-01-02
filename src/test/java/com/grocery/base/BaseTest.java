package com.grocery.base;

import com.grocery.annotations.MustExtendBaseTest;
import com.grocery.exceptions.GroceryShopException;
import com.grocery.utils.EnvironmentUtils;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;

import java.util.Map;

import static org.hamcrest.Matchers.*;

@MustExtendBaseTest
public class BaseTest {
    protected BaseTest() {
    }

    @BeforeMethod(alwaysRun = true)
    public void requestSetUp(Object[] data) {
        Map<String, String> map = (Map<String, String>) data[0];

        EnvironmentUtils.setBaseUri(map.get("BaseUri"));

        RestAssured.requestSpecification = new RequestSpecBuilder().
                setContentType(ContentType.JSON).
                setBaseUri(map.get("BaseUri")).
                log(LogDetail.ALL).build();

        try {
            RestAssured.responseSpecification = new ResponseSpecBuilder().
                    expectStatusCode(Integer.parseInt(map.get("Expected Status Code"))).
                    expectHeader("Content-Type", is(anyOf(equalTo("application/json; charset=utf-8"), nullValue()))).build();
        } catch (NumberFormatException e) {
            // Handling the case where the expected status code is either not a valid number or not mentioned
            System.err.println("Error: Invalid or missing expected status code in test data.");
            throw new GroceryShopException("Invalid or missing expected status code in test data.");
        }
    }
}
