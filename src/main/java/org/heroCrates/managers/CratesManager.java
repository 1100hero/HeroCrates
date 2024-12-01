package org.heroCrates.managers;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.heroCrates.dto.Crate;
import org.heroCrates.items.AbstractItem;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CratesManager {

    private final List<Crate> crates;

    public CratesManager() {
        crates = new ArrayList<>();
    }

    public boolean isCrateName(String name) {
        return crates.stream().anyMatch(crate -> crate.getType().equalsIgnoreCase(name));
    }

    public List<String> getCrateList() {
        return crates.stream().map(Crate::getType).toList();
    }

    public String getDisplayName(String type) {
        return crates.stream().filter(crate -> crate.getType().equalsIgnoreCase(type)).findFirst().map(Crate::getDisplayName).orElse(null);
    }

    public boolean isCrate(ItemStack itemStack) {
        return crates
                .stream()
                .anyMatch(crate -> crate.getItem().getItem().getItemMeta().getPersistentDataContainer().has(AbstractItem.ITEM_KEY, PersistentDataType.STRING));
    }
}