package com.salasar.depthwallpaper.scenes;

import android.graphics.*;
import com.salasar.depthwallpaper.ClockStyle;
import com.salasar.depthwallpaper.WallpaperScene;
import java.util.Random;

public class ArcticNight extends WallpaperScene {
    @Override public String getName() { return "Arctic Night"; }
    @Override public int getLayerCount() { return 5; }
    @Override public ClockStyle getClockStyle() { return ClockStyle.HYPEROS; }
    @Override public int getClockInsertAfterLayer() { return 1; }
    @Override public int getAccentColor() { return 0xFF00E5D4; }

    @Override
    public void drawLayer(Canvas c, int layer, int w, int h) {
        switch (layer) {
            case 0: drawSky(c, w, h); break;
            case 1: drawAurora(c, w, h); break;
            case 2: drawStars(c, w, h); break;
            case 3: drawIceMountains(c, w, h); break;
            case 4: drawIceForeground(c, w, h); break;
        }
    }

    private void drawSky(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint();
        p.setShader(new LinearGradient(0, 0, 0, h,
            new int[]{0xFF000810, 0xFF001A20, 0xFF002A30, 0xFF001818},
            new float[]{0f, 0.4f, 0.70f, 1f}, Shader.TileMode.CLAMP));
        c.drawRect(-ex, -ex, w+ex, h+ex, p);
    }

    private void drawAurora(Canvas c, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setMaskFilter(new BlurMaskFilter(35, BlurMaskFilter.Blur.NORMAL));
        // Primary aurora band
        p.setShader(new LinearGradient(0, h * 0.20f, 0, h * 0.48f,
            0x00000000, 0x8800FFDD, Shader.TileMode.CLAMP));
        Path a1 = new Path();
        a1.moveTo(-MAX_PARALLAX, h * 0.22f);
        a1.cubicTo(w * 0.2f, h * 0.14f, w * 0.5f, h * 0.30f, w * 0.8f, h * 0.18f);
        a1.cubicTo(w * 0.9f, h * 0.14f, w + MAX_PARALLAX * 0.5f, h * 0.16f, w + MAX_PARALLAX, h * 0.20f);
        a1.lineTo(w + MAX_PARALLAX, h * 0.42f);
        a1.cubicTo(w * 0.85f, h * 0.36f, w * 0.55f, h * 0.48f, w * 0.3f, h * 0.38f);
        a1.cubicTo(w * 0.15f, h * 0.32f, -MAX_PARALLAX * 0.5f, h * 0.38f, -MAX_PARALLAX, h * 0.36f);
        a1.close();
        c.drawPath(a1, p);
        // Secondary aurora
        p.setShader(new LinearGradient(0, h * 0.10f, 0, h * 0.32f, 0x00000000, 0x4400CCAA, Shader.TileMode.CLAMP));
        Path a2 = new Path();
        a2.moveTo(-MAX_PARALLAX, h * 0.12f);
        a2.cubicTo(w * 0.3f, h * 0.05f, w * 0.6f, h * 0.22f, w + MAX_PARALLAX, h * 0.10f);
        a2.lineTo(w + MAX_PARALLAX, h * 0.28f);
        a2.cubicTo(w * 0.65f, h * 0.36f, w * 0.35f, h * 0.22f, -MAX_PARALLAX, h * 0.28f);
        a2.close();
        c.drawPath(a2, p);
    }

    private void drawStars(Canvas c, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        Random rng = new Random(77);
        int ex = MAX_PARALLAX;
        for (int i = 0; i < 150; i++) {
            float sx = -ex + rng.nextFloat() * (w + 2*ex);
            float sy = rng.nextFloat() * h * 0.65f - ex;
            float r = 0.5f + rng.nextFloat() * 1.2f;
            int alpha = 80 + rng.nextInt(170);
            p.setColor(Color.argb(alpha, 200, 230, 255));
            c.drawCircle(sx, sy, r, p);
        }
    }

