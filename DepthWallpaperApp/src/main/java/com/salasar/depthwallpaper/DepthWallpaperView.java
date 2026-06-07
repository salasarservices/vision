package com.salasar.depthwallpaper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.View;

public class DepthWallpaperView extends View implements SensorEventListener {
    private WallpaperScene scene;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    // Smoothed tilt values, range approx -1..1
    private float tiltX = 0f;
    private float tiltY = 0f;
    private static final float SMOOTH = 0.08f;
    private static final float MAX_TILT = 6f; // degrees of acceleration to full offset

    // Per-layer parallax multipliers (0=sky, 4=foreground)
    private static final float[] PARALLAX = {0.05f, 0.15f, 0.30f, 0.50f, 0.80f};

    public DepthWallpaperView(Context ctx) { super(ctx); }
    public DepthWallpaperView(Context ctx, AttributeSet attrs) { super(ctx, attrs); }

    public void setScene(WallpaperScene s) {
        this.scene = s;
        invalidate();
    }

    public void startSensor(Context ctx) {
        sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometer != null) {
                sensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_GAME);
            }
        }
    }

    public void stopSensor() {
        if (sensorManager != null) sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;
        // event.values: [x, y, z] in m/s². Gravity contributes ~9.8 along z.
        // x: tilt left/right, y: tilt forward/back
        float rawX = -event.values[0] / MAX_TILT;  // negative: tilt right → move right
        float rawY = (event.values[1] - 5f) / MAX_TILT; // offset for natural held angle
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
        for (int i = 0; i < layers; i++) {
            float offX = tiltX * PARALLAX[i] * WallpaperScene.MAX_PARALLAX;
            float offY = tiltY * PARALLAX[i] * WallpaperScene.MAX_PARALLAX;
            canvas.save();
            canvas.translate(offX, offY);
            scene.drawLayer(canvas, i, w, h);
            canvas.restore();
        }
    }
}
