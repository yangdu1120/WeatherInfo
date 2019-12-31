package com.goat.weatherInfo.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.goat.weatherInfo.R;
import com.goat.weatherInfo.events.ErrorEvent;
import com.goat.weatherInfo.events.WeatherEvent;
import com.goat.weatherInfo.models.Currently;
import com.goat.weatherInfo.models.Data;
import com.goat.weatherInfo.models.HourlyData;
import com.goat.weatherInfo.services.WeatherServiceProvider;
import com.goat.weatherInfo.utils.MathUtil;
import com.goat.weatherInfo.utils.WeathericonUtil;

import android.Manifest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Animation refreshAnim;
    private List<Data> dailyDatas = new ArrayList<>();
    private List<Data> hourlyDatas = new ArrayList<>();

    LocationManager locationManager;

    //Defaute location (LA)
    private double latitude = 34.0686;
    private double longitude = -118.3228;

    // 获取位置服务
    String serviceName = Context.LOCATION_SERVICE;

    @BindView(R.id.iv_refresh)
    ImageView iv_refresh;
    @BindView(R.id.tv_city)
    TextView tv_city;
    @BindView(R.id.tv_temp)
    TextView tv_temp;
    @BindView(R.id.iv_weather)
    ImageView iv_weather;
    @BindView(R.id.text_summary)
    TextView tv_summary;
    @BindView(R.id.tv_temp_low)
    TextView tv_temp_low;
    @BindView(R.id.tv_temp_tag)
    TextView tv_temp_tag;
    @BindView(R.id.tv_temp_high)
    TextView tv_temp_high;
    @BindView(R.id.btn_detail_info)
    Button btn_detail_info;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        //MainActivityPermissionsDispatcher.getLocationWithPermissionCheck(this);
        //MainActivityPermissionsDispatcher.onWeatherEventWithPermissionCheck(this,);
        initLocation();

        requestCurrentWeather(latitude, longitude);

        tv_city.setVisibility(View.INVISIBLE);
        tv_temp.setVisibility(View.INVISIBLE);
        iv_weather.setVisibility(View.INVISIBLE);
        tv_summary.setVisibility(View.INVISIBLE);
        tv_temp_low.setVisibility(View.INVISIBLE);
        tv_temp_high.setVisibility(View.INVISIBLE);
        tv_temp_tag.setVisibility(View.INVISIBLE);
        btn_detail_info.setVisibility(View.INVISIBLE);

        iv_refresh.setOnClickListener(updateButtonClickListener);
        refreshAnim = AnimationUtils.loadAnimation(this, R.anim.refresh_anim);
        iv_refresh.startAnimation(refreshAnim);

        btn_detail_info.setOnClickListener(weatherDetailInfoClickListener);
    }

    private View.OnClickListener weatherDetailInfoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, WeatherDetailActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener updateButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            iv_refresh.startAnimation(refreshAnim);
            MainActivityPermissionsDispatcher.getLocationWithPermissionCheck(MainActivity.this);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    //get weather data from model
    @NeedsPermission(Manifest.permission.INTERNET)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWeatherEvent(WeatherEvent weatherEvent) {

        Currently currently = weatherEvent.getWeather().getCurrently();
        dailyDatas = weatherEvent.getWeather().getDaily().getData();
        Data dailyData = dailyDatas.get(0);
        hourlyDatas = weatherEvent.getWeather().getHourly().getData();
        HourlyData.setHourlyDatas(hourlyDatas);

        int tempHigh = MathUtil.tempConverterF2C(dailyData.getTemperatureHigh());
        int tempLow = MathUtil.tempConverterF2C(dailyData.getTemperatureLow());
        int temp = MathUtil.tempConverterF2C(currently.getTemperature());

        tv_city.setText(weatherEvent.getWeather().getTimezone());
        tv_city.setVisibility(View.VISIBLE);
        tv_temp_low.setText(String.valueOf((tempLow) + "\u00b0C"));
        tv_temp_low.setVisibility(View.VISIBLE);
        tv_temp_tag.setVisibility(View.VISIBLE);
        tv_temp_high.setText(String.valueOf((tempHigh) + "\u00b0C"));
        tv_temp_high.setVisibility(View.VISIBLE);
        tv_temp.setText(String.valueOf((temp) + "\u00b0C"));
        tv_temp.setVisibility(View.VISIBLE);
        iv_weather.setImageResource(WeathericonUtil.ICONS.get(currently.getIcon()));
        iv_weather.setVisibility(View.VISIBLE);
        tv_summary.setText(currently.getSummary());
        tv_summary.setVisibility(View.VISIBLE);
        btn_detail_info.setVisibility(View.VISIBLE);

        iv_refresh.clearAnimation();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorEvent(ErrorEvent errorEvent) {
        Toast.makeText(this, errorEvent.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    private void requestCurrentWeather(double lat, double lng) {
        WeatherServiceProvider weatherServiceProvider = new WeatherServiceProvider();
        weatherServiceProvider.getWeather(lat, lng);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //gps已打开
            getLocation();
        } else {
            toggleGPS();
            new Handler() {
            }.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getLocation();
                }
            }, 2000);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void toggleGPS() {
        Intent gpsIntent = new Intent();
        gpsIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
        gpsIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(this, 0, gpsIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 8000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude(); // 经度
                longitude = location.getLongitude(); // 纬度
            }
        }
    }

    // 多个权限
    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    void getLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Log.e(TAG, "-----------location----" + location);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        }
    }

    // 向用户说明为什么需要这些权限（可选）
    @OnShowRationale({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    void showRationaleForLocation(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.permission_location_rationale)
                .setPositiveButton(R.string.button_allow, (dialog, button) -> request.proceed())
                .setNegativeButton(R.string.button_deny, (dialog, button) -> request.cancel())
                .show();
    }

    LocationListener locationListener = new LocationListener() {
        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String provider) {

            Log.e(TAG, provider);
        }

        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, provider);
        }

        // 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Log.e(TAG, "Location changed : Lat: " + location.getLatitude() + " Lng: " + location.getLongitude());
                latitude = location.getLatitude(); // 经度
                longitude = location.getLongitude(); // 纬度
            }
        }
    };

    //定义一个更新显示的方法
    private void updateShow(Location location) {
        latitude = location.getLongitude();
        longitude = location.getLatitude();
        Log.e(TAG, "----latitude=" + latitude);
        Log.e(TAG, "----longitude=" + longitude);
        requestCurrentWeather(latitude, longitude);
    }
}