    private void drawIceMountains(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new LinearGradient(0, h * 0.45f, 0, h * 0.72f,
            0xFFBBDDEE, 0xFF6699AA, Shader.TileMode.CLAMP));
        Path mts = new Path();
        mts.moveTo(-ex, h + ex);
        mts.lineTo(-ex, h * 0.68f);
        mts.quadTo(w * 0.04f, h * 0.60f, w * 0.10f, h * 0.50f);
        mts.quadTo(w * 0.16f, h * 0.42f, w * 0.22f, h * 0.56f);
        mts.quadTo(w * 0.30f, h * 0.66f, w * 0.38f, h * 0.48f);
        mts.quadTo(w * 0.46f, h * 0.36f, w * 0.54f, h * 0.50f);
        mts.quadTo(w * 0.62f, h * 0.62f, w * 0.68f, h * 0.46f);
        mts.quadTo(w * 0.76f, h * 0.34f, w * 0.82f, h * 0.50f);
        mts.quadTo(w * 0.90f, h * 0.62f, w + ex, h * 0.52f);
        mts.lineTo(w + ex, h + ex);
        mts.close();
        c.drawPath(mts, p);
        // Blue tint on shadowed faces
        Paint shadow = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadow.setColor(0x33004466);
        c.drawPath(mts, shadow);
        // Flat ice plain
        Paint ice = new Paint(Paint.ANTI_ALIAS_FLAG);
        ice.setShader(new LinearGradient(0, h * 0.70f, 0, h + ex, 0xFF99BBCC, 0xFF335566, Shader.TileMode.CLAMP));
        c.drawRect(-ex, h * 0.70f, w + ex, h + ex, ice);
        // Ice reflection of aurora
        Paint ref = new Paint();
        ref.setShader(new LinearGradient(0, h * 0.72f, 0, h * 0.85f, 0x2200FFDD, 0x00000000, Shader.TileMode.CLAMP));
        c.drawRect(-ex, h * 0.72f, w + ex, h * 0.85f, ref);
    }

    private void drawIceForeground(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        // Large ice shards in foreground
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new LinearGradient(0, h * 0.82f, 0, h + ex, 0xFF88AACC, 0xFF223344, Shader.TileMode.CLAMP));
        // Left ice formation
        Path iceL = new Path();
        iceL.moveTo(-ex, h + ex);
        iceL.lineTo(-ex, h * 0.88f);
        iceL.lineTo(w * 0.08f, h * 0.80f);
        iceL.lineTo(w * 0.14f, h * 0.88f);
        iceL.lineTo(w * 0.20f, h * 0.75f);
        iceL.lineTo(w * 0.28f, h * 0.90f);
        iceL.lineTo(w * 0.35f, h + ex);
        iceL.close();
        c.drawPath(iceL, p);
        // Right ice formation
        Paint pR = new Paint(Paint.ANTI_ALIAS_FLAG);
        pR.setShader(new LinearGradient(0, h * 0.78f, 0, h + ex, 0xFF99BBCC, 0xFF334455, Shader.TileMode.CLAMP));
        Path iceR = new Path();
        iceR.moveTo(w * 0.65f, h + ex);
        iceR.lineTo(w * 0.70f, h * 0.84f);
        iceR.lineTo(w * 0.78f, h * 0.72f);
        iceR.lineTo(w * 0.84f, h * 0.82f);
        iceR.lineTo(w * 0.90f, h * 0.78f);
        iceR.lineTo(w + ex, h * 0.88f);
        iceR.lineTo(w + ex, h + ex);
        iceR.close();
        c.drawPath(iceR, pR);
        // Edge highlights on ice
        Paint edge = new Paint(Paint.ANTI_ALIAS_FLAG);
        edge.setColor(0xAADDEEFF);
        edge.setStrokeWidth(2f);
        edge.setStyle(Paint.Style.STROKE);
        c.drawPath(iceL, edge);
        c.drawPath(iceR, edge);
    }
}
