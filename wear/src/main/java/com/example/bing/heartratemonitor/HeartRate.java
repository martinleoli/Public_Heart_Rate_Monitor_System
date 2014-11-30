package com.example.bing.heartratemonitor;

import android.graphics.Color;

/**
 * Created by bing on 11/29/14.
 */
public class HeartRate {

    public static int getHeartRateColor(double hr){

        if(hr < 60) return Color.BLUE;
        if(hr < 90) return Color.GREEN;
        if(hr < 120) return Color.YELLOW;
        return Color.RED;
    }
}
