package com.salasar.depthwallpaper.scenes;

import android.graphics.*;
import com.salasar.depthwallpaper.ClockStyle;
import com.salasar.depthwallpaper.WallpaperScene;

public class OceanSurge extends WallpaperScene {
    @Override public String getName() { return "Ocean Surge"; }
    @Override public int getLayerCount() { return 5; }
    @Override public ClockStyle getClockStyle() { return ClockStyle.ELEGANT; }
    @Override public int getClockInsertAfterLayer() { return 1; }

    @Override
    public void drawLayer(Canvas c, int layer, int w, int h) {
        switch (layer) {
            case 0: drawSky(c, w, h); break;
            case 1: drawMoonReflection(c, w, h); break;
            case 2: drawWaveBody(c, w, h); break;
            case 3: drawWaveCrest(c, w, h); break;
            case 4: drawFoamForeground(c, w, h); break;
        }
    }

    private void drawSky(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint();
        p.setShader(new LinearGradient(0, 0, 0, h,
            new int[]{0xFF010510, 0xFF030A1E, 0xFF061530, 0xFF0A2045, 0xFF103060},
            new float[]{0f, 0.2f, 0.4f, 0.6f, 1f}, Shader.TileMode.CLAMP));
        c.drawRect(-ex, -ex, w + ex, h + ex, p);
        // Stars
        Paint star = new Paint(Paint.ANTI_ALIAS_FLAG);
        java.util.Random rng = new java.util.Random(888);
        for (int i = 0; i < 120; i++) {
            float sx = rng.nextFloat() * (w + 2 * ex) - ex;
            float sy = rng.nextFloat() * h * 0.50f;
            star.setColor(Color.argb(60 + rng.nextInt(140), 200, 220, 255));
            c.drawCircle(sx, sy, 0.5f + rng.nextFloat() * 1.2f, star);
        }
    }

    private void drawMoonReflection(Canvas c, int w, int h) {
        // Moon
        Paint moon = new Paint(Paint.ANTI_ALIAS_FLAG);
        moon.setShader(new RadialGradient(w * 0.72f, h * 0.10f, h * 0.065f,
            0xFFFFFFEE, 0xFFCCCCAA, Shader.TileMode.CLAMP));
        c.drawCircle(w * 0.72f, h * 0.10f, h * 0.058f, moon);
        // Reflection pillar on water
        Paint ref = new Paint(Paint.ANTI_ALIAS_FLAG);
        ref.setShader(new LinearGradient(w * 0.62f, 0, w * 0.82f, 0,
            0x00FFFFEE, 0x44FFFFEE, Shader.TileMode.MIRROR));
        for (int i = 0; i < 10; i++) {
            float ry = h * (0.50f + i * 0.025f);
            float rw = 15 + i * 8;
            c.drawRect(w * 0.72f - rw, ry, w * 0.72f + rw, ry + 8, ref);
        }
        // Horizon glow
        Paint hor = new Paint();
        hor.setShader(new LinearGradient(0, h * 0.44f, 0, h * 0.52f, 0x22AACCEE, 0x00000000, Shader.TileMode.CLAMP));
        c.drawRect(-MAX_PARALLAX, h * 0.44f, w + MAX_PARALLAX, h * 0.52f, hor);
    }

