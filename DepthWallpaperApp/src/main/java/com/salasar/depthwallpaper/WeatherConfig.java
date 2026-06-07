package com.salasar.depthwallpaper;

import android.content.Context;
import android.content.SharedPreferences;

public class WeatherConfig {
    private static final String PREFS = "weather_prefs";
    private static final String KEY_TEMP = "temp";
    private static final String KEY_COND = "condition";
    private static final String KEY_CODE = "code";

    public static String getTemperature(Context ctx) {
        return ctx.getSharedPreferences(PREFS, 0).getString(KEY_TEMP, "--");
    }

    public static String getCondition(Context ctx) {
        return ctx.getSharedPreferences(PREFS, 0).getString(KEY_COND, "Clear");
    }

    public static int getCode(Context ctx) {
        return ctx.getSharedPreferences(PREFS, 0).getInt(KEY_CODE, 0);
    }

    public static void save(Context ctx, String temp, String condition, int code) {
        ctx.getSharedPreferences(PREFS, 0).edit()
            .putString(KEY_TEMP, temp)
            .putString(KEY_COND, condition)
            .putInt(KEY_CODE, code)
            .apply();
    }

    // Legacy overload — keeps manual weather entry working
    public static void save(Context ctx, String temp, String condition) {
        int code = conditionToCode(condition);
        save(ctx, temp, condition, code);
    }

    private static int conditionToCode(String cond) {
        if (cond == null) return 0;
        String l = cond.toLowerCase();
        if (l.contains("storm") || l.contains("thunder")) return 95;
        if (l.contains("snow") || l.contains("blizzard")) return 71;
        if (l.contains("rain") || l.contains("drizzle") || l.contains("shower")) return 61;
        if (l.contains("fog") || l.contains("mist") || l.contains("haze")) return 45;
        if (l.contains("overcast")) return 3;
        if (l.contains("cloud")) return 2;
        if (l.contains("clear") || l.contains("sunny")) return 0;
        return 1;
    }
}
