package com.salasar.depthwallpaper.scenes;

import android.graphics.*;
import com.salasar.depthwallpaper.WallpaperScene;
import java.util.Random;

public class TropicalParadise extends WallpaperScene {
    @Override public String getName() { return "Tropical Paradise"; }
    @Override public int getLayerCount() { return 5; }
    @Override public int getAccentColor() { return 0xFF00BCD4; }

    @Override
    public void drawLayer(Canvas c, int layer, int w, int h) {
        switch (layer) {
            case 0: drawSky(c, w, h); break;
            case 1: drawClouds(c, w, h); break;
            case 2: drawOcean(c, w, h); break;
            case 3: drawLagoon(c, w, h); break;
            case 4: drawBeach(c, w, h); break;
        }
    }

    private void drawSky(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint();
        p.setShader(new LinearGradient(0, 0, 0, h * 0.70f,
            new int[]{0xFF0077C8, 0xFF0099DD, 0xFF44BBEE, 0xFF88DDFF},
            new float[]{0f, 0.35f, 0.65f, 1f}, Shader.TileMode.CLAMP));
        c.drawRect(-ex, -ex, w+ex, h+ex, p);
    }

    private void drawClouds(Canvas c, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFFFFFFFF);
        p.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL));
        // A few fluffy cloud groups
        drawCloud(c, p, w * 0.12f, h * 0.12f, 70, 28);
        drawCloud(c, p, w * 0.45f, h * 0.08f, 110, 32);
        drawCloud(c, p, w * 0.80f, h * 0.14f, 80, 25);
        drawCloud(c, p, w * 0.65f, h * 0.20f, 60, 20);
        drawCloud(c, p, w * 0.25f, h * 0.22f, 50, 18);
    }

    private void drawCloud(Canvas c, Paint p, float cx, float cy, float width, float height) {
        // Cluster of overlapping ovals
        c.drawOval(cx - width/2, cy - height/2, cx + width/2, cy + height/2, p);
        c.drawOval(cx - width/3, cy - height * 0.85f, cx + width/3, cy + height * 0.35f, p);
        c.drawOval(cx + width/6, cy - height * 0.8f, cx + width * 0.8f, cy + height * 0.3f, p);
        c.drawOval(cx - width * 0.8f, cy - height * 0.6f, cx - width * 0.1f, cy + height * 0.5f, p);
    }

    private void drawOcean(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint();
        p.setShader(new LinearGradient(0, h * 0.48f, 0, h * 0.68f,
            0xFF0055AA, 0xFF0077CC, Shader.TileMode.CLAMP));
        c.drawRect(-ex, h * 0.48f, w+ex, h * 0.68f, p);
        // Horizon shimmer
        Paint shimmer = new Paint(Paint.ANTI_ALIAS_FLAG);
        shimmer.setColor(0x44FFFFFF);
        shimmer.setStrokeWidth(1.5f);
        shimmer.setStyle(Paint.Style.STROKE);
        Random rng = new Random(33);
        for (int i = 0; i < 12; i++) {
            float sy = h * 0.50f + i * 10;
            float slen = 15 + rng.nextFloat() * 60;
            float sx = -ex + rng.nextFloat() * (w + 2*ex);
            c.drawLine(sx, sy, sx + slen, sy, shimmer);
        }
    }

    private void drawLagoon(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new LinearGradient(0, h * 0.65f, 0, h * 0.85f,
            new int[]{0xFF00CCC8, 0xFF00BBAA, 0xFF009988},
            new float[]{0f, 0.5f, 1f}, Shader.TileMode.CLAMP));
        Path lagoon = new Path();
        lagoon.moveTo(-ex, h + ex);
        lagoon.lineTo(-ex, h * 0.70f);
        lagoon.cubicTo(w * 0.2f, h * 0.62f, w * 0.5f, h * 0.66f, w * 0.75f, h * 0.60f);
        lagoon.cubicTo(w * 0.88f, h * 0.56f, w * 0.94f, h * 0.64f, w + ex, h * 0.67f);
        lagoon.lineTo(w + ex, h + ex);
        lagoon.close();
        c.drawPath(lagoon, p);
        // Water clarity gradient
        Paint clarity = new Paint();
        clarity.setShader(new LinearGradient(0, h * 0.70f, 0, h * 0.80f, 0x4400FFEE, 0x00000000, Shader.TileMode.CLAMP));
        c.drawPath(lagoon, clarity);
        // Gentle wave highlights
        Paint wave = new Paint(Paint.ANTI_ALIAS_FLAG);
        wave.setColor(0x3300FFFF);
        wave.setStrokeWidth(2f);
        wave.setStyle(Paint.Style.STROKE);
        Random rng = new Random(44);
        for (int i = 0; i < 8; i++) {
            float wy = h * 0.72f + i * 18;
            float wx = -ex + rng.nextFloat() * w;
            float wlen = 30 + rng.nextFloat() * 80;
            c.drawArc(wx, wy - 5, wx + wlen, wy + 5, 180, 180, false, wave);
        }
    }

    private void drawBeach(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new LinearGradient(0, h * 0.83f, 0, h + ex,
            0xFFFFE8A0, 0xFFDDB870, Shader.TileMode.CLAMP));
        Path beach = new Path();
        beach.moveTo(-ex, h + ex);
        beach.lineTo(-ex, h * 0.86f);
        beach.cubicTo(w * 0.25f, h * 0.82f, w * 0.55f, h * 0.88f, w + ex, h * 0.80f);
        beach.lineTo(w + ex, h + ex);
        beach.close();
        c.drawPath(beach, p);
        // Wave lapping on shore
        Paint foam = new Paint(Paint.ANTI_ALIAS_FLAG);
        foam.setShader(new LinearGradient(0, h * 0.83f, 0, h * 0.88f, 0x88FFFFFF, 0x00FFFFFF, Shader.TileMode.CLAMP));
        c.drawRect(-ex, h * 0.82f, w+ex, h * 0.88f, foam);
        // Palm trees
        drawPalmTree(c, w * 0.08f, h * 0.85f, h * 0.30f, true);
        drawPalmTree(c, w * 0.12f, h * 0.88f, h * 0.25f, false);
        drawPalmTree(c, w * 0.88f, h * 0.82f, h * 0.28f, true);
    }

    private void drawPalmTree(Canvas c, float base_x, float base_y, float height, boolean lean_left) {
        Paint trunk = new Paint(Paint.ANTI_ALIAS_FLAG);
        trunk.setColor(0xFF5A3A1A);
        trunk.setStrokeWidth(9);
        trunk.setStyle(Paint.Style.STROKE);
        trunk.setStrokeCap(Paint.Cap.ROUND);
        float leanX = lean_left ? -height * 0.3f : height * 0.25f;
        Path trunkPath = new Path();
        trunkPath.moveTo(base_x, base_y);
        trunkPath.cubicTo(base_x + leanX * 0.3f, base_y - height * 0.4f,
            base_x + leanX * 0.7f, base_y - height * 0.7f,
            base_x + leanX, base_y - height);
        c.drawPath(trunkPath, trunk);
        // Fronds
        float tipX = base_x + leanX, tipY = base_y - height;
        Paint frond = new Paint(Paint.ANTI_ALIAS_FLAG);
        frond.setColor(0xFF2A6018);
        frond.setStrokeWidth(5);
        frond.setStyle(Paint.Style.STROKE);
        frond.setStrokeCap(Paint.Cap.ROUND);
        float[][] fronds = {
            {-1.0f, -0.5f}, {-0.8f, -0.9f}, {-0.3f, -1.1f},
            {0.3f, -1.1f}, {0.8f, -0.9f}, {1.0f, -0.5f},
            {-0.5f, 0.3f}, {0.5f, 0.3f}
        };
        for (float[] dir : fronds) {
            float flen = height * 0.28f;
            Path fp = new Path();
            fp.moveTo(tipX, tipY);
            fp.quadTo(tipX + dir[0] * flen * 0.5f, tipY + dir[1] * flen * 0.5f,
                tipX + dir[0] * flen, tipY + dir[1] * flen);
            c.drawPath(fp, frond);
        }
        // Coconut
        Paint nut = new Paint(Paint.ANTI_ALIAS_FLAG);
        nut.setColor(0xFF7B4F1A);
        c.drawCircle(tipX, tipY + 8, 8, nut);
    }
}
