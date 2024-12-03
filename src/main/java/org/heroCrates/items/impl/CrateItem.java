package org.heroCrates.items.impl;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.heroCrates.HeroCrates;
import org.heroCrates.objects.Crate;
import org.heroCrates.items.AbstractItem;
import org.heroCrates.utils.Utils;

import java.util.stream.Collectors;

public class CrateItem extends AbstractItem {

    private final FileConfiguration config;
    private final HeroCrates plugin;
    private final ItemStack item;
    @Getter private final Crate crate;

    public CrateItem(HeroCrates plugin, Crate crate) {
        this.item = new ItemStack(Material.CHEST);
        this.config = plugin.getConfig();
        this.plugin = plugin;
        this.crate = crate;

        item.editMeta(meta -> {
            String itemName = config.getString("crates." + getCrate().type().toLowerCase() + ".item_name");
            if (itemName != null) {
                meta.displayName(Utils.colorize(itemName));

                meta.lore(
                        config.getStringList("crates." + getCrate().type().toLowerCase() + ".item_lore")
                                .stream()
                                .map(Utils::colorize)
                                .collect(Collectors.toList())
                );
            }

            meta.getPersistentDataContainer().set(AbstractItem.ITEM_KEY, PersistentDataType.STRING, getCrate().type());
        });
    }

    public void onRightClick(Player player, ItemStack item) {

    }

    @Override
    public Component getName() {
        return plugin.getCratesManager().getDisplayName(getCrate().type());
    }

    @Override
    public ItemStack getItem() {
        return this.item;
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
