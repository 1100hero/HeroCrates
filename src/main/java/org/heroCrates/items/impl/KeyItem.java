package org.heroCrates.items.impl;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.heroCrates.HeroCrates;
import org.heroCrates.dto.Key;
import org.heroCrates.items.AbstractItem;
import org.heroCrates.utils.Utils;

import java.util.stream.Collectors;

public class KeyItem extends AbstractItem {

    private final FileConfiguration config;
    private final HeroCrates plugin;
    private final ItemStack item;
    @Getter
    private final Key key;

    public KeyItem(HeroCrates plugin, Key key) {
        this.item = new ItemStack(Material.TRIPWIRE_HOOK);
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.key = key;

        item.editMeta(meta -> {
            String itemName = config.getString("crates." + getKey().type().toLowerCase() + ".key.display_name");
            if (itemName != null) {
                meta.displayName(Utils.colorize(itemName));

                meta.lore(
                        config.getStringList("crates." + getKey().type().toLowerCase() + ".key.lore")
                                .stream()
                                .map(Utils::colorize)
                                .collect(Collectors.toList())
                );
            }

            meta.getPersistentDataContainer().set(AbstractItem.ITEM_KEY, PersistentDataType.STRING, getKey().type() + "_key");
        });
    }

    @Override
    protected Component getName() {
        return plugin.getCratesManager().getDisplayName(getKey().type());
    }

    @Override
    protected ItemStack getItem() {
        return this.item;
    }

    @Override
    public boolean shouldCancelDrop() {
        return false;
    }

    @Override
    public void giveItem(Player player, int amount) {
        for (int i = 0; i < amount; i++) {
            player.getInventory().addItem(this.item);
        }
    }
}
