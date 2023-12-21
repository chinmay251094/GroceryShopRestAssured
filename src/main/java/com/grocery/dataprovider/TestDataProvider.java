package com.grocery.dataprovider;

import com.grocery.constants.FrameworkConstants;
import com.grocery.utils.JsonUtils;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class TestDataProvider {
    private TestDataProvider() {
    }

    @DataProvider(parallel = true)
    public static Object[] getData(Method method) {
        String name = method.getName();
        List<Map<String, String>> executeMethodsList = new ArrayList<>();

        try {
            List<ConcurrentHashMap<String, String>> jsonList = JsonUtils.readJsonFile(FrameworkConstants.getDataJsonPath("TestData.json"));

            for (ConcurrentHashMap<String, String> hashMap : jsonList) {
                String testName = hashMap.get("Test Name");
                String executeFlag = hashMap.get("Execute");

                // Check if the test name matches and execute flag is "yes"
                if (name.equalsIgnoreCase(testName) && "yes".equalsIgnoreCase(executeFlag)) {
                    // Create a new HashMap to copy the data
                    Map<String, String> map = new HashMap<>();
                    for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                        map.put(entry.getKey(), entry.getValue());
                    }
                    executeMethodsList.add(map);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("An error occurred while fetching data source.");
        }
        return executeMethodsList.toArray();
    }
}
