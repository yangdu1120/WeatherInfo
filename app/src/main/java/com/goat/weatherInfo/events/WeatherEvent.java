package com.goat.weatherInfo.events;

import com.goat.weatherInfo.models.Weather;

/**
 * Created by Remy on 28-12-2019.
 */

public class WeatherEvent {
    private final Weather weather;

    public WeatherEvent(Weather weather) {
        this.weather = weather;
    }

    public Weather getWeather() {
        return weather;
    }
}
