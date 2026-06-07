package com.salasar.depthwallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import java.util.Calendar;

/**
 * Draws date/time/weather overlays onto wallpaper canvas.
 * Styles closely replicate iOS lock-screen and HyperOS depth-wallpaper clocks.
 */
public class ClockPainter {

    private static final String[] MONTH = {
        "January","February","March","April","May","June",
        "July","August","September","October","November","December"
    };
    private static final String[] MONTH_S = {
        "JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"
    };
    private static final String[] DAY = {
        "Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"
    };
    private static final String[] DAY_S = {
        "SUN","MON","TUE","WED","THU","FRI","SAT"
    };

    // -----------------------------------------------------------------------
    // Public entry points
    // -----------------------------------------------------------------------

    public static void draw(Canvas canvas, int w, int h, ClockStyle style,
                            String weatherTemp, String weatherCondition, int weatherCode) {
        Calendar c = Calendar.getInstance();
        int hour24   = c.get(Calendar.HOUR_OF_DAY);
        int minute   = c.get(Calendar.MINUTE);
        int dow      = c.get(Calendar.DAY_OF_WEEK) - 1;
        int dom      = c.get(Calendar.DAY_OF_MONTH);
        int mon      = c.get(Calendar.MONTH);
        String mm    = minute < 10 ? "0" + minute : String.valueOf(minute);
        String hh2   = hour24 < 10 ? "0" + hour24 : String.valueOf(hour24);

        switch (style) {
            case APPLE:
                drawApple(canvas, w, h, hour24, mm, dow, dom, mon, weatherTemp, weatherCondition, weatherCode);
                break;
            case HYPEROS:
                drawHyperOS(canvas, w, h, hh2, mm, dow, dom, mon, weatherTemp, weatherCondition, weatherCode);
                break;
            case ARTISTIC:
                drawArtistic(canvas, w, h, hh2, mm, dow, dom, mon);
                break;
            case MINIMAL:
                drawMinimal(canvas, w, h, hh2, mm, dow, dom, mon, weatherTemp, weatherCondition, weatherCode);
                break;
            case FUTURISTIC:
                drawFuturistic(canvas, w, h, hh2, mm, dow, dom, mon, weatherTemp, weatherCondition, weatherCode);
                break;
            case ELEGANT:
                drawElegant(canvas, w, h, hour24, mm, dow, dom, mon, weatherTemp, weatherCondition, weatherCode);
                break;
            case BRUTAL:
                drawBrutal(canvas, w, h, hour24, mm, dow, dom, mon, weatherTemp, weatherCondition, weatherCode);
                break;
            case FROSTED:
                drawFrosted(canvas, w, h, hh2, mm, dow, dom, mon, weatherTemp, weatherCondition, weatherCode);
                break;
        }
    }

    // Backward compat — callers that don't pass weatherCode default to 0 (clear)
    public static void draw(Canvas canvas, int w, int h, ClockStyle style,
                            String weatherTemp, String weatherCondition) {
        draw(canvas, w, h, style, weatherTemp, weatherCondition, 0);
    }

    // -----------------------------------------------------------------------
    // APPLE — exact iOS lock-screen / depth-wallpaper replica
    // Ultra-thin large time, light date, small weather row with icon
    // -----------------------------------------------------------------------
    private static void drawApple(Canvas c, int w, int h,
                                  int hour24, String mm, int dow, int dom, int mon,
                                  String wTemp, String wCond, int wCode) {
        float cx = w / 2f;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextAlign(Paint.Align.CENTER);
        p.setColor(Color.WHITE);

        // --- Weather row (icon + temp + condition) ---
        float iconSz = w * 0.048f;
        float wY = h * 0.195f;
        p.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        p.setTextSize(w * 0.033f);
        p.setAlpha(210);
        p.setShadowLayer(18, 0, 3, 0xCC000000);
        String wText = "  " + wTemp + "  " + wCond;
        float wTextW = p.measureText(wText);
        float iconLeft = cx - (iconSz + wTextW) / 2f + iconSz * 0.5f;
        p.setTextAlign(Paint.Align.LEFT);
        canvas_drawWeatherIcon(c, iconLeft, wY - iconSz * 0.55f, iconSz * 0.85f, wCode,
                               Color.WHITE, 190);
        c.drawText(wText, iconLeft + iconSz * 0.6f, wY, p);
        p.setTextAlign(Paint.Align.CENTER);

        // --- Time — SF Pro Thin equivalent, no leading zero ---
        String timeStr = hour24 + ":" + mm;
        p.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
        p.setTextSize(w * 0.23f);
        p.setAlpha(255);
        p.setShadowLayer(35, 0, 6, 0xDD000000);
        c.drawText(timeStr, cx, h * 0.385f, p);

        // --- Date — "Wednesday, January 9" ---
        String dateStr = DAY[dow] + ", " + MONTH[mon] + " " + dom;
        p.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        p.setTextSize(w * 0.040f);
        p.setAlpha(215);
        p.setShadowLayer(18, 0, 3, 0xCC000000);
        c.drawText(dateStr, cx, h * 0.448f, p);
    }

