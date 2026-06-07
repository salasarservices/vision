package com.salasar.depthwallpaper;

import com.salasar.depthwallpaper.scenes.*;
import java.util.Arrays;
import java.util.List;

public final class SceneRegistry {
    private static final List<WallpaperScene> SCENES = Arrays.asList(
        new AuroraMountains(),
        new OceanTwilight(),
        new MysticForest(),
        new NeonCity(),
        new CosmicNebula(),
        new SaharaSunset(),
        new CherryBlossom(),
        new ArcticNight(),
        new TropicalParadise(),
        new AncientTemple()
    );

    public static List<WallpaperScene> getAll() { return SCENES; }

    public static WallpaperScene get(int index) { return SCENES.get(index); }

    public static int size() { return SCENES.size(); }

    private SceneRegistry() {}
}
