package org.heroCrates.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.heroCrates.HeroCrates;
import org.heroCrates.database.Operations;

public class VirtualKeySelectionGUI implements InventoryProvider {

    private final HeroCrates plugin;
    private final SmartInventory inventory;
    private final String crateType;
    private final Location chestLocation;

    public VirtualKeySelectionGUI(HeroCrates plugin, String crateType, Location chestLocation) {
        this.plugin = plugin;
        this.crateType = crateType.toLowerCase();
        this.chestLocation = chestLocation;
        this.inventory = SmartInventory.builder()
                .manager(plugin.getInventoryManager())
                .id("selection-virtual-gui")
                .provider(this)
                .size(3, 9)
                .title(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("crates." + this.crateType + ".gui_title")
                        .replace("{crate}", crateType)))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(1, 3, ClickableItem.of(ItemStack.of(Material.GREEN_STAINED_GLASS_PANE), e -> {
            if (!plugin.getKeysManager().startCountdown(player.getUniqueId(), crateType.toLowerCase())) return;
            plugin.getCratesManager().giveAward(player, crateType, chestLocation, true);
            new Operations(plugin).removeOneVirtualKey(player.getUniqueId(), crateType);
            player.closeInventory();
        }));

        contents.set(1, 5, ClickableItem.of(ItemStack.of(Material.RED_STAINED_GLASS_PANE), e -> {
            player.closeInventory();
        }));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }

    public void open(Player player) {
        this.inventory.open(player);
    }
}
