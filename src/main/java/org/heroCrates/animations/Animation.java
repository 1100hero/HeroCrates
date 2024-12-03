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
            case "EXPLOSION":
                playExplosionAnimation(player, location, size, particle);
                break;
        }
    }

    private void playExplosionAnimation(Player player, Location location, double size, String particle) {
        new BukkitRunnable() {
            double t = 0;
            final double maxRadius = size;

            @Override
            public void run() {
                t += 0.3;
                double radius = t * maxRadius;

                spawnExplosionParticles(player, location, radius, particle);

                if (radius > maxRadius) {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    private void spawnExplosionParticles(Player player, Location location, double radius, String particle) {
        if (particle == null || particle.isEmpty()) return;

        Particle particleEnum = Particle.valueOf(particle.toUpperCase());

        for (double theta = 0; theta < Math.PI; theta += Math.PI / 10) {
            double y = radius * Math.cos(theta);
            for (double phi = 0; phi < 2 * Math.PI; phi += Math.PI / 10) {
                double x = radius * Math.sin(theta) * Math.cos(phi);
                double z = radius * Math.sin(theta) * Math.sin(phi);
                location.add(x, y, z);
                player.spawnParticle(particleEnum, location, 1);
                location.subtract(x, y, z);
            }
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
