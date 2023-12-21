package com.grocery.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.grocery.pojo.CartIdentifier;
import com.grocery.pojo.Products;
import com.grocery.supplier.DataReader;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.Getter;
import one.util.streamex.StreamEx;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Utilities {
    private final StreamEx<CartIdentifier> cartReader = DataReader.getCartIdentifier();
    @Getter
    protected final CartIdentifier cartSupplier = cartReader.findFirst().orElse(null);
    private final StreamEx<Products> productsReader = DataReader.getProductData();
    @Getter
    protected final Products productsSupplier = productsReader.findFirst().orElse(null);
    private final StreamEx<Products> productIdReader = DataReader.getProductId();
    @Getter
    protected final Products productIdSupplier = productIdReader.findFirst().orElse(null);

    private Utilities() {
    }

    public static Utilities getInstance() {
        return new Utilities();
    }

    public static void writeDataToJsonFile(Map<String, ?> data, String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            // Use Jackson ObjectMapper to write the Map as JSON to the file
            new ObjectMapper().writeValue(fileWriter, data);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error writing data to file: " + e.getMessage());
        }
    }

    public static void writeDataToJsonFile(Object data, String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();

            writer.writeValue(new File(filePath), data);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error writing data to file: " + e.getMessage());
        }
    }

    public static void storeResponseToJsonFile(Response response) {
        try {
            Files.write(Paths.get("src/test/resources/ResponseData.json"), response.asByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Map<String, Object>> deserializeResponse(String responseBody) {
        try {
            return new ObjectMapper().readValue(responseBody, new TypeReference<List<Map<String, Object>>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deserializing response: " + e.getMessage());
        }
    }

    public static Map<String, Object> getRandomProduct(List<Map<String, Object>> products) {
        Random random = new Random();
        return products.get(random.nextInt(products.size()));
    }

    public static List<Map<String, Object>> readProductsFromJson(String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(new File(filePath), new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading products from JSON file: " + e.getMessage());
        }
    }

    public static List<?> readDataFromJson(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(filePath), new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading products from JSON file: " + e.getMessage());
        }
    }

    public static <T> T readJsonFile(String filePath, Class<T> targetType) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(filePath), targetType);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading JSON file: " + e.getMessage());
        }
    }

    public static <T> List<T> readJsonData(String filePath, Class<T> targetType) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(filePath), objectMapper.getTypeFactory().constructCollectionType(List.class, targetType));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading JSON file: " + e.getMessage());
        }
    }

    public static String getValueFromJsonFile(String filePath, String jsonPathExpression) {
        try {
            // Read the JSON content from the file
            String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));

            // Parse the JSON content using JsonPath
            JsonPath jsonPath = new JsonPath(jsonContent);

            // Get the value based on the provided JsonPath expression
            return jsonPath.getString(jsonPathExpression);
        } catch (Exception e) {
            throw new RuntimeException("Error reading JSON file: " + e.getMessage());
        }
    }

    public static List<String> extractAttributeFromJson(Response response, String attribute) {
        // Extract the JSON response as a string
        String jsonResponse = response.getBody().asString();

        // Parse the JSON content using JsonPath
        JsonPath jsonPath = new JsonPath(jsonResponse);

        // Extract the specified attribute using JsonPath
        return jsonPath.getList(attribute);
    }

    public static String readFileContent(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filePath, e);
        }
    }
}
