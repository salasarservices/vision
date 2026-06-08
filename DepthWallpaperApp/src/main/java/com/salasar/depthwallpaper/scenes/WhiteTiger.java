package com.salasar.depthwallpaper.scenes;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import com.salasar.depthwallpaper.ClockStyle;
import com.salasar.depthwallpaper.R;
import com.salasar.depthwallpaper.WallpaperScene;

/**
 * Bengal tiger walking toward camera photo.
 * Layer 0: full photo (background — blurry golden grass behind tiger)
 * Layer 1: empty — BRUTAL clock inserted here (upper area)
 * Layer 2: photo clipped to upper-center — tiger head/body covers the clock
 */
public class WhiteTiger extends WallpaperScene {

    private Bitmap photo;

    @Override public String getName() { return "Bengal Tiger"; }
    @Override public int getLayerCount() { return 3; }
    @Override public ClockStyle getClockStyle() { return ClockStyle.BRUTAL; }
    @Override public int getClockInsertAfterLayer() { return 1; }
    @Override public int getAccentColor() { return 0xFFE08020; }

    @Override
    public void init(Resources res) {
        if (photo != null) return;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 2;
        photo = BitmapFactory.decodeResource(res, R.drawable.photo_tiger, opts);
    }

    @Override
    public void drawLayer(Canvas canvas, int layer, int w, int h) {
        switch (layer) {
            case 0: drawBackground(canvas, w, h); break;
            case 1: break; // clock inserted here (BRUTAL style — date h*0.17, time h*0.40)
            case 2: drawForeground(canvas, w, h); break;
        }
    }

    private void drawBackground(Canvas canvas, int w, int h) {
        if (photo == null) {
            drawLinearGrad(canvas, w, h, 0xFF3A2800, 0xFF7A5020);
            return;
        }
        Paint p = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(photo, null,
            new RectF(-MAX_PARALLAX, -MAX_PARALLAX, w + MAX_PARALLAX, h + MAX_PARALLAX), p);
    }

    private void drawForeground(Canvas canvas, int w, int h) {
        if (photo == null) return;
        // Clip to tiger head only (top 28% of frame).
        // The date (h*0.185) sits behind the tiger, while the large time digits
        // (baseline h*0.415) emerge visibly below — the iOS/HyperOS depth effect.
        canvas.save();
        Path clip = new Path();
        clip.addRoundRect(
            new RectF(-MAX_PARALLAX, -MAX_PARALLAX, w + MAX_PARALLAX, h * 0.28f),
            w * 0.20f, w * 0.20f, Path.Direction.CW);
        canvas.clipPath(clip);
        Paint p = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(photo, null,
            new RectF(-MAX_PARALLAX, -MAX_PARALLAX, w + MAX_PARALLAX, h + MAX_PARALLAX), p);
        canvas.restore();
    }
}
