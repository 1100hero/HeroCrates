package org.heroCrates.holograms;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.heroCrates.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CrateHologram {

    private final List<ArmorStand> armorStands = new ArrayList<>();
    @Getter
    private Location location;

    public void spawnHologram(Location chestLocation, List<String> hologramLines) {
        this.location = chestLocation.clone().add(0.5, 1.5, 0.5);
        for (String line : hologramLines) {
            ArmorStand armorStand = (ArmorStand) chestLocation.getWorld().spawnEntity(getLocation(), EntityType.ARMOR_STAND);
            armorStand.setVisible(false);
            armorStand.setCustomNameVisible(true);
            armorStand.customName(Utils.colorize(line));
            armorStand.setGravity(false);
            armorStand.setMarker(true);
            armorStands.add(armorStand);
            this.location.add(0, 0.3, 0);
        }
    }

    public void removeHologram() {
        for (ArmorStand armorStand : armorStands) {
            if (armorStand != null) {
                armorStand.remove();
            }
        }
        armorStands.clear();
    }
}