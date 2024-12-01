package org.heroCrates.dto;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.heroCrates.HeroCrates;
import org.heroCrates.items.impl.CrateItem;

@Getter
public class Crate {

    @Setter private Location location;
    private final String type;
    private final String displayName;
    private final CrateItem item;
    private HeroCrates plugin;

    public Crate(HeroCrates plugin, Location location, String type, String displayName) {
        this.location = location;
        this.type = type;
        this.displayName = displayName;
        this.item = new CrateItem(plugin, type);
    }
}
