package com.grocery.exceptions;

import org.testng.SkipException;

public class GroceryShopTestSkipException extends SkipException {
    public GroceryShopTestSkipException(String message) {
        super(message);
    }
}