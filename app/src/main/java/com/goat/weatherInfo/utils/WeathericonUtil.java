package com.goat.weatherInfo.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


import com.goat.weatherInfo.R;
/**
 * Created by MEHAKMEET on 24-12-2017.
 */

public class WeathericonUtil {

    public static final Map<String, Integer> ICONS;
    static{
        Map<String, Integer> iconMap=new HashMap<>();
        iconMap.put("clear-day", R.drawable.clear_day);
        iconMap.put("clear-night",R.drawable.clear_night);
        iconMap.put("rain",R.drawable.rain);
        iconMap.put("snow",R.drawable.snow);
        iconMap.put("sleet",R.drawable.sleet);
        iconMap.put("wind",R.drawable.wind);
        iconMap.put("fog",R.drawable.fog);
        iconMap.put("cloudy",R.drawable.cloudy);
        iconMap.put("partly-cloudy-day",R.drawable.partly_cloudy_day);
        iconMap.put("partly-cloudy-night",R.drawable.partly_cloudy_night);
        ICONS= Collections.unmodifiableMap(iconMap);
    }
}
