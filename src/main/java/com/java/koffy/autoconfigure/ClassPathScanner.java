package com.java.koffy.autoconfigure;


import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;


public class ClassPathScanner {

    public static List<Class<?>> scanClasses(String basePackage, Class<? extends Annotation> annotation) {
        try (ScanResult scanResult = new ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
                .whitelistPackages(basePackage)
                .scan()) {
            List<Class<?>> annotatedClasses = new ArrayList<>();
            for (ClassInfo classInfo : scanResult.getClassesWithAnnotation(annotation.getName())) {
                annotatedClasses.add(classInfo.loadClass());
            }
            return annotatedClasses;
        }
    }

}
