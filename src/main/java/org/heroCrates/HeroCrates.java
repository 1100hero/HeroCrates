package org.heroCrates;

import fr.minuskube.inv.InventoryManager;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;
import org.heroCrates.database.HikariConfiguration;
import org.heroCrates.items.ItemsManager;
import org.heroCrates.managers.CratesManager;
import org.heroCrates.managers.HologramManager;
import org.heroCrates.managers.KeysManager;
import org.heroCrates.utils.Initializer;

@Getter
public final class HeroCrates extends JavaPlugin {

    private CratesManager cratesManager;
    private KeysManager keysManager;
    private ItemsManager itemsManager;
    private HikariConfiguration hikari;
    private HologramManager hologramManager;
    private InventoryManager inventoryManager;

    @Override
    @SneakyThrows
    public void onEnable() {
        this.hikari = new HikariConfiguration(this);
        this.cratesManager = new CratesManager(this);
        this.keysManager = new KeysManager(this);
        this.itemsManager = new ItemsManager(this);
        this.hologramManager = new HologramManager();
        this.inventoryManager = new InventoryManager(this);
        this.inventoryManager.init();

        Initializer initializer = new Initializer(this);
        initializer.initializeConfig();
        initializer.initializeCommands();
        initializer.initializeEvents();
        initializer.initializeHolograms();
    }

    @Override
    @SneakyThrows
    public void onDisable() {
        this.hologramManager.removeAllHolograms();
        if (this.getHikari().getDataSource() != null && !this.getHikari().getDataSource().isClosed()) {
            this.getHikari().getDataSource().close();
            getLogger().info("Connection closed.");
        }
    }
}
