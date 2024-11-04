package org.mellurboo.playerHandbook;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerHandbook extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("=-=-=-=-=-=- SIGMA PLUGIN =-=-=-=-=-=-");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
