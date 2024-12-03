package org.heroCrates.objects;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;

public record Crate(Location location, String type, Component displayName) {

}
