package org.heroCrates.managers;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.heroCrates.HeroCrates;
import org.heroCrates.dto.Crate;
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

    public CratesManager(HeroCrates plugin) {
        this.plugin = plugin;
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

    public void giveAward(Player player, String crateType) {
        Utils.removePhysicalKey(player);

        List<Map<?, ?>> rewards = plugin.getConfig().getMapList("crates." + crateType + ".rewards");
        double totalProbability = rewards.stream()
                .mapToDouble(reward -> {
                    Object probabilityObj = reward.get("probability");
                    if (probabilityObj instanceof Integer) {
                        return ((Integer) probabilityObj).doubleValue();
                    } else if (probabilityObj instanceof Double) {
                        return (Double) probabilityObj;
                    } else {
                        plugin.getLogger().warning("Invalid probability type: " + probabilityObj.getClass().getName());
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
                }
                break;
            }
            randomValue -= probability;
        }
    }
}
