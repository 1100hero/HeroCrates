package org.heroCrates.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.heroCrates.HeroCrates;
import org.heroCrates.items.AbstractItem;

public class ItemListener implements Listener {

    private final HeroCrates plugin;

    public ItemListener(HeroCrates plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(isAnAbstractItem(event.getItemDrop().getItemStack())) {
            AbstractItem item = plugin.getItemsManager().getItem(event.getItemDrop().getItemStack().getType());
            event.setCancelled(item.shouldCancelDrop());
        }
    }

    private boolean isAnAbstractItem(ItemStack item) {
        return item.hasItemMeta()
                && item.getPersistentDataContainer().get(AbstractItem.ITEM_KEY, PersistentDataType.STRING) != null;
    }
}
