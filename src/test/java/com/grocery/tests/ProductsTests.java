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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.grocery.builder.RequestBuilder.initiateRequest;
import static com.grocery.reports.GroceryShopReportLogger.logRequestInformation;
import static com.grocery.reports.GroceryShopReportLogger.logResponse;
import static com.grocery.utils.Utilities.*;
import static com.grocery.utils.VerificationUtils.runAndVerifyMandatoryPass;
import static org.hamcrest.Matchers.*;

public final class ProductsTests {
    private ProductsTests() {
        super();
    }

    @GroceryShopTeam(author = Tester.CHINMAY, category = TestCategory.SANITY)
    @Test
    void testGetAllProducts(Map<String, String> map) {
        RequestSpecification requestSpecification = initiateRequest().buildRequestForGetCalls().pathParam("products", "products");

        Response response = requestSpecification.get("/{products}");

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

        response.then().assertThat().body("category", hasItems("fresh-produce"));

        Assertions.assertThat(categories)
                .as("All categories should be 'fresh-produce'")
                .allMatch(category -> category.equals(productCategory));
    }

    @Test
    @GroceryShopTeam(author = Tester.CHINMAY, category = TestCategory.SANITY)
    void testGetAllProductsForCategoryOutOfStock(Map<String, String> map) {
        Response response = initiateRequest().buildRequestForGetCalls().when().
                queryParam("available", map.get("Availability")).
                queryParam("category", map.get("Product Category")).
                get("/products");

        response.then().log().body().
                assertThat().
                statusCode(200).
                body("category", hasItems("fresh-produce"),
                        "inStock", hasItems(false),
                        "name[0]", is(equalTo("Cucumber Organic")),
                        "category", everyItem(startsWith("fresh")),
                        "[0]", hasKey("id"),
                        "[0]", hasKey("category"),
                        "[0]", hasKey("name"),
                        "[0]", hasKey("inStock"),
                        "[0]", hasValue(false),
                        "[0]", hasValue("fresh-produce"),
                        "[0]", hasEntry("category", "fresh-produce"),
                        "[0]", not(emptyArray()),
                        "[0]", not(equalTo(Collections.EMPTY_MAP)),
                        "category", everyItem(containsString("produce"))
                );

        logResponse(response);
    }

    @Test
    @GroceryShopTeam(author = Tester.CHINMAY, category = TestCategory.SANITY)
    void testGetAllProductsForCategoryInStock(Map<String, String> map) {
        Response response = initiateRequest().buildRequestForGetCalls().when().
                queryParam("available", map.get("Availability")).
                queryParam("category", map.get("Product Category")).
                get("/products");

        Object name = response.path("name[0]");
        System.out.println("-----Fetching first object's value-----");
        System.out.println("name = " + name);

        response.then().log().all().
                assertThat().statusCode(200).
                body("category", hasItems("fresh-produce"),
                        "inStock", hasItems(true),
                        "category", everyItem(startsWith("fresh"))
                );
        logResponse(response);
    }

    @Test
    @GroceryShopTeam(author = Tester.CHINMAY, category = TestCategory.SANITY)
    void testGetSpecificProduct(Map<String, String> map) {
        Response response = initiateRequest().buildRequestForGetCalls().pathParam("productId", map.get("Product Id"))
                .log().all()
                .get("/products/{productId}");

        response.then().assertThat().statusCode(200);

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

            String filePath = "src/test/resources/files/Products.json";

            writeDataToJsonFile(responseData, filePath);

            System.out.println("Data successfully written to " + filePath);
        } else {
            System.err.println("Failed to retrieve data. Status code: " + response.getStatusCode());
        }

        logResponse(response);

        runAndVerifyMandatoryPass(() -> {
            response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(FrameworkConstants.getRESOURCEPATH() + "/files/ProductsSchema.json"));
        }, "Schema does not match.");

        Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @SneakyThrows
    @Test
    @GroceryShopTeam(author = Tester.CHINMAY, category = TestCategory.SANITY)
    void testAddItemsToCart(Map<String, String> map) {
        List<ProductIdentifier> productIdentifiers = readJsonData(FrameworkConstants.getRESOURCEPATH() + "/files/ItemsForCart.json",
                ProductIdentifier.class);
        
        ProductIdentifier productIdentifier = productIdentifiers.get(0);

        RequestSpecification requestSpecification = initiateRequest().
                buildRequestForPostCalls().
                pathParam("cartId", map.get("Cart Id"));

        Response response = requestSpecification.body(productIdentifier)
                .post("/carts/{cartId}/items");

        storeResponseToJsonFile(response, FrameworkConstants.getRESOURCEPATH() + "/files/ResponseData.json");

        logRequestInformation(requestSpecification);
    }
}
