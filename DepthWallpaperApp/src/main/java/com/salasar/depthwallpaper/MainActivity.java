package com.salasar.depthwallpaper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView grid = (GridView) findViewById(R.id.grid_wallpapers);
        WallpaperGridAdapter adapter = new WallpaperGridAdapter(this, SceneRegistry.getAll());
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Intent intent = new Intent(MainActivity.this, PreviewActivity.class);
                intent.putExtra(PreviewActivity.EXTRA_SCENE_INDEX, pos);
                startActivity(intent);
            }
        });
    }
}
