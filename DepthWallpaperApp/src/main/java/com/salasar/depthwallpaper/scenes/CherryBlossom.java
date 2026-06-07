package com.salasar.depthwallpaper.scenes;

import android.graphics.*;
import com.salasar.depthwallpaper.ClockStyle;
import com.salasar.depthwallpaper.WallpaperScene;
import java.util.Random;

public class CherryBlossom extends WallpaperScene {
    @Override public String getName() { return "Cherry Blossom"; }
    @Override public int getLayerCount() { return 5; }
    @Override public ClockStyle getClockStyle() { return ClockStyle.ELEGANT; }
    @Override public int getClockInsertAfterLayer() { return 1; }
    @Override public int getAccentColor() { return 0xFFFF80AB; }

    @Override
    public void drawLayer(Canvas c, int layer, int w, int h) {
        switch (layer) {
            case 0: drawSky(c, w, h); break;
            case 1: drawFuji(c, w, h); break;
            case 2: drawFarTrees(c, w, h); break;
            case 3: drawPetals(c, w, h); break;
            case 4: drawNearBranches(c, w, h); break;
        }
    }

    private void drawSky(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint();
        p.setShader(new LinearGradient(0, 0, 0, h,
            new int[]{0xFFFFC2D4, 0xFFFFD6E0, 0xFFFFF0F4, 0xFFFFE8EE, 0xFFFFD0DC},
            new float[]{0f, 0.25f, 0.55f, 0.78f, 1f}, Shader.TileMode.CLAMP));
        c.drawRect(-ex, -ex, w+ex, h+ex, p);
        // Soft sun haze
        Paint sun = new Paint(Paint.ANTI_ALIAS_FLAG);
        sun.setShader(new RadialGradient(w * 0.7f, h * 0.15f, h * 0.3f, 0x44FFFFFF, 0x00FFFFFF, Shader.TileMode.CLAMP));
        c.drawCircle(w * 0.7f, h * 0.15f, h * 0.3f, sun);
    }

