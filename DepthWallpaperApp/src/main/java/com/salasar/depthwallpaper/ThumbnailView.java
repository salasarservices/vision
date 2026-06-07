package com.salasar.depthwallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ThumbnailView extends View {
    private WallpaperScene scene;
    private Bitmap cache;

    public ThumbnailView(Context ctx) { super(ctx); }
    public ThumbnailView(Context ctx, AttributeSet attrs) { super(ctx, attrs); }

    public void setScene(WallpaperScene s) {
        this.scene = s;
        this.cache = null;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (scene == null) return;
        int w = getWidth(), h = getHeight();
        if (w <= 0 || h <= 0) return;
        if (cache == null || cache.getWidth() != w || cache.getHeight() != h) {
            cache = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas bc = new Canvas(cache);
            bc.drawColor(Color.BLACK);
            scene.drawAll(bc, w, h);
        }
        canvas.drawBitmap(cache, 0, 0, null);
    }
}
