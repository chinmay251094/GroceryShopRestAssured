package com.grocery.tests;

import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import static com.grocery.utils.ApiUtils.getInstance;
import static io.restassured.RestAssured.given;

public class CheckStatusTests {

    private final String baseUrl = getInstance().getDataSupplier().getBaseUrl();

    @Test
    void checkAppStatus() {
        Response response = given().get(baseUrl);

        int code = response.statusCode();

        response.getBody().prettyPrint();

        Assertions.assertThat(code).isEqualTo(200);
    }

}
