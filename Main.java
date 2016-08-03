package com.omnititan.staffmode;


import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    static Main getPlugin;
    @Override
    public void onEnable() {
        this.getCommand("smode").setExecutor(new smodeCommand());
        getPlugin = this;
    }

    @Override
    public void onDisable() {

    }


}


