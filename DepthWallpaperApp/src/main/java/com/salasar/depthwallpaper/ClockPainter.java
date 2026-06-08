package com.salasar.depthwallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import java.util.Calendar;

/**
 * Draws date/time/weather overlays onto the wallpaper canvas.
 * Fonts are large, bold, and readable. A dark scrim is drawn first to
 * ensure legibility over any photo background.
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
        int hour24  = c.get(Calendar.HOUR_OF_DAY);
        int minute  = c.get(Calendar.MINUTE);
        int dow     = c.get(Calendar.DAY_OF_WEEK) - 1;
        int dom     = c.get(Calendar.DAY_OF_MONTH);
        int mon     = c.get(Calendar.MONTH);
        String mm   = minute < 10 ? "0" + minute : String.valueOf(minute);
        String hh2  = hour24 < 10 ? "0" + hour24 : String.valueOf(hour24);

        switch (style) {
            case APPLE:
                drawApple(canvas, w, h, hour24, mm, dow, dom, mon,
                          weatherTemp, weatherCondition, weatherCode);
                break;
            case HYPEROS:
                drawHyperOS(canvas, w, h, hh2, mm, dow, dom, mon,
                            weatherTemp, weatherCondition, weatherCode);
                break;
            case ARTISTIC:
                drawArtistic(canvas, w, h, hh2, mm, dow, dom, mon);
                break;
            case MINIMAL:
                drawMinimal(canvas, w, h, hh2, mm, dow, dom, mon,
                            weatherTemp, weatherCondition, weatherCode);
                break;
            case FUTURISTIC:
                drawFuturistic(canvas, w, h, hh2, mm, dow, dom, mon,
                               weatherTemp, weatherCondition, weatherCode);
                break;
            case ELEGANT:
                drawElegant(canvas, w, h, hour24, mm, dow, dom, mon,
                            weatherTemp, weatherCondition, weatherCode);
                break;
            case BRUTAL:
                drawBrutal(canvas, w, h, hour24, mm, dow, dom, mon,
                           weatherTemp, weatherCondition, weatherCode);
                break;
            case FROSTED:
                drawFrosted(canvas, w, h, hh2, mm, dow, dom, mon,
                            weatherTemp, weatherCondition, weatherCode);
                break;
        }
    }

    public static void draw(Canvas canvas, int w, int h, ClockStyle style,
                            String weatherTemp, String weatherCondition) {
        draw(canvas, w, h, style, weatherTemp, weatherCondition, 0);
    }

    // -----------------------------------------------------------------------
    // Dark gradient scrim — drawn first so text is readable over any photo
    // -----------------------------------------------------------------------
    private static void drawScrim(Canvas c, int w, int h, float topFrac, float botFrac) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new LinearGradient(0, h * topFrac, 0, h * botFrac,
            new int[]{ 0x00000000, 0x72000000, 0x88000000, 0x72000000, 0x00000000 },
            new float[]{ 0f, 0.22f, 0.50f, 0.78f, 1f },
            Shader.TileMode.CLAMP));
        c.drawRect(0, h * topFrac, w, h * botFrac, p);
    }

    // -----------------------------------------------------------------------
    // APPLE — iOS lock-screen style (large, light-weight, centered)
    // Clock area: weather h*0.24  |  time h*0.42  |  date h*0.50
    // Foreground subjects should clip at h*0.30 so they cover the top third
    // of the time digits → genuine iOS depth effect.
    // -----------------------------------------------------------------------
    private static void drawApple(Canvas c, int w, int h,
                                  int hour24, String mm, int dow, int dom, int mon,
                                  String wTemp, String wCond, int wCode) {
        drawScrim(c, w, h, 0.16f, 0.58f);

        float cx = w / 2f;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextAlign(Paint.Align.CENTER);
        p.setColor(Color.WHITE);

        // --- Weather row ---
        float iconSz = w * 0.058f;
        float wY = h * 0.245f;
        p.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        p.setTextSize(w * 0.040f);
        p.setAlpha(230);
        p.setShadowLayer(20, 0, 3, 0xFF000000);
        String wText = "  " + wTemp + "   " + wCond;
        float wW = p.measureText(wText);
        float iLeft = cx - (iconSz * 0.5f + wW) / 2f;
        p.setTextAlign(Paint.Align.LEFT);
        drawWeatherIcon(c, iLeft + iconSz * 0.5f, wY - iconSz * 0.50f, iconSz * 0.85f,
                        wCode, Color.WHITE, 225);
        c.drawText(wText, iLeft + iconSz, wY, p);
        p.setTextAlign(Paint.Align.CENTER);

        // --- Time — large, regular weight (NOT thin — user wants bolder) ---
        String timeStr = hour24 + ":" + mm;
        p.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        p.setTextSize(w * 0.28f);
        p.setAlpha(255);
        p.setShadowLayer(40, 0, 8, 0xFF000000);
        c.drawText(timeStr, cx, h * 0.425f, p);

        // --- Date ---
        String dateStr = DAY[dow] + ", " + MONTH[mon] + " " + dom;
        p.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        p.setTextSize(w * 0.052f);
        p.setAlpha(240);
        p.setShadowLayer(25, 0, 4, 0xFF000000);
        c.drawText(dateStr, cx, h * 0.503f, p);
    }

    // -----------------------------------------------------------------------
    // HYPEROS — Xiaomi HyperOS (date pill → thin line → large time → weather)
    // Clock area: date h*0.235  |  time h*0.40  |  weather h*0.475
    // Foreground subjects clip at h*0.30 → covers top of time digits
    // -----------------------------------------------------------------------
    private static void drawHyperOS(Canvas c, int w, int h,
                                    String hh, String mm, int dow, int dom, int mon,
                                    String wTemp, String wCond, int wCode) {
        drawScrim(c, w, h, 0.16f, 0.55f);

        float cx = w / 2f;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.WHITE);
        p.setTextAlign(Paint.Align.CENTER);
        p.setShadowLayer(20, 0, 3, 0xFF000000);

        // Day + date
        String dateStr = DAY_S[dow] + "   ·   " + MONTH_S[mon] + " " + dom;
        p.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        p.setTextSize(w * 0.040f);
        p.setAlpha(220);
        c.drawText(dateStr, cx, h * 0.235f, p);

        // Thin separator line
        Paint ln = new Paint(Paint.ANTI_ALIAS_FLAG);
        ln.setColor(0x60FFFFFF);
        ln.setStrokeWidth(1.2f);
        c.drawLine(cx - w * 0.30f, h * 0.268f, cx + w * 0.30f, h * 0.268f, ln);

        // Time — large
        p.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        p.setTextSize(w * 0.26f);
        p.setAlpha(255);
        p.setShadowLayer(40, 0, 8, 0xFF000000);
        c.drawText(hh + ":" + mm, cx, h * 0.405f, p);

        // Thin separator below time
        ln.setColor(0x45FFFFFF);
        c.drawLine(cx - w * 0.26f, h * 0.430f, cx + w * 0.26f, h * 0.430f, ln);

        // Weather row
        float iconSz = w * 0.052f;
        float wY = h * 0.478f;
        p.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        p.setTextSize(w * 0.038f);
        p.setAlpha(210);
        p.setShadowLayer(18, 0, 3, 0xFF000000);
        String wText = "  " + wTemp + "   " + wCond;
        float wW = p.measureText(wText);
        float iLeft = cx - (iconSz * 0.5f + wW) / 2f;
        p.setTextAlign(Paint.Align.LEFT);
        drawWeatherIcon(c, iLeft + iconSz * 0.5f, wY - iconSz * 0.50f, iconSz * 0.85f,
                        wCode, Color.WHITE, 205);
        c.drawText(wText, iLeft + iconSz, wY, p);
        p.setTextAlign(Paint.Align.CENTER);
    }

    // -----------------------------------------------------------------------
    // ARTISTIC — huge stacked hour / minute (NightForest)
    // -----------------------------------------------------------------------
    private static void drawArtistic(Canvas c, int w, int h,
                                     String hh, String mm, int dow, int dom, int mon) {
        drawScrim(c, w, h, 0.10f, 0.65f);

        float cx = w / 2f;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextAlign(Paint.Align.CENTER);
        p.setShadowLayer(30, 0, 6, 0xFF000000);

        p.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        p.setTextSize(w * 0.44f);
        p.setColor(Color.WHITE);
        p.setAlpha(255);
        c.drawText(hh, cx, h * 0.32f, p);

        p.setAlpha(240);
        c.drawText(mm, cx, h * 0.59f, p);

        p.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        p.setTextSize(w * 0.046f);
        p.setAlpha(210);
        p.setShadowLayer(18, 0, 3, 0xFF000000);
        c.drawText(DAY[dow].toUpperCase() + "   ·   " + MONTH_S[mon] + " " + dom,
                   cx, h * 0.695f, p);
    }

    // -----------------------------------------------------------------------
    // MINIMAL — right-aligned compact (GlacierPeak)
    // -----------------------------------------------------------------------
    private static void drawMinimal(Canvas c, int w, int h,
                                    String hh, String mm, int dow, int dom, int mon,
                                    String wTemp, String wCond, int wCode) {
        float rx = w * 0.90f;
        float ty = h * 0.37f;

        // Subtle right-side scrim
        Paint scrim = new Paint(Paint.ANTI_ALIAS_FLAG);
        scrim.setShader(new LinearGradient(w * 0.55f, 0, w, 0,
            0x00000000, 0x66000000, Shader.TileMode.CLAMP));
        c.drawRect(w * 0.55f, h * 0.20f, w, h * 0.55f, scrim);

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextAlign(Paint.Align.RIGHT);
        p.setColor(Color.WHITE);
        p.setShadowLayer(25, 0, 4, 0xFF000000);

        // Time
        p.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        p.setTextSize(w * 0.095f);
        p.setAlpha(255);
        c.drawText(hh + ":" + mm, rx, ty, p);

        // Date
        p.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        p.setTextSize(w * 0.032f);
        p.setAlpha(210);
        c.drawText(DAY_S[dow] + "  " + (dom < 10 ? "0"+dom : ""+dom) + "  " + MONTH_S[mon],
                   rx, ty + w * 0.048f, p);

        // Weather
        float iconSz = w * 0.038f;
        float wY = ty + w * 0.088f;
        p.setTextSize(w * 0.028f);
        p.setAlpha(190);
        String wText = wTemp + "  " + wCond + "   ";
        float wW = p.measureText(wText);
        c.drawText(wText, rx, wY, p);
        drawWeatherIcon(c, rx - wW + iconSz * 0.4f, wY - iconSz * 0.55f, iconSz * 0.8f,
                        wCode, Color.WHITE, 185);
    }

    // -----------------------------------------------------------------------
    // FUTURISTIC — monospace neon (CosmicRift)
    // -----------------------------------------------------------------------
    private static void drawFuturistic(Canvas c, int w, int h,
                                       String hh, String mm, int dow, int dom, int mon,
                                       String wTemp, String wCond, int wCode) {
        drawScrim(c, w, h, 0.16f, 0.56f);

        float cx = w / 2f;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextAlign(Paint.Align.CENTER);
        p.setTypeface(Typeface.MONOSPACE);

        Paint ln = new Paint(Paint.ANTI_ALIAS_FLAG);
        ln.setColor(0x7000FFCC);
        ln.setStrokeWidth(1.2f);
        c.drawLine(w * 0.10f, h * 0.225f, w * 0.90f, h * 0.225f, ln);

        // Time — neon cyan glow
        p.setTextSize(w * 0.175f);
        p.setColor(0xFFCCFFEE);
        p.setAlpha(255);
        p.setShadowLayer(30, 0, 0, 0xCC00FFCC);
        c.drawText(hh + ":" + mm, cx, h * 0.390f, p);

        c.drawLine(w * 0.10f, h * 0.425f, w * 0.90f, h * 0.425f, ln);

        // Date
        p.setTypeface(Typeface.MONOSPACE);
        p.setTextSize(w * 0.036f);
        p.setColor(0xCCAAEEDD);
        p.setAlpha(210);
        p.setShadowLayer(15, 0, 3, 0xFF000000);
        c.drawText(String.format("%02d.%02d  //  %s", (mon+1), dom, DAY_S[dow]),
                   cx, h * 0.465f, p);

        // Weather with icon
        float iconSz = w * 0.048f;
        float wY = h * 0.510f;
        p.setTextSize(w * 0.030f);
        p.setColor(0xAA99DDCC);
        p.setAlpha(190);
        String wText = "  " + wTemp + "   " + wCond;
        float wW = p.measureText(wText);
        float iLeft = cx - (iconSz * 0.5f + wW) / 2f;
        p.setTextAlign(Paint.Align.LEFT);
        drawWeatherIcon(c, iLeft + iconSz * 0.5f, wY - iconSz * 0.50f, iconSz * 0.8f,
                        wCode, 0xCCAAEEDD, 190);
        c.drawText(wText, iLeft + iconSz, wY, p);
        p.setTextAlign(Paint.Align.CENTER);
    }

    // -----------------------------------------------------------------------
    // ELEGANT — serif warm cream (OceanSurge)
    // -----------------------------------------------------------------------
    private static void drawElegant(Canvas c, int w, int h,
                                    int hour24, String mm, int dow, int dom, int mon,
                                    String wTemp, String wCond, int wCode) {
        drawScrim(c, w, h, 0.14f, 0.56f);

        float cx = w / 2f;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextAlign(Paint.Align.CENTER);
        p.setColor(0xFFFFEEDD);
        p.setShadowLayer(28, 0, 5, 0xFF000000);

        // Weather row
        float iconSz = w * 0.052f;
        float wY = h * 0.235f;
        p.setTypeface(Typeface.create("serif", Typeface.ITALIC));
        p.setTextSize(w * 0.038f);
        p.setAlpha(220);
        String wText = "  " + wTemp + "   " + wCond;
        float wW = p.measureText(wText);
        float iLeft = cx - (iconSz * 0.5f + wW) / 2f;
        p.setTextAlign(Paint.Align.LEFT);
        drawWeatherIcon(c, iLeft + iconSz * 0.5f, wY - iconSz * 0.50f, iconSz * 0.85f,
                        wCode, 0xFFEEDD, 210);
        c.drawText(wText, iLeft + iconSz, wY, p);
        p.setTextAlign(Paint.Align.CENTER);

        // Time
        p.setTypeface(Typeface.create("serif", Typeface.NORMAL));
        p.setTextSize(w * 0.25f);
        p.setAlpha(255);
        p.setShadowLayer(40, 0, 8, 0xFF000000);
        c.drawText(hour24 + ":" + mm, cx, h * 0.415f, p);

        // Date
        p.setTypeface(Typeface.create("serif", Typeface.ITALIC));
        p.setTextSize(w * 0.048f);
        p.setAlpha(230);
        p.setShadowLayer(22, 0, 4, 0xFF000000);
        c.drawText(DAY[dow] + ", " + MONTH[mon] + " " + dom, cx, h * 0.494f, p);
    }

    // -----------------------------------------------------------------------
    // BRUTAL — condensed very large (WhiteTiger, RedCanyon)
    // -----------------------------------------------------------------------
    private static void drawBrutal(Canvas c, int w, int h,
                                   int hour24, String mm, int dow, int dom, int mon,
                                   String wTemp, String wCond, int wCode) {
        drawScrim(c, w, h, 0.10f, 0.56f);

        float cx = w / 2f;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextAlign(Paint.Align.CENTER);
        p.setColor(Color.WHITE);
        p.setTypeface(Typeface.create("sans-serif-condensed", Typeface.BOLD));
        p.setShadowLayer(30, 0, 6, 0xFF000000);

        // Date at top
        p.setTextSize(w * 0.062f);
        p.setAlpha(255);
        c.drawText(MONTH[mon].toUpperCase() + " " + dom, cx, h * 0.185f, p);

        // Time — very large condensed bold
        p.setTextSize(w * 0.30f);
        p.setShadowLayer(40, 0, 10, 0xFF000000);
        c.drawText(hour24 + ":" + mm, cx, h * 0.415f, p);

        // Weather row
        float iconSz = w * 0.052f;
        float wY = h * 0.508f;
        p.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
        p.setTextSize(w * 0.044f);
        p.setAlpha(230);
        p.setShadowLayer(18, 0, 3, 0xFF000000);
        String wText = "  " + wTemp + "   " + wCond.toUpperCase();
        float wW = p.measureText(wText);
        float iLeft = cx - (iconSz * 0.5f + wW) / 2f;
        p.setTextAlign(Paint.Align.LEFT);
        drawWeatherIcon(c, iLeft + iconSz * 0.5f, wY - iconSz * 0.50f, iconSz * 0.85f,
                        wCode, Color.WHITE, 225);
        c.drawText(wText, iLeft + iconSz, wY, p);
        p.setTextAlign(Paint.Align.CENTER);
    }

    // -----------------------------------------------------------------------
    // FROSTED — glass card (CyberRain)
    // -----------------------------------------------------------------------
    private static void drawFrosted(Canvas c, int w, int h,
                                    String hh, String mm, int dow, int dom, int mon,
                                    String wTemp, String wCond, int wCode) {
        float cx = w / 2f;
        float cardW = w * 0.85f;
        float cardH = h * 0.32f;
        float cardCY = h * 0.36f;
        float r = 28f;
        RectF rect = new RectF(cx - cardW/2f, cardCY - cardH/2f,
                               cx + cardW/2f, cardCY + cardH/2f);

        Paint glass = new Paint(Paint.ANTI_ALIAS_FLAG);
        glass.setColor(0x22FFFFFF);
        c.drawRoundRect(rect, r, r, glass);
        glass.setColor(0x44FFFFFF);
        glass.setStyle(Paint.Style.STROKE);
        glass.setStrokeWidth(1.5f);
        c.drawRoundRect(rect, r, r, glass);

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextAlign(Paint.Align.CENTER);
        p.setColor(Color.WHITE);
        p.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        p.setTextSize(w * 0.20f);
        p.setAlpha(255);
        p.setShadowLayer(25, 0, 5, 0xFF000000);
        c.drawText(hh + ":" + mm, cx, h * 0.368f, p);

        p.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        p.setTextSize(w * 0.042f);
        p.setAlpha(235);
        p.setShadowLayer(15, 0, 3, 0xFF000000);
        c.drawText(DAY[dow] + ", " + MONTH[mon] + " " + dom, cx, h * 0.440f, p);

        // Weather below card
        float iconSz = w * 0.048f;
        float wY = h * 0.492f;
        p.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        p.setTextSize(w * 0.034f);
        p.setAlpha(215);
        String wText = "  " + wTemp + "   " + wCond;
        float wW = p.measureText(wText);
        float iLeft = cx - (iconSz * 0.5f + wW) / 2f;
        p.setTextAlign(Paint.Align.LEFT);
        drawWeatherIcon(c, iLeft + iconSz * 0.5f, wY - iconSz * 0.50f, iconSz * 0.8f,
                        wCode, Color.WHITE, 210);
        c.drawText(wText, iLeft + iconSz, wY, p);
        p.setTextAlign(Paint.Align.CENTER);
    }

    // -----------------------------------------------------------------------
    // Weather icon drawing — pure Canvas geometry, no emoji
    // -----------------------------------------------------------------------
    private static void drawWeatherIcon(Canvas c, float cx, float cy, float r,
                                        int wCode, int baseColor, int alpha) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(baseColor);
        p.setAlpha(alpha);
        p.setStrokeCap(Paint.Cap.ROUND);

        if (wCode == 0 || wCode == 1)        { drawSun(c, cx, cy, r, p); }
        else if (wCode == 2)                  { drawPartlyCloudy(c, cx, cy, r, p); }
        else if (wCode == 3)                  { drawCloud(c, cx, cy, r, p); }
        else if (wCode <= 48)                 { drawFog(c, cx, cy, r, p); }
        else if (wCode <= 67)                 { drawRain(c, cx, cy, r, p); }
        else if (wCode <= 77)                 { drawSnow(c, cx, cy, r, p); }
        else if (wCode <= 82)                 { drawRain(c, cx, cy, r, p); }
        else if (wCode <= 86)                 { drawSnow(c, cx, cy, r, p); }
        else                                  { drawStorm(c, cx, cy, r, p); }
    }

    private static void drawSun(Canvas c, float cx, float cy, float r, Paint p) {
        p.setStyle(Paint.Style.FILL);
        c.drawCircle(cx, cy, r * 0.38f, p);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(r * 0.15f);
        for (int i = 0; i < 8; i++) {
            double a = i * Math.PI / 4;
            float cos = (float) Math.cos(a), sin = (float) Math.sin(a);
            c.drawLine(cx + cos*r*0.53f, cy + sin*r*0.53f,
                       cx + cos*r*0.82f, cy + sin*r*0.82f, p);
        }
    }

    private static void drawPartlyCloudy(Canvas c, float cx, float cy, float r, Paint p) {
        int saved = p.getAlpha();
        p.setAlpha(saved * 2 / 3);
        p.setStyle(Paint.Style.FILL);
        c.drawCircle(cx - r*0.28f, cy - r*0.28f, r*0.28f, p);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(r * 0.12f);
        for (int i = 0; i < 5; i++) {
            double a = -Math.PI/4 + i * Math.PI/4;
            float cos = (float) Math.cos(a), sin = (float) Math.sin(a);
            c.drawLine(cx - r*0.28f + cos*r*0.36f, cy - r*0.28f + sin*r*0.36f,
                       cx - r*0.28f + cos*r*0.55f, cy - r*0.28f + sin*r*0.55f, p);
        }
        p.setAlpha(saved);
        drawCloud(c, cx + r*0.16f, cy + r*0.10f, r*0.76f, p);
    }

    private static void drawCloud(Canvas c, float cx, float cy, float r, Paint p) {
        p.setStyle(Paint.Style.FILL);
        float rw = r * 0.92f, rh = r * 0.52f;
        c.drawCircle(cx - rw*0.30f, cy + rh*0.05f, rh*0.52f, p);
        c.drawCircle(cx + rw*0.08f, cy - rh*0.28f, rh*0.62f, p);
        c.drawCircle(cx + rw*0.44f, cy + rh*0.10f, rh*0.48f, p);
        c.drawRoundRect(new RectF(cx - rw*0.78f, cy - rh*0.08f,
                                  cx + rw*0.93f, cy + rh*0.58f),
                        rh*0.44f, rh*0.44f, p);
    }

    private static void drawFog(Canvas c, float cx, float cy, float r, Paint p) {
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(r * 0.16f);
        for (int i = 0; i < 3; i++) {
            float y = cy - r*0.28f + i * r*0.34f;
            float xo = (i % 2 == 0) ? 0 : r*0.10f;
            c.drawLine(cx - r*0.68f + xo, y, cx + r*0.68f - xo, y, p);
        }
    }

    private static void drawRain(Canvas c, float cx, float cy, float r, Paint p) {
        drawCloud(c, cx, cy - r*0.22f, r*0.76f, p);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(r * 0.14f);
        for (int i = 0; i < 3; i++) {
            float dx = (i - 1) * r*0.36f;
            c.drawLine(cx + dx, cy + r*0.30f, cx + dx - r*0.12f, cy + r*0.68f, p);
        }
    }

    private static void drawSnow(Canvas c, float cx, float cy, float r, Paint p) {
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(r * 0.15f);
        for (int i = 0; i < 6; i++) {
            double a = i * Math.PI / 3;
            float cos = (float) Math.cos(a), sin = (float) Math.sin(a);
            c.drawLine(cx, cy, cx + cos*r*0.70f, cy + sin*r*0.70f, p);
            for (int t = -1; t <= 1; t += 2) {
                double a2 = a + t * Math.PI/3;
                float m = 0.42f;
                c.drawLine(cx + cos*r*m, cy + sin*r*m,
                           cx + cos*r*m + (float)Math.cos(a2)*r*0.20f,
                           cy + sin*r*m + (float)Math.sin(a2)*r*0.20f, p);
            }
        }
    }

    private static void drawStorm(Canvas c, float cx, float cy, float r, Paint p) {
        drawCloud(c, cx, cy - r*0.22f, r*0.76f, p);
        p.setStyle(Paint.Style.FILL);
        Path bolt = new Path();
        bolt.moveTo(cx + r*0.05f, cy + r*0.22f);
        bolt.lineTo(cx - r*0.22f, cy + r*0.62f);
        bolt.lineTo(cx + r*0.05f, cy + r*0.60f);
        bolt.lineTo(cx - r*0.10f, cy + r*1.05f);
        bolt.lineTo(cx + r*0.36f, cy + r*0.48f);
        bolt.lineTo(cx + r*0.08f, cy + r*0.48f);
        bolt.lineTo(cx + r*0.28f, cy + r*0.22f);
        bolt.close();
        c.drawPath(bolt, p);
    }
}
