package com.salasar.depthwallpaper.scenes;

import android.graphics.*;
import com.salasar.depthwallpaper.ClockStyle;
import com.salasar.depthwallpaper.WallpaperScene;
import java.util.Random;

public class NeonCity extends WallpaperScene {
    @Override public String getName() { return "Neon City"; }
    @Override public int getLayerCount() { return 5; }
    @Override public ClockStyle getClockStyle() { return ClockStyle.FROSTED; }
    @Override public int getClockInsertAfterLayer() { return 1; }
    @Override public int getAccentColor() { return 0xFFFF00CC; }

    private static final int[] NEON_COLORS = {
        0xFFFF0099, 0xFF00FFEE, 0xFFFFEE00, 0xFF00FF44, 0xFFAA00FF
    };

    @Override
    public void drawLayer(Canvas c, int layer, int w, int h) {
        switch (layer) {
            case 0: drawSky(c, w, h); break;
            case 1: drawMoonStars(c, w, h); break;
            case 2: drawFarSkyline(c, w, h); break;
            case 3: drawMidBuildings(c, w, h); break;
            case 4: drawNearBuildings(c, w, h); break;
        }
    }

    private void drawSky(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint();
        p.setShader(new LinearGradient(0, 0, 0, h,
            new int[]{0xFF020010, 0xFF0D0028, 0xFF18004A, 0xFF200052, 0xFF0A000A},
            new float[]{0f, 0.3f, 0.5f, 0.65f, 1f}, Shader.TileMode.CLAMP));
        c.drawRect(-ex, -ex, w+ex, h+ex, p);
        // Subtle smog glow near horizon
        Paint smog = new Paint();
        smog.setShader(new LinearGradient(0, h * 0.55f, 0, h * 0.70f, 0x33FF00AA, 0x00000000, Shader.TileMode.CLAMP));
        c.drawRect(-ex, h * 0.55f, w+ex, h * 0.70f, smog);
    }

    private void drawMoonStars(Canvas c, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        // Moon
        p.setColor(0xFFDDEEFF);
        p.setShader(new RadialGradient(w * 0.75f, h * 0.12f, h * 0.055f, 0xFFEEF6FF, 0xFFBBCCDD, Shader.TileMode.CLAMP));
        c.drawCircle(w * 0.75f, h * 0.12f, h * 0.048f, p);
        p.setShader(null);
        // Stars
        Random rng = new Random(99);
        p.setColor(0x88AABBDD);
        for (int i = 0; i < 120; i++) {
            float sx = rng.nextFloat() * (w + 2 * MAX_PARALLAX) - MAX_PARALLAX;
            float sy = rng.nextFloat() * h * 0.6f - MAX_PARALLAX;
            c.drawCircle(sx, sy, 0.5f + rng.nextFloat(), p);
        }
    }

