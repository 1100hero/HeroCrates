package org.heroCrates.listeners;

import org.bukkit.Bukkit;
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
import org.heroCrates.utils.Utils;

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

    public void onChestRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (!isAnAbstractItem(event.getItem())) {
            player.sendMessage(Utils.colorize("&cDevi avere una chiave in mano per aprire questa cassa."));
            return;
        }
        if (!plugin.getCratesManager().isCrate(event.getClickedBlock().getLocation())) return;
        // TODO: APRI CRATE
    }

    public void onChestLeftClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        if (!plugin.getCratesManager().isCrate(event.getClickedBlock().getLocation())) return;
        // TODO: APRI PREVIEW CRATE
    }

    private boolean isAnAbstractItem(ItemStack item) {
        return item.hasItemMeta()
                && item.getPersistentDataContainer().get(AbstractItem.ITEM_KEY, PersistentDataType.STRING) != null;
    }
}
