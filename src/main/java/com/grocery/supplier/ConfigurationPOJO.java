package com.grocery.supplier;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ConfigurationPOJO {
    private ConfigurationPOJO() {
    }

    private String BaseUrl;
    private int ProductResults;
}
