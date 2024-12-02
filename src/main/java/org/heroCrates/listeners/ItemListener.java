package org.heroCrates.listeners;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.heroCrates.HeroCrates;
import org.heroCrates.items.AbstractItem;
import org.heroCrates.items.impl.CrateItem;

public class ItemListener implements Listener {

    private final HeroCrates plugin;

    public ItemListener(HeroCrates plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (isAnAbstractItem(event.getItemDrop().getItemStack())) {
            AbstractItem item = plugin.getItemsManager().getItem(event.getItemDrop().getItemStack().getType());
            event.setCancelled(item.shouldCancelDrop());
        }
    }

    public void onChestClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK
                || event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        BlockState state = event.getClickedBlock().getState();
        if (!(state instanceof Chest chest)) return;
        if (!chest.getPersistentDataContainer().has(CrateItem.ITEM_KEY, PersistentDataType.STRING)) return;

        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            
        } else {

            event.setCancelled(true);
        }
    }


    private boolean isAnAbstractItem(ItemStack item) {
        return item.hasItemMeta()
                && item.getPersistentDataContainer().get(AbstractItem.ITEM_KEY, PersistentDataType.STRING) != null;
    }
}
