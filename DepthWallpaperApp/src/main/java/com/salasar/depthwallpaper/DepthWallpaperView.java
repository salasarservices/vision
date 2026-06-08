package com.salasar.depthwallpaper;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class DepthWallpaperView extends View implements SensorEventListener {

    private WallpaperScene scene;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Handler clockHandler;
    private Runnable clockTick;

    private float tiltX = 0f;
    private float tiltY = 0f;
    private static final float SMOOTH = 0.08f;
    private static final float MAX_TILT = 6f;

    // Layer parallax multipliers: 0=far background (barely moves), higher=closer (moves a lot)
    // With MAX_PARALLAX=130: layer0 shifts 2px, layer2 shifts 59px, layer4 shifts 130px
    private static final float[] PARALLAX = {0.02f, 0.08f, 0.45f, 0.75f, 1.0f};

    private String weatherTemp = "--";
    private String weatherCondition = "Clear";
    private int weatherCode = 0;

    public DepthWallpaperView(Context ctx) { super(ctx); }
    public DepthWallpaperView(Context ctx, AttributeSet attrs) { super(ctx, attrs); }

    public void setScene(WallpaperScene s) {
        this.scene = s;
        s.init(getResources());
        invalidate();
    }

    public void setWeather(String temp, String condition, int code) {
        this.weatherTemp = temp;
        this.weatherCondition = condition;
        this.weatherCode = code;
        invalidate();
    }

    public void startSensor(Context ctx) {
        sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometer != null) {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
            }
        }
        clockHandler = new Handler();
        clockTick = new Runnable() {
            @Override public void run() {
                postInvalidateOnAnimation();
                clockHandler.postDelayed(this, 30000L);
            }
        };
        clockHandler.postDelayed(clockTick, 30000L);
    }

    public void stopSensor() {
        if (sensorManager != null) sensorManager.unregisterListener(this);
        if (clockHandler != null && clockTick != null) clockHandler.removeCallbacks(clockTick);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;
        float rawX = -event.values[0] / MAX_TILT;
        float rawY = (event.values[1] - 5f) / MAX_TILT;
        rawX = Math.max(-1f, Math.min(1f, rawX));
        rawY = Math.max(-1f, Math.min(1f, rawY));
        tiltX = tiltX + (rawX - tiltX) * SMOOTH;
        tiltY = tiltY + (rawY - tiltY) * SMOOTH;
        postInvalidateOnAnimation();
    }

    @Override public void onAccuracyChanged(Sensor s, int accuracy) {}

    @Override
    protected void onDraw(Canvas canvas) {
        if (scene == null) return;
        int w = getWidth(), h = getHeight();
        int layers = Math.min(scene.getLayerCount(), PARALLAX.length);
        int insertAfter = scene.getClockInsertAfterLayer();

        for (int i = 0; i < layers; i++) {
            float offX = tiltX * PARALLAX[i] * WallpaperScene.MAX_PARALLAX;
            float offY = tiltY * PARALLAX[i] * WallpaperScene.MAX_PARALLAX;
            canvas.save();
            canvas.translate(offX, offY);
            scene.drawLayer(canvas, i, w, h);
            canvas.restore();

            if (i == insertAfter) {
                // Clock drawn at a fixed (no parallax) position between depth layers
                ClockPainter.draw(canvas, w, h, scene.getClockStyle(),
                        weatherTemp, weatherCondition, weatherCode);
            }
        }

        if (insertAfter >= layers) {
            ClockPainter.draw(canvas, w, h, scene.getClockStyle(),
                    weatherTemp, weatherCondition, weatherCode);
        }
    }
}
