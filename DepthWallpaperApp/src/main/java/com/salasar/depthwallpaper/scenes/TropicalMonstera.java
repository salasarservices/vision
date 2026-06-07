package com.salasar.depthwallpaper.scenes;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import com.salasar.depthwallpaper.ClockStyle;
import com.salasar.depthwallpaper.WallpaperScene;

public class TropicalMonstera extends WallpaperScene {

    @Override public String getName() { return "Tropical Monstera"; }
    @Override public int getLayerCount() { return 5; }
    @Override public ClockStyle getClockStyle() { return ClockStyle.APPLE; }
    @Override public int getClockInsertAfterLayer() { return 1; }
    @Override public int getAccentColor() { return 0xFF3A7D44; }

    @Override
    public void drawLayer(Canvas canvas, int layer, int w, int h) {
        switch (layer) {
            case 0: drawBackground(canvas, w, h); break;
            case 1: drawFarBlurryLeaves(canvas, w, h); break;
            case 2: drawMidLeaves(canvas, w, h); break;
            case 3: drawCloseLeaves(canvas, w, h); break;
            case 4: drawForeground(canvas, w, h); break;
        }
    }

    private void drawBackground(Canvas canvas, int w, int h) {
        Paint p = new Paint();
        p.setShader(new LinearGradient(0, 0, 0, h,
            new int[]{0xFF2A1A08, 0xFF6B4020, 0xFF9B6030, 0xFF7A4820},
            new float[]{0f, 0.35f, 0.70f, 1f},
            Shader.TileMode.CLAMP));
        canvas.drawRect(-MAX_PARALLAX, -MAX_PARALLAX, w + MAX_PARALLAX, h + MAX_PARALLAX, p);
    }

    private void drawFarBlurryLeaves(Canvas canvas, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setMaskFilter(new BlurMaskFilter(40, BlurMaskFilter.Blur.NORMAL));

        int[][] blobs = {
            {(int)(w * 0.10f), (int)(h * 0.20f), (int)(w * 0.32f), (int)(h * 0.28f)},
            {(int)(w * 0.70f), (int)(h * 0.15f), (int)(w * 0.35f), (int)(h * 0.30f)},
            {(int)(w * 0.50f), (int)(h * 0.50f), (int)(w * 0.28f), (int)(h * 0.22f)},
            {(int)(w * 0.05f), (int)(h * 0.55f), (int)(w * 0.25f), (int)(h * 0.20f)},
            {(int)(w * 0.85f), (int)(h * 0.55f), (int)(w * 0.30f), (int)(h * 0.25f)},
            {(int)(w * 0.40f), (int)(h * 0.80f), (int)(w * 0.38f), (int)(h * 0.20f)},
        };
        int[] colors = {0xFF1A3010, 0xFF142808, 0xFF1E3A0E, 0xFF122406, 0xFF183012, 0xFF0E2006};

        for (int i = 0; i < blobs.length; i++) {
            p.setColor(colors[i % colors.length]);
            canvas.drawOval(
                blobs[i][0] - blobs[i][2] / 2f,
                blobs[i][1] - blobs[i][3] / 2f,
                blobs[i][0] + blobs[i][2] / 2f,
                blobs[i][1] + blobs[i][3] / 2f,
                p);
        }
    }

    private void drawMidLeaves(Canvas canvas, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFF2A5018);
        p.setStyle(Paint.Style.FILL);

        // Monstera leaf 1 — center-left
        Path leaf1 = buildMonsteraLeaf(w * 0.15f, h * 0.90f, w * 0.45f, h * 0.28f, w * 0.62f, h * 0.85f, 0.55f, w, h);
        canvas.drawPath(leaf1, p);

        p.setColor(0xFF224412);
        // Monstera leaf 2 — right side
        Path leaf2 = buildMonsteraLeaf(w * 0.45f, h * 0.95f, w * 0.75f, h * 0.32f, w * 0.95f, h * 0.88f, 0.50f, w, h);
        canvas.drawPath(leaf2, p);

