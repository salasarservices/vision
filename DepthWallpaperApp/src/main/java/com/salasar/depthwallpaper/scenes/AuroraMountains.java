package com.salasar.depthwallpaper.scenes;

import android.graphics.*;
import com.salasar.depthwallpaper.ClockStyle;
import com.salasar.depthwallpaper.WallpaperScene;
import java.util.Random;

public class AuroraMountains extends WallpaperScene {
    @Override public String getName() { return "Aurora Mountains"; }
    @Override public int getLayerCount() { return 5; }
    @Override public ClockStyle getClockStyle() { return ClockStyle.MINIMAL; }
    @Override public int getClockInsertAfterLayer() { return 1; }
    @Override public int getAccentColor() { return 0xFF00E5AA; }

    @Override
    public void drawLayer(Canvas c, int layer, int w, int h) {
        int ex = MAX_PARALLAX;
        switch (layer) {
            case 0: drawSky(c, w, h, ex); break;
            case 1: drawStars(c, w, h, ex); break;
            case 2: drawAurora(c, w, h, ex); break;
            case 3: drawFarMountains(c, w, h, ex); break;
            case 4: drawNearMountains(c, w, h, ex); break;
        }
    }

    private void drawSky(Canvas c, int w, int h, int ex) {
        Paint p = new Paint();
        p.setShader(new LinearGradient(0, 0, 0, h,
            new int[]{0xFF010520, 0xFF050B30, 0xFF0A1545, 0xFF081228},
            new float[]{0f, 0.4f, 0.75f, 1f}, Shader.TileMode.CLAMP));
        c.drawRect(-ex, -ex, w+ex, h+ex, p);
    }

    private void drawStars(Canvas c, int w, int h, int ex) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        Random rng = new Random(42);
        for (int i = 0; i < 200; i++) {
            float sx = -ex + rng.nextFloat() * (w + 2*ex);
            float sy = -ex + rng.nextFloat() * (h * 0.65f + ex);
            float radius = 0.5f + rng.nextFloat() * 1.5f;
            int alpha = 100 + rng.nextInt(155);
            p.setColor(Color.argb(alpha, 220, 230, 255));
            c.drawCircle(sx, sy, radius, p);
        }
    }

    private void drawAurora(Canvas c, int w, int h, int ex) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setMaskFilter(new BlurMaskFilter(40, BlurMaskFilter.Blur.NORMAL));
        // Three aurora ribbons
        drawAuroraRibbon(c, p, w, h, ex, 0xFF00FF90, 0.28f, 0.42f);
        drawAuroraRibbon(c, p, w, h, ex, 0xFF00D4CC, 0.18f, 0.35f);
        drawAuroraRibbon(c, p, w, h, ex, 0xFF44FF44, 0.35f, 0.52f);
    }

    private void drawAuroraRibbon(Canvas c, Paint paint, int w, int h, int ex, int color, float yStart, float yEnd) {
        paint.setShader(new LinearGradient(0, h * yStart, 0, h * yEnd,
            color & 0x00FFFFFF, color, Shader.TileMode.CLAMP));
        Path path = new Path();
        path.moveTo(-ex, h * yEnd);
        path.cubicTo(w * 0.2f, h * yStart, w * 0.6f, h * (yStart + 0.08f), w + ex, h * yEnd - h * 0.04f);
        path.lineTo(w + ex, h * yEnd);
        path.cubicTo(w * 0.6f, h * (yEnd + 0.02f), w * 0.2f, h * (yStart + 0.10f), -ex, h * yEnd);
        path.close();
        c.drawPath(path, paint);
    }

    private void drawFarMountains(Canvas c, int w, int h, int ex) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new LinearGradient(0, h * 0.48f, 0, h * 0.72f,
            0xFF8AB4D8, 0xFF4A6B88, Shader.TileMode.CLAMP));
        Path path = new Path();
        path.moveTo(-ex, h + ex);
        path.lineTo(-ex, h * 0.62f);
        path.quadTo(w * 0.05f, h * 0.50f, w * 0.12f, h * 0.58f);
        path.quadTo(w * 0.20f, h * 0.66f, w * 0.28f, h * 0.48f);
        path.quadTo(w * 0.36f, h * 0.38f, w * 0.44f, h * 0.54f);
        path.quadTo(w * 0.52f, h * 0.65f, w * 0.60f, h * 0.44f);
        path.quadTo(w * 0.70f, h * 0.30f, w * 0.78f, h * 0.50f);
        path.quadTo(w * 0.86f, h * 0.62f, w * 0.94f, h * 0.46f);
        path.quadTo(w + ex * 0.5f, h * 0.38f, w + ex, h * 0.55f);
        path.lineTo(w + ex, h + ex);
        path.close();
        c.drawPath(path, p);
        // Snow caps
        Paint snow = new Paint(Paint.ANTI_ALIAS_FLAG);
        snow.setColor(0xDDEEF4FF);
        drawSnowCap(c, snow, (int)(w * 0.28f), (int)(h * 0.48f), 30);
        drawSnowCap(c, snow, (int)(w * 0.60f), (int)(h * 0.44f), 35);
        drawSnowCap(c, snow, (int)(w * 0.78f), (int)(h * 0.30f), 50);
    }

    private void drawSnowCap(Canvas c, Paint p, int peakX, int peakY, int size) {
        Path cap = new Path();
        cap.moveTo(peakX, peakY);
        cap.lineTo(peakX - size, peakY + size);
        cap.lineTo(peakX + size, peakY + size);
        cap.close();
        c.drawPath(cap, p);
    }

    private void drawNearMountains(Canvas c, int w, int h, int ex) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new LinearGradient(0, h * 0.55f, 0, h + ex,
            0xFF1A2A4A, 0xFF0D1525, Shader.TileMode.CLAMP));
        Path path = new Path();
        path.moveTo(-ex, h + ex);
        path.lineTo(-ex, h * 0.75f);
        path.quadTo(w * 0.08f, h * 0.60f, w * 0.18f, h * 0.68f);
        path.quadTo(w * 0.26f, h * 0.72f, w * 0.34f, h * 0.55f);
        path.quadTo(w * 0.42f, h * 0.44f, w * 0.50f, h * 0.60f);
        path.quadTo(w * 0.58f, h * 0.72f, w * 0.66f, h * 0.52f);
        path.quadTo(w * 0.76f, h * 0.36f, w * 0.85f, h * 0.58f);
        path.quadTo(w * 0.92f, h * 0.70f, w + ex, h * 0.62f);
        path.lineTo(w + ex, h + ex);
        path.close();
        c.drawPath(path, p);
        // Near snow caps
        Paint snow = new Paint(Paint.ANTI_ALIAS_FLAG);
        snow.setColor(0xFFE8F0FF);
        drawSnowCap(c, snow, (int)(w * 0.34f), (int)(h * 0.55f), 22);
        drawSnowCap(c, snow, (int)(w * 0.66f), (int)(h * 0.52f), 28);
        drawSnowCap(c, snow, (int)(w * 0.76f), (int)(h * 0.36f), 45);
    }
}
