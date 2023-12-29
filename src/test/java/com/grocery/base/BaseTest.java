package com.grocery.base;

import com.grocery.utils.EnvironmentUtils;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.testng.annotations.BeforeMethod;

import java.util.Map;

public class BaseTest {
    protected BaseTest() {
    }

    @BeforeMethod(alwaysRun = true)
    public void requestSetUp(Object[] data) {
        Map<String, String> map = (Map<String, String>) data[0];
        EnvironmentUtils.setBaseUri(map.get("BaseUri"));
        RestAssured.requestSpecification = new RequestSpecBuilder().setBaseUri(map.get("BaseUri")).build().log().all();
    }
}
