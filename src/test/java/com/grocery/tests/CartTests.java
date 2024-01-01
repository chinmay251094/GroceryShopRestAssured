package com.grocery.tests;

import com.grocery.annotations.GroceryShopTeam;
import com.grocery.base.BaseTest;
import com.grocery.constants.FrameworkConstants;
import com.grocery.enums.TestCategory;
import com.grocery.enums.Tester;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.grocery.builder.RequestBuilder.initiateRequest;
import static com.grocery.reports.GroceryShopReportLogger.logRequestParams;
import static com.grocery.reports.GroceryShopReportLogger.logResponse;
import static com.grocery.utils.Utilities.writeDataToJsonFile;
import static io.restassured.RestAssured.given;

public final class CartTests extends BaseTest {
    private CartTests() {
    }

    @Test
    @GroceryShopTeam(author = Tester.CHINMAY, category = {TestCategory.REGRESSION, TestCategory.SANITY})
    void testGetNewCart() {
        Response response = given().get("/carts/:cartId");

        logResponse(response);
    }

    @Test
    @GroceryShopTeam(author = Tester.CHINMAY, category = {TestCategory.REGRESSION, TestCategory.SANITY})
    void testCreateNewCart(Map<String, String> map) {
        Response response = given().post("/carts");

        if (response.getStatusCode() == 201) {
            response.then().log().all();

            Map<String, ?> responseData = response.as(Map.class);

            String filePath = FrameworkConstants.getRESOURCEPATH() + "/files/CartIdentifier.json";

            writeDataToJsonFile(responseData, filePath);

            System.out.println("Data successfully written to " + filePath);
        } else {
            System.err.println("Failed to retrieve data. Status code: " + response.getStatusCode());
        }

        logResponse(response);
    }

    @Test
    @GroceryShopTeam(author = Tester.CHINMAY, category = {TestCategory.SMOKE, TestCategory.REGRESSION})
    void testGetItemsInCart(Map<String, String> map) {
        Response response = given().
                pathParam("cartId", map.get("Cart Id")).
                get("/carts/{cartId}/items");

        logResponse(response);
    }

    @Test
    @GroceryShopTeam(author = Tester.CHINMAY, category = TestCategory.SANITY)
    void testModifyItemsInCart(Map<String, String> map) {
        Map<String, Object> product = new HashMap<>();
        product.put("quantity", 3);

        given().
                pathParam("cartId", map.get("Cart Id")).
                pathParam("itemId", map.get("Item Id")).
                body(new JSONObject(product).toString()).
                patch("/carts/{cartId}/items/{itemId}");
    }

    @Test
    @GroceryShopTeam(author = Tester.CHINMAY, category = TestCategory.SANITY)
    void testDeleteItemsFromCart(Map<String, String> map) {
        RequestSpecification requestSpecification = initiateRequest().buildRequestForDeleteCalls()
                .pathParam("cartId", map.get("Cart Id"))
                .pathParam("itemId", map.get("Item Id"));

        Response response = requestSpecification.delete("/carts/{cartId}/items/{itemId}");
        logRequestParams(requestSpecification);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(204);
    }
}
