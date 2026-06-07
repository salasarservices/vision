package com.salasar.depthwallpaper;

import com.salasar.depthwallpaper.scenes.TropicalMonstera;
import com.salasar.depthwallpaper.scenes.WhiteTiger;
import com.salasar.depthwallpaper.scenes.GlacierPeak;
import com.salasar.depthwallpaper.scenes.LunarSurface;
import com.salasar.depthwallpaper.scenes.NightForest;
import com.salasar.depthwallpaper.scenes.OceanSurge;
import com.salasar.depthwallpaper.scenes.CosmicRift;
import com.salasar.depthwallpaper.scenes.SakuraNight;
import com.salasar.depthwallpaper.scenes.RedCanyon;
import com.salasar.depthwallpaper.scenes.CyberRain;
import java.util.Arrays;
import java.util.List;

public final class SceneRegistry {
    private static final List<WallpaperScene> SCENES = Arrays.asList(
        (WallpaperScene) new TropicalMonstera(),
        (WallpaperScene) new WhiteTiger(),
        (WallpaperScene) new GlacierPeak(),
        (WallpaperScene) new LunarSurface(),
        (WallpaperScene) new NightForest(),
        (WallpaperScene) new OceanSurge(),
        (WallpaperScene) new CosmicRift(),
        (WallpaperScene) new SakuraNight(),
        (WallpaperScene) new RedCanyon(),
        (WallpaperScene) new CyberRain()
    );

    public static List<WallpaperScene> getAll() { return SCENES; }

    public static WallpaperScene get(int index) { return SCENES.get(index); }

    public static int size() { return SCENES.size(); }

    private SceneRegistry() {}
}
