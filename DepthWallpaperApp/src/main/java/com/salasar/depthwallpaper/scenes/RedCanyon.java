package com.salasar.depthwallpaper.scenes;

import android.graphics.*;
import com.salasar.depthwallpaper.ClockStyle;
import com.salasar.depthwallpaper.WallpaperScene;

public class RedCanyon extends WallpaperScene {
    @Override public String getName() { return "Red Canyon"; }
    @Override public int getLayerCount() { return 5; }
    @Override public ClockStyle getClockStyle() { return ClockStyle.BRUTAL; }
    @Override public int getClockInsertAfterLayer() { return 1; }

    @Override
    public void drawLayer(Canvas c, int layer, int w, int h) {
        switch (layer) {
            case 0: drawSky(c, w, h); break;
            case 1: drawFarWalls(c, w, h); break;
            case 2: drawMidWalls(c, w, h); break;
            case 3: drawCloseWalls(c, w, h); break;
            case 4: drawForeground(c, w, h); break;
        }
    }

    private void drawSky(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint();
        p.setShader(new LinearGradient(0, 0, 0, h * 0.22f, 0xFF1A3A6A, 0xFF2A5A9A, Shader.TileMode.CLAMP));
        c.drawRect(-ex, -ex, w + ex, h * 0.22f, p);
        // Warm ground fill
        p.setShader(new LinearGradient(0, h * 0.22f, 0, h + ex, 0xFF8B3A00, 0xFF5A2000, Shader.TileMode.CLAMP));
        c.drawRect(-ex, h * 0.22f, w + ex, h + ex, p);
    }

    private void drawFarWalls(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        // Horizontal strata bands — distant canyon walls
        int[][] strata = {
            {0xFFCC7722, (int)(h * 0.22f), (int)(h * 0.32f)},
            {0xFF883300, (int)(h * 0.30f), (int)(h * 0.40f)},
            {0xFFAA5518, (int)(h * 0.37f), (int)(h * 0.47f)},
            {0xFFCC6610, (int)(h * 0.44f), (int)(h * 0.52f)},
        };
        for (int[] s : strata) {
            p.setColor(s[0]);
            Path band = new Path();
            band.moveTo(-ex, s[1]);
            band.cubicTo(w * 0.3f, s[1] + 8, w * 0.6f, s[1] - 5, w + ex, s[1] + 3);
            band.lineTo(w + ex, s[2]);
            band.cubicTo(w * 0.6f, s[2] + 5, w * 0.3f, s[2] - 8, -ex, s[2]);
            band.close();
            c.drawPath(band, p);
        }
    }

    private void drawMidWalls(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint leftP = new Paint(Paint.ANTI_ALIAS_FLAG);
        leftP.setShader(new LinearGradient(0, h * 0.20f, w * 0.35f, h * 0.20f,
            0xFF8B2200, 0xFFCC4400, Shader.TileMode.CLAMP));
        // Left canyon wall
        Path left = new Path();
        left.moveTo(-ex, -ex);
        left.lineTo(-ex, h + ex);
        left.lineTo(w * 0.30f, h + ex);
        left.lineTo(w * 0.25f, h * 0.65f);
        left.lineTo(w * 0.32f, h * 0.50f);
        left.lineTo(w * 0.22f, h * 0.35f);
        left.lineTo(w * 0.30f, h * 0.22f);
        left.lineTo(w * 0.18f, h * 0.10f);
        left.lineTo(-ex, h * 0.08f);
        left.close();
        c.drawPath(left, leftP);
        // Right canyon wall
        Paint rightP = new Paint(Paint.ANTI_ALIAS_FLAG);
        rightP.setShader(new LinearGradient(w * 0.65f, h * 0.20f, w + ex, h * 0.20f,
            0xFFCC4400, 0xFF8B2200, Shader.TileMode.CLAMP));
        Path right = new Path();
        right.moveTo(w + ex, -ex);
        right.lineTo(w + ex, h + ex);
        right.lineTo(w * 0.70f, h + ex);
        right.lineTo(w * 0.75f, h * 0.65f);
        right.lineTo(w * 0.68f, h * 0.50f);
        right.lineTo(w * 0.78f, h * 0.35f);
        right.lineTo(w * 0.70f, h * 0.22f);
        right.lineTo(w * 0.82f, h * 0.10f);
        right.lineTo(w + ex, h * 0.08f);
        right.close();
        c.drawPath(right, rightP);
    }

    private void drawCloseWalls(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        // Close overhanging ledges that enter clock area (BRUTAL: date at 0.17)
        p.setShader(new LinearGradient(0, 0, 0, h * 0.35f, 0xFF5A1800, 0xFF8B2E00, Shader.TileMode.CLAMP));
        // Left overhang at top — comes in to y~0.18 (clock date line)
        Path overL = new Path();
        overL.moveTo(-ex, -ex);
        overL.lineTo(-ex, h * 0.38f);
        overL.lineTo(w * 0.15f, h * 0.30f);
        overL.lineTo(w * 0.28f, h * 0.18f); // overhang tip into clock
        overL.lineTo(w * 0.20f, -ex);
        overL.close();
        c.drawPath(overL, p);
        // Right overhang
        Path overR = new Path();
        overR.moveTo(w + ex, -ex);
        overR.lineTo(w + ex, h * 0.38f);
        overR.lineTo(w * 0.85f, h * 0.30f);
        overR.lineTo(w * 0.72f, h * 0.18f); // tip into clock
        overR.lineTo(w * 0.80f, -ex);
        overR.close();
        c.drawPath(overR, p);
        // Shadow under overhangs
        Paint shadow = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadow.setShader(new LinearGradient(0, h * 0.18f, 0, h * 0.35f, 0x66000000, 0x00000000, Shader.TileMode.CLAMP));
        c.drawPath(overL, shadow);
        c.drawPath(overR, shadow);
    }

    private void drawForeground(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new LinearGradient(0, h * 0.85f, 0, h + ex, 0xFFDDA050, 0xFFAA7030, Shader.TileMode.CLAMP));
        // Sandy canyon floor
        c.drawRect(-ex, h * 0.85f, w + ex, h + ex, p);
        // Very close dark wall sections on sides
        p.setShader(new LinearGradient(0, 0, w * 0.15f, 0, 0xFF2A0800, 0x00000000, Shader.TileMode.CLAMP));
        c.drawRect(-ex, -ex, w * 0.15f, h + ex, p);
        p.setShader(new LinearGradient(w * 0.85f, 0, w + ex, 0, 0x00000000, 0xFF2A0800, Shader.TileMode.CLAMP));
        c.drawRect(w * 0.85f, -ex, w + ex, h + ex, p);
    }
}
