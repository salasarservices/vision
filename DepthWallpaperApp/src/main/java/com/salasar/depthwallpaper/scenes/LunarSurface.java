package com.salasar.depthwallpaper.scenes;

import android.graphics.*;
import com.salasar.depthwallpaper.ClockStyle;
import com.salasar.depthwallpaper.WallpaperScene;
import java.util.Random;

/** Moon surface depth — top limb of moon appears in front of HYPEROS clock. */
public class LunarSurface extends WallpaperScene {
    @Override public String getName() { return "Lunar Surface"; }
    @Override public int getLayerCount() { return 5; }
    @Override public ClockStyle getClockStyle() { return ClockStyle.HYPEROS; }
    @Override public int getClockInsertAfterLayer() { return 1; }

    private static final float MOON_CX_F = 0.52f;
    private static final float MOON_CY_F = 0.58f;
    private static final float MOON_R_F  = 0.38f;

    @Override
    public void drawLayer(Canvas c, int layer, int w, int h) {
        switch (layer) {
            case 0: drawSpace(c, w, h); break;
            case 1: drawStars(c, w, h); break;
            case 2: drawMoonFull(c, w, h); break;
            case 3: drawMoonUpperLimb(c, w, h); break;  // in front of clock
            case 4: drawSpaceParticles(c, w, h); break;
        }
    }

    private void drawSpace(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint();
        p.setShader(new LinearGradient(0, 0, w, h, 0xFF000005, 0xFF01000C, Shader.TileMode.CLAMP));
        c.drawRect(-ex, -ex, w + ex, h + ex, p);
    }

    private void drawStars(Canvas c, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        Random rng = new Random(999);
        int ex = MAX_PARALLAX;
        for (int i = 0; i < 350; i++) {
            float sx = rng.nextFloat() * (w + 2 * ex) - ex;
            float sy = rng.nextFloat() * (h + 2 * ex) - ex;
            float r = 0.4f + rng.nextFloat() * 1.2f;
            p.setColor(Color.argb(80 + rng.nextInt(175), 210, 220, 255));
            c.drawCircle(sx, sy, r, p);
        }
    }

    private void drawMoonFull(Canvas c, int w, int h) {
        float mx = w * MOON_CX_F, my = h * MOON_CY_F, mr = h * MOON_R_F;

        // Moon body with off-center gradient (lit from upper-right)
        Paint moon = new Paint(Paint.ANTI_ALIAS_FLAG);
        moon.setShader(new RadialGradient(mx + mr * 0.25f, my - mr * 0.35f, mr * 1.1f,
            new int[]{0xFFDDDDBB, 0xFFBBBB99, 0xFF888866, 0xFF555544},
            new float[]{0f, 0.4f, 0.75f, 1f}, Shader.TileMode.CLAMP));
        c.drawCircle(mx, my, mr, moon);

        // Terminator shadow (dark left side)
        Paint terminator = new Paint(Paint.ANTI_ALIAS_FLAG);
        terminator.setShader(new RadialGradient(mx - mr * 0.4f, my, mr * 0.85f,
            0x88000000, 0x00000000, Shader.TileMode.CLAMP));
        c.drawCircle(mx, my, mr, terminator);

        // Craters
        Paint crater = new Paint(Paint.ANTI_ALIAS_FLAG);
        Random rng = new Random(42);
        float[][] craters = {
            {mx + mr * 0.25f, my - mr * 0.30f, mr * 0.09f},
            {mx - mr * 0.15f, my - mr * 0.45f, mr * 0.06f},
            {mx + mr * 0.50f, my + mr * 0.10f, mr * 0.11f},
            {mx - mr * 0.40f, my + mr * 0.25f, mr * 0.08f},
            {mx + mr * 0.10f, my + mr * 0.40f, mr * 0.07f},
            {mx + mr * 0.60f, my - mr * 0.50f, mr * 0.07f},
            {mx - mr * 0.55f, my - mr * 0.20f, mr * 0.05f},
            {mx + mr * 0.30f, my + mr * 0.60f, mr * 0.05f},
            {mx - mr * 0.20f, my + mr * 0.55f, mr * 0.06f},
            {mx + mr * 0.70f, my + mr * 0.40f, mr * 0.04f},
        };
        for (float[] cr : craters) {
            // Crater shadow
            crater.setColor(0x55000000);
            c.drawCircle(cr[0] + cr[2] * 0.15f, cr[1] + cr[2] * 0.15f, cr[2], crater);
            // Crater floor
            crater.setShader(new RadialGradient(cr[0], cr[1], cr[2],
                0x88222210, 0x00000000, Shader.TileMode.CLAMP));
            c.drawCircle(cr[0], cr[1], cr[2], crater);
            crater.setShader(null);
            // Crater rim highlight
            crater.setColor(0x33FFFFCC);
            crater.setStyle(Paint.Style.STROKE);
            crater.setStrokeWidth(cr[2] * 0.18f);
            c.drawCircle(cr[0] - cr[2] * 0.1f, cr[1] - cr[2] * 0.1f, cr[2], crater);
            crater.setStyle(Paint.Style.FILL);
        }
    }

    private void drawMoonUpperLimb(Canvas c, int w, int h) {
        // Redraw the upper portion of the moon (y < h*0.40) to appear in front of clock.
        // HYPEROS clock: date at 0.22, time at 0.32 — moon limb crosses both.
        float mx = w * MOON_CX_F, my = h * MOON_CY_F, mr = h * MOON_R_F;
        float clipBottom = h * 0.40f;

        c.save();
        c.clipRect(-MAX_PARALLAX, -MAX_PARALLAX, w + MAX_PARALLAX, clipBottom);
        drawMoonFull(c, w, h);
        c.restore();

        // Atmospheric glow around moon limb
        Paint glow = new Paint(Paint.ANTI_ALIAS_FLAG);
        glow.setShader(new RadialGradient(mx, my, mr * 1.15f,
            new int[]{0x00000000, 0x22FFFFAA, 0x00000000},
            new float[]{0.85f, 0.96f, 1f}, Shader.TileMode.CLAMP));
        c.drawCircle(mx, my, mr * 1.15f, glow);
    }

    private void drawSpaceParticles(Canvas c, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        Random rng = new Random(707);
        // A few bright foreground stars with tiny flares
        float[][] stars = {
            {w * 0.12f, h * 0.10f, 3f},
            {w * 0.88f, h * 0.08f, 2.5f},
            {w * 0.05f, h * 0.55f, 2f},
            {w * 0.95f, h * 0.40f, 2.5f},
            {w * 0.20f, h * 0.88f, 2f},
        };
        for (float[] s : stars) {
            p.setShader(new RadialGradient(s[0], s[1], s[2] * 4,
                0x66FFFFFF, 0x00FFFFFF, Shader.TileMode.CLAMP));
            c.drawCircle(s[0], s[1], s[2] * 4, p);
            p.setShader(null);
            p.setColor(0xFFFFFFFF);
            c.drawCircle(s[0], s[1], s[2], p);
        }
    }
}
