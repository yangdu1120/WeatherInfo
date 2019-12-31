package com.goat.weatherInfo.services;


import android.annotation.SuppressLint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class LocationProvider implements LocationListener {


    private static final String TAG = "LocationProvider";

    private Context mContext;
    private LocationManager mLocationManager;
    private LocationGetListener mListener;


    public interface LocationGetListener {
        void getLocationInfo(Location location);
    }

    void initProvider() {
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    @SuppressLint("MissingPermission")
    public void getBestLocation(LocationGetListener listener) {
        mListener = listener;
        Location location;
        Criteria criteria = new Criteria();

        String provider = mLocationManager.getBestProvider(criteria, true);
        if (TextUtils.isEmpty(provider)) {
            location = getNetWorkLocation(mContext);
        } else {
            location = mLocationManager.getLastKnownLocation(provider);
        }
        if (location == null) {
            // Once the existing location information is used. Get it with GPS and update the information
            provider = LocationManager.PASSIVE_PROVIDER;
            location = mLocationManager.getLastKnownLocation(provider);
            if (mListener != null) {
                mListener.getLocationInfo(location);
            }

            mLocationManager.requestLocationUpdates(provider, 60000, 1, this);
        } else if (mListener != null) {
            mListener.getLocationInfo(location);
        }
    }

    @SuppressLint("MissingPermission")
    public Location getNetWorkLocation(Context context) {
        Location location = null;

        if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Log.i(TAG, "location:" + location);
        }
        return location;
    }


    public void unregisterLocationUpdaterListener() {
        mLocationManager.removeUpdates(this);
    }


    @Override
    public void onLocationChanged(Location location) {

        Log.i(TAG, "location : onLocationChanged");
        if (mListener != null) {
            mListener.getLocationInfo(location);
        }
        unregisterLocationUpdaterListener();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
