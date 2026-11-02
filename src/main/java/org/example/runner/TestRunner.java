package org.example.runner;

import org.example.annotations.After;
import org.example.annotations.Before;
import org.example.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public final class TestRunner {

    private static final Logger logger = Logger.getLogger(TestRunner.class.getSimpleName());

    private TestRunner() {
    }

    public static void Run(Class<?> testedClass) {
        logger.info("Получен для запуска тестовый класс " + testedClass.getCanonicalName());

        List<Method> beforeMethods = findMethods(testedClass, Before.class);
        List<Method> testMethods = findMethods(testedClass, Test.class);
        List<Method> afterMethods = findMethods(testedClass, After.class);

        int passed = 0;
        for (Method testMethod : testMethods) {
            if (runSingleTest(testedClass, beforeMethods, testMethod, afterMethods)) {
                passed++;
            }
        }
        printStatistics(testMethods.size(), passed);
    }

    private static boolean runSingleTest(Class<?> testedClass, List<Method> beforeMethods,
                                         Method testMethod, List<Method> afterMethods) {
        logger.info("--- Тест " + testMethod.getName() + " ---");
        Object instance;
        try {
            instance = createInstance(testedClass);
        } catch (Exception e) {
            logger.severe("Не удалось создать экземпляр: " + e);
            return false;
        }

        boolean success = invokeAll(instance, beforeMethods);
        if (success) {
            success = invokeAll(instance, Collections.singletonList(testMethod));
        }
        boolean afterSuccess = invokeAll(instance, afterMethods);
        return success && afterSuccess;
    }

    private static List<Method> findMethods(Class<?> clazz, Class<? extends Annotation> annotation) {
        List<Method> result = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                result.add(method);
            }
        }
        result.sort(Comparator.comparing(Method::getName));
        return result;
    }

    private static boolean invokeAll(Object instance, List<Method> methods) {
        boolean success = true;
        for (Method method : methods) {
            try {
                invoke(instance, method);
            } catch (Throwable t) {
                success = false;
                logger.severe("Метод " + method.getName() + " завершился с ошибкой: " + t);
            }
        }
        return success;
    }

    private static void invoke(Object instance, Method method) throws Throwable {
        try {
            method.setAccessible(true);
            method.invoke(instance);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    private static Object createInstance(Class<?> clazz) throws Exception {
        java.lang.reflect.Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    private static void printStatistics(int total, int passed) {
        logger.info("Статистика: всего " + total + ", успешно " + passed
                + ", упало " + (total - passed));
    }
}
