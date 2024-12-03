package org.heroCrates.managers;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.heroCrates.HeroCrates;
import org.heroCrates.animations.Animation;
import org.heroCrates.database.Operations;
import org.heroCrates.gui.CratePreviewGUI;
import org.heroCrates.gui.VirtualKeySelectionGUI;
import org.heroCrates.items.AbstractItem;
import org.heroCrates.items.impl.CrateItem;
import org.heroCrates.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class CratesManager {

    private final List<CrateItem> crates;
    private final HeroCrates plugin;
    private final FileConfiguration config;

    public CratesManager(HeroCrates plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        crates = new ArrayList<>();
    }

    public boolean isCrateName(String name) {
        return crates
                .stream()
                .anyMatch(crateItem -> crateItem.getCrate().type().equalsIgnoreCase(name));
    }

    public List<String> getCrateList() {
        return crates
                .stream()
                .map(crateItem -> crateItem.getCrate().type())
                .toList();
    }

    public Component getDisplayName(String type) {
        return crates
                .stream()
                .filter(crateItem -> crateItem.getCrate().type().equalsIgnoreCase(type))
                .findFirst()
                .map(crateItem -> crateItem.getCrate().displayName())
                .orElse(null);
    }

    public boolean isCrate(ItemStack itemStack) {
        return itemStack.getItemMeta().getPersistentDataContainer().has(AbstractItem.ITEM_KEY, PersistentDataType.STRING);
    }

    public boolean exists(String type) {
        return crates
                .stream()
                .anyMatch(crateItem -> crateItem.getCrate().type().equalsIgnoreCase(type));
    }

    public void giveAward(Player player, String crateType, Location chestLocation, boolean isVirtual) {
        if (!isVirtual) Utils.removePhysicalKey(player);

        List<Map<?, ?>> rewards = config.getMapList("crates." + crateType + ".rewards");
        double totalProbability = rewards.stream()
                .mapToDouble(reward -> {
                    Object probabilityObj = reward.get("probability");
                    if (probabilityObj instanceof Integer) {
                        return ((Integer) probabilityObj).doubleValue();
                    } else if (probabilityObj instanceof Double) {
                        return (Double) probabilityObj;
                    } else {
                        return 0.0;
                    }
                })
                .sum();

        double randomValue = Math.random() * totalProbability;

        for (Map<?, ?> rewardData : rewards) {
            double probability = rewardData.get("probability") instanceof Integer
                    ? ((Integer) rewardData.get("probability")).doubleValue()
                    : (Double) rewardData.get("probability");

            if (randomValue < probability) {
                Material material = Material.getMaterial((String) rewardData.get("item"));
                if (material != null) {
                    player.getInventory().addItem(new ItemStack(material, (int) rewardData.get("amount")));
                    player.sendMessage(Utils.colorize(config.getString("messages.received_item")
                            .replace("{item}", material.name().toLowerCase())
                            .replace("{amount}", String.valueOf(rewardData.get("amount")))));

                    String animationType = config.getString("crates." + crateType.toLowerCase() + ".animation.type");
                    double size = config.getDouble("crates." + crateType.toLowerCase() + ".animation.distance");
                    String particle = config.getString("crates." + crateType.toLowerCase() + ".animation.particle");

                    new Animation(plugin).playAnimation(
                            player,
                            chestLocation.add(0.5, 0.5, 0.5),
                            animationType,
                            size,
                            particle);

                    String sound = config.getString("crates." + crateType.toLowerCase() + ".animation.sound.type").toUpperCase();
                    float volume = (float) config.getDouble("crates." + crateType.toLowerCase() + ".animation.sound.volume");
                    float pitch = (float) config.getDouble("crates." + crateType.toLowerCase() + ".animation.sound.pitch");
                    player.playSound(player.getLocation(), Sound.valueOf(sound), volume, pitch);
                }
                break;
            }
            randomValue -= probability;
        }
    }

    public void handleCrateInteraction(Player player, Action action, Location chestLocation, String crateType, ItemStack item) {
        if (action == Action.LEFT_CLICK_BLOCK) {
            new CratePreviewGUI(plugin, crateType).open(player);
        } else {
            int virtualKeys = new Operations(plugin).countUnusedVirtualKeys(player.getUniqueId(), crateType);
            if (!Utils.isCorrectKey(item, crateType) && virtualKeys <= 0) {
                player.sendMessage(Utils.colorize(config.getString("messages.invalid_key")));
                player.sendMessage(Utils.colorize(config.getString("messages.invalid_key")));
                player.setVelocity(player.getLocation().getDirection().multiply(-0.5).setY(0.3));
            } else if (virtualKeys > 0) {
                new VirtualKeySelectionGUI(plugin, crateType, chestLocation).open(player);
            } else if (Utils.isCorrectKey(item, crateType)) {
                if (!plugin.getKeysManager().startCountdown(player.getUniqueId(), crateType.toLowerCase())) return;
                giveAward(player, crateType.toLowerCase(), chestLocation, false);
                new Operations(plugin).insertPhysicalKey(player.getUniqueId(), crateType.toLowerCase());
            }
        }
    }
}
