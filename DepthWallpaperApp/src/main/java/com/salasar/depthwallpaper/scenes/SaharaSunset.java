package com.salasar.depthwallpaper.scenes;

import android.graphics.*;
import com.salasar.depthwallpaper.ClockStyle;
import com.salasar.depthwallpaper.WallpaperScene;

public class SaharaSunset extends WallpaperScene {
    @Override public String getName() { return "Sahara Sunset"; }
    @Override public int getLayerCount() { return 5; }
    @Override public ClockStyle getClockStyle() { return ClockStyle.BRUTAL; }
    @Override public int getClockInsertAfterLayer() { return 1; }
    @Override public int getAccentColor() { return 0xFFFF8C00; }

    @Override
    public void drawLayer(Canvas c, int layer, int w, int h) {
        switch (layer) {
            case 0: drawSky(c, w, h); break;
            case 1: drawSun(c, w, h); break;
            case 2: drawFarDunes(c, w, h); break;
            case 3: drawMidDunes(c, w, h); break;
            case 4: drawNearDune(c, w, h); break;
        }
    }

    private void drawSky(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint();
        p.setShader(new LinearGradient(0, 0, 0, h,
            new int[]{0xFF1A0505, 0xFF8B1A00, 0xFFCC4400, 0xFFFF8800, 0xFFFFBB44},
            new float[]{0f, 0.2f, 0.45f, 0.65f, 0.85f}, Shader.TileMode.CLAMP));
        c.drawRect(-ex, -ex, w+ex, h+ex, p);
    }

    private void drawSun(Canvas c, int w, int h) {
        float sx = w * 0.5f, sy = h * 0.56f;
        // Outer atmospheric glow
        Paint glow = new Paint(Paint.ANTI_ALIAS_FLAG);
        glow.setMaskFilter(new BlurMaskFilter(90, BlurMaskFilter.Blur.NORMAL));
        glow.setShader(new RadialGradient(sx, sy, h * 0.45f,
            0x88FF8800, 0x00FF6600, Shader.TileMode.CLAMP));
        c.drawCircle(sx, sy, h * 0.45f, glow);
        // Sun disc (slightly squished near horizon)
        Paint sun = new Paint(Paint.ANTI_ALIAS_FLAG);
        sun.setShader(new RadialGradient(sx, sy - 10, h * 0.07f, 0xFFFFEE88, 0xFFFF7700, Shader.TileMode.CLAMP));
        c.drawOval(sx - h * 0.068f, sy - h * 0.068f * 0.7f, sx + h * 0.068f, sy + h * 0.068f * 0.7f, sun);
        // Heat shimmer bands
        Paint shimmer = new Paint(Paint.ANTI_ALIAS_FLAG);
        shimmer.setColor(0x22FF9900);
        for (int i = 1; i <= 5; i++) {
            float sr = h * 0.07f + i * h * 0.04f;
            shimmer.setAlpha(50 - i * 8);
            c.drawOval(sx - sr, sy - sr * 0.3f, sx + sr, sy + sr * 0.3f, shimmer);
        }
    }

