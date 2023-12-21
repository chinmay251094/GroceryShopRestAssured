package com.grocery.supplier;

import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.data.JsonReader;
import one.util.streamex.StreamEx;

import static io.github.sskorol.data.TestDataReader.use;

public class ConfigurationReader {
    private ConfigurationReader() {
    }

    @DataSupplier
    public static StreamEx<ConfigurationPOJO> getData() {
        return use(JsonReader.class)
                .withTarget(ConfigurationPOJO.class)
                .withSource("TestConfiguration.json")
                .read();
    }
}
