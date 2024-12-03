package org.heroCrates.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.heroCrates.HeroCrates;
import org.heroCrates.objects.Crate;
import org.heroCrates.items.impl.CrateItem;
import org.heroCrates.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GiveCratesCommand implements CommandExecutor, TabExecutor {
    private final FileConfiguration config;
    private final HeroCrates plugin;

    public GiveCratesCommand(HeroCrates plugin) {
        PluginCommand command = plugin.getCommand("givecrate");
        Objects.requireNonNull(command).setTabCompleter(this);
        command.setExecutor(this);
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Utils.colorize(config.getString("messages.only_players")));
            return true;
        }

        if (!player.hasPermission("herocrates.givecrate")) {
            player.sendMessage(Utils.colorize(config.getString("messages.no_permission")));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(Utils.colorize(config.getString("messages.player_not_found")));
            return true;
        }

        if (!plugin.getCratesManager().isCrateName(args[1])) {
            player.sendMessage(Utils.colorize(config.getString("messages.invalid_crate_type")));
            return true;
        }

        if (target.getInventory().firstEmpty() == -1) {
            if (player == target) {
                player.sendMessage(Utils.colorize(config.getString("messages.inventory_full_self")));
            } else {
                player.sendMessage(Utils.colorize(config.getString("messages.inventory_full_other").replace("{player}", target.getName())));
            }
            return true;
        }

        if (player == target) {
            player.sendMessage(Utils.colorize(config.getString("messages.received_crate").replace("{crate}", Utils.decolorize(plugin.getCratesManager().getDisplayName(args[1])))));
        } else {
            player.sendMessage(Utils.colorize(config.getString("messages.given_crate").replace("{crate}", Utils.decolorize(plugin.getCratesManager().getDisplayName(args[1])).replace("{player}", target.getName()))));
            target.sendMessage(Utils.colorize(config.getString("messages.received_crate").replace("{crate}", Utils.decolorize(plugin.getCratesManager().getDisplayName(args[1])))));
        }

        int amount = 1;
        if (args.length >= 3) {
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                player.sendMessage(Utils.colorize(config.getString("messages.invalid_amount")));
                return true;
            }
            if(amount >= 64) {
                player.sendMessage(Utils.colorize(config.getString("messages.invalid_amount")));
                return true;
            }
        }

        Component displayName = Utils.colorize(plugin.getConfig().getString("crates." + args[1].toLowerCase() + ".display_name"));
        new CrateItem(plugin, new Crate(null, args[1], displayName)).giveItem(target, amount);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return null;
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(player.getServer().getOnlinePlayers().stream().map(Player::getName).toList());
        } else if (args.length == 2) {
            completions.addAll(plugin.getCratesManager().getCrateList());
        } else if (args.length == 3) {
            for (int i = 1; i <= 64; i++) {
                completions.add(String.valueOf(i));
            }
        }
        return StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<>());
    }
}
