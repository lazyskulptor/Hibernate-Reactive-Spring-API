package me.lazyskulptor.demo;

import org.springframework.boot.test.autoconfigure.filter.StandardAnnotationCustomizableTypeExcludeFilter;

public class DataHBTypeExcludeFilter extends StandardAnnotationCustomizableTypeExcludeFilter<DataHBTest> {
    DataHBTypeExcludeFilter(Class<?> testClass) {
        super(testClass);
    }
}
