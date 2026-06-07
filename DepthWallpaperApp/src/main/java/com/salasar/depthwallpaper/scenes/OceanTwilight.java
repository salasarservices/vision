package com.salasar.depthwallpaper.scenes;

import android.graphics.*;
import com.salasar.depthwallpaper.ClockStyle;
import com.salasar.depthwallpaper.WallpaperScene;

public class OceanTwilight extends WallpaperScene {
    @Override public String getName() { return "Ocean Twilight"; }
    @Override public int getLayerCount() { return 5; }
    @Override public ClockStyle getClockStyle() { return ClockStyle.ELEGANT; }
    @Override public int getClockInsertAfterLayer() { return 1; }
    @Override public int getAccentColor() { return 0xFFFF7043; }

    @Override
    public void drawLayer(Canvas c, int layer, int w, int h) {
        switch (layer) {
            case 0: drawSky(c, w, h); break;
            case 1: drawSun(c, w, h); break;
            case 2: drawHorizon(c, w, h); break;
            case 3: drawMidWaves(c, w, h); break;
            case 4: drawForeground(c, w, h); break;
        }
    }

    private void drawSky(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint();
        p.setShader(new LinearGradient(0, 0, 0, h,
            new int[]{0xFF1A0A2E, 0xFF8B1A4A, 0xFFD4400A, 0xFFFF8C00, 0xFFFFCC44},
            new float[]{0f, 0.25f, 0.5f, 0.72f, 1f}, Shader.TileMode.CLAMP));
        c.drawRect(-ex, -ex, w+ex, h+ex, p);
    }

    private void drawSun(Canvas c, int w, int h) {
        float sunX = w * 0.5f;
        float sunY = h * 0.52f;
        // Outer glow
        Paint glow = new Paint(Paint.ANTI_ALIAS_FLAG);
        glow.setShader(new RadialGradient(sunX, sunY, h * 0.35f,
            0x88FFDD00, 0x00FFAA00, Shader.TileMode.CLAMP));
        c.drawCircle(sunX, sunY, h * 0.35f, glow);
        // Sun body
        Paint sun = new Paint(Paint.ANTI_ALIAS_FLAG);
        sun.setShader(new RadialGradient(sunX, sunY - 20, h * 0.085f,
            0xFFFFFF88, 0xFFFF8C00, Shader.TileMode.CLAMP));
        c.drawCircle(sunX, sunY, h * 0.085f, sun);
        // Reflection on water (rippled streaks)
        Paint ref = new Paint(Paint.ANTI_ALIAS_FLAG);
        ref.setShader(new LinearGradient(sunX - 60, 0, sunX + 60, 0, 0x88FFCC44, 0x00FFCC44, Shader.TileMode.MIRROR));
        for (int i = 0; i < 8; i++) {
            float ry = h * 0.53f + i * 18;
            float rw = 40 + i * 15;
            c.drawRect(sunX - rw, ry, sunX + rw, ry + 6, ref);
        }
    }

    private void drawHorizon(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint();
        p.setShader(new LinearGradient(0, h * 0.50f, 0, h * 0.62f,
            0xFF112244, 0xFF1A3A6A, Shader.TileMode.CLAMP));
        c.drawRect(-ex, h * 0.50f, w+ex, h * 0.62f, p);
    }

    private void drawMidWaves(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new LinearGradient(0, h * 0.60f, 0, h * 0.80f,
            0xFF0D2A50, 0xFF102040, Shader.TileMode.CLAMP));
        Path waves = new Path();
        waves.moveTo(-ex, h + ex);
        waves.lineTo(-ex, h * 0.62f);
        float step = (w + 2 * ex) / 8f;
        for (int i = 0; i <= 8; i++) {
            float wx = -ex + i * step;
            float wy = h * 0.62f + (i % 2 == 0 ? -8 : 8);
            if (i == 0) waves.lineTo(wx, wy);
            else waves.quadTo(wx - step / 2, h * 0.62f + (i % 2 == 0 ? 8 : -8), wx, wy);
        }
        waves.lineTo(w + ex, h + ex);
        waves.close();
        c.drawPath(waves, p);
    }

    private void drawForeground(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        // Dark ocean foreground with wave crests
        Paint ocean = new Paint(Paint.ANTI_ALIAS_FLAG);
        ocean.setShader(new LinearGradient(0, h * 0.76f, 0, h + ex,
            0xFF071828, 0xFF050E18, Shader.TileMode.CLAMP));
        Path fg = new Path();
        fg.moveTo(-ex, h + ex);
        fg.lineTo(-ex, h * 0.78f);
        float step = (w + 2 * ex) / 6f;
        for (int i = 0; i <= 6; i++) {
            float wx = -ex + i * step;
            float dy = (i % 2 == 0) ? -12 : 0;
            if (i == 0) fg.lineTo(wx, h * 0.78f + dy);
            else fg.quadTo(wx - step / 2, h * 0.78f + (i % 2 == 0 ? 0 : -8), wx, h * 0.78f + dy);
        }
        fg.lineTo(w + ex, h + ex);
        fg.close();
        c.drawPath(fg, ocean);
        // Foam highlights on wave crests
        Paint foam = new Paint(Paint.ANTI_ALIAS_FLAG);
        foam.setColor(0x44AACCEE);
        foam.setStrokeWidth(2.5f);
        foam.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < 6; i++) {
            float wx = -ex + i * step;
            c.drawArc(wx - 20, h * 0.78f - 14, wx + 20, h * 0.78f + 4, 200, 140, false, foam);
        }
    }
}
