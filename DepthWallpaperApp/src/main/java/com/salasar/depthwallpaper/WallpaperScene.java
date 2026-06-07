package com.salasar.depthwallpaper;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;

public abstract class WallpaperScene {
    protected static final int MAX_PARALLAX = 80; // px offset per full tilt

    public abstract String getName();
    public abstract int getLayerCount();

    /** Draw a single depth layer. */
    public abstract void drawLayer(Canvas canvas, int layer, int width, int height);

    /** Which ClockStyle this scene uses. */
    public abstract ClockStyle getClockStyle();

    /**
     * The clock is drawn AFTER this layer index.
     * E.g., returning 1 means: draw layers 0, 1, then clock, then layers 2, 3, 4.
     * Returning a value >= getLayerCount()-1 means clock is drawn last (no layer in front).
     */
    public abstract int getClockInsertAfterLayer();

    public int getAccentColor() { return 0xFF6C63FF; }

    /**
     * Draw all layers with clock inserted at the correct depth position.
     */
    public final void drawAll(Canvas canvas, int width, int height,
                              String weatherTemp, String weatherCondition) {
        int insertAfter = getClockInsertAfterLayer();
        for (int i = 0; i < getLayerCount(); i++) {
            drawLayer(canvas, i, width, height);
            if (i == insertAfter) {
                ClockPainter.draw(canvas, width, height, getClockStyle(),
                        weatherTemp, weatherCondition);
            }
        }
        // If insertAfter >= layerCount, clock was never drawn — draw it last
        if (insertAfter >= getLayerCount()) {
            ClockPainter.draw(canvas, width, height, getClockStyle(),
                    weatherTemp, weatherCondition);
        }
    }

    /** Fallback drawAll with no weather info. */
    public final void drawAll(Canvas canvas, int width, int height) {
        drawAll(canvas, width, height, "--", "Clear");
    }

    // ---- Shared helpers ----
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
