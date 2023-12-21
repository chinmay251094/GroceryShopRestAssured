package com.grocery.listeners;

import com.grocery.dataprovider.TestDataProvider;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public final class AnnotationTransformer implements IAnnotationTransformer {
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setDataProvider("getData");
        annotation.setDataProviderClass(TestDataProvider.class);
    }
}
