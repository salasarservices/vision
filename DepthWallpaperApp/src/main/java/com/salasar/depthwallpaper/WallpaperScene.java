package com.salasar.depthwallpaper;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;

public abstract class WallpaperScene {
    protected static final int MAX_PARALLAX = 80;

    public abstract String getName();
    public abstract int getLayerCount();
    public abstract void drawLayer(Canvas canvas, int layer, int width, int height);
    public abstract ClockStyle getClockStyle();
    public abstract int getClockInsertAfterLayer();

    public int getAccentColor() { return 0xFF6C63FF; }

    /**
     * Called once before the scene is drawn (e.g., to decode a photo bitmap).
     * Idempotent — safe to call multiple times.
     */
    public void init(Resources res) {}

    /** Draw all layers + clock sandwiched at the right depth. */
    public final void drawAll(Canvas canvas, int width, int height,
                              String weatherTemp, String weatherCondition, int weatherCode) {
        int insertAfter = getClockInsertAfterLayer();
        for (int i = 0; i < getLayerCount(); i++) {
            drawLayer(canvas, i, width, height);
            if (i == insertAfter) {
                ClockPainter.draw(canvas, width, height, getClockStyle(),
                        weatherTemp, weatherCondition, weatherCode);
            }
        }
        if (insertAfter >= getLayerCount()) {
            ClockPainter.draw(canvas, width, height, getClockStyle(),
                    weatherTemp, weatherCondition, weatherCode);
        }
    }

    /** Backward-compat overload (no weatherCode — defaults to clear sky). */
    public final void drawAll(Canvas canvas, int width, int height,
                              String weatherTemp, String weatherCondition) {
        drawAll(canvas, width, height, weatherTemp, weatherCondition, 0);
    }

    /** Fallback with no weather info. */
    public final void drawAll(Canvas canvas, int width, int height) {
        drawAll(canvas, width, height, "--", "Clear", 0);
    }

    // ---- Shared painting helpers ----

    protected Paint newPaint(int color) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(color);
        return p;
    }

    protected Paint newPaint(int color, Paint.Style style) {
        Paint p = newPaint(color);
        p.setStyle(style);
        return p;
    }

    protected void drawLinearGrad(Canvas canvas, int w, int h, int top, int bottom) {
        Paint p = new Paint();
        p.setShader(new LinearGradient(0, 0, 0, h, top, bottom, Shader.TileMode.CLAMP));
        canvas.drawRect(-MAX_PARALLAX, -MAX_PARALLAX, w + MAX_PARALLAX, h + MAX_PARALLAX, p);
    }

    protected Path mountainPath(int[] xs, int[] ys, int w, int h) {
        Path path = new Path();
        path.moveTo(-MAX_PARALLAX, h + MAX_PARALLAX);
        path.lineTo(xs[0], ys[0]);
        for (int i = 1; i < xs.length; i++) {
            int cx = (xs[i - 1] + xs[i]) / 2;
            int cy = (ys[i - 1] + ys[i]) / 2;
            path.quadTo(xs[i - 1], ys[i - 1], cx, cy);
        }
        path.lineTo(w + MAX_PARALLAX, h + MAX_PARALLAX);
        path.close();
        return path;
    }
}
