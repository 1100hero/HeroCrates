package org.heroCrates.dto;

import org.bukkit.Location;

public record Crate(Location location, String type, String displayName) {

}
