package org.heroCrates.animations;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.heroCrates.HeroCrates;

public abstract class AbstractAnimation extends BukkitRunnable {
    protected final HeroCrates plugin;
    protected final Player player;
    protected final Location location;
    protected final double size;
    protected final String particle;

    public AbstractAnimation(HeroCrates plugin, Player player, Location location, double size, String particle) {
        this.plugin = plugin;
        this.player = player;
        this.location = location;
        this.size = size;
        this.particle = particle;
    }

    public abstract void play();
}