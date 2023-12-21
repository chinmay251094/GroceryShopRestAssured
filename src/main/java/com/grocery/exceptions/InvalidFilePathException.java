package com.grocery.exceptions;

public class InvalidFilePathException extends GroceryShopException {
    public InvalidFilePathException(String message) {
        super(message);
    }

    public InvalidFilePathException(String message, Throwable cause) {
        super(message, cause);
    }
}
