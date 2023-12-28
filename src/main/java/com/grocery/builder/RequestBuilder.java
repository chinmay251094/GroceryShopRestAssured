package com.grocery.builder;

import com.grocery.enums.ConfigKey;
import com.grocery.utils.PropertyUtils;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;

import static io.restassured.RestAssured.given;

public final class RequestBuilder {
    private static final String CONTENT_TYPE = "Content-Type";
    @Getter
    private final String baseUrl;

    private RequestBuilder() {
        this.baseUrl = PropertyUtils.get(ConfigKey.BASEURL);
    }

    public static RequestBuilder initiateRequest() {
        return new RequestBuilder();
    }

    public RequestSpecification buildRequestForGetCalls() {
        return given().baseUri(baseUrl).log().all();
    }

    public RequestSpecification buildRequestForPostCalls() {
        return buildRequestForGetCalls().header(CONTENT_TYPE, ContentType.JSON);
    }

    public RequestSpecification buildRequestForPostCallsWithAuthorization() {
        return given().
                baseUri(baseUrl).
                header(CONTENT_TYPE, ContentType.JSON).
                config(RestAssuredConfig.config().
                        logConfig(LogConfig.logConfig().
                                blacklistHeader("Authorization"))).log().all();
    }


    public RequestSpecification buildRequestForPutCalls() {
        return buildRequestForGetCalls().header(CONTENT_TYPE, ContentType.JSON);
    }

    public RequestSpecification buildRequestForPatchCalls() {
        return buildRequestForGetCalls().header(CONTENT_TYPE, ContentType.JSON);
    }

    public RequestSpecification buildRequestForDeleteCalls() {
        return buildRequestForGetCalls().header(CONTENT_TYPE, ContentType.JSON);
    }
}
