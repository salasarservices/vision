package com.salasar.depthwallpaper.scenes;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import com.salasar.depthwallpaper.ClockStyle;
import com.salasar.depthwallpaper.R;
import com.salasar.depthwallpaper.WallpaperScene;

/**
 * Iceberg/glacier photo (dark blue ocean, massive ice wall).
 * Layer 0: full photo background
 * Layer 1: empty — MINIMAL clock inserted (right-aligned in sky area)
 * Layer 2: photo clipped to iceberg body (h 8%–58%) — ice wall crosses lower clock text
 */
public class GlacierPeak extends WallpaperScene {

    private Bitmap photo;

    @Override public String getName() { return "Glacier Peak"; }
    @Override public int getLayerCount() { return 3; }
    @Override public ClockStyle getClockStyle() { return ClockStyle.MINIMAL; }
    @Override public int getClockInsertAfterLayer() { return 1; }
    @Override public int getAccentColor() { return 0xFF5090C0; }

    @Override
    public void init(Resources res) {
        if (photo != null) return;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 2;
        photo = BitmapFactory.decodeResource(res, R.drawable.photo_glacier, opts);
    }

    @Override
    public void drawLayer(Canvas canvas, int layer, int w, int h) {
        switch (layer) {
            case 0: drawBackground(canvas, w, h); break;
            case 1: break; // clock drawn here (MINIMAL right-aligned, time at h*0.34)
            case 2: drawForeground(canvas, w, h); break;
        }
    }

    private void drawBackground(Canvas canvas, int w, int h) {
        if (photo == null) {
            drawLinearGrad(canvas, w, h, 0xFF0A1F3A, 0xFF1C3D6E);
            return;
        }
        Paint p = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(photo, null,
            new RectF(-MAX_PARALLAX, -MAX_PARALLAX, w + MAX_PARALLAX, h + MAX_PARALLAX), p);
    }

    private void drawForeground(Canvas canvas, int w, int h) {
        if (photo == null) return;
        // Clip to the iceberg body — from about 8% to 58% height.
        // The MINIMAL clock is right-aligned at h*0.34; the iceberg top edge (h*0.08)
        // slices across the upper portion of the clock, creating the depth effect.
        canvas.save();
        canvas.clipRect(-MAX_PARALLAX, h * 0.07f, w + MAX_PARALLAX, h * 0.58f);
        Paint p = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(photo, null,
            new RectF(-MAX_PARALLAX, -MAX_PARALLAX, w + MAX_PARALLAX, h + MAX_PARALLAX), p);
        canvas.restore();
    }
}
