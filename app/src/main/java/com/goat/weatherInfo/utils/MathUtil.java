package com.goat.weatherInfo.utils;

public class MathUtil {

    public static int tempConverterF2C(Double temp) {
        return (int) Math.round((temp - 32) * (5.0 / 9.0));
    }

}