    private void drawFarSkyline(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFF0D0022);
        Random rng = new Random(5);
        float baseline = h * 0.65f;
        for (int i = 0; i < 22; i++) {
            float bx = -ex + i * (w + 2*ex) / 21f;
            float bh = h * (0.05f + rng.nextFloat() * 0.20f);
            float bw = 18 + rng.nextFloat() * 35;
            c.drawRect(bx - bw/2, baseline - bh, bx + bw/2, baseline, p);
            // Tiny lit windows
            Paint win = new Paint();
            win.setColor(0x66FFDD88);
            for (int wy = (int)(baseline - bh + 4); wy < baseline - 4; wy += 7) {
                for (float wx = bx - bw/2 + 3; wx < bx + bw/2 - 3; wx += 6) {
                    if (rng.nextFloat() > 0.55f)
                        c.drawRect(wx, wy, wx + 3, wy + 4, win);
                }
            }
        }
        // Ground
        p.setShader(new LinearGradient(0, baseline, 0, h+ex, 0xFF0D0022, 0xFF060010, Shader.TileMode.CLAMP));
        c.drawRect(-ex, baseline, w+ex, h+ex, p);
    }

    private void drawMidBuildings(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        float baseline = h * 0.72f;
        Random rng = new Random(13);
        Paint bp = new Paint(Paint.ANTI_ALIAS_FLAG);
        bp.setColor(0xFF080018);
        for (int i = 0; i < 10; i++) {
            float bx = -ex + i * (w + 2*ex) / 9f;
            float bh = h * (0.12f + rng.nextFloat() * 0.28f);
            float bw = 35 + rng.nextFloat() * 50;
            RectF building = new RectF(bx - bw/2, baseline - bh, bx + bw/2, baseline);
            c.drawRect(building, bp);
            // Neon edge accent
            int neon = NEON_COLORS[rng.nextInt(NEON_COLORS.length)];
            Paint neonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            neonPaint.setColor(neon & 0x88FFFFFF);
            neonPaint.setStrokeWidth(2.5f);
            neonPaint.setStyle(Paint.Style.STROKE);
            c.drawRect(building, neonPaint);
            // Windows
            Paint win = new Paint();
            win.setColor(neon & 0x44FFFFFF);
            for (int wy = (int)(baseline - bh + 6); wy < baseline - 6; wy += 12) {
                for (float wx = bx - bw/2 + 5; wx < bx + bw/2 - 5; wx += 10) {
                    if (rng.nextFloat() > 0.4f)
                        c.drawRect(wx, wy, wx + 5, wy + 7, win);
                }
            }
        }
        // Ground reflection
        Paint ref = new Paint();
        ref.setShader(new LinearGradient(0, baseline, 0, h+ex, 0x44110022, 0xFF060010, Shader.TileMode.CLAMP));
        c.drawRect(-ex, baseline, w+ex, h+ex, ref);
    }

    private void drawNearBuildings(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        float baseline = h * 0.82f;
        // Two very close large buildings left and right
        Paint bp = new Paint(Paint.ANTI_ALIAS_FLAG);
        bp.setColor(0xFF040010);
        // Left building
        c.drawRect(-ex, h * 0.28f, w * 0.22f, baseline, bp);
        // Right building
        c.drawRect(w * 0.78f, h * 0.18f, w + ex, baseline, bp);
        // Neon signs
        drawNeonSign(c, -ex + 20, h * 0.4f, w * 0.14f, 28, 0xFFFF0099);
        drawNeonSign(c, w * 0.80f, h * 0.32f, w * 0.14f, 22, 0xFF00FFEE);
        // Foreground road
        Paint road = new Paint();
        road.setShader(new LinearGradient(0, baseline, 0, h+ex, 0xFF0A0018, 0xFF060010, Shader.TileMode.CLAMP));
        c.drawRect(-ex, baseline, w+ex, h+ex, road);
        // Neon road reflections
        Paint roadGlow = new Paint(Paint.ANTI_ALIAS_FLAG);
        roadGlow.setShader(new LinearGradient(w*0.3f, 0, w*0.7f, 0, 0x00FF0099, 0x22FF0099, Shader.TileMode.MIRROR));
        c.drawRect(w*0.2f, h*0.85f, w*0.8f, h*0.95f, roadGlow);
    }

    private void drawNeonSign(Canvas c, float x, float y, float width, float height, int color) {
        Paint sign = new Paint(Paint.ANTI_ALIAS_FLAG);
        sign.setColor(color);
        sign.setStrokeWidth(3f);
        sign.setStyle(Paint.Style.STROKE);
        sign.setMaskFilter(new BlurMaskFilter(6, BlurMaskFilter.Blur.NORMAL));
        c.drawRect(x + 5, y, x + width - 5, y + height, sign);
        sign.setMaskFilter(null);
        sign.setAlpha(200);
        c.drawRect(x + 5, y, x + width - 5, y + height, sign);
    }
}
