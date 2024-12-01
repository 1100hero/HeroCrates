package org.heroCrates;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.heroCrates.managers.CratesManager;
import org.heroCrates.items.ItemsManager;
import org.heroCrates.utils.Initializer;

@Getter
public final class HeroCrates extends JavaPlugin {

    private CratesManager cratesManager;
    private ItemsManager itemsManager;

    @Override
    public void onEnable() {
        this.cratesManager = new CratesManager();
        this.itemsManager = new ItemsManager(this);

        Initializer initializer = new Initializer(this);
        initializer.initializeConfig();
        initializer.initializeCommands();
        initializer.initializeEvents();
    }
}
