package com.grocery.utils;

import org.testng.Assert;

public class VerificationUtils {
    public static void runAndVerifyMandatoryPass(Runnable action, String failureMessage) {
        try {
            action.run();
        } catch (Exception e) {
            // Log or print the exception details if needed
            e.printStackTrace();

            // Fail the test explicitly using TestNG or JUnit
            Assert.fail(failureMessage);
        }
    }
}
