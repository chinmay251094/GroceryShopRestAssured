package com.grocery.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class JsonUtils {
    private JsonUtils() {
    }

    public static List<ConcurrentHashMap<String, String>> readJsonFile(String filePath) {
        List<ConcurrentHashMap<String, String>> resultList = new ArrayList<>();

        try {
            // Create an ObjectMapper to parse JSON
            ObjectMapper objectMapper = new ObjectMapper();

            // Read JSON from the file into a JsonNode
            JsonNode jsonNode = objectMapper.readTree(new File(filePath));

            // Check if the root JSON node is an object
            if (jsonNode.isObject()) {
                // If it's a single object, convert it to a ConcurrentHashMap and add it to the list
                ConcurrentHashMap<String, String> concurrentMap = new ConcurrentHashMap<>();
                jsonNode.fields().forEachRemaining(entry -> {
                    String key = entry.getKey();
                    String value = entry.getValue().asText();
                    concurrentMap.put(key, value);
                });
                resultList.add(concurrentMap);
            } else if (jsonNode.isArray()) {
                // If it's an array, iterate through the elements and convert them to ConcurrentHashMaps
                for (JsonNode element : jsonNode) {
                    ConcurrentHashMap<String, String> concurrentMap = new ConcurrentHashMap<>();
                    element.fields().forEachRemaining(entry -> {
                        String key = entry.getKey();
                        String value = entry.getValue().asText();
                        concurrentMap.put(key, value);
                    });
                    resultList.add(concurrentMap);
                }
            }
        } catch (IOException e) {
            // Handle any IO exceptions that may occur
            e.printStackTrace();
        }

        return resultList;
    }
}
