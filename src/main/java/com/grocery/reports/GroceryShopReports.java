package com.grocery.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.grocery.builder.RequestBuilder;
import com.grocery.enums.TestCategory;
import com.grocery.enums.Tester;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static com.grocery.constants.FrameworkConstants.getExtentReportFilepath;

public final class GroceryShopReports {
    private static ExtentReports extentReports;

    private GroceryShopReports() {
    }

    public static void initReports() {
        if (Objects.isNull(extentReports)) {
            extentReports = new ExtentReports();
            ExtentSparkReporter spark = new ExtentSparkReporter(getExtentReportFilepath());
            extentReports.attachReporter(spark);
            spark.config().setTheme(Theme.DARK);
            spark.config().setDocumentTitle("Grocery Shop Reports");
            spark.config().setReportName("RestAPI Automation Test Reports");
        }
    }

    public static void flushReports() {
        if (Objects.nonNull(extentReports)) {
            extentReports.flush();
        }
        GroceryShopReportManager.unload();

        try {
            Desktop.getDesktop().browse(new File(getExtentReportFilepath()).toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createTest(String testcasename) {
        GroceryShopReportManager.setExtent(extentReports.createTest(testcasename));
    }

    public static void addAuthors(Tester[] authors) {
        for (Tester tester : authors) {
            GroceryShopReportManager.getExtent().assignAuthor(tester.toString());
        }
    }

    public static void addCategory(TestCategory[] categories) {
        for (TestCategory category : categories) {
            GroceryShopReportManager.getExtent().assignCategory(category.toString());
        }
    }

    public static void setExecutionEnvironmentInfo() {
        GroceryShopReportManager.getExtent().info("<font color='yellow'><b>" + "\uD83D\uDCBB "
                + System.getProperty("os.name").replace(" ", "_"));
    }

    public static void setUrlInfo() {
        GroceryShopReportManager.getExtent().info("<font color='yellow'><b>" + "\uD83C\uDF10 " + RequestBuilder.initiateRequest().getBaseUrl() + "</b></font>");
    }
}
