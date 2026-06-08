package com.salasar.depthwallpaper;

import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class DepthWallpaperService extends WallpaperService {

    static final String PREFS = "depth_wp";
    static final String KEY_SCENE = "scene_index";
    private static final float[] PARALLAX = {0.02f, 0.08f, 0.45f, 0.75f, 1.0f};

    @Override
    public Engine onCreateEngine() {
        return new DepthEngine();
    }

    private class DepthEngine extends Engine implements SensorEventListener {

        private SensorManager sensorManager;
        private Sensor accelerometer;
        private final Handler handler = new Handler();

        private WallpaperScene scene;
        private float tiltX = 0f;
        private float tiltY = 0f;
        private boolean engineVisible = false;

        private static final float SMOOTH = 0.08f;
        private static final float MAX_TILT = 6f;

        private final Runnable drawRunner = new Runnable() {
            @Override public void run() { drawFrame(); }
        };

        // Redraws every 30 s so the clock minute stays current even without motion.
        private final Runnable clockTick = new Runnable() {
            @Override public void run() {
                if (engineVisible) {
                    drawFrame();
                    handler.postDelayed(this, 30000L);
                }
            }
        };

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            loadScene();
            setTouchEventsEnabled(false);
        }

        private void loadScene() {
            int idx = getSharedPreferences(PREFS, 0).getInt(KEY_SCENE, 0);
            scene = SceneRegistry.get(idx);
            scene.init(getResources());
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            engineVisible = visible;
            if (visible) {
                // Reload scene in case the user changed selection while screen was off.
                loadScene();
                sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                if (sensorManager != null) {
                    accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                    if (accelerometer != null) {
                        sensorManager.registerListener(this, accelerometer,
                                SensorManager.SENSOR_DELAY_GAME);
                    }
                }
                handler.post(clockTick);
                drawFrame();
            } else {
                if (sensorManager != null) sensorManager.unregisterListener(this);
                handler.removeCallbacks(clockTick);
                handler.removeCallbacks(drawRunner);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            drawFrame();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (sensorManager != null) sensorManager.unregisterListener(this);
            handler.removeCallbacks(clockTick);
            handler.removeCallbacks(drawRunner);
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
            handler.removeCallbacks(drawRunner);
            handler.post(drawRunner);
        }

        @Override public void onAccuracyChanged(Sensor s, int accuracy) {}

        private void drawFrame() {
            if (scene == null) return;
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas == null) return;
                int w = canvas.getWidth(), h = canvas.getHeight();
                String temp = WeatherConfig.getTemperature(DepthWallpaperService.this);
                String cond = WeatherConfig.getCondition(DepthWallpaperService.this);
                int code = WeatherConfig.getCode(DepthWallpaperService.this);

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
                        ClockPainter.draw(canvas, w, h, scene.getClockStyle(),
                                temp, cond, code);
                    }
                }
                if (insertAfter >= layers) {
                    ClockPainter.draw(canvas, w, h, scene.getClockStyle(), temp, cond, code);
                }
            } finally {
                if (canvas != null) holder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
