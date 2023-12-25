package com.grocery.constants;

import com.grocery.enums.ConfigProperties;
import com.grocery.utils.DateTimeUtils;
import com.grocery.utils.PropertyUtils;
import lombok.Getter;

public final class FrameworkConstants {
    @Getter
    private static final String RESOURCEPATH = System.getProperty("user.dir") + "/src/test/resources/";
    private static final String DATAJSONSPATH = RESOURCEPATH + "/json/";
    private static final String CONFIGPATH = RESOURCEPATH + "/config/config.properties";
    private static final String EXTENTREPORTSPATH = System.getProperty("user.dir") + "/extent-test-output/";
    private static String extentReportFilepath = "";

    private FrameworkConstants() {
    }

    public static String getDataJsonPath(String fileName) {
        return DATAJSONSPATH + "\\" + fileName + "";
    }

    public static String getConfigPath() {
        return CONFIGPATH;
    }

    public static String getExtentReportFilepath() {
        if (extentReportFilepath.isEmpty()) {
            extentReportFilepath = createReportPath();
        }
        return extentReportFilepath;
    }

    public static String createReportPath() {
        if (PropertyUtils.get(ConfigProperties.OVERRIDEREPORTS).equalsIgnoreCase("no")) {
            return EXTENTREPORTSPATH + "/" + "GroceryShop_Ver1.0_" + DateTimeUtils.getDateTime() + ".html";
        } else {
            return EXTENTREPORTSPATH + "/" + "index.html";
        }
    }
}
