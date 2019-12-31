package com.goat.weatherInfo.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

    public static String getTime(Integer dateInt) {
        long CurrentTimeLong = new Long(dateInt).longValue() * 1000;
        DateFormat ymdhmsFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);
        String CurrentTime = ymdhmsFormat.format(CurrentTimeLong);
        return CurrentTime;
    }


}
