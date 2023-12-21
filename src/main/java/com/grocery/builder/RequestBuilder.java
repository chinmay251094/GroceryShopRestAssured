package com.grocery.builder;

import com.grocery.supplier.ConfigurationPOJO;
import com.grocery.utils.CommonUtils;
import io.github.sskorol.data.JsonReader;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import one.util.streamex.StreamEx;

import static io.github.sskorol.data.TestDataReader.use;
import static io.restassured.RestAssured.given;

public final class RequestBuilder implements CommonUtils {
    @Getter
    private final String baseUrl;

    private RequestBuilder() {
        this.baseUrl = getData().findFirst().map(ConfigurationPOJO::getBaseUrl).orElse(null);
    }

    public static RequestBuilder initiateRequest() {
        return new RequestBuilder();
    }

    public RequestSpecification buildRequestForGetCalls() {
        return given().baseUri(baseUrl).log().all();
    }

    public RequestSpecification buildRequestForPostCalls() {
        return buildRequestForGetCalls().header("Content-Type", ContentType.JSON);
    }

    public RequestSpecification buildRequestForPutCalls() {
        return buildRequestForGetCalls().header("Content-Type", ContentType.JSON);
    }

    public RequestSpecification buildRequestForPatchCalls() {
        return buildRequestForGetCalls().header("Content-Type", ContentType.JSON);
    }

    public RequestSpecification buildRequestForDeleteCalls() {
        return buildRequestForGetCalls().header("Content-Type", ContentType.JSON);
    }

    @Override
    public StreamEx<ConfigurationPOJO> getData() {
        return use(JsonReader.class)
                .withTarget(ConfigurationPOJO.class)
                .withSource("TestConfiguration.json")
                .read();
    }
}
