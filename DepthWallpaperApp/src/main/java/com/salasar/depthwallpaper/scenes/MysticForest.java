package com.salasar.depthwallpaper.scenes;

import android.graphics.*;
import com.salasar.depthwallpaper.WallpaperScene;
import java.util.Random;

public class MysticForest extends WallpaperScene {
    @Override public String getName() { return "Mystic Forest"; }
    @Override public int getLayerCount() { return 5; }
    @Override public int getAccentColor() { return 0xFF4CAF50; }

    @Override
    public void drawLayer(Canvas c, int layer, int w, int h) {
        switch (layer) {
            case 0: drawSky(c, w, h); break;
            case 1: drawMist(c, w, h); break;
            case 2: drawFarTrees(c, w, h); break;
            case 3: drawLightRays(c, w, h); break;
            case 4: drawNearTrees(c, w, h); break;
        }
    }

    private void drawSky(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint();
        p.setShader(new LinearGradient(0, 0, 0, h,
            new int[]{0xFF0A1A0A, 0xFF1A3010, 0xFF2A5020, 0xFF1A4015, 0xFF0D2A0D},
            new float[]{0f, 0.3f, 0.55f, 0.75f, 1f}, Shader.TileMode.CLAMP));
        c.drawRect(-ex, -ex, w+ex, h+ex, p);
        // Canopy opening glow
        Paint glow = new Paint(Paint.ANTI_ALIAS_FLAG);
        glow.setShader(new RadialGradient(w * 0.5f, 0, h * 0.5f,
            0x55D4A820, 0x00203010, Shader.TileMode.CLAMP));
        c.drawRect(-ex, -ex, w+ex, h * 0.6f, glow);
    }

    private void drawMist(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        // Misty ground fog layers
        for (int i = 0; i < 4; i++) {
            float yCenter = h * (0.55f + i * 0.12f);
            float yRadius = 60 + i * 20;
            p.setShader(new LinearGradient(0, yCenter - yRadius, 0, yCenter + yRadius,
                0x00A8C8A0, 0x28A8C8A0, Shader.TileMode.CLAMP));
            c.drawRect(-ex, yCenter - yRadius, w + ex, yCenter + yRadius, p);
        }
    }

    private void drawFarTrees(Canvas c, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFF1A3015);
        int ex = MAX_PARALLAX;
        Random rng = new Random(77);
        for (int i = 0; i < 18; i++) {
            float tx = -ex + (i * (w + 2 * ex)) / 17f;
            float treeH = h * (0.28f + rng.nextFloat() * 0.18f);
            float treeW = 14 + rng.nextFloat() * 22;
            drawPineTree(c, p, tx, h * 0.72f, treeH, treeW, false);
        }
    }

    private void drawPineTree(Canvas c, Paint p, float x, float base, float height, float width, boolean near) {
        Path tree = new Path();
        tree.moveTo(x, base);
        tree.lineTo(x - width, base);
        tree.lineTo(x, base - height * 0.3f);
        tree.lineTo(x - width * 0.7f, base - height * 0.3f);
        tree.lineTo(x, base - height * 0.6f);
        tree.lineTo(x - width * 0.5f, base - height * 0.6f);
        tree.lineTo(x, base - height);
        // Mirror right side
        tree.lineTo(x + width * 0.5f, base - height * 0.6f);
        tree.lineTo(x + width, base - height * 0.6f);
        tree.lineTo(x + width * 0.7f, base - height * 0.3f);
        tree.lineTo(x + width, base - height * 0.3f);
        tree.lineTo(x + width, base);
        tree.close();
        c.drawPath(tree, p);
    }

    private void drawLightRays(Canvas c, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        // Golden light rays from upper center
        float srcX = w * 0.5f;
        float srcY = -h * 0.1f;
        int[] angles = {-40, -25, -12, 0, 12, 28, 45};
        int ex = MAX_PARALLAX;
        for (int angle : angles) {
            double rad = Math.toRadians(90 - angle);
            float endX = srcX + (float) Math.cos(rad) * h * 1.5f;
            float endY = srcY + (float) Math.sin(rad) * h * 1.5f;
            Path ray = new Path();
            ray.moveTo(srcX - 5, srcY);
            ray.lineTo(srcX + 5, srcY);
            ray.lineTo(endX + 50, endY);
            ray.lineTo(endX - 50, endY);
            ray.close();
            p.setShader(new LinearGradient(srcX, srcY, endX, endY, 0x22F0CC40, 0x00F0CC40, Shader.TileMode.CLAMP));
            c.drawPath(ray, p);
        }
    }

    private void drawNearTrees(Canvas c, int w, int h) {
        // Dark large tree trunks at edges
        Paint trunk = new Paint(Paint.ANTI_ALIAS_FLAG);
        trunk.setShader(new LinearGradient(0, 0, 30, 0, 0xFF050E05, 0xFF0A1A0A, Shader.TileMode.CLAMP));
        // Left trunks
        c.drawRect(-MAX_PARALLAX, 0, 28, h + MAX_PARALLAX, trunk);
        c.drawRect(55, 0, 80, h + MAX_PARALLAX, new Paint(){{ setColor(0xFF07120A); }});
        // Right trunks
        Paint trunkR = new Paint(Paint.ANTI_ALIAS_FLAG);
        trunkR.setShader(new LinearGradient(w - 28, 0, w, 0, 0xFF0A1A0A, 0xFF050E05, Shader.TileMode.CLAMP));
        c.drawRect(w - 28, 0, w + MAX_PARALLAX, h + MAX_PARALLAX, trunkR);
        c.drawRect(w - 75, 0, w - 50, h + MAX_PARALLAX, new Paint(){{ setColor(0xFF07120A); }});
        // Foreground foliage overlay at top
        Paint foliage = new Paint(Paint.ANTI_ALIAS_FLAG);
        foliage.setShader(new LinearGradient(0, 0, 0, h * 0.35f, 0xFF040C04, 0x00040C04, Shader.TileMode.CLAMP));
        c.drawRect(-MAX_PARALLAX, -MAX_PARALLAX, w + MAX_PARALLAX, h * 0.35f, foliage);
    }
}
