package com.goat.weatherInfo.events;

/**
 * Created by Remy on 28-12-2019.
 */

public class ErrorEvent {

    private final String errorMessage;

    public ErrorEvent(String errorMessage) {

        this.errorMessage=errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
