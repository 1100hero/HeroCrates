package org.heroCrates.items;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractItem {

    public static final NamespacedKey ITEM_KEY = NamespacedKey.minecraft("abstract-item");

    protected abstract String getName();

    protected abstract ItemStack getItem();

    public abstract void onRightClick();

    public abstract void onLeftClick();

    public abstract boolean shouldCancelDrop();

    public abstract void giveItem(Player player, int amount);
}
