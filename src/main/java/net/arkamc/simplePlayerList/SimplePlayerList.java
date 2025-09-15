package net.arkamc.simplePlayerList;

import net.arkamc.simplePlayerList.command.ListCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimplePlayerList extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.getCommand("list").setExecutor(new ListCommand(this));
        this.getLogger().info("SimplePlayerList enabled!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("SimplePlayerList disabled!");
    }
}
