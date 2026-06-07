package com.salasar.depthwallpaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class WallpaperGridAdapter extends BaseAdapter {
    private final Context ctx;
    private final List<WallpaperScene> scenes;
    private String weatherTemp = "--";
    private String weatherCondition = "Clear";
    private int weatherCode = 0;

    public WallpaperGridAdapter(Context ctx, List<WallpaperScene> scenes) {
        this.ctx = ctx;
        this.scenes = scenes;
    }

    public void setWeather(String temp, String condition, int code) {
        this.weatherTemp = temp;
        this.weatherCondition = condition;
        this.weatherCode = code;
        notifyDataSetChanged();
    }

    // Backward compat for manual weather entry (legacy dialog)
    public void setWeather(String temp, String condition) {
        setWeather(temp, condition, 0);
    }

    @Override public int getCount() { return scenes.size(); }
    @Override public WallpaperScene getItem(int pos) { return scenes.get(pos); }
    @Override public long getItemId(int pos) { return pos; }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(ctx).inflate(R.layout.item_wallpaper, parent, false);
        }
        WallpaperScene scene = scenes.get(pos);
        ThumbnailView thumb = (ThumbnailView) view.findViewById(R.id.thumbnail_view);
        TextView name = (TextView) view.findViewById(R.id.tv_name);
        thumb.setWeather(weatherTemp, weatherCondition, weatherCode);
        thumb.setScene(scene);
        name.setText(scene.getName());
        return view;
    }
}
