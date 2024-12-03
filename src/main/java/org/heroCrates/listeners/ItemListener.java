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
import org.bukkit.persistence.PersistentDataType;
import org.heroCrates.HeroCrates;
import org.heroCrates.items.AbstractItem;
import org.heroCrates.items.impl.CrateItem;
import org.heroCrates.utils.Utils;

import java.util.HashSet;
import java.util.Set;

public class ItemListener implements Listener {

    private final HeroCrates plugin;
    private final Set<Player> recentBlocks = new HashSet<>();

    public ItemListener(HeroCrates plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (Utils.isAnAbstractItem(event.getItemDrop().getItemStack())) {
            AbstractItem item = plugin.getItemsManager().getItem(event.getItemDrop().getItemStack().getType());
            event.setCancelled(item.shouldCancelDrop());
        }
    }

    @EventHandler
    public void onChestClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK) return;

        BlockState state = event.getClickedBlock().getState();
        if (!(state instanceof Chest chest)) return;

        if (!chest.getPersistentDataContainer().has(CrateItem.ITEM_KEY, PersistentDataType.STRING)) return;

        Player player = event.getPlayer();
        if (recentBlocks.contains(player)) return;

        recentBlocks.add(player);
        Bukkit.getScheduler().runTaskLater(plugin, () -> recentBlocks.remove(player), 2L);

        String crateType = chest.getPersistentDataContainer().get(CrateItem.ITEM_KEY, PersistentDataType.STRING);
        if (!plugin.getCratesManager().exists(crateType)) {
            chest.getPersistentDataContainer().remove(CrateItem.ITEM_KEY);
            chest.update();
            return;
        }

        if (chest.getPersistentDataContainer().has(CrateItem.ITEM_KEY, PersistentDataType.STRING))
            event.setCancelled(true);

        plugin.getCratesManager().handleCrateInteraction(player, event.getAction(), chest.getLocation(), crateType, event.getItem());
    }
}
