package com.salasar.depthwallpaper.scenes;

import android.graphics.*;
import com.salasar.depthwallpaper.ClockStyle;
import com.salasar.depthwallpaper.WallpaperScene;
import java.util.Random;

public class CyberRain extends WallpaperScene {
    @Override public String getName() { return "Cyber Rain"; }
    @Override public int getLayerCount() { return 5; }
    @Override public ClockStyle getClockStyle() { return ClockStyle.FROSTED; }
    @Override public int getClockInsertAfterLayer() { return 1; }

    private static final int[] NEON = {0xFFFF0088, 0xFF00FFCC, 0xFFFF8800, 0xFF8800FF};

    @Override
    public void drawLayer(Canvas c, int layer, int w, int h) {
        switch (layer) {
            case 0: drawSky(c, w, h); break;
            case 1: drawCityGlow(c, w, h); break;
            case 2: drawBuildings(c, w, h); break;
            case 3: drawRain(c, w, h); break;
            case 4: drawStreetLevel(c, w, h); break;
        }
    }

    private void drawSky(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint();
        p.setShader(new LinearGradient(0, 0, 0, h,
            new int[]{0xFF04000C, 0xFF080018, 0xFF100025, 0xFF0C001E},
            new float[]{0f, 0.3f, 0.6f, 1f}, Shader.TileMode.CLAMP));
        c.drawRect(-ex, -ex, w + ex, h + ex, p);
    }

    private void drawCityGlow(Canvas c, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setMaskFilter(new BlurMaskFilter(60, BlurMaskFilter.Blur.NORMAL));
        // Horizon glow halos from city light sources
        float[][] glows = {
            {w * 0.25f, h * 0.60f, 0x22FF0088},
            {w * 0.55f, h * 0.58f, 0x1800FFCC},
            {w * 0.80f, h * 0.62f, 0x22FF8800},
            {w * 0.10f, h * 0.64f, 0x188800FF},
        };
        for (float[] g : glows) {
            p.setColor((int) g[2]);
            c.drawCircle(g[0], g[1], h * 0.28f, p);
        }
    }

    private void drawBuildings(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Random rng = new Random(77);
        float baseline = h * 0.75f;
        Paint bp = new Paint(Paint.ANTI_ALIAS_FLAG);
        for (int i = 0; i < 14; i++) {
            float bx = -ex + i * (w + 2 * ex) / 13f + rng.nextFloat() * 20 - 10;
            float bh = h * (0.10f + rng.nextFloat() * 0.35f);
            float bw = 28 + rng.nextFloat() * 55;
            bp.setColor(0xFF060010);
            c.drawRect(bx - bw / 2, baseline - bh, bx + bw / 2, baseline, bp);
            // Windows
            Paint win = new Paint();
            int neonColor = NEON[rng.nextInt(NEON.length)];
            win.setColor(neonColor & 0x33FFFFFF);
            for (float wy = baseline - bh + 6; wy < baseline - 6; wy += 14) {
                for (float wx = bx - bw / 2 + 5; wx < bx + bw / 2 - 5; wx += 10) {
                    if (rng.nextFloat() > 0.45f) c.drawRect(wx, wy, wx + 6, wy + 8, win);
                }
            }
            // Neon sign on some buildings
            if (rng.nextFloat() > 0.55f) {
                Paint neon = new Paint(Paint.ANTI_ALIAS_FLAG);
                neon.setColor(neonColor & 0xBBFFFFFF);
                neon.setStrokeWidth(3f);
                neon.setStyle(Paint.Style.STROKE);
                neon.setMaskFilter(new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL));
                float signY = baseline - bh * 0.55f;
                c.drawRect(bx - bw * 0.35f, signY, bx + bw * 0.35f, signY + 18, neon);
            }
        }
        // Ground
        bp.setShader(new LinearGradient(0, baseline, 0, h + ex, 0xFF080015, 0xFF030008, Shader.TileMode.CLAMP));
        c.drawRect(-ex, baseline, w + ex, h + ex, bp);
    }

    private void drawRain(Canvas c, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0x3388AACC);
        p.setStrokeWidth(1.2f);
        p.setStyle(Paint.Style.STROKE);
        Random rng = new Random(111);
        int ex = MAX_PARALLAX;
        float angle = (float) Math.toRadians(10); // slight diagonal
        float dx = (float) Math.sin(angle) * 1;
        for (int i = 0; i < 250; i++) {
            float rx = rng.nextFloat() * (w + 2 * ex) - ex;
            float ry = rng.nextFloat() * h;
            float rlen = 20 + rng.nextFloat() * 50;
            p.setAlpha(30 + rng.nextInt(50));
            c.drawLine(rx, ry, rx + dx * rlen, ry + rlen, p);
        }
    }

    private void drawStreetLevel(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        // Dark close building silhouettes left+right
        p.setColor(0xFF020008);
        c.drawRect(-ex, h * 0.40f, w * 0.15f, h + ex, p);
        c.drawRect(w * 0.85f, h * 0.35f, w + ex, h + ex, p);
        // Neon reflections on wet street (wavy horizontal smears)
        Paint ref = new Paint(Paint.ANTI_ALIAS_FLAG);
        ref.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL));
        float[][] reflections = {
            {w * 0.30f, h * 0.86f, 0x44FF0088, 60},
            {w * 0.60f, h * 0.88f, 0x3300FFCC, 80},
            {w * 0.50f, h * 0.92f, 0x33FF8800, 50},
        };
        Random rng = new Random(222);
        for (float[] r : reflections) {
            ref.setColor((int) r[2]);
            for (int i = 0; i < 4; i++) {
                float ry = r[1] + i * 12;
                float rw = r[3] + rng.nextFloat() * 30;
                ref.setAlpha(((int) r[2] >>> 24) - i * 10);
                c.drawOval(r[0] - rw, ry - 5, r[0] + rw, ry + 5, ref);
            }
        }
    }
}
