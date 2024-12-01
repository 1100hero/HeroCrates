package org.heroCrates.items;

import org.bukkit.Material;
import org.heroCrates.HeroCrates;
import org.heroCrates.dto.Crate;
import org.heroCrates.enums.ItemsType;
import org.heroCrates.items.impl.CrateItem;

import java.util.EnumMap;
import java.util.Map;

public class ItemsManager {


    private final Map<ItemsType, AbstractItem> items = new EnumMap<>(ItemsType.class);
    private final HeroCrates plugin;

    public ItemsManager(HeroCrates plugin) {
        this.plugin = plugin;
        initializeItems();
    }

    public AbstractItem getItem(Material material) {
        ItemsType type = ItemsType.fromMaterial(material);
        if (type == null) return null;
        return items.get(type);
    }

    private void initializeItems() {
        items.put(ItemsType.CRATE, new CrateItem(plugin, new Crate(null, "DEFAULT", "Default Crate")));
    }
}
