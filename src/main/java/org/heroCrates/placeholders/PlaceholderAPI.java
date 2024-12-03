package org.heroCrates.placeholders;

import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.heroCrates.HeroCrates;
import org.heroCrates.database.Operations;
import org.jetbrains.annotations.NotNull;

import java.util.regex.PatternSyntaxException;

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
        if (identifier.toLowerCase().startsWith("leaderboard_")) {
            try {
                String[] split = identifier.split("_");
                if (split.length < 4) return null;
                String crate = split[1];
                int page = Integer.parseInt(split[2]);
                int row = Integer.parseInt(split[3]);

                return new Operations(plugin).getPosition(crate, page, row);
            } catch (NumberFormatException | PatternSyntaxException | NullPointerException e) {
                return null;
            }
        }
        return null;
    }
}