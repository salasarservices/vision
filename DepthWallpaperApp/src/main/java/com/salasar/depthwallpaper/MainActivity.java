package com.salasar.depthwallpaper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private WallpaperGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView grid = (GridView) findViewById(R.id.grid_wallpapers);

        String temp = WeatherConfig.getTemperature(this);
        String cond = WeatherConfig.getCondition(this);
        int code = WeatherConfig.getCode(this);

        adapter = new WallpaperGridAdapter(this, SceneRegistry.getAll());
        adapter.setWeather(temp, cond, code);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                android.content.Intent intent =
                    new android.content.Intent(MainActivity.this, PreviewActivity.class);
                intent.putExtra(PreviewActivity.EXTRA_SCENE_INDEX, pos);
                startActivity(intent);
            }
        });

        Button btnSettings = (Button) findViewById(R.id.btn_settings);
        if (btnSettings != null) {
            btnSettings.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { showWeatherDialog(); }
            });
        }

        autoFetchWeather();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String temp = WeatherConfig.getTemperature(this);
        String cond = WeatherConfig.getCondition(this);
        int code = WeatherConfig.getCode(this);
        adapter.setWeather(temp, cond, code);
    }

    private void autoFetchWeather() {
        new WeatherFetcher(this, new WeatherFetcher.Callback() {
            @Override
            public void onWeather(final String temp, final String condition, final int code) {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        adapter.setWeather(temp, condition, code);
                    }
                });
            }
        }).execute();
    }

    private void showWeatherDialog() {
        final EditText etTemp = new EditText(this);
        etTemp.setHint("Temperature (e.g. 28°C)");
        etTemp.setText(WeatherConfig.getTemperature(this));

        final EditText etCond = new EditText(this);
        etCond.setHint("Condition (e.g. Partly Cloudy)");
        etCond.setText(WeatherConfig.getCondition(this));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) (16 * getResources().getDisplayMetrics().density);
        layout.setPadding(pad, pad, pad, pad);

        TextView tvNote = new TextView(this);
        tvNote.setText("Weather auto-fetches on app start. Override manually below:");
        layout.addView(tvNote);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = pad / 2;
        layout.addView(etTemp, lp);
        layout.addView(etCond, lp);

        new AlertDialog.Builder(this)
            .setTitle("Weather Settings")
            .setView(layout)
            .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String temp = etTemp.getText().toString().trim();
                    String cond = etCond.getText().toString().trim();
                    if (temp.isEmpty()) temp = "--";
                    if (cond.isEmpty()) cond = "Clear";
                    WeatherConfig.save(MainActivity.this, temp, cond);
                    int savedCode = WeatherConfig.getCode(MainActivity.this);
                    adapter.setWeather(temp, cond, savedCode);
                }
            })
            .setNeutralButton("Auto-Fetch Now", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Fetching weather…", Toast.LENGTH_SHORT).show();
                    autoFetchWeather();
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }
}
