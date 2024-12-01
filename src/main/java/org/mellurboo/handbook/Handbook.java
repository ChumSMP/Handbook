package org.mellurboo.handbook;

import org.bukkit.plugin.java.JavaPlugin;
import org.mellurboo.handbook.commands.openHandbook;

public final class Handbook extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("handbook").setExecutor(new openHandbook(this));
        getLogger().info(  "=============== HANDBOOK PLUGIN ===============" +
                                "\n======= Enabled                              " +
                                "\n=============== HANDBOOK PLUGIN ===============");
    }

    @Override
    public void onDisable() {
        saveDefaultConfig();
        getLogger().info("HANDBOOK PLUGIN DISABLED");
    }


}