package org.heroCrates.animations;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.heroCrates.HeroCrates;

public class ExplosionAnimation extends AbstractAnimation {

    public ExplosionAnimation(HeroCrates plugin, Player player, Location location, double size, String particle) {
        super(plugin, player, location, size, particle);
    }

    @Override
    public void play() {
        this.runTaskTimer(plugin, 0, 1);
    }

    @Override
    public void run() {
        double t = 0;
        final double maxRadius = size;

        while (t <= maxRadius) {
            t += 0.3;
            double radius = t * maxRadius;

            spawnExplosionParticles(radius);

            if (radius > maxRadius) {
                this.cancel();
            }
        }
    }

    private void spawnExplosionParticles(double radius) {
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
}