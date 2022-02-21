package com.softkit.utils;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class TurnUtils {
    private static final Random RANDOM = new Random();

    public static boolean isValidTurn(long prevValue, long currentValue) {
        return Math.abs(currentValue * 3 - prevValue) <= 1;
    }

    public static long generateTurnValue(long currentValue) {
        if (currentValue % 3 == 0) {
            return currentValue / 3;
        }

        if ((currentValue + 1) % 3 == 0) {
            return (currentValue + 1) / 3;
        } else {
            return (currentValue - 1) / 3;
        }
    }

    public static long generateStartValue() {
        return RANDOM.nextInt(50) + 50L;
    }

    public static boolean isValidStartValue(Long value) {
        return value != null && value > 1;
    }
}
