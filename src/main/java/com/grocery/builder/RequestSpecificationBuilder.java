package com.grocery.builder;

import io.restassured.authentication.AuthenticationScheme;
import io.restassured.http.Headers;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import org.apache.http.client.HttpClient;

public final class RequestSpecificationBuilder {
    static private QueryableRequestSpecification query;

    private RequestSpecificationBuilder() {
    }

    public static Headers getRequestHeaders(RequestSpecification requestSpecification) {
        query = SpecificationQuerier.query(requestSpecification);
        return query.getHeaders();
    }

    public static Headers getRequestBody(RequestSpecification requestSpecification) {
        query = SpecificationQuerier.query(requestSpecification);
        return query.getBody();
    }

    public static HttpClient getRequestHttpClient(RequestSpecification requestSpecification) {
        query = SpecificationQuerier.query(requestSpecification);
        return query.getHttpClient();
    }

    public static AuthenticationScheme getAuthenticationScheme(RequestSpecification requestSpecification) {
        query = SpecificationQuerier.query(requestSpecification);
        return query.getAuthenticationScheme();
    }
}
