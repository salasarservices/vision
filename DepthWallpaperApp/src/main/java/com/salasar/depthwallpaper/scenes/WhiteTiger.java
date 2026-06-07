package com.salasar.depthwallpaper.scenes;

import android.graphics.*;
import com.salasar.depthwallpaper.ClockStyle;
import com.salasar.depthwallpaper.WallpaperScene;
import java.util.Random;

/** White tiger face — forehead & ears break through BRUTAL clock digits. */
public class WhiteTiger extends WallpaperScene {
    @Override public String getName() { return "White Tiger"; }
    @Override public int getLayerCount() { return 5; }
    @Override public ClockStyle getClockStyle() { return ClockStyle.BRUTAL; }
    @Override public int getClockInsertAfterLayer() { return 1; }

    @Override
    public void drawLayer(Canvas c, int layer, int w, int h) {
        switch (layer) {
            case 0: drawJungleBg(c, w, h); break;
            case 1: drawFarFoliage(c, w, h); break;
            case 2: drawTigerBody(c, w, h); break;
            case 3: drawTigerHead(c, w, h); break;
            case 4: drawForeground(c, w, h); break;
        }
    }

    private void drawJungleBg(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint();
        p.setShader(new LinearGradient(0, 0, 0, h,
            new int[]{0xFF020806, 0xFF041008, 0xFF061808, 0xFF081505},
            new float[]{0f, 0.35f, 0.65f, 1f}, Shader.TileMode.CLAMP));
        c.drawRect(-ex, -ex, w + ex, h + ex, p);
    }

