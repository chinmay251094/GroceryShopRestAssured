package com.grocery.base;

import com.grocery.annotations.MustExtendBaseTest;
import com.grocery.utils.EnvironmentUtils;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Map;

import static com.grocery.utils.DateTimeUtils.getDate;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.config.RestAssuredConfig.config;
import static org.hamcrest.Matchers.*;

@MustExtendBaseTest
public class BaseTest {
    protected BaseTest() {
    }

    @SneakyThrows
    @BeforeMethod(alwaysRun = true)
    public void requestSetUp(Object[] data) {
        Map<String, String> map = (Map<String, String>) data[0];

        EnvironmentUtils.setBaseUri(map.get("BaseUri"));

        String typeOfEncoding = map.get("Encoding");

        String currentDate = getDate();
        FileOutputStream fileOutputStream = new FileOutputStream(new File(currentDate + "_GroceryShop.log"), true);
        PrintStream printStream = new PrintStream(fileOutputStream);

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder()
                .addFilter(new RequestLoggingFilter(printStream))
                .addFilter(new ResponseLoggingFilter(printStream))
                .setBaseUri(map.get("BaseUri"))
                .log(LogDetail.ALL);

        if (typeOfEncoding.equalsIgnoreCase("JSON")) {
            RestAssured.requestSpecification = requestSpecBuilder.setContentType(ContentType.JSON).build();
        } else if (typeOfEncoding.equalsIgnoreCase("FORM_URL_ENCODED")) {
            RestAssured.requestSpecification = requestSpecBuilder
                    .setContentType(ContentType.URLENC)
                    .build()
                    .config(config().encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)));
        }

        RestAssured.responseSpecification = new ResponseSpecBuilder()
                .expectHeader("Content-Type", is(anyOf(
                        equalTo("application/json; charset=utf-8"),
                        equalTo("application/json"),
                        nullValue())))
                .build()
                .logDetail(LogDetail.ALL);
    }
}
