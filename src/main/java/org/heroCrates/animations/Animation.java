package org.heroCrates.animations;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.heroCrates.HeroCrates;

public class Animation {

    private final HeroCrates plugin;

    public Animation(HeroCrates plugin) {
        this.plugin = plugin;
    }

    public void playAnimation(Player player, Location location, String type, double size, String particle) {
        AbstractAnimation animation = switch (type.toUpperCase()) {
            case "SPIRAL" -> new SpiralAnimation(plugin, player, location, size, particle);
            case "EXPLOSION" -> new ExplosionAnimation(plugin, player, location, size, particle);
            default -> throw new IllegalArgumentException("Unknown animation");
        };
        animation.play();
    }
}