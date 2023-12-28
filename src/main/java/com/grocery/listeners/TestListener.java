package com.grocery.listeners;

import com.grocery.annotations.GroceryShopTeam;
import com.grocery.enums.ConfigKey;
import com.grocery.exceptions.GroceryShopException;
import com.grocery.exceptions.GroceryShopTestSkipException;
import com.grocery.reports.GroceryShopReports;
import com.grocery.utils.PropertyUtils;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import static com.grocery.reports.GroceryShopReportLogger.*;
import static com.grocery.reports.GroceryShopReports.*;
import static com.grocery.utils.UtilityService.sendEmailWithAttachment;

public final class TestListener implements ITestListener, ISuiteListener {
    @Override
    public void onStart(ISuite suite) {
        try {
            initReports();
        } catch (Exception e) {
            throw new GroceryShopException("Problem with reports creation.", e);
        }
    }

    @Override
    public void onFinish(ISuite suite) {
        try {
            flushReports();
            if (PropertyUtils.get(ConfigKey.SEND_REPORTS_MAIL).equalsIgnoreCase("yes")) {
                sendEmailWithAttachment();
            }
        } catch (Exception e) {
            throw new GroceryShopException("Problem with generating reports and mailing them.");
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        GroceryShopReports.createTest(result.getMethod().getDescription());
        addAuthors(result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(GroceryShopTeam.class).author());
        addCategory(result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(GroceryShopTeam.class).category());
        setExecutionEnvironmentInfo();
        setUrlInfo();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        pass("<span style='background-color: #008000; color: white;'><b>" + result.getMethod().getDescription() + " has passed successfully. 😊</b></span>");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            if (result.getThrowable() instanceof GroceryShopException) {
                // Handle the FrameworkException differently
                String message = result.getThrowable().getMessage();
                fail("<span style='background-color: red; color: white;'><b>" + message + " 😔</b></span>");
            } else {
                fail("<span style='background-color: red; color: white;'><b>"
                        + result.getMethod().getDescription() + " has failed the test. 😔</span></b>");
                failWithDetails("<b><font color='red'> Reasons for test failure stated below</font></b>", result.getThrowable());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (result.getStatus() == ITestResult.SKIP) {
            if (result.getThrowable() instanceof GroceryShopTestSkipException) {
                String message = result.getThrowable().getMessage();
                skip("<span style='background-color: #FF8C00; color: white;'><b>" + message + "</b></span> 👋");

            } else {
                skip("<span style='color: #FF8C00'><b>" + result.getMethod().getDescription() + " has been skipped.</b></span>");
            }
        }
    }
}
