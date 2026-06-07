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

    public WallpaperGridAdapter(Context ctx, List<WallpaperScene> scenes) {
        this.ctx = ctx;
        this.scenes = scenes;
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
        thumb.setScene(scene);
        name.setText(scene.getName());
        return view;
    }
}