    private void drawFuji(Canvas c, int w, int h) {
        // Mount Fuji silhouette
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new LinearGradient(0, h * 0.25f, 0, h * 0.65f,
            0xFFCCD8E8, 0xFF9DB0C4, Shader.TileMode.CLAMP));
        Path fuji = new Path();
        fuji.moveTo(-MAX_PARALLAX, h * 0.65f);
        fuji.lineTo(w * 0.25f, h * 0.65f);
        fuji.lineTo(w * 0.40f, h * 0.55f);
        fuji.lineTo(w * 0.50f, h * 0.25f);  // peak
        fuji.lineTo(w * 0.60f, h * 0.55f);
        fuji.lineTo(w * 0.75f, h * 0.65f);
        fuji.lineTo(w + MAX_PARALLAX, h * 0.65f);
        c.drawPath(fuji, p);
        // Snow cap
        Paint snow = new Paint(Paint.ANTI_ALIAS_FLAG);
        snow.setShader(new LinearGradient(0, h * 0.25f, 0, h * 0.45f,
            0xFFFFFFFF, 0xAADDEEFF, Shader.TileMode.CLAMP));
        Path cap = new Path();
        cap.moveTo(w * 0.50f, h * 0.25f);
        cap.lineTo(w * 0.42f, h * 0.47f);
        cap.quadTo(w * 0.50f, h * 0.44f, w * 0.58f, h * 0.47f);
        cap.close();
        c.drawPath(cap, snow);
        // Ground plane
        Paint ground = new Paint(Paint.ANTI_ALIAS_FLAG);
        ground.setShader(new LinearGradient(0, h * 0.63f, 0, h * 0.75f, 0xFF88B870, 0xFF5A8840, Shader.TileMode.CLAMP));
        c.drawRect(-MAX_PARALLAX, h * 0.63f, w + MAX_PARALLAX, h * 0.75f, ground);
    }

    private void drawFarTrees(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        Random rng = new Random(22);
        float base = h * 0.72f;
        for (int i = 0; i < 12; i++) {
            float tx = -ex + i * (w + 2*ex) / 11f;
            float treeH = h * (0.10f + rng.nextFloat() * 0.12f);
            float treeW = h * 0.06f + rng.nextFloat() * h * 0.04f;
            int alpha = 140 + rng.nextInt(80);
            p.setShader(new RadialGradient(tx, base - treeH * 0.5f, treeW,
                Color.argb(alpha, 255, 160, 180), Color.argb(0, 255, 140, 170), Shader.TileMode.CLAMP));
            c.drawOval(tx - treeW, base - treeH, tx + treeW, base, p);
        }
        // Ground
        Paint ground = new Paint();
        ground.setShader(new LinearGradient(0, h * 0.70f, 0, h + ex, 0xFF70A055, 0xFF3A6025, Shader.TileMode.CLAMP));
        c.drawRect(-ex, h * 0.70f, w+ex, h+ex, ground);
    }

    private void drawPetals(Canvas c, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        Random rng = new Random(88);
        int ex = MAX_PARALLAX;
        for (int i = 0; i < 80; i++) {
            float px = -ex + rng.nextFloat() * (w + 2*ex);
            float py = rng.nextFloat() * h;
            float size = 4 + rng.nextFloat() * 8;
            float angle = rng.nextFloat() * 360;
            int alpha = 150 + rng.nextInt(105);
            p.setColor(Color.argb(alpha, 255, 180 + rng.nextInt(40), 200 + rng.nextInt(40)));
            c.save();
            c.rotate(angle, px, py);
            // Petal shape: slightly oval
            c.drawOval(px - size, py - size * 0.6f, px + size, py + size * 0.6f, p);
            c.restore();
        }
    }

    private void drawNearBranches(Canvas c, int w, int h) {
        Paint branch = new Paint(Paint.ANTI_ALIAS_FLAG);
        branch.setColor(0xFF5A3020);
        branch.setStrokeWidth(12);
        branch.setStyle(Paint.Style.STROKE);
        branch.setStrokeCap(Paint.Cap.ROUND);
        // Left main branch from top-left
        Path bl = new Path();
        bl.moveTo(-MAX_PARALLAX, 0);
        bl.cubicTo(w * 0.05f, h * 0.15f, w * 0.12f, h * 0.30f, w * 0.20f, h * 0.42f);
        c.drawPath(bl, branch);
        // Sub-branches left
        branch.setStrokeWidth(7);
        Path bl2 = new Path();
        bl2.moveTo(w * 0.10f, h * 0.22f);
        bl2.cubicTo(w * 0.18f, h * 0.15f, w * 0.28f, h * 0.10f, w * 0.38f, h * 0.08f);
        c.drawPath(bl2, branch);
        // Right branch from top-right
        branch.setStrokeWidth(14);
        Path br = new Path();
        br.moveTo(w + MAX_PARALLAX, 0);
        br.cubicTo(w * 0.92f, h * 0.12f, w * 0.80f, h * 0.28f, w * 0.72f, h * 0.40f);
        c.drawPath(br, branch);
        branch.setStrokeWidth(8);
        Path br2 = new Path();
        br2.moveTo(w * 0.85f, h * 0.18f);
        br2.cubicTo(w * 0.75f, h * 0.12f, w * 0.62f, h * 0.08f, w * 0.50f, h * 0.04f);
        c.drawPath(br2, branch);
        // Blossom clusters on branches
        drawBlossomCluster(c, w * 0.20f, h * 0.42f, 55);
        drawBlossomCluster(c, w * 0.38f, h * 0.08f, 45);
        drawBlossomCluster(c, w * 0.72f, h * 0.40f, 55);
        drawBlossomCluster(c, w * 0.50f, h * 0.04f, 45);
        drawBlossomCluster(c, w * 0.12f, h * 0.22f, 35);
        drawBlossomCluster(c, w * 0.85f, h * 0.18f, 35);
    }

    private void drawBlossomCluster(Canvas c, float cx, float cy, float radius) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new RadialGradient(cx, cy, radius, 0xEEFFB8C8, 0x88FF90AA, Shader.TileMode.CLAMP));
        c.drawCircle(cx, cy, radius, p);
        // Individual petals
        p.setShader(null);
        Random rng = new Random((long)(cx + cy));
        for (int i = 0; i < 12; i++) {
            float angle = rng.nextFloat() * 360;
            float dist = rng.nextFloat() * radius * 0.8f;
            double rad = Math.toRadians(angle);
            float px = cx + (float)(Math.cos(rad) * dist);
            float py = cy + (float)(Math.sin(rad) * dist);
            float size = 6 + rng.nextFloat() * 8;
            p.setColor(Color.argb(200, 255, 160 + rng.nextInt(60), 180 + rng.nextInt(50)));
            c.save();
            c.rotate(angle, px, py);
            c.drawOval(px - size, py - size * 0.6f, px + size, py + size * 0.6f, p);
            c.restore();
        }
    }
}
