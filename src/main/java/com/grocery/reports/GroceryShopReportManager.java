package com.grocery.reports;

import com.aventstack.extentreports.ExtentTest;

public class GroceryShopReportManager {
    private static final ThreadLocal<ExtentTest> extTest = new ThreadLocal<>();

    private GroceryShopReportManager() {
    }

    static ExtentTest getExtent() {
        return extTest.get();
    }

    static void setExtent(ExtentTest extentTest) {
        extTest.set(extentTest);
    }

    static void unload() {
        extTest.remove();
    }
}
