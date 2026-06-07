package com.salasar.depthwallpaper.scenes;

import android.graphics.*;
import com.salasar.depthwallpaper.ClockStyle;
import com.salasar.depthwallpaper.WallpaperScene;

/** Dramatic icy mountain peak rising through MINIMAL right-aligned clock. */
public class GlacierPeak extends WallpaperScene {
    @Override public String getName() { return "Glacier Peak"; }
    @Override public int getLayerCount() { return 5; }
    @Override public ClockStyle getClockStyle() { return ClockStyle.MINIMAL; }
    @Override public int getClockInsertAfterLayer() { return 1; }

    @Override
    public void drawLayer(Canvas c, int layer, int w, int h) {
        switch (layer) {
            case 0: drawSky(c, w, h); break;
            case 1: drawFarMountains(c, w, h); break;
            case 2: drawMainPeak(c, w, h); break;
            case 3: drawIceFormations(c, w, h); break;
            case 4: drawForegroundSnow(c, w, h); break;
        }
    }

    private void drawSky(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint();
        p.setShader(new LinearGradient(0, 0, 0, h,
            new int[]{0xFF010508, 0xFF060E18, 0xFF0E1E35, 0xFF1A3050, 0xFF2A4870, 0xFF3A6090},
            new float[]{0f, 0.15f, 0.3f, 0.5f, 0.7f, 1f}, Shader.TileMode.CLAMP));
        c.drawRect(-ex, -ex, w + ex, h + ex, p);
        // Stars
        Paint star = new Paint(Paint.ANTI_ALIAS_FLAG);
        star.setColor(0xAADDEEFF);
        java.util.Random rng = new java.util.Random(312);
        for (int i = 0; i < 180; i++) {
            float sx = rng.nextFloat() * (w + 2 * ex) - ex;
            float sy = rng.nextFloat() * h * 0.55f - ex;
            c.drawCircle(sx, sy, 0.5f + rng.nextFloat(), star);
        }
    }

    private void drawFarMountains(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new LinearGradient(0, h * 0.45f, 0, h * 0.65f, 0xFF8AACCB, 0xFF557799, Shader.TileMode.CLAMP));
        Path mts = new Path();
        mts.moveTo(-ex, h + ex);
        mts.lineTo(-ex, h * 0.62f);
        mts.quadTo(w * 0.08f, h * 0.52f, w * 0.16f, h * 0.58f);
        mts.quadTo(w * 0.24f, h * 0.64f, w * 0.30f, h * 0.50f);
        mts.quadTo(w * 0.38f, h * 0.42f, w * 0.45f, h * 0.56f);
        mts.quadTo(w * 0.55f, h * 0.66f, w * 0.62f, h * 0.50f);
        mts.quadTo(w * 0.70f, h * 0.40f, w * 0.76f, h * 0.54f);
        mts.quadTo(w * 0.86f, h * 0.62f, w + ex, h * 0.56f);
        mts.lineTo(w + ex, h + ex);
        mts.close();
        c.drawPath(mts, p);
    }

    private void drawMainPeak(Canvas c, int w, int h) {
        // Dominant central peak — tip at y=0.20, well into MINIMAL clock zone
        float peakX = w * 0.5f, peakY = h * 0.20f;
        float baseY = h * 0.88f;
        float baseW = w * 0.55f;

        // Left (lit) face
        Paint lit = new Paint(Paint.ANTI_ALIAS_FLAG);
        lit.setShader(new LinearGradient(peakX - baseW * 0.5f, 0, peakX, 0,
            0xFF6688AA, 0xFFCCDDEE, Shader.TileMode.CLAMP));
        Path leftFace = new Path();
        leftFace.moveTo(peakX, peakY);
        leftFace.lineTo(peakX - baseW * 0.5f, baseY);
        leftFace.lineTo(peakX, baseY);
        leftFace.close();
        c.drawPath(leftFace, lit);

        // Right (shadow) face
        Paint shadow = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadow.setShader(new LinearGradient(peakX, 0, peakX + baseW * 0.5f, 0,
            0xFF334455, 0xFF223344, Shader.TileMode.CLAMP));
        Path rightFace = new Path();
        rightFace.moveTo(peakX, peakY);
        rightFace.lineTo(peakX, baseY);
        rightFace.lineTo(peakX + baseW * 0.5f, baseY);
        rightFace.close();
        c.drawPath(rightFace, shadow);

        // Snow cap (bright white from tip to ~35% down)
        Paint snow = new Paint(Paint.ANTI_ALIAS_FLAG);
        snow.setShader(new LinearGradient(0, peakY, 0, peakY + (baseY - peakY) * 0.35f,
            0xFFFFFFFF, 0xCCDDEEFF, Shader.TileMode.CLAMP));
        float snowBase = peakY + (baseY - peakY) * 0.35f;
        float snowHW = baseW * 0.5f * 0.35f;
        Path cap = new Path();
        cap.moveTo(peakX, peakY);
        cap.lineTo(peakX - snowHW, snowBase);
        cap.lineTo(peakX + snowHW, snowBase);
        cap.close();
        c.drawPath(cap, snow);

        // Ice crevasse lines on lit face
        Paint line = new Paint(Paint.ANTI_ALIAS_FLAG);
        line.setColor(0x44AABBCC);
        line.setStrokeWidth(1f);
        for (int i = 1; i <= 4; i++) {
            float t = i * 0.12f;
            float lx = peakX - baseW * 0.5f * t;
            float ly = peakY + (baseY - peakY) * t;
            c.drawLine(peakX, peakY + (baseY - peakY) * (t - 0.05f), lx, ly, line);
        }
    }

    private void drawIceFormations(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new LinearGradient(0, h * 0.70f, 0, h * 0.90f, 0xFF7799AA, 0xFF334455, Shader.TileMode.CLAMP));
        // Rocky/icy base formations
        Path rocks = new Path();
        rocks.moveTo(-ex, h + ex);
        rocks.lineTo(-ex, h * 0.82f);
        rocks.lineTo(w * 0.10f, h * 0.74f);
        rocks.lineTo(w * 0.18f, h * 0.80f);
        rocks.lineTo(w * 0.24f, h * 0.72f);
        rocks.lineTo(w * 0.35f, h * 0.78f);
        rocks.lineTo(w * 0.50f, h * 0.75f);
        rocks.lineTo(w * 0.65f, h * 0.78f);
        rocks.lineTo(w * 0.76f, h * 0.72f);
        rocks.lineTo(w * 0.82f, h * 0.80f);
        rocks.lineTo(w * 0.90f, h * 0.74f);
        rocks.lineTo(w + ex, h * 0.82f);
        rocks.lineTo(w + ex, h + ex);
        rocks.close();
        c.drawPath(rocks, p);
    }

    private void drawForegroundSnow(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new LinearGradient(0, h * 0.88f, 0, h + ex, 0xFFBBCCDD, 0xFF7799AA, Shader.TileMode.CLAMP));
        Path snow = new Path();
        snow.moveTo(-ex, h + ex);
        snow.lineTo(-ex, h * 0.90f);
        snow.quadTo(w * 0.15f, h * 0.85f, w * 0.30f, h * 0.90f);
        snow.quadTo(w * 0.50f, h * 0.96f, w * 0.70f, h * 0.88f);
        snow.quadTo(w * 0.85f, h * 0.84f, w + ex, h * 0.90f);
        snow.lineTo(w + ex, h + ex);
        snow.close();
        c.drawPath(snow, p);
    }
}
