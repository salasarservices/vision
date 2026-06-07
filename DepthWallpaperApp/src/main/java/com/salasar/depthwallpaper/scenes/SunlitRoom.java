package com.salasar.depthwallpaper.scenes;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import com.salasar.depthwallpaper.ClockStyle;
import com.salasar.depthwallpaper.R;
import com.salasar.depthwallpaper.WallpaperScene;

/**
 * Bright minimal room with a monstera plant in a wicker basket.
 * White wall = clean backdrop for APPLE clock text.
 * Layer 0: full photo (white room background)
 * Layer 1: subtle dark gradient scrim so white clock text reads on white wall
 * Layer 2: photo clipped to left leaf region — tall leaf covers left side of clock
 */
public class SunlitRoom extends WallpaperScene {

    private Bitmap photo;

    @Override public String getName() { return "Sunlit Room"; }
    @Override public int getLayerCount() { return 3; }
    @Override public ClockStyle getClockStyle() { return ClockStyle.APPLE; }
    @Override public int getClockInsertAfterLayer() { return 1; }
    @Override public int getAccentColor() { return 0xFF5A8040; }

    @Override
    public void init(Resources res) {
        if (photo != null) return;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 2;
        photo = BitmapFactory.decodeResource(res, R.drawable.photo_room, opts);
    }

    @Override
    public void drawLayer(Canvas canvas, int layer, int w, int h) {
        switch (layer) {
            case 0: drawBackground(canvas, w, h); break;
            case 1: drawScrim(canvas, w, h); break;   // dark scrim → APPLE clock on top
            case 2: drawForeground(canvas, w, h); break;
        }
    }

    private void drawBackground(Canvas canvas, int w, int h) {
        if (photo == null) {
            drawLinearGrad(canvas, w, h, 0xFFF5F0E8, 0xFFE8DDD0);
            return;
        }
        Paint p = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(photo, null,
            new RectF(-MAX_PARALLAX, -MAX_PARALLAX, w + MAX_PARALLAX, h + MAX_PARALLAX), p);
    }

    private void drawScrim(Canvas canvas, int w, int h) {
        // Gradient from dark (behind clock area) to transparent — makes white text readable
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new LinearGradient(0, h * 0.12f, 0, h * 0.54f,
            new int[]{ 0x00000000, 0x66000000, 0x88000000, 0x55000000, 0x00000000 },
            new float[]{ 0f, 0.25f, 0.55f, 0.80f, 1f },
            Shader.TileMode.CLAMP));
        canvas.drawRect(0, h * 0.12f, w, h * 0.54f, p);
    }

    private void drawForeground(Canvas canvas, int w, int h) {
        if (photo == null) return;
        // Clip to left third of the photo — the tall monstera leaf that reaches upward
        // from the left of the frame crosses into the clock text area.
        canvas.save();
        canvas.clipRect(-MAX_PARALLAX, h * 0.05f, w * 0.44f, h * 0.72f);
        Paint p = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(photo, null,
            new RectF(-MAX_PARALLAX, -MAX_PARALLAX, w + MAX_PARALLAX, h + MAX_PARALLAX), p);
        canvas.restore();
    }
}
