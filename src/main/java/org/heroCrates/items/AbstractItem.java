package org.heroCrates.items;

import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractItem {

    public static final NamespacedKey ITEM_KEY = NamespacedKey.minecraft("abstract-item");

    protected abstract Component getName();

    protected abstract ItemStack getItem();

    public abstract boolean shouldCancelDrop();

    public abstract void giveItem(Player player, int amount);
}