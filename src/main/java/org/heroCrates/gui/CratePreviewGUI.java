package org.heroCrates.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.heroCrates.HeroCrates;
import org.heroCrates.utils.Utils;

import java.util.List;
import java.util.Map;

public class CratePreviewGUI implements InventoryProvider {

    private final FileConfiguration config;
    private final SmartInventory inventory;
    private final String crateType;

    public CratePreviewGUI(HeroCrates plugin, String crateType) {
        this.config = plugin.getConfig();
        this.crateType = crateType.toLowerCase();
        this.inventory = SmartInventory.builder()
                .manager(plugin.getInventoryManager())
                .id("crate-preview-gui")
                .provider(this)
                .size(6, 9)
                .title(config.getString("crates." + this.crateType + ".gui_title")
                        .replace("{crate}", crateType).replace("&", "§"))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        ConfigurationSection crateConfig = config.getConfigurationSection("crates." + crateType);
        if (crateConfig == null) return;

        List<Map<?, ?>> rewards = crateConfig.getMapList("rewards");
        int slot = 0;

        for (Map<?, ?> rewardData : rewards) {
            String itemType = (String) rewardData.get("item");
            int amount = (int) rewardData.get("amount");
            Material material = Material.getMaterial(itemType);
            if (material == null) continue;

            ItemStack item = new ItemStack(material, amount);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.displayName(Utils.colorize((String) rewardData.get("name")));
                meta.lore(((List<String>) rewardData.get("lore")).stream()
                        .map(Utils::colorize)
                        .toList());
                item.setItemMeta(meta);
            }

            contents.set(slot / 9, slot % 9, ClickableItem.empty(item));
            if (++slot >= 54) break;
        }
    }


    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }

    public void open(Player player) {
        this.inventory.open(player);
    }
}
