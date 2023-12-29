package com.grocery.utils;

import lombok.Getter;
import lombok.Setter;

public class EnvironmentUtils {
    @Setter
    @Getter
    private static String baseUri;

    private EnvironmentUtils() {
    }
}
