package com.grocery.tests;

import com.grocery.annotations.GroceryShopTeam;
import com.grocery.enums.TestCategory;
import com.grocery.enums.Tester;
import com.grocery.utils.Utilities;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.Map;

import static com.grocery.builder.RequestBuilder.initiateRequest;
import static com.grocery.reports.GroceryShopReportLogger.logRequestParams;
import static com.grocery.reports.GroceryShopReportLogger.logResponse;
import static com.grocery.utils.Utilities.writeDataToJsonFile;

public final class CartTests {
    @Test
    void testGetNewCart() {
        RequestSpecification requestSpecification = initiateRequest().buildRequestForGetCalls();
        Response response = requestSpecification.get("/carts/:cartId");

        logResponse(response);
    }

    @Test
    void testCreateNewCart() {
        RequestSpecification requestSpecification = initiateRequest().buildRequestForPostCalls();
        Response response = requestSpecification.post("/carts");

        if (response.getStatusCode() == 201) {
            Map<String, ?> responseData = response.as(Map.class);

            String filePath = "src/test/resources/CartIdentifier.json";

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
        Response response = initiateRequest().buildRequestForGetCalls().get("/carts/" + map.get("Cart Id") + "/items");

        logResponse(response);
    }

    @Test
    @GroceryShopTeam(author = Tester.CHINMAY, category = TestCategory.SANITY)
    void testModifyItemsInCart(Map<String, String> map) {
        String requestBody = Utilities.readFileContent("src/test/resources/CartModifier.json");

        Response patched = initiateRequest().buildRequestForPatchCalls()
                .pathParam("cartId", map.get("Cart Id"))
                .pathParam("itemId", 694265213)
                .body(requestBody)
                .patch("/carts/{cartId}/items/{itemId}");

        patched.prettyPrint();

        Assertions.assertThat(patched.getStatusCode()).isEqualTo(204);
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
