package org.heroCrates.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.heroCrates.HeroCrates;
import org.heroCrates.commands.GiveCratesCommand;
import org.heroCrates.commands.GiveKeyCommand;
import org.heroCrates.dto.Crate;
import org.heroCrates.items.impl.CrateItem;
import org.heroCrates.listeners.CrateListener;
import org.heroCrates.listeners.ItemListener;

public class Initializer {

    private final HeroCrates plugin;

    public Initializer(HeroCrates plugin) {
        this.plugin = plugin;
    }

    public void initializeCommands() {
        new GiveCratesCommand(plugin);
        new GiveKeyCommand(plugin);
    }

    public void initializeConfig() {
        FileConfiguration config = plugin.getConfig();

        ConfigurationSection cratesSection = config.getConfigurationSection("crates");
        if (cratesSection != null) {
            for (String key : cratesSection.getKeys(false)) {
                String displayName = cratesSection.getString(key + ".display_name");
                plugin.getCratesManager().getCrates().add(
                        new CrateItem(
                                plugin,
                                new Crate(null, key.toUpperCase(), Utils.colorize(displayName))));
            }
        } else {
            plugin.getCratesManager().getCrates().add(
                    new CrateItem(
                            plugin,
                            new Crate(null, "DEFAULT", Utils.colorize("Default Crate"))));
        }



        plugin.saveDefaultConfig();
    }

    public void initializeEvents() {
        plugin.getServer().getPluginManager().registerEvents(new ItemListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new CrateListener(plugin), plugin);
    }
}