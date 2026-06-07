package com.salasar.depthwallpaper.scenes;

import android.graphics.*;
import com.salasar.depthwallpaper.ClockStyle;
import com.salasar.depthwallpaper.WallpaperScene;
import java.util.Random;

/** Bare trees against dusky sky — ARTISTIC huge numbers fill the sky, trunks cut through them. */
public class NightForest extends WallpaperScene {
    @Override public String getName() { return "Night Forest"; }
    @Override public int getLayerCount() { return 5; }
    @Override public ClockStyle getClockStyle() { return ClockStyle.ARTISTIC; }
    @Override public int getClockInsertAfterLayer() { return 0; } // clock very early, all trees in front

    @Override
    public void drawLayer(Canvas c, int layer, int w, int h) {
        switch (layer) {
            case 0: drawSky(c, w, h); break;
            case 1: drawHillAndMoon(c, w, h); break;
            case 2: drawFarTrees(c, w, h); break;
            case 3: drawMidTrees(c, w, h); break;
            case 4: drawNearTrees(c, w, h); break;
        }
    }

    private void drawSky(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint();
        p.setShader(new LinearGradient(0, 0, 0, h,
            new int[]{0xFF0D1508, 0xFF1A2810, 0xFF283818, 0xFF2A3A15, 0xFF1A280E},
            new float[]{0f, 0.3f, 0.55f, 0.75f, 1f}, Shader.TileMode.CLAMP));
        c.drawRect(-ex, -ex, w + ex, h + ex, p);
    }

    private void drawHillAndMoon(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        // Gentle rolling hill
        Paint hill = new Paint(Paint.ANTI_ALIAS_FLAG);
        hill.setShader(new LinearGradient(0, h * 0.60f, 0, h + ex, 0xFF0D1808, 0xFF080F04, Shader.TileMode.CLAMP));
        Path hillPath = new Path();
        hillPath.moveTo(-ex, h + ex);
        hillPath.lineTo(-ex, h * 0.72f);
        hillPath.cubicTo(w * 0.2f, h * 0.62f, w * 0.5f, h * 0.66f, w * 0.8f, h * 0.60f);
        hillPath.cubicTo(w * 0.9f, h * 0.57f, w + ex * 0.5f, h * 0.64f, w + ex, h * 0.68f);
        hillPath.lineTo(w + ex, h + ex);
        hillPath.close();
        c.drawPath(hillPath, hill);

        // Crescent moon upper-right
        Paint moon = new Paint(Paint.ANTI_ALIAS_FLAG);
        float mx = w * 0.82f, my = h * 0.08f, mr = h * 0.055f;
        moon.setColor(0xFFEEEECC);
        c.drawCircle(mx, my, mr, moon);
        moon.setColor(0xFF1A2810); // cut out to form crescent
        c.drawCircle(mx + mr * 0.40f, my - mr * 0.10f, mr * 0.80f, moon);
    }

    private void drawFarTrees(Canvas c, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFF0C1608);
        p.setStrokeWidth(5);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeCap(Paint.Cap.ROUND);
        Random rng = new Random(11);
        int ex = MAX_PARALLAX;
        for (int i = 0; i < 20; i++) {
            float tx = -ex + i * (w + 2 * ex) / 19f + rng.nextFloat() * 15 - 7;
            float th = h * (0.20f + rng.nextFloat() * 0.15f);
            float baseY = h * (0.65f + rng.nextFloat() * 0.08f);
            drawBareTree(c, p, tx, baseY, th, 2.5f + rng.nextFloat() * 2f, rng, 3);
        }
    }

    private void drawMidTrees(Canvas c, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFF081004);
        p.setStrokeWidth(8);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeCap(Paint.Cap.ROUND);
        Random rng = new Random(22);
        int ex = MAX_PARALLAX;
        for (int i = 0; i < 12; i++) {
            float tx = -ex + i * (w + 2 * ex) / 11f + rng.nextFloat() * 20 - 10;
            float th = h * (0.30f + rng.nextFloat() * 0.20f);
            float baseY = h * (0.68f + rng.nextFloat() * 0.06f);
            drawBareTree(c, p, tx, baseY, th, 4f + rng.nextFloat() * 3f, rng, 4);
        }
    }

    private void drawNearTrees(Canvas c, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFF040A02);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeCap(Paint.Cap.ROUND);
        Random rng = new Random(33);
        // 4 large trees that definitely cross the big ARTISTIC number display
        float[][] trees = {
            {w * 0.08f, h * 0.95f, h * 0.80f, 16f},  // reaches y=0.15
            {w * 0.28f, h * 0.90f, h * 0.70f, 12f},  // reaches y=0.20
            {w * 0.65f, h * 0.92f, h * 0.75f, 14f},  // reaches y=0.17
            {w * 0.88f, h * 0.95f, h * 0.85f, 18f},  // reaches y=0.10
        };
        for (float[] t : trees) {
            p.setStrokeWidth(t[3]);
            drawBareTree(c, p, t[0], t[1], t[2], t[3] * 0.45f, rng, 5);
        }
        // Ground fill
        Paint ground = new Paint();
        ground.setShader(new LinearGradient(0, h * 0.90f, 0, h + MAX_PARALLAX,
            0xFF050A03, 0xFF030602, Shader.TileMode.CLAMP));
        c.drawRect(-MAX_PARALLAX, h * 0.90f, w + MAX_PARALLAX, h + MAX_PARALLAX, ground);
    }

    private void drawBareTree(Canvas c, Paint p, float x, float baseY, float height,
                              float branchWidth, Random rng, int depth) {
        if (depth <= 0 || height < 8) return;
        float topY = baseY - height;
        c.drawLine(x, baseY, x, topY, p);
        // Branches
        int branches = 2 + rng.nextInt(2);
        float origWidth = p.getStrokeWidth();
        p.setStrokeWidth(Math.max(1f, origWidth * 0.6f));
        for (int i = 0; i < branches; i++) {
            float t = 0.4f + rng.nextFloat() * 0.5f;
            float branchY = baseY - height * t;
            int side = (i % 2 == 0) ? -1 : 1;
            float angle = (float) Math.toRadians(25 + rng.nextInt(35));
            float blen = height * (0.3f + rng.nextFloat() * 0.25f);
            float endX = x + side * (float) Math.sin(angle) * blen;
            float endY = branchY - (float) Math.cos(angle) * blen;
            c.drawLine(x, branchY, endX, endY, p);
            drawBareTree(c, p, endX, endY, blen * 0.6f, branchWidth * 0.6f, rng, depth - 1);
        }
        p.setStrokeWidth(origWidth);
    }
}
