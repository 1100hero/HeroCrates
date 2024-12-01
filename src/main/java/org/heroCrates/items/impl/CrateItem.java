package org.heroCrates.items.impl;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.heroCrates.HeroCrates;
import org.heroCrates.items.AbstractItem;
import org.heroCrates.utils.Utils;

import java.util.stream.Collectors;

public class CrateItem extends AbstractItem {

    private final FileConfiguration config;
    private final HeroCrates plugin;
    private final ItemStack item;
    private final String crateType;

    public CrateItem(HeroCrates plugin, String crateType) {
        this.item = new ItemStack(Material.CHEST);
        this.config = plugin.getConfig();
        this.plugin = plugin;
        this.crateType = crateType;

        item.editMeta(meta -> {
            String itemName = config.getString("crates." + crateType.toLowerCase() + ".item_name");
            if (itemName != null) {
                meta.displayName(Utils.colorize(itemName));

                meta.lore(
                        config.getStringList("crates." + crateType.toLowerCase() + ".item_lore")
                                .stream()
                                .map(Utils::colorize)
                                .collect(Collectors.toList())
                );
            }

            meta.getPersistentDataContainer().set(AbstractItem.ITEM_KEY, PersistentDataType.STRING, crateType);
        });
    }

    @Override
    public String getName() {
        return plugin.getCratesManager().getDisplayName(crateType);
    }

    @Override
    public ItemStack getItem() {
        return this.item;
    }

    @Override
    public void onRightClick() {

    }

    @Override
    public void onLeftClick() {

    }

    @Override
    public boolean shouldCancelDrop() {
        return true;
    }

    @Override
    public void giveItem(Player player, int amount) {
        for (int i = 0; i < amount; i++) {
            player.getInventory().addItem(this.item);
        }
    }
}
