package org.example.app;

import org.example.annotations.After;
import org.example.annotations.Before;
import org.example.annotations.Test;

import java.util.logging.Logger;

public class MyClassTest {

    private static final Logger logger = Logger.getLogger(MyClassTest.class.getSimpleName());

    private MyClass target;

    @Before
    public void before1() {
        logger.info("Выполняется метод #1 before()");
        target = new MyClass();
    }

    @Before
    public void before2() {
        logger.info("Выполняется метод #2 before()");
    }

    @After
    public void after1() {
        logger.info("Выполняется метод #1 after()");
    }

    @After
    public void after2() {
        logger.info("Выполняется метод #2 after()");
    }

    @Test
    public void getSumTest_twoPositiveNumber() {
        logger.info("Выполняется метод getSumTest_twoPositiveNumber()");
        check(target.getSum(2, 3) == 5, "2 + 3 должно быть 5");
        throw new RuntimeException("Broken test");
    }

    @Test
    public void getSumTest_twoNegativeNumber() {
        logger.info("Выполняется метод getSumTest_twoNegativeNumber()");
        check(target.getSum(-2, -3) == -5, "-2 + -3 должно быть -5");
    }

    @Test
    public void getMultiplicationTest_twoPositiveNumber() {
        logger.info("Выполняется метод getMultiplicationTest_twoPositiveNumber()");
        check(target.getMultiplication(4, 5) == 20, "4 * 5 должно быть 20");
    }

    @Test
    public void getDivisionTest_byZero() {
        logger.info("Выполняется метод getDivisionTest_byZero()");
        target.getDivision(10, 0);
    }

    private void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
