package org.heroCrates.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.heroCrates.HeroCrates;
import org.heroCrates.crates.Crate;
import org.heroCrates.items.AbstractItem;

public class CrateListener implements Listener {

    private final HeroCrates plugin;

    public CrateListener(HeroCrates plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    // TODO: Lo esegue due volte
    @EventHandler
    public void onCratePlace(BlockPlaceEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        ItemStack item = event.getItemInHand();
        if (!plugin.getCratesManager().isCrate(item)) return;

        Player player = event.getPlayer();
        int heldSlot = player.getInventory().getHeldItemSlot();
        ItemStack currentItem = player.getInventory().getItem(heldSlot);

        if (currentItem != null) {
            if (currentItem.getAmount() > 1) {
                currentItem.setAmount(currentItem.getAmount() - 1);
            } else {
                player.getInventory().setItem(heldSlot, null);
            }
        }

        plugin.getCratesManager().getCrates().add(
                new Crate(plugin,
                        event.getBlock().getLocation(),
                        item.getItemMeta().getPersistentDataContainer().get(AbstractItem.ITEM_KEY, PersistentDataType.STRING),
                        plugin.getCratesManager().getDisplayName(item.getItemMeta().getPersistentDataContainer().get(AbstractItem.ITEM_KEY,
                                PersistentDataType.STRING))));

    }
}
