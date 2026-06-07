package com.salasar.depthwallpaper;

import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Method;

public class PreviewActivity extends Activity {
    public static final String EXTRA_SCENE_INDEX = "scene_index";
    private DepthWallpaperView depthView;
    private WallpaperScene scene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        int index = getIntent().getIntExtra(EXTRA_SCENE_INDEX, 0);
        scene = SceneRegistry.get(index);

        depthView = (DepthWallpaperView) findViewById(R.id.depth_view);
        depthView.setScene(scene);

        TextView nameView = (TextView) findViewById(R.id.tv_wallpaper_name);
        nameView.setText(scene.getName());

        Button btnHome = (Button) findViewById(R.id.btn_home);
        Button btnBoth = (Button) findViewById(R.id.btn_both);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyWallpaper(false);
            }
        });
        btnBoth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyWallpaper(true);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        depthView.startSensor(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        depthView.stopSensor();
    }

    @SuppressWarnings("deprecation")
    private void applyWallpaper(final boolean includeLock) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Display d = getWindowManager().getDefaultDisplay();
                int w = depthView.getWidth() > 0 ? depthView.getWidth() : d.getWidth();
                int h = depthView.getHeight() > 0 ? depthView.getHeight() : d.getHeight();
                if (w <= 0) w = 1080;
                if (h <= 0) h = 1920;

                final Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bmp);
                canvas.drawColor(Color.BLACK);
                scene.drawAll(canvas, w, h);

                WallpaperManager wm = WallpaperManager.getInstance(PreviewActivity.this);
                try {
                    if (includeLock && Build.VERSION.SDK_INT >= 24) {
                        try {
                            Method m = WallpaperManager.class.getMethod("setBitmap",
                                Bitmap.class, android.graphics.Rect.class, boolean.class, int.class);
                            m.invoke(wm, bmp, null, true, 3);
                        } catch (Exception ex) {
                            wm.setBitmap(bmp);
                        }
                    } else {
                        wm.setBitmap(bmp);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PreviewActivity.this, R.string.wallpaper_set, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    final String msg = e.getMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PreviewActivity.this, "Error: " + msg, Toast.LENGTH_LONG).show();
                        }
                    });
                } finally {
                    bmp.recycle();
                }
            }
        }).start();
    }
}
