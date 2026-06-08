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
 * Close-up moon photo (dark space on left, moon fills right side).
 * Layer 0: full photo (moon + space background)
 * Layer 1: empty — HYPEROS clock inserted (centered, left-center dark space area)
 * Layer 2: photo clipped to moon region (right 75% of frame) — moon surface covers
 *          the right side of the clock display, replicating the iOS/HyperOS depth effect
 */
public class LunarSurface extends WallpaperScene {

    private Bitmap photo;

    @Override public String getName() { return "Lunar Surface"; }
    @Override public int getLayerCount() { return 3; }
    @Override public ClockStyle getClockStyle() { return ClockStyle.HYPEROS; }
    @Override public int getClockInsertAfterLayer() { return 1; }
    @Override public int getAccentColor() { return 0xFF8899AA; }

    @Override
    public void init(Resources res) {
        if (photo != null) return;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 2;
        photo = BitmapFactory.decodeResource(res, R.drawable.photo_moon, opts);
    }

    @Override
    public void drawLayer(Canvas canvas, int layer, int w, int h) {
        switch (layer) {
            case 0: drawBackground(canvas, w, h); break;
            case 1: break; // HYPEROS clock drawn here
            case 2: drawForeground(canvas, w, h); break;
        }
    }

    private void drawBackground(Canvas canvas, int w, int h) {
        if (photo == null) {
            drawLinearGrad(canvas, w, h, 0xFF050810, 0xFF0A0F1E);
            return;
        }
        Paint p = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(photo, null,
            new RectF(-MAX_PARALLAX, -MAX_PARALLAX, w + MAX_PARALLAX, h + MAX_PARALLAX), p);
    }

    private void drawForeground(Canvas canvas, int w, int h) {
        if (photo == null) return;
        // Clip to right 62% of the frame — the moon fills this region.
        // Moon left edge at x=0.38w cuts through the right side of the centered
        // HYPEROS clock, leaving the left portion readable against dark space —
        // exact replication of HyperOS Moon depth wallpaper.
        canvas.save();
        canvas.clipRect(w * 0.38f, -MAX_PARALLAX, w + MAX_PARALLAX, h * 0.78f);
        Paint p = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(photo, null,
            new RectF(-MAX_PARALLAX, -MAX_PARALLAX, w + MAX_PARALLAX, h + MAX_PARALLAX), p);
        canvas.restore();
    }
}
