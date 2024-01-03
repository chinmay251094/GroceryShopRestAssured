package com.grocery.tests;

import com.grocery.annotations.GroceryShopTeam;
import com.grocery.base.BaseTest;
import com.grocery.enums.TestCategory;
import com.grocery.enums.Tester;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;

import static com.grocery.reports.GroceryShopReportLogger.logResponse;
import static io.restassured.RestAssured.given;

public class MiscellaneousTests extends BaseTest {
    private MiscellaneousTests() {
    }

    @SneakyThrows
    @Test
    @GroceryShopTeam(author = Tester.CHINMAY, category = TestCategory.SANITY)
    void testFileDownload(Map<String, String> map) {
        Response response = given().get("/storage/feee3c905665949b89ba53e/2017/02/file_example_JSON_1kb.json").then().extract().response();

        response.then().log().all();

        InputStream inputStream = response.asInputStream();

        FileOutputStream fileOutputStream = new FileOutputStream(new File("file_example_JSON_1kb.json"));
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        fileOutputStream.write(bytes);
        fileOutputStream.close();
        logResponse(response);
    }

    @Test
    @GroceryShopTeam(author = Tester.CHINMAY, category = TestCategory.SANITY)
    void testFormUrlEncoding(Map<String, String> map) {
        Response response = given().
                when().
                formParam("foo1", "bar1").
                formParam("foo 2", "b ar2").
                formParam("foo3", "b,ar3").
                post("/post").then().extract().response();

        response.then().log().all();

        logResponse(response);
    }
}
