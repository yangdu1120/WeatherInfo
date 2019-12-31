//package com.goat.weatherInfo.services;
//
//import android.content.Context;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.util.Log;
//
//import java.util.List;
//
//public class LocationService {
//
//    private static final String TAG = "LocationService";
//
//    private static LocationListener locationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
//            if(location != null){
//                String string = "纬度为：" + location.getLatitude() + ",经度为："+ location.getLongitude();
//                Log.i(TAG,"string"+string);
//                AndroidLocation.getAddress(location);
//                AndroidLocation.onActivityStoped();
//            }
//        }
//
//        @Override
//        public void onProviderDisabled(String arg0) {
//        }
//
//        @Override
//        public void onProviderEnabled(String arg0) {
//        }
//
//        @Override
//        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
//        }
//    };
//    public static void startLocation(Context context){
//        mContext = context;
//        //获取定位服务
//        m_locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        //获取当前可用的位置控制器
//        List<String> list = m_locationManager.getProviders(true);
//        if (list.contains(LocationManager.GPS_PROVIDER)) {
//            //是否为GPS位置控制器
//            m_provider = LocationManager.NETWORK_PROVIDER;//NETWORK_PROVIDER GPS_PROVIDER
//            Log.i(logTag,"is GPS");
//        }
//        else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
//            //是否为网络位置控制器
//            m_provider = LocationManager.NETWORK_PROVIDER;
//            Log.i(logTag,"is network");
//        }
//        if(m_provider != null){
//            Location location = m_locationManager.getLastKnownLocation(m_provider);
//            if(location!=null){
//                //AndroidLocation.getAddress(location);
////直接获取
//            }else{
////没有需要加监听等待获取
//                m_locationManager.requestLocationUpdates(m_provider, 3000, 1, locationListener);
//            }
//        }
//    }
//
//}
