package org.heroCrates.managers;


import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.heroCrates.HeroCrates;
import org.heroCrates.utils.Utils;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class KeysManager {

    private final HeroCrates plugin;
    private final FileConfiguration config;
    @Getter
    private final ConcurrentHashMap<UUID, Integer> countdownKeys;

    public KeysManager(HeroCrates plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.countdownKeys = new ConcurrentHashMap<>();
    }

    public boolean startCountdown(UUID uuid, String type) {
        if (isPlayerInCountdown(uuid)) return false;
        int countdownTime = this.config.getInt("crates." + type + ".countdown") * 20;
        countdownKeys.put(uuid, countdownTime);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!countdownKeys.containsKey(uuid)) {
                    cancel();
                    return;
                }
                int time = countdownKeys.get(uuid);
                if (time <= 0) {
                    countdownKeys.remove(uuid);
                    cancel();
                    return;
                }
                countdownKeys.put(uuid, time - 1);
            }
        }.runTaskTimer(plugin, 0, 1L);
        return true;
    }

    private boolean isPlayerInCountdown(UUID uuid) {
        if (countdownKeys.containsKey(uuid)) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.sendMessage(Utils.colorize(this.config.getString("messages.countdown_error")
                        .replace("{time}", String.valueOf(this.getCountdownKeys().get(uuid) / 20))));
            }
            return true;
        }
        return false;
    }
}
