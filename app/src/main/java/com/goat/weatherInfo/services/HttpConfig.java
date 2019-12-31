package com.goat.weatherInfo.services;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HttpConfig {

    private static final String APICALL = "https://api.darksky.net/forecast/66203959032c727f3cf9fbebd266fab2/";
    private static Retrofit retrofit = null;

    public static Retrofit retrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(APICALL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
