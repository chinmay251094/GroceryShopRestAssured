package com.grocery.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grocery.annotations.GroceryShopTeam;
import com.grocery.base.BaseTest;
import com.grocery.enums.TestCategory;
import com.grocery.enums.Tester;
import com.grocery.pojo.*;
import com.grocery.reports.GroceryShopReportLogger;
import com.grocery.utils.KeyUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.grocery.reports.GroceryShopReportLogger.logResponse;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

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

    @SneakyThrows
    @Test
    @GroceryShopTeam(author = Tester.CHINMAY, category = TestCategory.SANITY)
    void testCreateNewCollection(Map<String, String> map) {
        String apiKey = KeyUtils.getKey();

        Header header = new Header("Content-Type", "application/json");
        List<Header> headerList = new ArrayList<>();
        headerList.add(header);

        Body body = new Body("raw", "{\"data\":\"123\"}");

        RequestRequest request = new RequestRequest(body, "POST", headerList
                , "This is a sample POST Request", "https://postman-echo.com/post");

        RequestRootRequest requestRoot = new RequestRootRequest("Sample POST Request", request);
        List<RequestRootRequest> requestRoots = new ArrayList<>();
        requestRoots.add(requestRoot);

        FolderRequest folder = new FolderRequest("This is a folder", requestRoots);
        List<FolderRequest> folderList = new ArrayList<>();
        folderList.add(folder);

        Info info = new Info("Sample Collection 83 RestAssured",
                "This is just a sample collection.",
                "https://schema.getpostman.com/json/coll ection/v2.1.0/collection.json");

        CollectionRequest collection = new CollectionRequest(info, folderList);

        CollectionRootRequest collectionRoot = new CollectionRootRequest(collection);

        RequestSpecification requestSpecification = given().header("X-API-Key", apiKey)
                .body(collectionRoot);

        Response response = requestSpecification.post("/collections");

        String uid = response.then().extract().path("collection.uid");

        int responseStatusCode = response.getStatusCode();
        int expectedCode = Integer.parseInt(map.get("Expected Status Code"));
        assertThat(responseStatusCode, Matchers.is(equalTo(expectedCode)));

        GroceryShopReportLogger.logRequestInformation(requestSpecification);
        logResponse(response);

        Response fetchCollection = given().pathParam("collectionUid", uid).when().
                header("X-API-Key", apiKey).
                get("/collections/{collectionUid}");

        CollectionRootResponse deserialized = fetchCollection.then().extract().as(CollectionRootResponse.class);

        ObjectMapper objectMapper = new ObjectMapper();
        String collectionRootStr = objectMapper.writeValueAsString(collectionRoot).replaceAll("\\s", "");
        String deserializedStr = objectMapper.writeValueAsString(deserialized).replaceAll("\\s", "");

        JSONAssert.assertEquals(collectionRootStr, deserializedStr,
                new CustomComparator(JSONCompareMode.STRICT,
                        new Customization("collection.item[*].item[*].request.url", (o1, o2) -> true)));

        List<String> urlRequestList = new ArrayList<>();
        List<String> urlResponseList = new ArrayList<>();

        for (RequestRootRequest list : requestRoots) {
            System.out.println("list.getRequest().getUrl() = " + list.getRequest().getUrl());
            urlRequestList.add(list.getRequest().getUrl());
        }

        List<FolderResponse> folderResponses = deserialized.getCollection().getItem();
        for (FolderResponse folderResponse : folderResponses) {
            List<RequestRootResponse> rootResponses = folderResponse.getItem();
            for (RequestRootResponse requestRootResponse : rootResponses) {
                URL url = requestRootResponse.getRequest().getUrl();
                System.out.println("url.getRaw() = " + url.getRaw());
                urlResponseList.add(url.getRaw());
            }
        }

        assertThat(urlResponseList, containsInAnyOrder(urlRequestList.toArray()));
        logResponse(fetchCollection);
    }
}
