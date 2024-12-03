package org.heroCrates.animations;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.heroCrates.HeroCrates;

public class SpiralAnimation extends AbstractAnimation {

    public SpiralAnimation(HeroCrates plugin, Player player, Location location, double size, String particle) {
        super(plugin, player, location, size, particle);
    }

    @Override
    public void play() {
        this.runTaskTimer(plugin, 0, 1);
    }

    @Override
    public void run() {
        double t = 0;
        while (t <= 10) {
            t += Math.PI / 16;
            double y = t * 0.5;

            spawnSpiralParticles(t, y, true);
            spawnSpiralParticles(t, y, false);

            if (t > 10) {
                this.cancel();
            }
        }
    }

    private void spawnSpiralParticles(double t, double y, boolean firstSpiral) {
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