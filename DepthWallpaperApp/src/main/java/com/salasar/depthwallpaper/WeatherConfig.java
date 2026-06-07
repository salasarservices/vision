package com.salasar.depthwallpaper;

import android.content.Context;
import android.content.SharedPreferences;

public class WeatherConfig {
    private static final String PREFS = "weather_prefs";
    private static final String KEY_TEMP = "temp";
    private static final String KEY_COND = "condition";

    public static String getTemperature(Context ctx) {
        return ctx.getSharedPreferences(PREFS, 0).getString(KEY_TEMP, "--");
    }

    public static String getCondition(Context ctx) {
        return ctx.getSharedPreferences(PREFS, 0).getString(KEY_COND, "Clear");
    }

    public static void save(Context ctx, String temp, String condition) {
        ctx.getSharedPreferences(PREFS, 0).edit()
            .putString(KEY_TEMP, temp)
            .putString(KEY_COND, condition)
            .apply();
    }
}