    // -----------------------------------------------------------------------
    // HYPEROS — Xiaomi HyperOS depth-wallpaper layout
    // Small date pill → thin line → large medium-weight time → weather row
    // -----------------------------------------------------------------------
    private static void drawHyperOS(Canvas c, int w, int h,
                                    String hh, String mm, int dow, int dom, int mon,
                                    String wTemp, String wCond, int wCode) {
        float cx = w / 2f;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.WHITE);
        p.setTextAlign(Paint.Align.CENTER);
        p.setShadowLayer(20, 0, 4, 0xCC000000);

        // --- Day + date pill: "THU · JAN 9" ---
        String dateStr = DAY_S[dow] + "  ·  " + MONTH_S[mon] + " " + dom;
        p.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        p.setTextSize(w * 0.034f);
        p.setAlpha(200);
        c.drawText(dateStr, cx, h * 0.215f, p);

        // Thin separator line
        Paint line = new Paint(Paint.ANTI_ALIAS_FLAG);
        line.setColor(0x50FFFFFF);
        line.setStrokeWidth(0.8f);
        c.drawLine(cx - w * 0.28f, h * 0.248f, cx + w * 0.28f, h * 0.248f, line);

        // --- Time — large, light weight ---
        String timeStr = hh + ":" + mm;
        p.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        p.setTextSize(w * 0.20f);
        p.setAlpha(255);
        p.setShadowLayer(30, 0, 5, 0xDD000000);
        c.drawText(timeStr, cx, h * 0.375f, p);

        // Thin separator below time
        line.setColor(0x38FFFFFF);
        c.drawLine(cx - w * 0.25f, h * 0.403f, cx + w * 0.25f, h * 0.403f, line);

