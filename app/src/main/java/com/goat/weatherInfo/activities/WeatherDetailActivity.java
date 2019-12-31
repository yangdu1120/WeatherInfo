package com.goat.weatherInfo.activities;

import android.content.Context;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goat.weatherInfo.R;
import com.goat.weatherInfo.models.HourlyData;
import com.goat.weatherInfo.utils.MathUtil;
import com.goat.weatherInfo.utils.TimeUtil;
import com.goat.weatherInfo.utils.WeathericonUtil;

public class WeatherDetailActivity extends AppCompatActivity {

    private LayoutInflater inflater;
    private HourlyReportAdapter hourlyReportAdapter = new HourlyReportAdapter();
    private static final String TAG = "WeatherDetailActivity";

    private long timeStampSec;
    private String timestamp;

    RecyclerView recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_weather_detail);

        this.recycler_view = findViewById(R.id.recycler_view);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        recycler_view.setAdapter(hourlyReportAdapter);
        recycler_view.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //get current timeStamp
        timeStampSec = System.currentTimeMillis();

        // Convert 13 to 10 digits from current timeStamp
        timeStampSec = System.currentTimeMillis()/1000;
        timestamp = String.format("%010d", timeStampSec);

        Log.e(TAG, "timeStamp="+timestamp);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private class HourlyReportAdapter extends RecyclerView.Adapter<HourlyReportViewHolder> {
        @Override
        public HourlyReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_hourly_forecast, parent, false);

            return new HourlyReportViewHolder(view);
        }

        @Override
        public void onBindViewHolder(HourlyReportViewHolder holder, int position) {

            holder.setIcon(HourlyData.getHourlyDatas().get(position).getIcon());
            int temp = MathUtil.tempConverterF2C(HourlyData.getHourlyDatas().get(position).getTemperature());
            holder.setTemp(String.valueOf(temp));
            String time = TimeUtil.getTime(HourlyData.getHourlyDatas().get(position).getTime());
            holder.setTime(time);

            Log.e(TAG,"--getTime()="+ HourlyData.getHourlyDatas().get(position).getTime() );
            
            if(Integer.parseInt(time) > Integer.parseInt(timestamp)){
                //holder.getPosition().setRelativeLayout(Color.parseColor("#B6B4B4"));

                Log.e(TAG,"-- position="+ position);
            }
        }

        @Override
        public int getItemCount() {

            return HourlyData.getHourlyDatas().size();
        }
    }

    public class HourlyReportViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        ImageView icon;
        TextView time, temp;


        HourlyReportViewHolder(View view) {
            super(view);

            relativeLayout  = view.findViewById(R.id.rl_hourly_item);
            icon = view.findViewById(R.id.iv_hourly_icon);
            temp = view.findViewById(R.id.tv_hourly_temp);
            time = view.findViewById(R.id.tv_hourly_time);
        }

        public void setRelativeLayout(int color) {
            this.relativeLayout.setBackgroundColor(color);
        }

        public void setIcon(String icon) {
            Integer imageResource = WeathericonUtil.ICONS.get(icon);
            this.icon.setImageResource(Integer.valueOf(imageResource));
        }

        public void setTemp(String temp) {
            this.temp.setText(temp + "\u00b0C");
        }

        public void setTime(String time) {
            this.time.setText(time);
        }
    }

}
