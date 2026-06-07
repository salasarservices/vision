package com.salasar.depthwallpaper.scenes;

import android.graphics.*;
import com.salasar.depthwallpaper.ClockStyle;
import com.salasar.depthwallpaper.WallpaperScene;
import java.util.Random;

public class AncientTemple extends WallpaperScene {
    @Override public String getName() { return "Ancient Temple"; }
    @Override public int getLayerCount() { return 5; }
    @Override public ClockStyle getClockStyle() { return ClockStyle.ELEGANT; }
    @Override public int getClockInsertAfterLayer() { return 1; }
    @Override public int getAccentColor() { return 0xFFFFAB40; }

    @Override
    public void drawLayer(Canvas c, int layer, int w, int h) {
        switch (layer) {
            case 0: drawSky(c, w, h); break;
            case 1: drawJungleBg(c, w, h); break;
            case 2: drawTemple(c, w, h); break;
            case 3: drawVines(c, w, h); break;
            case 4: drawForeground(c, w, h); break;
        }
    }

    private void drawSky(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint();
        p.setShader(new LinearGradient(0, 0, 0, h,
            new int[]{0xFF1A2A0A, 0xFF2A3A10, 0xFF3A4A18, 0xFF2A380E},
            new float[]{0f, 0.35f, 0.65f, 1f}, Shader.TileMode.CLAMP));
        c.drawRect(-ex, -ex, w+ex, h+ex, p);
        // Misty glow through trees
        Paint mist = new Paint();
        mist.setShader(new RadialGradient(w * 0.5f, h * 0.2f, h * 0.5f, 0x22EECC88, 0x00000000, Shader.TileMode.CLAMP));
        c.drawRect(-ex, -ex, w+ex, h*0.6f, mist);
    }

    private void drawJungleBg(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        // Dense canopy silhouette
        p.setShader(new LinearGradient(0, h * 0.05f, 0, h * 0.45f, 0xFF0D1A06, 0xFF162510, Shader.TileMode.CLAMP));
        // Left canopy blob
        Path canopy = new Path();
        canopy.moveTo(-ex, 0);
        canopy.cubicTo(w * 0.05f, h * 0.08f, w * 0.12f, 0, w * 0.20f, h * 0.05f);
        canopy.cubicTo(w * 0.28f, h * 0.10f, w * 0.35f, h * 0.02f, w * 0.42f, h * 0.08f);
        canopy.cubicTo(w * 0.50f, h * 0.14f, w * 0.58f, h * 0.04f, w * 0.65f, h * 0.10f);
        canopy.cubicTo(w * 0.72f, h * 0.16f, w * 0.80f, h * 0.06f, w * 0.88f, h * 0.12f);
        canopy.cubicTo(w * 0.94f, h * 0.18f, w + ex * 0.5f, h * 0.08f, w + ex, h * 0.10f);
        canopy.lineTo(w + ex, -ex);
        canopy.lineTo(-ex, -ex);
        canopy.close();
        c.drawPath(canopy, p);
        // Jungle floor background
        Paint floor = new Paint(Paint.ANTI_ALIAS_FLAG);
        floor.setShader(new LinearGradient(0, h * 0.65f, 0, h,
            0xFF0E1A08, 0xFF060E04, Shader.TileMode.CLAMP));
        c.drawRect(-ex, h * 0.65f, w+ex, h+ex, floor);
        // Far tree trunks
        Paint trunks = new Paint(Paint.ANTI_ALIAS_FLAG);
        trunks.setColor(0xFF0A1408);
        trunks.setStrokeWidth(12);
        trunks.setStyle(Paint.Style.STROKE);
        Random rng = new Random(111);
        for (int i = 0; i < 15; i++) {
            float tx = -ex + i * (w + 2*ex) / 14f + rng.nextFloat() * 20 - 10;
            c.drawLine(tx, h * 0.18f, tx + rng.nextFloat() * 15 - 7, h * 0.90f, trunks);
        }
    }

    private void drawTemple(Canvas c, int w, int h) {
        // Ancient step pyramid temple in center
        Paint stone = new Paint(Paint.ANTI_ALIAS_FLAG);
        int stoneColor = 0xFF7A7060;
        // Steps (5 tiers, each smaller and higher)
        int tiers = 5;
        float baseWidth = w * 0.72f;
        float baseY = h * 0.85f;
        float tierH = h * 0.10f;
        for (int t = 0; t < tiers; t++) {
            float tierW = baseWidth * (1f - t * 0.18f);
            float tierX = (w - tierW) / 2f;
            float tierY = baseY - t * tierH;
            // Stone face
            int lightness = 110 + t * 8;
            stone.setColor(Color.rgb(lightness, lightness - 10, lightness - 20));
            c.drawRect(tierX, tierY - tierH, tierX + tierW, tierY, stone);
            // Shadow on top edge of each tier
            Paint edge = new Paint();
            edge.setShader(new LinearGradient(0, tierY - tierH, 0, tierY - tierH + 12, 0x55000000, 0x00000000, Shader.TileMode.CLAMP));
            c.drawRect(tierX, tierY - tierH, tierX + tierW, tierY - tierH + 12, edge);
            // Mortar lines
            Paint mortar = new Paint(Paint.ANTI_ALIAS_FLAG);
            mortar.setColor(0x44000000);
            mortar.setStrokeWidth(1f);
            mortar.setStyle(Paint.Style.STROKE);
            for (float mx = tierX + 20; mx < tierX + tierW; mx += 25) {
                c.drawLine(mx, tierY - tierH, mx, tierY, mortar);
            }
        }
        // Temple top: small shrine room
        float topW = baseWidth * (1f - tiers * 0.18f);
        float topX = (w - topW) / 2f;
        float topY = baseY - tiers * tierH;
        stone.setColor(0xFF888078);
        c.drawRect(topX + topW * 0.2f, topY - h * 0.10f, topX + topW * 0.8f, topY, stone);
        // Doorway
        Paint door = new Paint(Paint.ANTI_ALIAS_FLAG);
        door.setColor(0xFF050A04);
        RectF doorRect = new RectF(w * 0.46f, topY - h * 0.08f, w * 0.54f, topY);
        c.drawRoundRect(doorRect, 8, 8, door);
        // Amber torch glow inside doorway
        Paint torch = new Paint(Paint.ANTI_ALIAS_FLAG);
        torch.setShader(new RadialGradient(w * 0.5f, topY - h * 0.04f, h * 0.06f,
            0x66FFAA00, 0x00000000, Shader.TileMode.CLAMP));
        c.drawRect(w * 0.42f, topY - h * 0.12f, w * 0.58f, topY, torch);
        // Pyramid base / ground connection
        paint_ground_shadow(c, w, h, baseY);
    }

