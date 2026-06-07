package com.salasar.depthwallpaper.scenes;

import android.graphics.*;
import com.salasar.depthwallpaper.ClockStyle;
import com.salasar.depthwallpaper.WallpaperScene;
import java.util.Random;

public class SakuraNight extends WallpaperScene {
    @Override public String getName() { return "Sakura Night"; }
    @Override public int getLayerCount() { return 5; }
    @Override public ClockStyle getClockStyle() { return ClockStyle.ELEGANT; }
    @Override public int getClockInsertAfterLayer() { return 1; }

    @Override
    public void drawLayer(Canvas c, int layer, int w, int h) {
        switch (layer) {
            case 0: drawSky(c, w, h); break;
            case 1: drawMoonAndFarTrees(c, w, h); break;
            case 2: drawMidBranches(c, w, h); break;
            case 3: drawMainBranches(c, w, h); break;
            case 4: drawForegroundBranch(c, w, h); break;
        }
    }

    private void drawSky(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint();
        p.setShader(new LinearGradient(0, 0, 0, h,
            new int[]{0xFF050210, 0xFF0A0520, 0xFF120830, 0xFF160A38, 0xFF100828},
            new float[]{0f, 0.3f, 0.55f, 0.75f, 1f}, Shader.TileMode.CLAMP));
        c.drawRect(-ex, -ex, w + ex, h + ex, p);
        // Faint stars
        Paint star = new Paint(Paint.ANTI_ALIAS_FLAG);
        Random rng = new Random(512);
        for (int i = 0; i < 150; i++) {
            float sx = rng.nextFloat() * (w + 2 * ex) - ex;
            float sy = rng.nextFloat() * h * 0.70f;
            star.setColor(Color.argb(60 + rng.nextInt(120), 220, 200, 255));
            c.drawCircle(sx, sy, 0.5f + rng.nextFloat(), star);
        }
    }

    private void drawMoonAndFarTrees(Canvas c, int w, int h) {
        // Soft full moon
        Paint moon = new Paint(Paint.ANTI_ALIAS_FLAG);
        float mx = w * 0.68f, my = h * 0.16f, mr = h * 0.10f;
        moon.setShader(new RadialGradient(mx, my, mr * 1.4f,
            new int[]{0xDDFFFFFF, 0x88EEDDFF, 0x00000000},
            new float[]{0f, 0.5f, 1f}, Shader.TileMode.CLAMP));
        c.drawCircle(mx, my, mr * 1.4f, moon);
        moon.setShader(null);
        moon.setColor(0xFFEEDDFF);
        c.drawCircle(mx, my, mr * 0.75f, moon);

        // Far tree silhouettes
        Paint tree = new Paint(Paint.ANTI_ALIAS_FLAG);
        tree.setColor(0xFF0E0820);
        Random rng = new Random(44);
        int ex = MAX_PARALLAX;
        for (int i = 0; i < 14; i++) {
            float tx = -ex + i * (w + 2 * ex) / 13f;
            float th = h * (0.12f + rng.nextFloat() * 0.10f);
            float base = h * 0.75f;
            // Simple pine shape
            Path pine = new Path();
            pine.moveTo(tx - 10, base);
            pine.lineTo(tx, base - th);
            pine.lineTo(tx + 10, base);
            c.drawPath(pine, tree);
            // Pink puff tops
            Paint puff = new Paint(Paint.ANTI_ALIAS_FLAG);
            puff.setColor(Color.argb(60, 255, 180, 200));
            c.drawCircle(tx, base - th - 8, 14 + rng.nextFloat() * 8, puff);
        }
    }

    private void drawMidBranches(Canvas c, int w, int h) {
        Paint branch = new Paint(Paint.ANTI_ALIAS_FLAG);
        branch.setColor(0xFF1A0A18);
        branch.setStrokeWidth(7);
        branch.setStyle(Paint.Style.STROKE);
        branch.setStrokeCap(Paint.Cap.ROUND);
        Random rng = new Random(77);
        // Several mid-size branches from sides
        Path b1 = new Path();
        b1.moveTo(w * 0.05f, h * 0.55f);
        b1.cubicTo(w * 0.20f, h * 0.45f, w * 0.35f, h * 0.40f, w * 0.50f, h * 0.35f);
        c.drawPath(b1, branch);
        drawBlossoms(c, w * 0.50f, h * 0.35f, 35, rng);
        Path b2 = new Path();
        b2.moveTo(w * 0.95f, h * 0.50f);
        b2.cubicTo(w * 0.80f, h * 0.42f, w * 0.65f, h * 0.38f, w * 0.52f, h * 0.32f);
        c.drawPath(b2, branch);
        drawBlossoms(c, w * 0.52f, h * 0.32f, 30, rng);
    }

