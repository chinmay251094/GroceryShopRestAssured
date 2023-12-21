package com.grocery.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryFailedTests implements IRetryAnalyzer {

    private int count = 0;
    private int retries = 1;

    public boolean retry(ITestResult result) {
        boolean value = false;
        try {
            value = count < retries;
            count++;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

}
