package com.grocery.reports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.grocery.utils.Utilities;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;

import java.util.Map;

import static com.grocery.reports.GroceryShopReportManager.getExtent;

public final class GroceryShopReportLogger {
    private GroceryShopReportLogger() {
    }

    public static void pass(String message) {
        getExtent().pass(message);
    }

    public static void fail(String message) {
        getExtent().fail(message);
    }

    public static void skip(String message) {
        getExtent().skip(message);
    }

    public static void failWithDetails(String message, Throwable throwable) {
        ExtentTest test = getExtent();
        test.log(Status.FAIL, message);
        test.log(Status.FAIL, MarkupHelper.createLabel("Details:", ExtentColor.RED));
        test.log(Status.FAIL, throwable);
    }

    public static void logResponse(Response response) {
        getExtent().info(MarkupHelper.createCodeBlock(response.getBody().asString(), CodeLanguage.JSON));
    }

    public static void logRequestInformation(RequestSpecification requestSpecification) {
        QueryableRequestSpecification query = SpecificationQuerier.query(requestSpecification);
        for (Header headers : query.getHeaders()) {
            getExtent().info(headers.getName() + " " + headers.getValue());
        }

        // Log request body
        Object requestBody = query.getBody();
        if (requestBody != null) {
            if (requestBody instanceof byte[]) {
                byte[] requestBodyBytes = (byte[]) requestBody;
                String requestBodyString = new String(requestBodyBytes);
                getExtent().info("<b>" + MarkupHelper.createCodeBlock(requestBodyString, CodeLanguage.JSON) + "</b>");
            } else if (requestBody instanceof String) {
                String requestBodyString = (String) requestBody;
                getExtent().info("<b>" + MarkupHelper.createCodeBlock(requestBodyString, CodeLanguage.JSON) + "</b>");
            } else {
                getExtent().info("Request Body is of unexpected type: " + requestBody.getClass());
            }
        } else {
            getExtent().info("Request Body is empty or not present.");
        }
    }

    public static void logRequestParams(RequestSpecification requestSpecification) {
        QueryableRequestSpecification query = SpecificationQuerier.query(requestSpecification);

        // Log base path
        getExtent().info("Base Path: " + query.getBasePath());

        // Log path parameters
        Map<String, String> pathParams = query.getPathParams();
        if (pathParams != null && !pathParams.isEmpty()) {
            getExtent().info("Path Parameters used are stated below:");
            for (Map.Entry<String, String> entry : pathParams.entrySet()) {
                getExtent().info(entry.getKey() + " " + "<b>" + Utilities.encodeTextToBase64(entry.getValue()) + "</b>");
            }
        } else {
            getExtent().info("No Path Parameters.");
        }
    }
}