        // --- Weather row: icon + temp + condition ---
        float iconSz = w * 0.046f;
        float wY = h * 0.445f;
        p.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        p.setTextSize(w * 0.034f);
        p.setAlpha(175);
        p.setShadowLayer(15, 0, 3, 0xAA000000);
        String wText = "  " + wTemp + "  " + wCond;
        float wTextW = p.measureText(wText);
        float iLeft = cx - (iconSz * 0.6f + wTextW) / 2f;
        p.setTextAlign(Paint.Align.LEFT);
        canvas_drawWeatherIcon(c, iLeft, wY - iconSz * 0.55f, iconSz * 0.85f, wCode,
                               Color.WHITE, 170);
        c.drawText(wText, iLeft + iconSz * 0.7f, wY, p);
        p.setTextAlign(Paint.Align.CENTER);
    }

    // -----------------------------------------------------------------------
    // ARTISTIC — huge hour/minute stack (NightForest trees overlay digits)
    // -----------------------------------------------------------------------
    private static void drawArtistic(Canvas c, int w, int h,
                                     String hh, String mm, int dow, int dom, int mon) {
        float cx = w / 2f;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextAlign(Paint.Align.CENTER);
        p.setShadowLayer(25, 0, 6, 0xBB000000);

        p.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
        p.setTextSize(w * 0.42f);
        p.setColor(Color.WHITE);
        p.setAlpha(255);
        c.drawText(hh, cx, h * 0.30f, p);

        p.setAlpha(230);
        c.drawText(mm, cx, h * 0.57f, p);

        p.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        p.setTextSize(w * 0.040f);
        p.setAlpha(190);
        p.setShadowLayer(15, 0, 3, 0x99000000);
        c.drawText(DAY[dow].toUpperCase() + "  ·  " + MONTH_S[mon] + " " + dom, cx, h * 0.68f, p);
    }

    // -----------------------------------------------------------------------
    // MINIMAL — right-aligned small clock (GlacierPeak)
    // -----------------------------------------------------------------------
    private static void drawMinimal(Canvas c, int w, int h,
                                    String hh, String mm, int dow, int dom, int mon,
                                    String wTemp, String wCond, int wCode) {
        float rx = w * 0.90f;
        float ry = h * 0.34f;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextAlign(Paint.Align.RIGHT);
        p.setColor(Color.WHITE);
        p.setShadowLayer(20, 0, 4, 0xCC000000);

        p.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
        p.setTextSize(w * 0.072f);
        p.setAlpha(255);
        c.drawText(hh + ":" + mm, rx, ry, p);

        p.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        p.setTextSize(w * 0.025f);
        p.setAlpha(175);
        c.drawText(DAY_S[dow] + "  " + (dom < 10 ? "0"+dom : ""+dom) + "  " + MONTH_S[mon], rx, ry + w*0.038f, p);

        // Weather with tiny icon
        float iconSz = w * 0.030f;
        float wY = ry + w * 0.072f;
        p.setTextSize(w * 0.021f);
        p.setAlpha(155);
        String wText = wTemp + "  " + wCond + "  ";
        float wW = p.measureText(wText);
        c.drawText(wText, rx, wY, p);
        canvas_drawWeatherIcon(c, rx - wW + iconSz * 0.4f, wY - iconSz * 0.55f, iconSz * 0.8f,
                               wCode, Color.WHITE, 150);
    }

    // -----------------------------------------------------------------------
    // FUTURISTIC — monospace neon (CosmicRift)
    // -----------------------------------------------------------------------
    private static void drawFuturistic(Canvas c, int w, int h,
                                       String hh, String mm, int dow, int dom, int mon,
                                       String wTemp, String wCond, int wCode) {
        float cx = w / 2f;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextAlign(Paint.Align.CENTER);
        p.setTypeface(Typeface.MONOSPACE);

        // Neon horizontal lines
        Paint ln = new Paint(Paint.ANTI_ALIAS_FLAG);
        ln.setColor(0x6600FFCC);
        ln.setStrokeWidth(1f);
        c.drawLine(w * 0.12f, h * 0.21f, w * 0.88f, h * 0.21f, ln);

        // Time — neon cyan glow
        p.setTextSize(w * 0.145f);
        p.setColor(0xFFCCFFEE);
        p.setAlpha(255);
        p.setShadowLayer(24, 0, 0, 0xAA00FFCC);
        c.drawText(hh + ":" + mm, cx, h * 0.365f, p);

        c.drawLine(w * 0.12f, h * 0.405f, w * 0.88f, h * 0.405f, ln);

        // Date
        p.setTextSize(w * 0.030f);
        p.setColor(0xCCAAEEDD);
        p.setAlpha(190);
        p.setShadowLayer(12, 0, 3, 0x88000000);
        c.drawText(String.format("%02d.%02d  //  %s", (mon+1), dom, DAY_S[dow]), cx, h * 0.445f, p);

        // Weather
        float iconSz = w * 0.040f;
        float wY = h * 0.49f;
        p.setTextSize(w * 0.026f);
        p.setColor(0xAA99DDCC);
        p.setAlpha(160);
        String wText = "  " + wTemp + "  " + wCond;
        float wW = p.measureText(wText);
        float iLeft = cx - (iconSz * 0.6f + wW) / 2f;
        p.setTextAlign(Paint.Align.LEFT);
        canvas_drawWeatherIcon(c, iLeft, wY - iconSz * 0.55f, iconSz * 0.8f, wCode, 0xCCAAEEDD, 160);
        c.drawText(wText, iLeft + iconSz * 0.6f, wY, p);
        p.setTextAlign(Paint.Align.CENTER);
    }

    // -----------------------------------------------------------------------
    // ELEGANT — serif warm cream (OceanSurge, SakuraNight)
    // -----------------------------------------------------------------------
    private static void drawElegant(Canvas c, int w, int h,
                                    int hour24, String mm, int dow, int dom, int mon,
                                    String wTemp, String wCond, int wCode) {
        float cx = w / 2f;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextAlign(Paint.Align.CENTER);
        p.setColor(0xFFFFEEDD);
        p.setShadowLayer(22, 0, 4, 0xCC000000);

        // Weather row
        float iconSz = w * 0.044f;
        float wY = h * 0.18f;
        p.setTypeface(Typeface.create("serif", Typeface.ITALIC));
        p.setTextSize(w * 0.030f);
        p.setAlpha(185);
        String wText = "  " + wTemp + "  " + wCond;
        float wW = p.measureText(wText);
        float iLeft = cx - (iconSz * 0.6f + wW) / 2f;
        p.setTextAlign(Paint.Align.LEFT);
        canvas_drawWeatherIcon(c, iLeft, wY - iconSz * 0.55f, iconSz * 0.8f, wCode, 0xFFEEDD, 175);
        c.drawText(wText, iLeft + iconSz * 0.6f, wY, p);
        p.setTextAlign(Paint.Align.CENTER);

        // Time
        p.setTypeface(Typeface.create("serif", Typeface.NORMAL));
        p.setTextSize(w * 0.20f);
        p.setAlpha(255);
        p.setShadowLayer(30, 0, 5, 0xDD000000);
        c.drawText(hour24 + ":" + mm, cx, h * 0.378f, p);

        // Date
        p.setTypeface(Typeface.create("serif", Typeface.ITALIC));
        p.setTextSize(w * 0.038f);
        p.setAlpha(210);
        p.setShadowLayer(18, 0, 3, 0xBB000000);
        c.drawText(DAY[dow] + ", " + MONTH[mon] + " " + dom, cx, h * 0.445f, p);
    }

    // -----------------------------------------------------------------------
    // BRUTAL — condensed very large (WhiteTiger, RedCanyon)
    // -----------------------------------------------------------------------
    private static void drawBrutal(Canvas c, int w, int h,
                                   int hour24, String mm, int dow, int dom, int mon,
                                   String wTemp, String wCond, int wCode) {
        float cx = w / 2f;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextAlign(Paint.Align.CENTER);
        p.setColor(Color.WHITE);
        p.setTypeface(Typeface.create("sans-serif-condensed", Typeface.BOLD));
        p.setShadowLayer(25, 0, 5, 0xCC000000);

        // Date at top — uppercase condensed
        p.setTextSize(w * 0.055f);
        p.setAlpha(255);
        c.drawText(MONTH[mon].toUpperCase() + " " + dom, cx, h * 0.17f, p);

        // Time — extra large condensed bold
        p.setTextSize(w * 0.28f);
        p.setShadowLayer(35, 0, 8, 0xCC000000);
        c.drawText(hour24 + ":" + mm, cx, h * 0.40f, p);

        // Weather row
        float iconSz = w * 0.046f;
        float wY = h * 0.50f;
        p.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
        p.setTextSize(w * 0.038f);
        p.setAlpha(200);
        p.setShadowLayer(15, 0, 3, 0xAA000000);
        String wText = "  " + wTemp + "  " + wCond.toUpperCase();
        float wW = p.measureText(wText);
        float iLeft = cx - (iconSz * 0.6f + wW) / 2f;
        p.setTextAlign(Paint.Align.LEFT);
        canvas_drawWeatherIcon(c, iLeft, wY - iconSz * 0.55f, iconSz * 0.8f, wCode, Color.WHITE, 195);
        c.drawText(wText, iLeft + iconSz * 0.6f, wY, p);
        p.setTextAlign(Paint.Align.CENTER);
    }

    // -----------------------------------------------------------------------
    // FROSTED — glass card (CyberRain)
    // -----------------------------------------------------------------------
    private static void drawFrosted(Canvas c, int w, int h,
                                    String hh, String mm, int dow, int dom, int mon,
                                    String wTemp, String wCond, int wCode) {
        float cx = w / 2f;
        float cardW = w * 0.82f;
        float cardH = h * 0.30f;
        float cardCY = h * 0.34f;
        float r = 24f;
        RectF rect = new RectF(cx - cardW/2f, cardCY - cardH/2f, cx + cardW/2f, cardCY + cardH/2f);

        // Frosted glass card
        Paint glass = new Paint(Paint.ANTI_ALIAS_FLAG);
        glass.setColor(0x1AFFFFFF);
        c.drawRoundRect(rect, r, r, glass);
        glass.setColor(0x38FFFFFF);
        glass.setStyle(Paint.Style.STROKE);
        glass.setStrokeWidth(1f);
        c.drawRoundRect(rect, r, r, glass);

        // Time
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextAlign(Paint.Align.CENTER);
        p.setColor(Color.WHITE);
        p.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        p.setTextSize(w * 0.175f);
        p.setAlpha(255);
        p.setShadowLayer(20, 0, 4, 0xCC000000);
        c.drawText(hh + ":" + mm, cx, h * 0.348f, p);

        // Date
        p.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        p.setTextSize(w * 0.036f);
        p.setAlpha(215);
        p.setShadowLayer(12, 0, 2, 0xAA000000);
        c.drawText(DAY[dow] + ", " + MONTH[mon] + " " + dom, cx, h * 0.422f, p);

        // Weather with icon below card
        float iconSz = w * 0.043f;
        float wY = h * 0.476f;
        p.setTextSize(w * 0.030f);
        p.setAlpha(185);
        String wText = "  " + wTemp + "  " + wCond;
        float wW = p.measureText(wText);
        float iLeft = cx - (iconSz * 0.6f + wW) / 2f;
        p.setTextAlign(Paint.Align.LEFT);
        canvas_drawWeatherIcon(c, iLeft, wY - iconSz * 0.55f, iconSz * 0.8f, wCode, Color.WHITE, 180);
        c.drawText(wText, iLeft + iconSz * 0.6f, wY, p);
        p.setTextAlign(Paint.Align.CENTER);
    }

    // -----------------------------------------------------------------------
    // Weather icon drawing — canvas shapes, no emoji
    // cx,cy = center of icon bounding box, r = radius / half-size
    // -----------------------------------------------------------------------
    private static void canvas_drawWeatherIcon(Canvas c, float cx, float cy, float r,
                                               int wCode, int baseColor, int alpha) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(baseColor);
        p.setAlpha(alpha);
        p.setStrokeCap(Paint.Cap.ROUND);

        if (wCode == 0) {
            drawSun(c, cx, cy, r, p);
        } else if (wCode <= 1) {
            drawSun(c, cx, cy, r, p);
        } else if (wCode <= 2) {
            drawPartlyCloudy(c, cx, cy, r, p);
        } else if (wCode <= 3) {
            drawCloud(c, cx, cy, r, p);
        } else if (wCode <= 48) {
            drawFog(c, cx, cy, r, p);
        } else if (wCode <= 67) {
            drawRain(c, cx, cy, r, p);
        } else if (wCode <= 77) {
            drawSnow(c, cx, cy, r, p);
        } else if (wCode <= 82) {
            drawRain(c, cx, cy, r, p);
        } else if (wCode <= 86) {
            drawSnow(c, cx, cy, r, p);
        } else {
            drawStorm(c, cx, cy, r, p);
        }
    }

    private static void drawSun(Canvas c, float cx, float cy, float r, Paint p) {
        float bodyR = r * 0.38f;
        p.setStyle(Paint.Style.FILL);
        c.drawCircle(cx, cy, bodyR, p);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(r * 0.14f);
        for (int i = 0; i < 8; i++) {
            double a = i * Math.PI / 4;
            float cos = (float) Math.cos(a), sin = (float) Math.sin(a);
            c.drawLine(cx + cos * r * 0.52f, cy + sin * r * 0.52f,
                       cx + cos * r * 0.80f, cy + sin * r * 0.80f, p);
        }
    }

    private static void drawPartlyCloudy(Canvas c, float cx, float cy, float r, Paint p) {
        // Small sun behind-left
        int savedAlpha = p.getAlpha();
        p.setAlpha(savedAlpha * 2 / 3);
        p.setStyle(Paint.Style.FILL);
        c.drawCircle(cx - r * 0.28f, cy - r * 0.30f, r * 0.28f, p);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(r * 0.11f);
        for (int i = 0; i < 6; i++) {
            double a = -Math.PI / 6 + i * Math.PI / 3;
            float cos = (float) Math.cos(a), sin = (float) Math.sin(a);
            c.drawLine(cx - r*0.28f + cos * r * 0.36f, cy - r*0.30f + sin * r * 0.36f,
                       cx - r*0.28f + cos * r * 0.55f, cy - r*0.30f + sin * r * 0.55f, p);
        }
        p.setAlpha(savedAlpha);
        drawCloud(c, cx + r * 0.18f, cy + r * 0.12f, r * 0.75f, p);
    }

    private static void drawCloud(Canvas c, float cx, float cy, float r, Paint p) {
        p.setStyle(Paint.Style.FILL);
        float w2 = r * 0.95f, h2 = r * 0.55f;
        // Three overlapping circles for cloud shape
        c.drawCircle(cx - w2 * 0.30f, cy + h2 * 0.05f, h2 * 0.55f, p);
        c.drawCircle(cx + w2 * 0.10f, cy - h2 * 0.28f, h2 * 0.65f, p);
        c.drawCircle(cx + w2 * 0.45f, cy + h2 * 0.10f, h2 * 0.50f, p);
        // Fill bottom
        RectF base = new RectF(cx - w2 * 0.80f, cy - h2 * 0.10f, cx + w2 * 0.95f, cy + h2 * 0.60f);
        c.drawRoundRect(base, h2 * 0.45f, h2 * 0.45f, p);
    }

    private static void drawFog(Canvas c, float cx, float cy, float r, Paint p) {
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(r * 0.14f);
        for (int i = 0; i < 3; i++) {
            float y = cy - r * 0.30f + i * r * 0.35f;
            float xOff = (i % 2 == 0) ? 0 : r * 0.12f;
            c.drawLine(cx - r * 0.70f + xOff, y, cx + r * 0.70f - xOff, y, p);
        }
    }

    private static void drawRain(Canvas c, float cx, float cy, float r, Paint p) {
        drawCloud(c, cx, cy - r * 0.22f, r * 0.78f, p);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(r * 0.13f);
        for (int i = 0; i < 3; i++) {
            float dx = (i - 1) * r * 0.38f;
            c.drawLine(cx + dx, cy + r * 0.30f, cx + dx - r * 0.12f, cy + r * 0.70f, p);
        }
    }

    private static void drawSnow(Canvas c, float cx, float cy, float r, Paint p) {
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(r * 0.14f);
        for (int i = 0; i < 6; i++) {
            double a = i * Math.PI / 3;
            float cos = (float) Math.cos(a), sin = (float) Math.sin(a);
            c.drawLine(cx, cy, cx + cos * r * 0.70f, cy + sin * r * 0.70f, p);
            // Crossbar ticks
            for (int t = 1; t >= -1; t -= 2) {
                double a2 = a + t * Math.PI / 3;
                float m = 0.45f;
                c.drawLine(cx + cos * r * m, cy + sin * r * m,
                           cx + cos * r * m + (float) Math.cos(a2) * r * 0.20f,
                           cy + sin * r * m + (float) Math.sin(a2) * r * 0.20f, p);
            }
        }
    }

    private static void drawStorm(Canvas c, float cx, float cy, float r, Paint p) {
        drawCloud(c, cx, cy - r * 0.22f, r * 0.78f, p);
        // Lightning bolt
        p.setStyle(Paint.Style.FILL);
        Path bolt = new Path();
        bolt.moveTo(cx + r * 0.05f, cy + r * 0.22f);
        bolt.lineTo(cx - r * 0.22f, cy + r * 0.62f);
        bolt.lineTo(cx + r * 0.05f, cy + r * 0.60f);
        bolt.lineTo(cx - r * 0.10f, cy + r * 1.05f);
        bolt.lineTo(cx + r * 0.36f, cy + r * 0.48f);
        bolt.lineTo(cx + r * 0.08f, cy + r * 0.48f);
        bolt.lineTo(cx + r * 0.28f, cy + r * 0.22f);
        bolt.close();
        c.drawPath(bolt, p);
    }
}