    private void drawFarFoliage(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFF0A1808);
        // Dense foliage silhouette top and sides
        Path top = new Path();
        top.moveTo(-ex, -ex);
        top.lineTo(-ex, h * 0.22f);
        top.cubicTo(w * 0.1f, h * 0.16f, w * 0.25f, h * 0.20f, w * 0.35f, h * 0.14f);
        top.cubicTo(w * 0.45f, h * 0.08f, w * 0.55f, h * 0.12f, w * 0.65f, h * 0.08f);
        top.cubicTo(w * 0.75f, h * 0.04f, w * 0.88f, h * 0.14f, w + ex, h * 0.18f);
        top.lineTo(w + ex, -ex);
        top.close();
        c.drawPath(top, p);
        // Side foliage
        c.drawRect(-ex, 0, w * 0.08f, h + ex, p);
        c.drawRect(w * 0.92f, 0, w + ex, h + ex, p);
    }

    private void drawTigerBody(Canvas c, int w, int h) {
        float cx = w * 0.5f, cy = h * 0.75f, rx = w * 0.42f, ry = h * 0.28f;
        Paint body = new Paint(Paint.ANTI_ALIAS_FLAG);
        body.setShader(new RadialGradient(cx, cy - ry * 0.2f, rx,
            new int[]{0xFFDDDDCC, 0xFFBBBBAA, 0xFF888877},
            new float[]{0f, 0.6f, 1f}, Shader.TileMode.CLAMP));
        c.drawOval(cx - rx, cy - ry, cx + rx, cy + ry, body);
        drawStripes(c, cx, cy, rx, ry, false);
    }

    private void drawStripes(Canvas c, float cx, float cy, float rx, float ry, boolean isHead) {
        Paint stripe = new Paint(Paint.ANTI_ALIAS_FLAG);
        stripe.setColor(0xFF1A1A14);
        stripe.setStyle(Paint.Style.STROKE);
        stripe.setStrokeCap(Paint.Cap.ROUND);
        if (isHead) {
            // Face stripes
            stripe.setStrokeWidth(rx * 0.07f);
            // Forehead stripe
            Path s1 = new Path();
            s1.moveTo(cx, cy - ry * 0.55f);
            s1.lineTo(cx, cy - ry * 0.20f);
            c.drawPath(s1, stripe);
            // Side stripes
            stripe.setStrokeWidth(rx * 0.05f);
            for (int side : new int[]{-1, 1}) {
                Path s = new Path();
                s.moveTo(cx + side * rx * 0.25f, cy - ry * 0.40f);
                s.quadTo(cx + side * rx * 0.45f, cy - ry * 0.20f,
                         cx + side * rx * 0.55f, cy);
                c.drawPath(s, stripe);
                s = new Path();
                s.moveTo(cx + side * rx * 0.40f, cy - ry * 0.50f);
                s.quadTo(cx + side * rx * 0.60f, cy - ry * 0.30f,
                         cx + side * rx * 0.70f, cy - ry * 0.05f);
                c.drawPath(s, stripe);
            }
        } else {
            stripe.setStrokeWidth(rx * 0.06f);
            for (int i = -3; i <= 3; i++) {
                Path s = new Path();
                float sx = cx + i * rx * 0.28f;
                s.moveTo(sx, cy - ry * 0.8f);
                s.quadTo(sx + rx * 0.08f, cy, sx - rx * 0.05f, cy + ry * 0.8f);
                c.drawPath(s, stripe);
            }
        }
    }

    private void drawTigerHead(Canvas c, int w, int h) {
        // Head centered, top of head at ~y=0.22 (into BRUTAL date line at 0.17)
        float cx = w * 0.5f, cy = h * 0.52f;
        float rx = w * 0.38f, ry = h * 0.30f;

        // Head base
        Paint head = new Paint(Paint.ANTI_ALIAS_FLAG);
        head.setShader(new RadialGradient(cx - rx * 0.1f, cy - ry * 0.2f, rx,
            new int[]{0xFFEEEEE0, 0xFFCCCCBB, 0xFF999988},
            new float[]{0f, 0.5f, 1f}, Shader.TileMode.CLAMP));
        c.drawOval(cx - rx, cy - ry, cx + rx, cy + ry, head);
        drawStripes(c, cx, cy, rx, ry, true);

        // Ears (triangular, above head — go into date area at h*0.22)
        Paint ear = new Paint(Paint.ANTI_ALIAS_FLAG);
        ear.setColor(0xFFCCCCBB);
        for (int side : new int[]{-1, 1}) {
            float ex2 = cx + side * rx * 0.62f;
            float ey = cy - ry * 0.85f;
            Path earPath = new Path();
            earPath.moveTo(ex2 - side * rx * 0.18f, cy - ry * 0.60f);
            earPath.lineTo(ex2, ey - ry * 0.35f); // ear tip (at ~h*0.17)
            earPath.lineTo(ex2 + side * rx * 0.18f, cy - ry * 0.55f);
            earPath.close();
            c.drawPath(earPath, ear);
            // Inner ear pink
            ear.setColor(0xFFEEAAAA);
            earPath.reset();
            earPath.moveTo(ex2 - side * rx * 0.10f, cy - ry * 0.62f);
            earPath.lineTo(ex2, ey - ry * 0.25f);
            earPath.lineTo(ex2 + side * rx * 0.10f, cy - ry * 0.58f);
            earPath.close();
            c.drawPath(earPath, ear);
            ear.setColor(0xFFCCCCBB);
        }

        // Eyes
        float eyeY = cy - ry * 0.12f;
        for (int side : new int[]{-1, 1}) {
            float ex2 = cx + side * rx * 0.35f;
            // Outer (dark socket)
            Paint ep = new Paint(Paint.ANTI_ALIAS_FLAG);
            ep.setColor(0xFF111108);
            c.drawOval(ex2 - rx * 0.13f, eyeY - ry * 0.10f, ex2 + rx * 0.13f, eyeY + ry * 0.10f, ep);
            // Amber iris
            ep.setShader(new RadialGradient(ex2, eyeY, rx * 0.09f, 0xFFFFCC22, 0xFFAA7700, Shader.TileMode.CLAMP));
            c.drawOval(ex2 - rx * 0.09f, eyeY - ry * 0.07f, ex2 + rx * 0.09f, eyeY + ry * 0.07f, ep);
            // Pupil
            ep.setShader(null);
            ep.setColor(0xFF050503);
            c.drawOval(ex2 - rx * 0.04f, eyeY - ry * 0.06f, ex2 + rx * 0.04f, eyeY + ry * 0.06f, ep);
            // Highlight
            ep.setColor(0xCCFFFFFF);
            c.drawCircle(ex2 + rx * 0.03f, eyeY - ry * 0.03f, rx * 0.018f, ep);
        }

        // Nose & muzzle
        Paint muzzle = new Paint(Paint.ANTI_ALIAS_FLAG);
        muzzle.setColor(0xFFEEEEE0);
        c.drawOval(cx - rx * 0.30f, cy + ry * 0.08f, cx + rx * 0.30f, cy + ry * 0.35f, muzzle);
        muzzle.setColor(0xFFCC9999);
        Path nose = new Path();
        nose.moveTo(cx, cy + ry * 0.12f);
        nose.lineTo(cx - rx * 0.08f, cy + ry * 0.22f);
        nose.lineTo(cx + rx * 0.08f, cy + ry * 0.22f);
        nose.close();
        c.drawPath(nose, muzzle);
    }

    private void drawForeground(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFF030A03);
        // Dark vines / leaves at extreme foreground corners
        c.drawRect(-ex, h * 0.85f, w * 0.18f, h + ex, p);
        c.drawRect(w * 0.82f, h * 0.85f, w + ex, h + ex, p);
        // Hanging leaf at top-right
        p.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
        c.drawOval(w * 0.75f, -ex, w + ex, h * 0.12f, p);
        c.drawOval(-ex, -ex, w * 0.12f, h * 0.10f, p);
    }
}
