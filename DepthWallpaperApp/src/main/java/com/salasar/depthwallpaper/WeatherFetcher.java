package com.salasar.depthwallpaper;

import android.content.Context;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherFetcher extends AsyncTask<Void, Void, String[]> {

    public interface Callback {
        void onWeather(String temp, String condition, int code);
    }

    private final Context ctx;
    private final Callback callback;

    public WeatherFetcher(Context ctx, Callback cb) {
        this.ctx = ctx.getApplicationContext();
        this.callback = cb;
    }

    @Override
    protected String[] doInBackground(Void... params) {
        try {
            // Step 1: Get lat/lon from IP geolocation (free, no key)
            String geoJson = fetch("https://ipinfo.io/json");
            String loc = jsonGet(geoJson, "loc"); // "28.61,77.20"
            if (loc.isEmpty()) return null;
            String[] ll = loc.split(",");
            if (ll.length < 2) return null;
            String lat = ll[0].trim();
            String lon = ll[1].trim();

            // Step 2: Get current weather from Open-Meteo (free, no key)
            String url = "https://api.open-meteo.com/v1/forecast"
                + "?latitude=" + lat + "&longitude=" + lon
                + "&current_weather=true";
            String weatherJson = fetch(url);

            String tempRaw = jsonGet(weatherJson, "temperature");
            String codeStr = jsonGet(weatherJson, "weathercode");
            if (tempRaw.isEmpty() || codeStr.isEmpty()) return null;

            int code = (int) Float.parseFloat(codeStr);
            int tempRounded = Math.round(Float.parseFloat(tempRaw));
            String condition = codeToCondition(code);

            return new String[]{ tempRounded + "°", condition, String.valueOf(code) };
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String[] result) {
        if (result == null || callback == null) return;
        int code = Integer.parseInt(result[2]);
        WeatherConfig.save(ctx, result[0], result[1], code);
        callback.onWeather(result[0], result[1], code);
    }

    private static String fetch(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);
        conn.setRequestProperty("User-Agent", "DepthWallpaper/1.0");
        int code = conn.getResponseCode();
        if (code != 200) throw new Exception("HTTP " + code);
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();
        conn.disconnect();
        return sb.toString();
    }

    // Minimal JSON value extractor — no library needed
    static String jsonGet(String json, String key) {
        String token = "\"" + key + "\"";
        int i = json.indexOf(token);
        if (i < 0) return "";
        i += token.length();
        while (i < json.length() && (json.charAt(i) == ' ' || json.charAt(i) == ':' || json.charAt(i) == '\t')) i++;
        if (i >= json.length()) return "";
        char first = json.charAt(i);
        if (first == '"') {
            i++;
            int end = json.indexOf('"', i);
            return end < 0 ? "" : json.substring(i, end);
        }
        // number / bool / null
        int end = i;
        while (end < json.length() && "0123456789.-eE".indexOf(json.charAt(end)) >= 0) end++;
        return json.substring(i, end);
    }

    static String codeToCondition(int code) {
        if (code == 0) return "Clear";
        if (code <= 1) return "Mostly Clear";
        if (code <= 2) return "Partly Cloudy";
        if (code <= 3) return "Overcast";
        if (code <= 48) return "Foggy";
        if (code <= 55) return "Drizzle";
        if (code <= 65) return "Rainy";
        if (code <= 67) return "Freezing Rain";
        if (code <= 77) return "Snowy";
        if (code <= 82) return "Rain Showers";
        if (code <= 86) return "Snow Showers";
        if (code <= 99) return "Thunderstorm";
        return "Clear";
    }
}
