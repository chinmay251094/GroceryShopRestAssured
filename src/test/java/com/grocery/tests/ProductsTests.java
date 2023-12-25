package com.grocery.tests;

import com.grocery.annotations.GroceryShopTeam;
import com.grocery.constants.FrameworkConstants;
import com.grocery.enums.TestCategory;
import com.grocery.enums.Tester;
import com.grocery.pojo.ProductIdentifier;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static com.grocery.builder.RequestBuilder.initiateRequest;
import static com.grocery.reports.GroceryShopReportLogger.logRequestInformation;
import static com.grocery.reports.GroceryShopReportLogger.logResponse;
import static com.grocery.utils.Utilities.*;
import static com.grocery.utils.VerificationUtils.runAndVerifyMandatoryPass;

public final class ProductsTests {
    private ProductsTests() {
        super();
    }

    @GroceryShopTeam(author = Tester.CHINMAY, category = TestCategory.SANITY)
    @Test
    void testGetAllProducts(Map<String, String> map) {
        RequestSpecification requestSpecification = initiateRequest().buildRequestForGetCalls().pathParam("products", "products");

        Response response = requestSpecification.get("/{products}");

        logResponse(response);

        Assertions.assertThat(response.statusCode()).isEqualTo(200);
    }

    @GroceryShopTeam(author = Tester.CHINMAY, category = TestCategory.SANITY)
    @Test
    void testGetAllProductsForCategory(Map<String, String> map) {
        String productCategory = map.get("Product Category");
        Response response = initiateRequest().buildRequestForGetCalls()
                .queryParam("category", productCategory)
                .log().all()
                .get("/products");

        logResponse(response);

        List<String> categories = extractAttributeFromJson(response, "category");

        Assertions.assertThat(categories)
                .as("All categories should be 'fresh-produce'")
                .allMatch(category -> category.equals(productCategory));
    }

    @GroceryShopTeam(author = Tester.CHINMAY, category = TestCategory.SANITY)
    @Test
    void testGetSpecificProduct(Map<String, String> map) {
        final Response response = initiateRequest().buildRequestForGetCalls()
                .pathParam("productId", map.get("Product Id"))
                .log().all()
                .get("/products/{productId}");

        logResponse(response);
    }

    @GroceryShopTeam(author = Tester.CHINMAY, category = TestCategory.SANITY)
    @Test
    void testGetSpecificNumberOfProducts(Map<String, String> map) {
        final Response response = initiateRequest()
                .buildRequestForGetCalls()
                .queryParam("results", map.get("Results"))
                .get("/products");

        if (response.getStatusCode() == 200) {
            List<Map<String, Object>> responseData = deserializeResponse(response.getBody().asString());

            String filePath = "src/test/resources/Products.json";

            writeDataToJsonFile(responseData, filePath);

            System.out.println("Data successfully written to " + filePath);
        } else {
            System.err.println("Failed to retrieve data. Status code: " + response.getStatusCode());
        }

        logResponse(response);

        runAndVerifyMandatoryPass(() -> {
            response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("files/ProductsSchema.json"));
        }, "Schema does not match.");

        Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @SneakyThrows
    @GroceryShopTeam(author = Tester.CHINMAY, category = TestCategory.SANITY)
    @Test
    void testAddItemsToCart(Map<String, String> map) {
        List<ProductIdentifier> productIdentifiers = readJsonData(FrameworkConstants.getRESOURCEPATH() + "/files/ItemsForCart.json", ProductIdentifier.class);
        ProductIdentifier productIdentifier = productIdentifiers.get(0);

        RequestSpecification requestSpecification = initiateRequest()
                .buildRequestForPostCalls()
                .pathParam("cartId", map.get("Cart Id"));

        Response response = requestSpecification.body(productIdentifier)
                .post("/carts/{cartId}/items");

        storeResponseToJsonFile(response, FrameworkConstants.getRESOURCEPATH() + "/files/ResponseData.json");

        logRequestInformation(requestSpecification);
    }
}
