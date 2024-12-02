package org.heroCrates.items.impl;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.heroCrates.HeroCrates;
import org.heroCrates.items.AbstractItem;

public class KeyItem extends AbstractItem {

    private final HeroCrates plugin;
    private final ItemStack item;

    public KeyItem(HeroCrates plugin) {
        this.plugin = plugin;

        this.item = null;
    }

    @Override
    protected Component getName() {
        return null;
    }

    @Override
    protected ItemStack getItem() {
        return null;
    }

    @Override
    public void onRightClick() {

    }

    @Override
    public void onLeftClick() {

    }

    @Override
    public boolean shouldCancelDrop() {
        return false;
    }

    @Override
    public void giveItem(Player player, int amount) {

    }
}
