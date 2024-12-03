package org.heroCrates.utils;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.persistence.PersistentDataType;
import org.heroCrates.HeroCrates;
import org.heroCrates.commands.GiveCratesCommand;
import org.heroCrates.commands.GiveKeyCommand;
import org.heroCrates.objects.Crate;
import org.heroCrates.holograms.CrateHologram;
import org.heroCrates.items.impl.CrateItem;
import org.heroCrates.listeners.CrateListener;
import org.heroCrates.listeners.ItemListener;

import java.util.List;

public class Initializer {

    private final HeroCrates plugin;

    public Initializer(HeroCrates plugin) {
        this.plugin = plugin;
    }

    public void initializeCommands() {
        new GiveCratesCommand(plugin);
        new GiveKeyCommand(plugin);
    }

    public void initializeEvents() {
        plugin.getServer().getPluginManager().registerEvents(new ItemListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new CrateListener(plugin), plugin);
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

    public void initializeHolograms() {
        for (World world : Bukkit.getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                for (BlockState state : chunk.getTileEntities()) {
                    if (!(state instanceof Chest chest)) continue;

                    String crateType = chest.getPersistentDataContainer().get(CrateItem.ITEM_KEY, PersistentDataType.STRING);
                    if (crateType == null || !plugin.getCratesManager().exists(crateType)) continue;

                    Location location = chest.getLocation();
                    List<String> hologramLines = plugin.getConfig().getStringList("crates." + crateType.toLowerCase() + ".hologram");
                    CrateHologram hologram = new CrateHologram();
                    hologram.spawnHologram(location, hologramLines);
                    plugin.getHologramManager().getHolograms().put(location, hologram);
                }
            }
        }
    }
}