    private void drawWaveBody(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new LinearGradient(0, h * 0.48f, 0, h * 0.78f,
            new int[]{0xFF0A2845, 0xFF103060, 0xFF0D4070, 0xFF165080},
            new float[]{0f, 0.3f, 0.6f, 1f}, Shader.TileMode.CLAMP));
        Path wave = new Path();
        wave.moveTo(-ex, h + ex);
        wave.lineTo(-ex, h * 0.60f);
        wave.cubicTo(w * 0.1f, h * 0.52f, w * 0.25f, h * 0.56f, w * 0.38f, h * 0.48f);
        wave.cubicTo(w * 0.52f, h * 0.40f, w * 0.65f, h * 0.50f, w * 0.80f, h * 0.44f);
        wave.cubicTo(w * 0.90f, h * 0.40f, w + ex * 0.5f, h * 0.46f, w + ex, h * 0.50f);
        wave.lineTo(w + ex, h + ex);
        wave.close();
        c.drawPath(wave, p);
        // Inner translucency near crest
        Paint glow = new Paint(Paint.ANTI_ALIAS_FLAG);
        glow.setShader(new LinearGradient(0, h * 0.40f, 0, h * 0.55f, 0x3322CCFF, 0x00000000, Shader.TileMode.CLAMP));
        c.drawPath(wave, glow);
    }

    private void drawWaveCrest(Canvas c, int w, int h) {
        // Curling wave crest that crosses y=0.30 (into ELEGANT time area)
        int ex = MAX_PARALLAX;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new LinearGradient(0, h * 0.26f, 0, h * 0.46f,
            new int[]{0xCC88BBDD, 0xFF44AABB, 0xFF2288AA},
            new float[]{0f, 0.5f, 1f}, Shader.TileMode.CLAMP));
        Path crest = new Path();
        crest.moveTo(-ex, h * 0.46f);
        crest.cubicTo(w * 0.15f, h * 0.44f, w * 0.28f, h * 0.36f, w * 0.40f, h * 0.30f);
        crest.cubicTo(w * 0.52f, h * 0.24f, w * 0.62f, h * 0.28f, w * 0.72f, h * 0.26f);
        crest.cubicTo(w * 0.82f, h * 0.24f, w * 0.92f, h * 0.30f, w + ex, h * 0.32f);
        crest.lineTo(w + ex, h * 0.48f);
        crest.cubicTo(w * 0.85f, h * 0.40f, w * 0.60f, h * 0.44f, w * 0.40f, h * 0.46f);
        crest.cubicTo(w * 0.22f, h * 0.48f, w * 0.08f, h * 0.50f, -ex, h * 0.46f);
        crest.close();
        c.drawPath(crest, p);
        // White foam on crest edge
        Paint foam = new Paint(Paint.ANTI_ALIAS_FLAG);
        foam.setColor(0xBBFFFFFF);
        foam.setStrokeWidth(4f);
        foam.setStyle(Paint.Style.STROKE);
        foam.setMaskFilter(new BlurMaskFilter(6, BlurMaskFilter.Blur.NORMAL));
        Path foamLine = new Path();
        foamLine.moveTo(-ex, h * 0.46f);
        foamLine.cubicTo(w * 0.15f, h * 0.44f, w * 0.28f, h * 0.36f, w * 0.40f, h * 0.30f);
        foamLine.cubicTo(w * 0.52f, h * 0.24f, w * 0.62f, h * 0.28f, w * 0.72f, h * 0.26f);
        foamLine.cubicTo(w * 0.82f, h * 0.24f, w * 0.92f, h * 0.30f, w + ex, h * 0.32f);
        c.drawPath(foamLine, foam);
    }

    private void drawFoamForeground(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new LinearGradient(0, h * 0.80f, 0, h + ex, 0xFF082030, 0xFF041018, Shader.TileMode.CLAMP));
        c.drawRect(-ex, h * 0.80f, w + ex, h + ex, p);
        // Foam patches
        Paint foam = new Paint(Paint.ANTI_ALIAS_FLAG);
        foam.setMaskFilter(new BlurMaskFilter(12, BlurMaskFilter.Blur.NORMAL));
        java.util.Random rng = new java.util.Random(55);
        for (int i = 0; i < 15; i++) {
            float fx = rng.nextFloat() * (w + 2 * ex) - ex;
            float fy = h * 0.78f + rng.nextFloat() * h * 0.14f;
            float fr = 10 + rng.nextFloat() * 30;
            foam.setColor(Color.argb(60 + rng.nextInt(60), 200, 230, 255));
            c.drawOval(fx - fr * 2, fy - fr * 0.5f, fx + fr * 2, fy + fr * 0.5f, foam);
        }
    }
}