    private void drawFarDunes(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new LinearGradient(0, h * 0.52f, 0, h * 0.75f,
            0xFFD46A00, 0xFF8B3800, Shader.TileMode.CLAMP));
        Path dunes = new Path();
        dunes.moveTo(-ex, h + ex);
        dunes.lineTo(-ex, h * 0.62f);
        dunes.cubicTo(w * 0.15f, h * 0.52f, w * 0.28f, h * 0.68f, w * 0.38f, h * 0.58f);
        dunes.cubicTo(w * 0.50f, h * 0.48f, w * 0.65f, h * 0.64f, w * 0.72f, h * 0.54f);
        dunes.cubicTo(w * 0.82f, h * 0.44f, w * 0.92f, h * 0.60f, w+ex, h * 0.56f);
        dunes.lineTo(w + ex, h + ex);
        dunes.close();
        c.drawPath(dunes, p);
        // Dune shadow
        Paint shadow = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadow.setShader(new LinearGradient(0, h * 0.58f, 0, h * 0.70f, 0x44220000, 0x00000000, Shader.TileMode.CLAMP));
        c.drawPath(dunes, shadow);
    }

    private void drawMidDunes(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new LinearGradient(0, h * 0.60f, 0, h,
            0xFFC26000, 0xFF7A3500, Shader.TileMode.CLAMP));
        Path dunes = new Path();
        dunes.moveTo(-ex, h + ex);
        dunes.lineTo(-ex, h * 0.74f);
        dunes.cubicTo(w * 0.18f, h * 0.63f, w * 0.30f, h * 0.76f, w * 0.42f, h * 0.66f);
        dunes.cubicTo(w * 0.55f, h * 0.55f, w * 0.68f, h * 0.72f, w * 0.80f, h * 0.62f);
        dunes.cubicTo(w * 0.90f, h * 0.54f, w + ex * 0.3f, h * 0.68f, w + ex, h * 0.64f);
        dunes.lineTo(w + ex, h + ex);
        dunes.close();
        c.drawPath(dunes, p);
        // Dark shadow ridge
        Paint shadow = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadow.setShader(new LinearGradient(0, h * 0.65f, 0, h * 0.75f, 0x66110000, 0x00000000, Shader.TileMode.CLAMP));
        c.drawPath(dunes, shadow);
    }

    private void drawNearDune(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new LinearGradient(0, h * 0.72f, 0, h + ex,
            0xFFAA5500, 0xFF5A2800, Shader.TileMode.CLAMP));
        Path dune = new Path();
        dune.moveTo(-ex, h + ex);
        dune.lineTo(-ex, h * 0.85f);
        dune.cubicTo(w * 0.22f, h * 0.70f, w * 0.50f, h * 0.90f, w * 0.68f, h * 0.72f);
        dune.cubicTo(w * 0.80f, h * 0.62f, w * 0.92f, h * 0.80f, w + ex, h * 0.82f);
        dune.lineTo(w + ex, h + ex);
        dune.close();
        c.drawPath(dune, p);
        // Dark shadow on near dune
        Paint shadow = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadow.setShader(new LinearGradient(0, h * 0.73f, 0, h * 0.88f, 0x88000000, 0x00000000, Shader.TileMode.CLAMP));
        c.drawPath(dune, shadow);
        // Palm tree silhouettes
        drawPalm(c, w * 0.12f, h * 0.85f, h * 0.22f, 0xFF2A1200);
        drawPalm(c, w * 0.85f, h * 0.74f, h * 0.18f, 0xFF2A1200);
    }

    private void drawPalm(Canvas c, float x, float base, float height, int color) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(color);
        // Trunk (slightly curved)
        p.setStrokeWidth(7);
        p.setStyle(Paint.Style.STROKE);
        Path trunk = new Path();
        trunk.moveTo(x, base);
        trunk.cubicTo(x + 10, base - height * 0.4f, x - 8, base - height * 0.7f, x, base - height);
        c.drawPath(trunk, p);
        // Fronds
        p.setStyle(Paint.Style.FILL);
        p.setStrokeWidth(1);
        float[] frondAngles = {-60, -30, 0, 30, 60, 90, 120, 150, 180};
        for (float angle : frondAngles) {
            Path frond = new Path();
            double rad = Math.toRadians(angle);
            float ex2 = (float)(Math.cos(rad) * height * 0.28f);
            float ey = (float)(Math.sin(rad) * height * 0.28f);
            frond.moveTo(x, base - height);
            frond.quadTo(x + ex2 * 0.5f, base - height - ey * 0.5f, x + ex2, base - height - ey);
            p.setStrokeWidth(3);
            p.setStyle(Paint.Style.STROKE);
            c.drawPath(frond, p);
        }
    }
}
