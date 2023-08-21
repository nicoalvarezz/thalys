package com.java.thalys.autoconfigure;


import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to scan classes with the project using the kinetic framework.
 */
public class ClassPathScanner {

    /**
     * Scan all the classes within a given path that uses a given annotation.
     * This will return a list of classes that match the search conditions.
     *
     * @param basePackage
     * @param annotation
     * @return List of annotated classes
     */
    public static List<Class<?>> scanClassesForGivenAnnotation(String basePackage,
                                                               Class<? extends Annotation> annotation) {
        try (ScanResult scanResult = new ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
                .acceptPackages(basePackage)
                .scan()) {
            List<Class<?>> annotatedClasses = new ArrayList<>();
            for (ClassInfo classInfo : scanResult.getClassesWithAnnotation(annotation.getName())) {
                annotatedClasses.add(classInfo.loadClass());
            }
            return annotatedClasses;
        }
    }

}
