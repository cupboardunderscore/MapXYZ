package dev.letsgoaway.mapxyz;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;

public class Config {
    //region DEFAULTS
    public static boolean enableStartingMap = true;

    public static boolean useLegacyConsoleDefaultZoom = true;

    public static boolean enableReducedDebugInfo = true;

    public static boolean useEyeLevelPosition = false;
    //endregion

    private static FileConfiguration config;

    private static void updateValues() {
        if (config.contains("enable-starting-map", true)) {
            enableStartingMap = config.getBoolean("enable-starting-map");
        }

        if (config.contains("use-legacy-console-default-zoom", true)) {
            useLegacyConsoleDefaultZoom = config.getBoolean("use-legacy-console-default-zoom");
        }

        if (config.contains("enable-reduced-debug-info", true)) {
            enableReducedDebugInfo = config.getBoolean("enable-reduced-debug-info");
        }

        if (config.contains("use-eye-level-position", true)) {
            useEyeLevelPosition = config.getBoolean("use-eye-level-position");
        }

        MapXYZ.onConfigLoad();
    }

    private static void saveValues() {
        config.set("enable-starting-map", enableStartingMap);
        config.set("use-legacy-console-default-zoom", useLegacyConsoleDefaultZoom);
        config.set("enable-reduced-debug-info", enableReducedDebugInfo);
        config.set("use-eye-level-position", useEyeLevelPosition);
        try {
            config.save(MapXYZ.instance.getDataFolder().toPath().resolve("config.yml").toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void init() {
        if (!MapXYZ.instance.getDataFolder().exists()) {
            MapXYZ.instance.saveResource("config.yml", false);
        }
        config = MapXYZ.instance.getConfig();
        updateValues();
        MapXYZ.instance.getDataFolder().toPath().resolve("config.yml").toFile().delete();
        MapXYZ.instance.saveResource("config.yml", false);
        try {
            config.load(MapXYZ.instance.getDataFolder().toPath().resolve("config.yml").toFile());
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        // and save the values again so that if any new values are added in an update it will be default but other settings are user preference
        saveValues();
    }

    public static void reload() {
        MapXYZ.instance.reloadConfig();
        init();
    }
}
