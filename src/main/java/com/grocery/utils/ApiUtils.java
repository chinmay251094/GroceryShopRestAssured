package com.grocery.utils;

import com.grocery.supplier.ConfigurationPOJO;
import com.grocery.supplier.ConfigurationReader;
import io.github.sskorol.data.JsonReader;
import lombok.Data;
import lombok.Getter;
import one.util.streamex.StreamEx;

import static io.github.sskorol.data.TestDataReader.use;

@Data
@Getter
public class ApiUtils implements CommonUtils {
    private final StreamEx<ConfigurationPOJO> dataReader = ConfigurationReader.getData();
    private final ConfigurationPOJO dataSupplier = dataReader.findFirst().orElse(null);

    private ApiUtils() {
    }

    public static ApiUtils getInstance() {
        return new ApiUtils();
    }

    @Override
    public StreamEx<ConfigurationPOJO> getData() {
        return use(JsonReader.class)
                .withTarget(ConfigurationPOJO.class)
                .withSource("TestConfiguration.json")
                .read();
    }
}

