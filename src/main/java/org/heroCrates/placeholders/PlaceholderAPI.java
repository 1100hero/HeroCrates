package org.heroCrates.placeholders;

import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.heroCrates.HeroCrates;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class PlaceholderAPI extends PlaceholderExpansion {

    private final HeroCrates plugin;

    @Override
    public @NotNull String getIdentifier() {
        return "herocrates";
    }

    @Override
    public @NotNull String getAuthor() {
        return "1100hero";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        return null;
    }
}