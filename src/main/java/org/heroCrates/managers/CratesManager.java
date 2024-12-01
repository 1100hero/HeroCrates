package org.heroCrates.managers;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.heroCrates.items.AbstractItem;
import org.heroCrates.items.impl.CrateItem;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CratesManager {

    private final List<CrateItem> crates;

    public CratesManager() {
        crates = new ArrayList<>();
    }

    public boolean isCrateName(String name) {
        return crates
                .stream()
                .anyMatch(crateItem -> crateItem.getCrate().type().equalsIgnoreCase(name));
    }

    public List<String> getCrateList() {
        return crates
                .stream()
                .map(crateItem -> crateItem.getCrate().type())
                .toList();
    }

    public String getDisplayName(String type) {
        return crates
                .stream()
                .filter(crateItem -> crateItem.getCrate().type().equalsIgnoreCase(type))
                .findFirst()
                .map(crateItem -> crateItem.getCrate().displayName())
                .orElse(null);
    }

    public boolean isCrate(ItemStack itemStack) {
        return itemStack.getItemMeta().getPersistentDataContainer().has(AbstractItem.ITEM_KEY, PersistentDataType.STRING);
    }

    public boolean isCrate(Location location) {
        return crates
                .stream()
                .anyMatch(crateItem -> crateItem.getCrate().location().distance(location) < 1);
    }
}
