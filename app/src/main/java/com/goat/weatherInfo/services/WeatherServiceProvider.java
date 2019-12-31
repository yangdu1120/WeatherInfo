package com.goat.weatherInfo.services;

import android.util.Log;

import com.goat.weatherInfo.events.ErrorEvent;
import com.goat.weatherInfo.events.WeatherEvent;
import com.goat.weatherInfo.models.Currently;
import com.goat.weatherInfo.models.Weather;


import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Remy on 28-12-2019.
 * get weather info via the coordinates of the current position
 */
public class WeatherServiceProvider {

    public void getWeather(double lat, double lng) {
        WeatherService weatherServies = HttpConfig.retrofit().create(WeatherService.class);
        Call<Weather> weatherData = weatherServies.getWeather(lat, lng);
        weatherData.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {

                if (response.body() != null) {
                    Weather weather = response.body();
                    Currently currently = weather.getCurrently();
                    Log.e("currentTemperature:", currently.getTemperature().toString());

                    EventBus.getDefault().post(new WeatherEvent(weather));
                } else {
                    EventBus.getDefault().post(new ErrorEvent("No Info"));
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {

                EventBus.getDefault().post(new ErrorEvent("Unable to make weather server"));
            }
        });
    }

}
