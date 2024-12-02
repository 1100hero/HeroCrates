package org.heroCrates.animations;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.heroCrates.HeroCrates;

public class Animation {

    private final HeroCrates plugin;

    public Animation(HeroCrates plugin) {
        this.plugin = plugin;
    }

    public void playAnimation(Player player, Location location, String type, double size, String particle) {
        switch (type.toUpperCase()) {
            case "SPIRAL":
                playSpiralAnimation(player, location, size, particle);
                break;
        }
    }

    private void playSpiralAnimation(Player player, Location location, double size, String particle) {
        new BukkitRunnable() {
            double t = 0;

            @Override
            public void run() {
                t += Math.PI / 16;
                double y = t * 0.5;

                spawnSpiralParticles(player, location, size, t, y, true, particle);
                spawnSpiralParticles(player, location, size, t, y, false, particle);

                if (t > 10) {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    private void spawnSpiralParticles(Player player, Location location, double size, double t, double y, boolean firstSpiral, String particle) {
        if (particle == null || particle.isEmpty()) return;

        Particle particleEnum = Particle.valueOf(particle.toUpperCase());


        double x = firstSpiral ? size * Math.cos(t) : size * Math.sin(t);
        double z = firstSpiral ? size * Math.sin(t) : size * Math.cos(t);

        location.add(x, y, z);
        for (int i = 0; i < 5; i++) {
            double offsetX = (Math.random() - 0.5) * 0.2;
            double offsetY = (Math.random() - 0.5) * 0.2;
            double offsetZ = (Math.random() - 0.5) * 0.2;
            player.spawnParticle(particleEnum, location.clone().add(offsetX, offsetY, offsetZ), 1);
        }
        location.subtract(x, y, z);
    }
}
