package com.crossover.utils;

public class Utilx {
    public static int get() {
        return 0;
    }

    private String privateMethod(String message) {
        return message;
    }

    public String callPrivateMethod(String message) {
        return privateMethod(message);
    }

    public final String finalMethod(String message) {
        return message;
    }

}
