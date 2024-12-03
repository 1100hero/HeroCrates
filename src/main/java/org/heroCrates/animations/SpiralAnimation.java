package org.heroCrates.animations;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.heroCrates.HeroCrates;

public class SpiralAnimation extends AbstractAnimation {

    public SpiralAnimation(HeroCrates plugin, Player player, Location location, double size, String particle, Color color) {
        super(plugin, player, location, size, particle, color);
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
            if (particleEnum == Particle.DUST) {
                Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1.0F);
                player.spawnParticle(particleEnum, location, 1, dustOptions);
            } else {
                player.spawnParticle(particleEnum, location, 1);
            }
        }
        location.subtract(x, y, z);
    }
}