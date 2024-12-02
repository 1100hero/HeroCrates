package org.heroCrates.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.heroCrates.HeroCrates;
import org.heroCrates.dto.Crate;
import org.heroCrates.holograms.CrateHologram;
import org.heroCrates.items.impl.CrateItem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CrateListener implements Listener {

    private final HeroCrates plugin;
    private final Set<Location> recentBlocks = new HashSet<>();

    public CrateListener(HeroCrates plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onCratePlace(BlockPlaceEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        ItemStack item = event.getItemInHand();
        if (!plugin.getCratesManager().isCrate(item)) return;

        Location location = event.getBlockPlaced().getLocation();
        if (recentBlocks.contains(location)) return;

        recentBlocks.add(location);
        Bukkit.getScheduler().runTaskLater(plugin, () -> recentBlocks.remove(location), 5L);

        String crateType = item.getItemMeta().getPersistentDataContainer().get(CrateItem.ITEM_KEY, PersistentDataType.STRING);
        Component displayName = item.getItemMeta().displayName();

        plugin.getCratesManager().getCrates().add(new CrateItem(plugin, new Crate(location, crateType, displayName)));

        List<String> hologramLines = plugin.getConfig().getStringList("crates." + crateType.toLowerCase() + ".hologram");
        CrateHologram hologram = new CrateHologram();
        hologram.spawnHologram(location, hologramLines);

        plugin.getHologramManager().getHolograms().put(location, hologram);
    }

    @EventHandler
    public void onCrateBreak(BlockBreakEvent event) {
        Location location = event.getBlock().getLocation();
        CrateHologram hologram = plugin.getHologramManager().getHolograms().remove(location);
        if (hologram != null) {
            hologram.removeHologram();
        }
    }
}