    private void paint_ground_shadow(Canvas c, int w, int h, float baseY) {
        Paint p = new Paint();
        p.setShader(new LinearGradient(0, baseY, 0, baseY + 30, 0x44000000, 0x00000000, Shader.TileMode.CLAMP));
        c.drawRect(w * 0.10f, baseY, w * 0.90f, baseY + 30, p);
    }

    private void drawVines(Canvas c, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFF2A5018);
        p.setStrokeWidth(3f);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeCap(Paint.Cap.ROUND);
        // Hanging vines from top
        Random rng = new Random(66);
        for (int i = 0; i < 14; i++) {
            float vx = rng.nextFloat() * (w + 2 * MAX_PARALLAX) - MAX_PARALLAX;
            float vlen = h * (0.12f + rng.nextFloat() * 0.30f);
            Path vine = new Path();
            vine.moveTo(vx, -MAX_PARALLAX);
            vine.cubicTo(vx + rng.nextFloat() * 20 - 10, vlen * 0.3f,
                vx + rng.nextFloat() * 30 - 15, vlen * 0.6f,
                vx + rng.nextFloat() * 20 - 10, vlen);
            c.drawPath(vine, p);
            // Small leaf
            Paint leaf = new Paint(Paint.ANTI_ALIAS_FLAG);
            leaf.setColor(0xFF2A7020);
            float leafX = vx + rng.nextFloat() * 20 - 10;
            float leafY = vlen;
            c.save();
            c.rotate(rng.nextFloat() * 60 - 30, leafX, leafY);
            c.drawOval(leafX - 8, leafY - 4, leafX + 8, leafY + 4, leaf);
            c.restore();
        }
    }

    private void drawForeground(Canvas c, int w, int h) {
        int ex = MAX_PARALLAX;
        // Dense foreground vegetation silhouettes
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new LinearGradient(0, h * 0.78f, 0, h + ex, 0xFF0A1406, 0xFF050A03, Shader.TileMode.CLAMP));
        Path fg = new Path();
        fg.moveTo(-ex, h + ex);
        fg.lineTo(-ex, h * 0.82f);
        fg.cubicTo(w * 0.05f, h * 0.76f, w * 0.10f, h * 0.84f, w * 0.15f, h * 0.78f);
        fg.cubicTo(w * 0.20f, h * 0.72f, w * 0.25f, h * 0.84f, w * 0.30f, h * 0.77f);
        fg.cubicTo(w * 0.35f, h * 0.70f, w * 0.40f, h * 0.82f, w * 0.45f, h * 0.76f);
        fg.lineTo(w * 0.50f, h * 0.74f);
        fg.cubicTo(w * 0.55f, h * 0.80f, w * 0.60f, h * 0.70f, w * 0.65f, h * 0.76f);
        fg.cubicTo(w * 0.70f, h * 0.82f, w * 0.75f, h * 0.72f, w * 0.80f, h * 0.78f);
        fg.cubicTo(w * 0.85f, h * 0.84f, w * 0.90f, h * 0.75f, w * 0.95f, h * 0.80f);
        fg.lineTo(w + ex, h * 0.82f);
        fg.lineTo(w + ex, h + ex);
        fg.close();
        c.drawPath(fg, p);
        // Left ruin stone
        Paint ruin = new Paint(Paint.ANTI_ALIAS_FLAG);
        ruin.setColor(0xFF4A4435);
        c.drawRect(-ex, h * 0.72f, w * 0.12f, h * 0.90f, ruin);
        ruin.setColor(0xFF3A3428);
        c.drawRect(-ex, h * 0.82f, w * 0.18f, h + ex, ruin);
        // Right ruin stone
        c.drawRect(w * 0.88f, h * 0.70f, w + ex, h * 0.88f, ruin);
        c.drawRect(w * 0.82f, h * 0.82f, w + ex, h + ex, ruin);
        // Moss texture on ruins
        Paint moss = new Paint(Paint.ANTI_ALIAS_FLAG);
        moss.setColor(0x882A5010);
        c.drawRect(-ex, h * 0.80f, w * 0.18f, h * 0.86f, moss);
        c.drawRect(w * 0.82f, h * 0.78f, w + ex, h * 0.84f, moss);
    }
}