    private void drawMainBranches(Canvas c, int w, int h) {
        Paint branch = new Paint(Paint.ANTI_ALIAS_FLAG);
        branch.setColor(0xFF120610);
        branch.setStrokeWidth(11);
        branch.setStyle(Paint.Style.STROKE);
        branch.setStrokeCap(Paint.Cap.ROUND);
        Random rng = new Random(99);
        // Left main branch — crosses clock area at y~0.38
        Path left = new Path();
        left.moveTo(-MAX_PARALLAX, h * 0.65f);
        left.cubicTo(w * 0.12f, h * 0.55f, w * 0.25f, h * 0.42f, w * 0.42f, h * 0.38f);
        c.drawPath(left, branch);
        branch.setStrokeWidth(7);
        Path lsub = new Path();
        lsub.moveTo(w * 0.22f, h * 0.50f);
        lsub.cubicTo(w * 0.28f, h * 0.40f, w * 0.38f, h * 0.35f, w * 0.50f, h * 0.28f);
        c.drawPath(lsub, branch);
        drawBlossoms(c, w * 0.42f, h * 0.38f, 45, rng);
        drawBlossoms(c, w * 0.50f, h * 0.28f, 38, rng);
        // Right main branch
        branch.setStrokeWidth(11);
        Path right = new Path();
        right.moveTo(w + MAX_PARALLAX, h * 0.60f);
        right.cubicTo(w * 0.88f, h * 0.50f, w * 0.72f, h * 0.40f, w * 0.60f, h * 0.36f);
        c.drawPath(right, branch);
        drawBlossoms(c, w * 0.60f, h * 0.36f, 42, rng);
    }

    private void drawForegroundBranch(Canvas c, int w, int h) {
        // Thick foreground branch crosses top 25% of screen (clock area)
        Paint branch = new Paint(Paint.ANTI_ALIAS_FLAG);
        branch.setColor(0xFF0A0408);
        branch.setStrokeWidth(18);
        branch.setStyle(Paint.Style.STROKE);
        branch.setStrokeCap(Paint.Cap.ROUND);
        Random rng = new Random(321);
        Path fg = new Path();
        fg.moveTo(-MAX_PARALLAX, h * 0.30f);
        fg.cubicTo(w * 0.15f, h * 0.22f, w * 0.35f, h * 0.18f, w * 0.55f, h * 0.15f);
        fg.cubicTo(w * 0.70f, h * 0.12f, w * 0.85f, h * 0.16f, w + MAX_PARALLAX, h * 0.10f);
        c.drawPath(fg, branch);
        // Dense blossom clusters along this foreground branch
        for (float t = 0.1f; t <= 0.9f; t += 0.15f) {
            float bx = w * t;
            float by = h * (0.30f - t * 0.15f + 0.05f);
            drawBlossoms(c, bx, by, 50 + (int)(t * 20), rng);
        }
    }

    private void drawBlossoms(Canvas c, float cx, float cy, float radius, Random rng) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new RadialGradient(cx, cy, radius, 0xDDFFB8CC, 0x44FF80AA, Shader.TileMode.CLAMP));
        c.drawCircle(cx, cy, radius, p);
        p.setShader(null);
        for (int i = 0; i < 10; i++) {
            double ang = rng.nextDouble() * 2 * Math.PI;
            float dist = rng.nextFloat() * radius * 0.75f;
            float px = cx + (float)(Math.cos(ang) * dist);
            float py = cy + (float)(Math.sin(ang) * dist);
            float pr = 5 + rng.nextFloat() * 9;
            p.setColor(Color.argb(180 + rng.nextInt(75), 255, 155 + rng.nextInt(60), 175 + rng.nextInt(50)));
            c.save();
            c.rotate(rng.nextFloat() * 360, px, py);
            c.drawOval(px - pr, py - pr * 0.6f, px + pr, py + pr * 0.6f, p);
            c.restore();
        }
    }
}
