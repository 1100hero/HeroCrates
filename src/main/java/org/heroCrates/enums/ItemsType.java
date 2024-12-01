package org.heroCrates.enums;

import lombok.Getter;
import org.bukkit.Material;

@Getter
public enum ItemsType {
    KEY(Material.TRIPWIRE_HOOK),
    CRATE(Material.CHEST);

    private final Material material;

    ItemsType(Material material) {
        this.material = material;
    }

    public static ItemsType fromMaterial(Material material) {
        for (ItemsType type : values()) {
            if (type.getMaterial() == material) {
                return type;
            }
        }
        return null;
    }
}
