package com.grocery.base;

import com.grocery.exceptions.GroceryShopException;
import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.List;

public class BaseTestEnforcer implements IAlterSuiteListener {

    @Override
    public void alter(List<XmlSuite> suites) {
        for (XmlSuite suite : suites) {
            for (XmlTest xmlTest : suite.getTests()) {
                List<XmlClass> classes = xmlTest.getXmlClasses();
                for (XmlClass xmlClass : classes) {
                    String className = xmlClass.getName();
                    try {
                        Class<?> clazz = Class.forName(className);
                        if (!isBaseTestExtended(clazz)) {
                            throw new GroceryShopException("Test class '" + className + "' must extend BaseTest.");
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean isBaseTestExtended(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        if (clazz == BaseTest.class) {
            return true;
        }
        return isBaseTestExtended(clazz.getSuperclass());
    }
}
