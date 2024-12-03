package org.heroCrates.animations;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.heroCrates.HeroCrates;

public class Animation {

    private final HeroCrates plugin;

    public Animation(HeroCrates plugin) {
        this.plugin = plugin;
    }

    public void playAnimation(Player player, Location location, String type, double size, String particle, String color) {

        Color c = Color.fromRGB(255, 0, 0);
        try {
            c = (Color) Color.class.getField(color.toUpperCase()).get(null);
        } catch (Exception e) {
            plugin.getLogger().warning("Invalid color specified in config: " + color);
        }
        AbstractAnimation animation = switch (type.toUpperCase()) {
            case "SPIRAL" -> new SpiralAnimation(plugin, player, location, size, particle, c);
            case "EXPLOSION" -> new ExplosionAnimation(plugin, player, location, size, particle, c);
            default -> throw new IllegalArgumentException("Unknown animation");
        };
        animation.play();
    }
}