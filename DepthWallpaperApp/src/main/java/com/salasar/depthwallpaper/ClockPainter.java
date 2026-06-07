package com.salasar.depthwallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import java.util.Calendar;

public class ClockPainter {

    private static final String[] MONTH_NAMES = {
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    };

    private static final String[] DAY_NAMES = {
        "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    };

    private static final String[] DAY_SHORT = {
        "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"
    };

    private static final String[] MONTH_SHORT = {
        "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
        "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
    };

    public static void draw(Canvas canvas, int w, int h,
                            ClockStyle style, String weatherTemp, String weatherCondition) {
        Calendar cal = Calendar.getInstance();
        int hour24 = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1; // 0=Sunday
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH); // 0-based

        String minuteStr = (minute < 10) ? "0" + minute : "" + minute;
        String hourStr24 = (hour24 < 10) ? "0" + hour24 : "" + hour24;

        String weatherLine = weatherTemp + "° " + weatherCondition;

        switch (style) {
            case APPLE:
                drawApple(canvas, w, h, hour24, minuteStr, dayOfWeek, dayOfMonth, month, weatherLine);
                break;
            case HYPEROS:
                drawHyperOS(canvas, w, h, hourStr24, minuteStr, dayOfWeek, dayOfMonth, month, weatherLine, weatherTemp, weatherCondition);
                break;
            case ARTISTIC:
                drawArtistic(canvas, w, h, hour24, minuteStr, dayOfWeek);
                break;
            case MINIMAL:
                drawMinimal(canvas, w, h, hourStr24, minuteStr, dayOfWeek, dayOfMonth, month, weatherLine);
                break;
            case FUTURISTIC:
                drawFuturistic(canvas, w, h, hourStr24, minuteStr, dayOfWeek, dayOfMonth, month, weatherLine);
                break;
            case ELEGANT:
                drawElegant(canvas, w, h, hour24, minuteStr, dayOfWeek, dayOfMonth, month, weatherLine);
                break;
            case BRUTAL:
                drawBrutal(canvas, w, h, hour24, minuteStr, dayOfWeek, dayOfMonth, month, weatherLine);
                break;
            case FROSTED:
                drawFrosted(canvas, w, h, hourStr24, minuteStr, dayOfWeek, dayOfMonth, month, weatherLine);
                break;
        }
    }

