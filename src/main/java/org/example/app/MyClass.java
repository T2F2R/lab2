package org.example.app;


public class MyClass {

    public int getSum(int a, int b) {
        return a + b;
    }

    public int getMultiplication(int a, int b) {
        return a * b;
    }

    public int getDivision(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("Деление на ноль");
        }
        return a / b;
    }
}
