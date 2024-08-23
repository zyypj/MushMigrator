package br.com.pulse.mushmigrator;

import br.com.pulse.mushmigrator.core.LayoutMigrator;
import br.com.pulse.mushmigrator.listeners.InventoryListener;
import br.com.pulse.mushmigrator.support.BW2023;
import br.com.pulse.mushmigrator.support.IBedWars;
import com.tomkeuper.bedwars.BedWars;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private IBedWars bedWars;
    private LayoutMigrator migrator;
    private static Main instance;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("BedWars2023") == null) {
            getLogger().severe("BedWars2023 was not found. Disabling...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;
        bedWars = new BW2023();
        this.migrator = new LayoutMigrator(this);
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public IBedWars getBedWars() {
        return this.bedWars;
    }

    public LayoutMigrator getMigrator() {
        return this.migrator;
    }

    public static Main getInstance() {
        return instance;
    }
}