    // -----------------------------------------------------------------------
    // APPLE style
    // -----------------------------------------------------------------------
    private static void drawApple(Canvas canvas, int w, int h,
                                  int hour24, String minuteStr,
                                  int dayOfWeek, int dayOfMonth, int month,
                                  String weatherLine) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);
        float cx = w / 2f;

        // Weather
        paint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        paint.setTextSize(w * 0.038f);
        paint.setColor(Color.WHITE);
        paint.setAlpha(200);
        paint.setShadowLayer(15, 0, 4, 0x88000000);
        canvas.drawText(weatherLine, cx, h * 0.14f, paint);

        // Time — no leading zero
        String timeStr = hour24 + ":" + minuteStr;
        paint.setTypeface(Typeface.create("sans-serif-black", Typeface.NORMAL));
        paint.setTextSize(w * 0.22f);
        paint.setColor(Color.WHITE);
        paint.setAlpha(255);
        paint.setShadowLayer(20, 0, 6, 0x88000000);
        canvas.drawText(timeStr, cx, h * 0.36f, paint);

        // Date
        String dateStr = DAY_NAMES[dayOfWeek] + ", " + MONTH_NAMES[month] + " " + dayOfMonth;
        paint.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
        paint.setTextSize(w * 0.045f);
        paint.setColor(Color.WHITE);
        paint.setAlpha(220);
        paint.setShadowLayer(15, 0, 4, 0x88000000);
        canvas.drawText(dateStr, cx, h * 0.44f, paint);
    }

    // -----------------------------------------------------------------------
    // HYPEROS style (Xiaomi stacked layout)
    // -----------------------------------------------------------------------
    private static void drawHyperOS(Canvas canvas, int w, int h,
                                    String hourStr, String minuteStr,
                                    int dayOfWeek, int dayOfMonth, int month,
                                    String weatherLine, String weatherTemp, String weatherCondition) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);
        float cx = w / 2f;

        // Date at top
        String dateStr = dayOfMonth + "/" + (month + 1);
        paint.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        paint.setTextSize(w * 0.04f);
        paint.setColor(Color.WHITE);
        paint.setAlpha(180);
        paint.setShadowLayer(15, 0, 4, 0x88000000);
        canvas.drawText(dateStr, cx, h * 0.22f, paint);

        // Time large
        String timeStr = hourStr + ":" + minuteStr;
        paint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        paint.setTextSize(w * 0.19f);
        paint.setColor(Color.WHITE);
        paint.setAlpha(255);
        paint.setShadowLayer(20, 0, 6, 0x88000000);
        canvas.drawText(timeStr, cx, h * 0.32f, paint);

        // Weather below time
        paint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        paint.setTextSize(w * 0.04f);
        paint.setColor(Color.WHITE);
        paint.setAlpha(160);
        paint.setShadowLayer(15, 0, 4, 0x88000000);
        canvas.drawText(weatherLine, cx, h * 0.43f, paint);
    }

    // -----------------------------------------------------------------------
    // ARTISTIC style (huge numbers, tree-inspired)
    // -----------------------------------------------------------------------
    private static void drawArtistic(Canvas canvas, int w, int h,
                                     int hour24, String minuteStr, int dayOfWeek) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setShadowLayer(15, 0, 4, 0x88000000);
        float cx = w / 2f;

        // Hour digits huge
        String hourStr = (hour24 < 10) ? "0" + hour24 : "" + hour24;
        paint.setTextSize(w * 0.40f);
        paint.setColor(Color.WHITE);
        paint.setAlpha(255);
        canvas.drawText(hourStr, cx, h * 0.30f, paint);

        // Minute digits huge
        paint.setTextSize(w * 0.40f);
        paint.setColor(Color.WHITE);
        paint.setAlpha(220);
        canvas.drawText(minuteStr, cx, h * 0.55f, paint);

        // Day name
        paint.setTypeface(Typeface.create("sans-serif-condensed", Typeface.BOLD));
        paint.setTextSize(w * 0.14f);
        paint.setColor(Color.WHITE);
        paint.setAlpha(200);
        paint.setShadowLayer(15, 0, 4, 0x88000000);
        canvas.drawText(DAY_NAMES[dayOfWeek].toUpperCase(), cx, h * 0.70f, paint);
    }

    // -----------------------------------------------------------------------
    // MINIMAL style (right-aligned, small)
    // -----------------------------------------------------------------------
    private static void drawMinimal(Canvas canvas, int w, int h,
                                    String hourStr, String minuteStr,
                                    int dayOfWeek, int dayOfMonth, int month,
                                    String weatherLine) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.RIGHT);
        float rx = w * 0.90f;
        paint.setShadowLayer(15, 0, 4, 0x88000000);

        // Time
        String timeStr = hourStr + ":" + minuteStr;
        paint.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
        paint.setTextSize(w * 0.08f);
        paint.setColor(Color.WHITE);
        paint.setAlpha(255);
        canvas.drawText(timeStr, rx, h * 0.36f, paint);

        // Date: "MON 07 JUN"
        String dateStr = DAY_SHORT[dayOfWeek] + " "
                + (dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth)
                + " " + MONTH_SHORT[month];
        paint.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
        paint.setTextSize(w * 0.025f);
        paint.setColor(Color.WHITE);
        paint.setAlpha(160);
        canvas.drawText(dateStr, rx, h * 0.42f, paint);

        // Weather
        paint.setTextSize(w * 0.022f);
        paint.setAlpha(140);
        canvas.drawText(weatherLine, rx, h * 0.47f, paint);
    }

    // -----------------------------------------------------------------------
    // FUTURISTIC style (monospace, neon)
    // -----------------------------------------------------------------------
    private static void drawFuturistic(Canvas canvas, int w, int h,
                                       String hourStr, String minuteStr,
                                       int dayOfWeek, int dayOfMonth, int month,
                                       String weatherLine) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setShadowLayer(15, 0, 4, 0x88000000);
        float cx = w / 2f;

        // Horizontal separator line
        Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(0x88FFFFFF);
        linePaint.setStrokeWidth(2f);
        canvas.drawLine(w * 0.15f, h * 0.22f, w * 0.85f, h * 0.22f, linePaint);

        // Time — neon cyan
        String timeStr = hourStr + ":" + minuteStr;
        paint.setTextSize(w * 0.14f);
        paint.setColor(0xFF00FFCC);
        paint.setAlpha(255);
        paint.setShadowLayer(18, 0, 0, 0xCC00FFCC);
        canvas.drawText(timeStr, cx, h * 0.36f, paint);

        // Date: "06.07 // MON"
        String dateStr = String.format("%02d.%02d // %s", (month + 1), dayOfMonth, DAY_SHORT[dayOfWeek]);
        paint.setTextSize(w * 0.033f);
        paint.setColor(0xAAFFFFFF);
        paint.setAlpha(170);
        paint.setShadowLayer(15, 0, 4, 0x88000000);
        canvas.drawText(dateStr, cx, h * 0.44f, paint);

        // Weather
        paint.setTextSize(w * 0.025f);
        paint.setColor(0xAAFFFFFF);
        paint.setAlpha(130);
        canvas.drawText(weatherLine, cx, h * 0.49f, paint);
    }

    // -----------------------------------------------------------------------
    // ELEGANT style (serif, warm cream)
    // -----------------------------------------------------------------------
    private static void drawElegant(Canvas canvas, int w, int h,
                                    int hour24, String minuteStr,
                                    int dayOfWeek, int dayOfMonth, int month,
                                    String weatherLine) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setShadowLayer(15, 0, 4, 0x88000000);
        float cx = w / 2f;

        // Weather
        paint.setTypeface(Typeface.create("serif", Typeface.ITALIC));
        paint.setTextSize(w * 0.030f);
        paint.setColor(0xAAFFEEDD);
        paint.setAlpha(170);
        canvas.drawText(weatherLine, cx, h * 0.14f, paint);

        // Time
        String timeStr = hour24 + ":" + minuteStr;
        paint.setTypeface(Typeface.create("serif", Typeface.NORMAL));
        paint.setTextSize(w * 0.20f);
        paint.setColor(0xFFFFEEDD);
        paint.setAlpha(255);
        paint.setShadowLayer(20, 0, 6, 0x88000000);
        canvas.drawText(timeStr, cx, h * 0.36f, paint);

        // Date
        String dateStr = DAY_NAMES[dayOfWeek] + ", " + MONTH_NAMES[month] + " " + dayOfMonth;
        paint.setTypeface(Typeface.create("serif", Typeface.ITALIC));
        paint.setTextSize(w * 0.038f);
        paint.setColor(0xCCFFEEDD);
        paint.setAlpha(200);
        paint.setShadowLayer(15, 0, 4, 0x88000000);
        canvas.drawText(dateStr, cx, h * 0.44f, paint);
    }

    // -----------------------------------------------------------------------
    // BRUTAL style (condensed, very large)
    // -----------------------------------------------------------------------
    private static void drawBrutal(Canvas canvas, int w, int h,
                                   int hour24, String minuteStr,
                                   int dayOfWeek, int dayOfMonth, int month,
                                   String weatherLine) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.create("sans-serif-condensed", Typeface.BOLD));
        paint.setShadowLayer(15, 0, 4, 0x88000000);
        float cx = w / 2f;

        // Date uppercase at top
        String dateStr = MONTH_NAMES[month].toUpperCase() + " " + dayOfMonth;
        paint.setTextSize(w * 0.06f);
        paint.setColor(Color.WHITE);
        paint.setAlpha(255);
        canvas.drawText(dateStr, cx, h * 0.17f, paint);

        // Time very large
        String timeStr = hour24 + ":" + minuteStr;
        paint.setTextSize(w * 0.28f);
        paint.setColor(Color.WHITE);
        paint.setAlpha(255);
        paint.setShadowLayer(20, 0, 8, 0x88000000);
        canvas.drawText(timeStr, cx, h * 0.40f, paint);

        // Weather
        paint.setTextSize(w * 0.04f);
        paint.setColor(Color.WHITE);
        paint.setAlpha(180);
        paint.setShadowLayer(15, 0, 4, 0x88000000);
        canvas.drawText(weatherLine, cx, h * 0.50f, paint);
    }

    // -----------------------------------------------------------------------
    // FROSTED style (glass card behind clock)
    // -----------------------------------------------------------------------
    private static void drawFrosted(Canvas canvas, int w, int h,
                                    String hourStr, String minuteStr,
                                    int dayOfWeek, int dayOfMonth, int month,
                                    String weatherLine) {
        float cx = w / 2f;
        float cardW = w * 0.80f;
        float cardH = h * 0.28f;
        float cardCY = h * 0.33f;
        float left = cx - cardW / 2f;
        float top = cardCY - cardH / 2f;
        float right = cx + cardW / 2f;
        float bottom = cardCY + cardH / 2f;
        float cornerR = 20f;

        // Frosted glass fill
        Paint glassFill = new Paint(Paint.ANTI_ALIAS_FLAG);
        glassFill.setColor(0x19FFFFFF);
        glassFill.setStyle(Paint.Style.FILL);
        RectF rect = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(rect, cornerR, cornerR, glassFill);

        // Frosted glass stroke
        Paint glassStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        glassStroke.setColor(0x3CFFFFFF);
        glassStroke.setStyle(Paint.Style.STROKE);
        glassStroke.setStrokeWidth(1.5f);
        canvas.drawRoundRect(rect, cornerR, cornerR, glassStroke);

        // Time inside card
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setShadowLayer(15, 0, 4, 0x88000000);

        String timeStr = hourStr + ":" + minuteStr;
        paint.setTextSize(w * 0.18f);
        paint.setColor(Color.WHITE);
        paint.setAlpha(255);
        canvas.drawText(timeStr, cx, h * 0.34f, paint);

        // Date
        String dateStr = DAY_NAMES[dayOfWeek] + ", " + MONTH_NAMES[month] + " " + dayOfMonth;
        paint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        paint.setTextSize(w * 0.038f);
        paint.setColor(Color.WHITE);
        paint.setAlpha(200);
        canvas.drawText(dateStr, cx, h * 0.43f, paint);

        // Weather line
        paint.setTextSize(w * 0.030f);
        paint.setAlpha(170);
        canvas.drawText(weatherLine, cx, h * 0.48f, paint);
    }
}
