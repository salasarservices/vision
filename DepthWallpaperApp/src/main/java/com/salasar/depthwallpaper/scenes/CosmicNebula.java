package com.salasar.depthwallpaper.scenes;

import android.graphics.*;
import com.salasar.depthwallpaper.WallpaperScene;
import java.util.Random;

public class CosmicNebula extends WallpaperScene {
    @Override public String getName() { return "Cosmic Nebula"; }
    @Override public int getLayerCount() { return 5; }
    @Override public int getAccentColor() { return 0xFFAA44FF; }

    @Override
    public void drawLayer(Canvas c, int layer, int w, int h) {
        switch (layer) {
            case 0: drawSpaceBackground(c, w, h); break;
            case 1: drawDistantStars(c, w, h); break;
            case 2: drawNebulaClouds(c, w, h); break;
            case 3: drawBrightStars(c, w, h); break;
            case 4: drawPlanet(c, w, h); break;
        }
    }

    private void drawSpaceBackground(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint();
        p.setShader(new LinearGradient(0, 0, w, h,
            new int[]{0xFF000005, 0xFF02000F, 0xFF050018, 0xFF000008},
            new float[]{0f, 0.3f, 0.7f, 1f}, Shader.TileMode.CLAMP));
        c.drawRect(-ex, -ex, w+ex, h+ex, p);
    }

    private void drawDistantStars(Canvas c, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        Random rng = new Random(1234);
        int ex = MAX_PARALLAX;
        for (int i = 0; i < 400; i++) {
            float sx = -ex + rng.nextFloat() * (w + 2 * ex);
            float sy = -ex + rng.nextFloat() * (h + 2 * ex);
            float radius = 0.4f + rng.nextFloat() * 0.9f;
            int alpha = 60 + rng.nextInt(180);
            int r = 200 + rng.nextInt(55), g = 200 + rng.nextInt(55), b = 220 + rng.nextInt(35);
            p.setColor(Color.argb(alpha, r, g, b));
            c.drawCircle(sx, sy, radius, p);
        }
    }

    private void drawNebulaClouds(Canvas c, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setMaskFilter(new BlurMaskFilter(80, BlurMaskFilter.Blur.NORMAL));
        // Purple/pink nebula cloud
        p.setShader(new RadialGradient(w * 0.4f, h * 0.35f, h * 0.55f,
            new int[]{0x55AA00FF, 0x33880088, 0x11440066, 0x00000000},
            new float[]{0f, 0.4f, 0.7f, 1f}, Shader.TileMode.CLAMP));
        c.drawCircle(w * 0.4f, h * 0.35f, h * 0.55f, p);
        // Blue tint on the right
        p.setShader(new RadialGradient(w * 0.72f, h * 0.55f, h * 0.40f,
            new int[]{0x440066FF, 0x220044AA, 0x00000000},
            new float[]{0f, 0.5f, 1f}, Shader.TileMode.CLAMP));
        c.drawCircle(w * 0.72f, h * 0.55f, h * 0.40f, p);
        // Warm orange cluster
        p.setShader(new RadialGradient(w * 0.25f, h * 0.68f, h * 0.30f,
            new int[]{0x33FF6600, 0x11FF4400, 0x00000000},
            new float[]{0f, 0.5f, 1f}, Shader.TileMode.CLAMP));
        c.drawCircle(w * 0.25f, h * 0.68f, h * 0.30f, p);
    }

    private void drawBrightStars(Canvas c, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        Random rng = new Random(555);
        int ex = MAX_PARALLAX;
        // Bright foreground stars with cross flares
        float[][] brightStars = {
            {w * 0.15f, h * 0.20f, 4f},
            {w * 0.65f, h * 0.15f, 5f},
            {w * 0.82f, h * 0.38f, 3.5f},
            {w * 0.30f, h * 0.72f, 4.5f},
            {w * 0.55f, h * 0.80f, 3f},
            {w * 0.90f, h * 0.85f, 3f},
            {w * 0.10f, h * 0.55f, 3.5f},
        };
        for (float[] star : brightStars) {
            float sx = star[0], sy = star[1], sr = star[2];
            // Glow
            p.setShader(new RadialGradient(sx, sy, sr * 5, 0x88FFFFFF, 0x00FFFFFF, Shader.TileMode.CLAMP));
            c.drawCircle(sx, sy, sr * 5, p);
            // Core
            p.setShader(null);
            p.setColor(0xFFFFFFFF);
            c.drawCircle(sx, sy, sr, p);
            // Cross flare
            p.setAlpha(100);
            p.setStrokeWidth(1f);
            p.setStyle(Paint.Style.STROKE);
            c.drawLine(sx - sr * 6, sy, sx + sr * 6, sy, p);
            c.drawLine(sx, sy - sr * 6, sx, sy + sr * 6, p);
            p.setStyle(Paint.Style.FILL);
            p.setAlpha(255);
        }
    }

    private void drawPlanet(Canvas c, int w, int h) {
        float px = w * 0.78f, py = h * 0.72f, pr = h * 0.22f;
        // Planet glow
        Paint glow = new Paint(Paint.ANTI_ALIAS_FLAG);
        glow.setShader(new RadialGradient(px, py, pr * 1.6f, 0x44884499, 0x00000000, Shader.TileMode.CLAMP));
        c.drawCircle(px, py, pr * 1.6f, glow);
        // Planet body
        Paint planet = new Paint(Paint.ANTI_ALIAS_FLAG);
        planet.setShader(new RadialGradient(px - pr * 0.3f, py - pr * 0.3f, pr * 1.2f,
            new int[]{0xFF334455, 0xFF223344, 0xFF112233, 0xFF080D18},
            new float[]{0f, 0.4f, 0.7f, 1f}, Shader.TileMode.CLAMP));
        c.drawCircle(px, py, pr, planet);
        // Atmosphere rim
        Paint atmo = new Paint(Paint.ANTI_ALIAS_FLAG);
        atmo.setShader(new RadialGradient(px, py, pr,
            new int[]{0x00446688, 0x224488AA, 0x005599BB},
            new float[]{0.7f, 0.88f, 1f}, Shader.TileMode.CLAMP));
        c.drawCircle(px, py, pr, atmo);
        // Ring
        Paint ring = new Paint(Paint.ANTI_ALIAS_FLAG);
        ring.setColor(0x66AABBCC);
        ring.setStrokeWidth(8);
        ring.setStyle(Paint.Style.STROKE);
        c.drawOval(px - pr * 1.5f, py - pr * 0.22f, px + pr * 1.5f, py + pr * 0.22f, ring);
        // Shadow on ring (planet occlude part)
        Paint shadow = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadow.setColor(0xFF0D1525);
        c.drawArc(px - pr, py - pr, px + pr, py + pr, 175, 190, false, shadow);
    }
}
