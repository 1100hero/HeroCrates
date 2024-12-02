package org.heroCrates.managers;

import lombok.Getter;
import org.bukkit.Location;
import org.heroCrates.holograms.CrateHologram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
public class HologramManager {

    private final Map<Location, CrateHologram> holograms;

    public HologramManager() {
        this.holograms = new HashMap<>();
    }

    public void removeAllHolograms() {
        for (CrateHologram hologram : new ArrayList<>(holograms.values())) {
            hologram.removeHologram();
        }
    }
}
