package com.salasar.depthwallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

public class ThumbnailView extends View {
    private WallpaperScene scene;
    private Bitmap cache;
    private String weatherTemp = "--";
    private String weatherCondition = "Clear";
    private int weatherCode = 0;

    public ThumbnailView(Context ctx) { super(ctx); }
    public ThumbnailView(Context ctx, AttributeSet attrs) { super(ctx, attrs); }

    public void setScene(WallpaperScene s) {
        this.scene = s;
        this.cache = null;
        invalidate();
    }

    public void setWeather(String temp, String condition, int code) {
        this.weatherTemp = temp;
        this.weatherCondition = condition;
        this.weatherCode = code;
        this.cache = null;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (scene == null) return;
        int w = getWidth(), h = getHeight();
        if (w <= 0 || h <= 0) return;
        if (cache == null || cache.getWidth() != w || cache.getHeight() != h) {
            scene.init(getResources());
            cache = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas bc = new Canvas(cache);
            bc.drawColor(Color.BLACK);
            scene.drawAll(bc, w, h, weatherTemp, weatherCondition, weatherCode);
        }
        canvas.drawBitmap(cache, 0, 0, null);
    }
}
