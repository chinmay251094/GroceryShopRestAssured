package com.grocery.exceptions;

public class GroceryShopException extends RuntimeException {
    public GroceryShopException(String message) {
        super(message);
    }

    public GroceryShopException(String message, Throwable cause) {
        super(message, cause);
    }
}
