package org.example;

import org.example.app.MyClassTest;
import org.example.runner.TestRunner;

public class MainProgram {

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$s] %5$s%n");
        TestRunner.Run(MyClassTest.class);
    }
}
