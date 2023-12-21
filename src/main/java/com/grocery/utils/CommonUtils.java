package com.grocery.utils;

import com.grocery.supplier.ConfigurationPOJO;
import one.util.streamex.StreamEx;

public interface CommonUtils {
    StreamEx<ConfigurationPOJO> getData();
}