        // Vein lines
        Paint vein = new Paint(Paint.ANTI_ALIAS_FLAG);
        vein.setColor(0x55366020);
        vein.setStyle(Paint.Style.STROKE);
        vein.setStrokeWidth(1.5f);
        drawVeins(canvas, vein, w * 0.15f, h * 0.90f, w * 0.45f, h * 0.28f);
        drawVeins(canvas, vein, w * 0.45f, h * 0.95f, w * 0.75f, h * 0.32f);
    }

    private void drawCloseLeaves(Canvas canvas, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFF1E4010);
        p.setStyle(Paint.Style.FILL);

        // Large leaf from bottom-left crossing into clock area
        Path leaf = buildMonsteraLeaf(-w * 0.05f, h * 0.92f, w * 0.35f, h * 0.22f, w * 0.65f, h * 0.90f, 0.60f, w, h);
        canvas.drawPath(leaf, p);

        // Sheen highlight
        Paint sheen = new Paint(Paint.ANTI_ALIAS_FLAG);
        sheen.setColor(0x18AAFFAA);
        sheen.setStyle(Paint.Style.FILL);
        sheen.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.NORMAL));
        Path sheenPath = new Path();
        sheenPath.moveTo(w * 0.10f, h * 0.70f);
        sheenPath.cubicTo(w * 0.15f, h * 0.55f, w * 0.25f, h * 0.40f, w * 0.30f, h * 0.28f);
        sheenPath.lineTo(w * 0.35f, h * 0.30f);
        sheenPath.cubicTo(w * 0.30f, h * 0.44f, w * 0.20f, h * 0.58f, w * 0.18f, h * 0.72f);
        sheenPath.close();
        canvas.drawPath(sheenPath, sheen);

        // Vein lines
        Paint vein = new Paint(Paint.ANTI_ALIAS_FLAG);
        vein.setColor(0x44326618);
        vein.setStyle(Paint.Style.STROKE);
        vein.setStrokeWidth(2f);
        drawVeins(canvas, vein, -w * 0.05f, h * 0.92f, w * 0.35f, h * 0.22f);
    }

    private void drawForeground(Canvas canvas, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFF152808);
        p.setStyle(Paint.Style.FILL);

        // Very large Monstera leaf from left edge, tip reaching high (25% screen height)
        Path leaf = buildMonsteraLeaf(-w * 0.10f, h * 0.88f, w * 0.28f, h * 0.14f, w * 0.58f, h * 0.82f, 0.65f, w, h);
        canvas.drawPath(leaf, p);

        // Second foreground leaf at right bottom
        p.setColor(0xFF0F1E06);
        Path leaf2 = buildMonsteraLeaf(w * 0.55f, h * 0.98f, w * 0.90f, h * 0.50f, w * 1.10f, h * 0.92f, 0.50f, w, h);
        canvas.drawPath(leaf2, p);

        // Vein lines on main leaf
        Paint vein = new Paint(Paint.ANTI_ALIAS_FLAG);
        vein.setColor(0x442A6020);
        vein.setStyle(Paint.Style.STROKE);
        vein.setStrokeWidth(2.5f);
        drawVeins(canvas, vein, -w * 0.10f, h * 0.88f, w * 0.28f, h * 0.14f);
    }

    /**
     * Build a Monstera leaf path.
     * tipX/tipY = leaf tip (topmost point)
     * baseX/baseY = base of the leaf stem area
     * rightX/rightY = right lobe end
     * splitRatio = where the splits/holes occur along the leaf (0-1)
     */
    private Path buildMonsteraLeaf(float baseX, float baseY,
                                   float tipX, float tipY,
                                   float rightX, float rightY,
                                   float splitRatio, int w, int h) {
        Path path = new Path();

        // Mid point of the leaf
        float midX = (baseX + rightX) / 2f;
        float midY = (tipY + baseY) / 2f;

        // Leaf outline: heart-shaped with splits
        path.moveTo(baseX, baseY);
        // Left side up to tip
        path.cubicTo(
            baseX - (tipX - baseX) * 0.3f, baseY - (baseY - tipY) * 0.4f,
            tipX - (rightX - tipX) * 0.4f, tipY + (midY - tipY) * 0.1f,
            tipX, tipY);
        // Right side from tip to right lobe
        path.cubicTo(
            tipX + (rightX - tipX) * 0.3f, tipY + (midY - tipY) * 0.2f,
            rightX - (rightX - midX) * 0.3f, midY - (midY - tipY) * 0.1f,
            rightX, rightY);
        // Bottom right back to base
        path.cubicTo(
            rightX - (rightX - midX) * 0.2f, rightY + (baseY - rightY) * 0.5f,
            midX + (baseX - midX) * 0.1f, baseY - (baseY - rightY) * 0.1f,
            baseX, baseY);
        path.close();

        // Add characteristic Monstera holes/splits as counter-clockwise sub-paths
        // Split 1 — left lobe hole
        float hx1 = baseX + (tipX - baseX) * 0.3f;
        float hy1 = tipY + (baseY - tipY) * (splitRatio - 0.15f);
        float hr1w = (tipX - baseX) * 0.18f;
        float hr1h = (baseY - tipY) * 0.10f;
        addMonsteraHole(path, hx1 - hr1w, hy1 - hr1h, hx1 + hr1w, hy1 + hr1h);

        // Split 2 — right lobe hole
        float hx2 = tipX + (rightX - tipX) * 0.45f;
        float hy2 = tipY + (midY - tipY) * (splitRatio);
        float hr2w = (rightX - tipX) * 0.15f;
        float hr2h = (midY - tipY) * 0.18f;
        addMonsteraHole(path, hx2 - hr2w, hy2 - hr2h, hx2 + hr2w, hy2 + hr2h);

        // Split 3 — small center notch hole
        float hx3 = (tipX + midX) / 2f;
        float hy3 = tipY + (baseY - tipY) * (splitRatio * 0.6f);
        float hr3w = Math.abs(rightX - baseX) * 0.08f;
        float hr3h = (baseY - tipY) * 0.07f;
        addMonsteraHole(path, hx3 - hr3w, hy3 - hr3h, hx3 + hr3w, hy3 + hr3h);

        return path;
    }

    /** Add an oval hole sub-path (counter-clockwise = "cut out") */
    private void addMonsteraHole(Path path, float left, float top, float right, float bottom) {
        // Use addOval with CCW direction to create a "hole" in even-odd fill
        path.addOval(left, top, right, bottom, Path.Direction.CCW);
    }

    private void drawVeins(Canvas canvas, Paint vein, float baseX, float baseY, float tipX, float tipY) {
        // Central vein
        Path central = new Path();
        central.moveTo(baseX, baseY);
        central.quadTo((baseX + tipX) / 2f + 10, (baseY + tipY) / 2f, tipX, tipY);
        canvas.drawPath(central, vein);

        // Side veins
        for (int i = 1; i <= 5; i++) {
            float t = i / 6f;
            float vx = baseX + (tipX - baseX) * t;
            float vy = baseY + (tipY - baseY) * t;
            float vx2 = vx - (tipX - baseX) * 0.12f + (tipY - baseY) * 0.08f;
            float vy2 = vy + (tipX - baseX) * 0.08f + (tipY - baseY) * 0.05f;
            float vx3 = vx + (tipX - baseX) * 0.12f - (tipY - baseY) * 0.08f;
            float vy3 = vy - (tipX - baseX) * 0.08f + (tipY - baseY) * 0.05f;
            Path v1 = new Path();
            v1.moveTo(vx, vy);
            v1.lineTo(vx2, vy2);
            canvas.drawPath(v1, vein);
            Path v2 = new Path();
            v2.moveTo(vx, vy);
            v2.lineTo(vx3, vy3);
            canvas.drawPath(v2, vein);
        }
    }
}
