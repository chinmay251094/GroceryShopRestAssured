package com.grocery.exceptions;

public class ConfigPropertyEnumException extends GroceryShopException {
    public ConfigPropertyEnumException(String message) {
        super(message);
    }

    public ConfigPropertyEnumException(String message, Throwable cause) {
        super(message, cause);
    }
}
