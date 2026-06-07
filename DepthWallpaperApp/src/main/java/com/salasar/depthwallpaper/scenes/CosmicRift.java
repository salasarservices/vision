package com.salasar.depthwallpaper.scenes;

import android.graphics.*;
import com.salasar.depthwallpaper.ClockStyle;
import com.salasar.depthwallpaper.WallpaperScene;
import java.util.Random;

public class CosmicRift extends WallpaperScene {
    @Override public String getName() { return "Cosmic Rift"; }
    @Override public int getLayerCount() { return 5; }
    @Override public ClockStyle getClockStyle() { return ClockStyle.FUTURISTIC; }
    @Override public int getClockInsertAfterLayer() { return 1; }

    @Override
    public void drawLayer(Canvas c, int layer, int w, int h) {
        switch (layer) {
            case 0: drawDeepSpace(c, w, h); break;
            case 1: drawGalaxyArm(c, w, h); break;
            case 2: drawNebula(c, w, h); break;
            case 3: drawBrightStars(c, w, h); break;
            case 4: drawPlanet(c, w, h); break;
        }
    }

    private void drawDeepSpace(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint();
        p.setColor(0xFF000008);
        c.drawRect(-ex, -ex, w + ex, h + ex, p);
    }

    private void drawGalaxyArm(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        Random rng = new Random(2024);
        // Dense diagonal band of stars (top-left to bottom-right)
        for (int i = 0; i < 500; i++) {
            float t = rng.nextFloat();
            float sx = -ex + t * (w + 2 * ex);
            float sy = t * h + (float)(rng.nextGaussian() * h * 0.12f);
            float r = 0.3f + rng.nextFloat() * 1.5f;
            int alpha = 40 + rng.nextInt(180);
            int rb = 180 + rng.nextInt(75);
            int gb = 180 + rng.nextInt(75);
            p.setColor(Color.argb(alpha, rb, gb, 255));
            c.drawCircle(sx, (float) sy, r, p);
        }
    }

    private void drawNebula(Canvas c, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setMaskFilter(new BlurMaskFilter(90, BlurMaskFilter.Blur.NORMAL));
        // Magenta nebula cloud
        p.setShader(new RadialGradient(w * 0.38f, h * 0.40f, h * 0.45f,
            new int[]{0x55FF00AA, 0x33AA0066, 0x00000000},
            new float[]{0f, 0.5f, 1f}, Shader.TileMode.CLAMP));
        c.drawCircle(w * 0.38f, h * 0.40f, h * 0.45f, p);
        // Blue cloud
        p.setShader(new RadialGradient(w * 0.70f, h * 0.30f, h * 0.32f,
            new int[]{0x440044FF, 0x220022AA, 0x00000000},
            new float[]{0f, 0.5f, 1f}, Shader.TileMode.CLAMP));
        c.drawCircle(w * 0.70f, h * 0.30f, h * 0.32f, p);
        // Orange cloud lower
        p.setShader(new RadialGradient(w * 0.22f, h * 0.70f, h * 0.25f,
            new int[]{0x33FF6600, 0x00000000},
            new float[]{0f, 1f}, Shader.TileMode.CLAMP));
        c.drawCircle(w * 0.22f, h * 0.70f, h * 0.25f, p);
    }

    private void drawBrightStars(Canvas c, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        float[][] stars = {
            {w * 0.15f, h * 0.12f, 4.5f}, {w * 0.68f, h * 0.08f, 5f},
            {w * 0.05f, h * 0.35f, 3.5f}, {w * 0.88f, h * 0.22f, 4f},
            {w * 0.45f, h * 0.18f, 3f},   {w * 0.78f, h * 0.55f, 3.5f},
        };
        for (float[] s : stars) {
            p.setShader(new RadialGradient(s[0], s[1], s[2] * 5, 0x66FFFFFF, 0x00FFFFFF, Shader.TileMode.CLAMP));
            c.drawCircle(s[0], s[1], s[2] * 5, p);
            p.setShader(null);
            p.setColor(0xFFFFFFFF);
            c.drawCircle(s[0], s[1], s[2], p);
            // Cross flare
            p.setAlpha(80); p.setStrokeWidth(1f); p.setStyle(Paint.Style.STROKE);
            c.drawLine(s[0] - s[2] * 7, s[1], s[0] + s[2] * 7, s[1], p);
            c.drawLine(s[0], s[1] - s[2] * 7, s[0], s[1] + s[2] * 7, p);
            p.setStyle(Paint.Style.FILL); p.setAlpha(255);
        }
    }

    private void drawPlanet(Canvas c, int w, int h) {
        float px = w * 0.82f, py = h * 0.80f, pr = h * 0.22f;
        // Glow
        Paint glow = new Paint(Paint.ANTI_ALIAS_FLAG);
        glow.setShader(new RadialGradient(px, py, pr * 1.8f, 0x440066AA, 0x00000000, Shader.TileMode.CLAMP));
        c.drawCircle(px, py, pr * 1.8f, glow);
        // Planet body
        Paint planet = new Paint(Paint.ANTI_ALIAS_FLAG);
        planet.setShader(new RadialGradient(px - pr * 0.28f, py - pr * 0.28f, pr,
            new int[]{0xFF223344, 0xFF112233, 0xFF05101A},
            new float[]{0f, 0.55f, 1f}, Shader.TileMode.CLAMP));
        c.drawCircle(px, py, pr, planet);
        // Atmospheric rim
        Paint atmo = new Paint(Paint.ANTI_ALIAS_FLAG);
        atmo.setShader(new RadialGradient(px, py, pr,
            new int[]{0x00224466, 0x3300AAFF, 0x0000AAFF},
            new float[]{0.75f, 0.90f, 1f}, Shader.TileMode.CLAMP));
        c.drawCircle(px, py, pr, atmo);
        // Ring
        Paint ring = new Paint(Paint.ANTI_ALIAS_FLAG);
        ring.setColor(0x88AABBCC);
        ring.setStrokeWidth(10);
        ring.setStyle(Paint.Style.STROKE);
        c.drawOval(px - pr * 1.7f, py - pr * 0.3f, px + pr * 1.7f, py + pr * 0.3f, ring);
    }
}
