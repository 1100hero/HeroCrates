package org.heroCrates.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.heroCrates.items.AbstractItem;

public class Utils {
    public static Component colorize(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message)
                .decoration(TextDecoration.ITALIC, false);
    }

    public static String decolorize(Component component) {
        return LegacyComponentSerializer.legacyAmpersand().serialize(component);
    }

    public static boolean isAnAbstractItem(ItemStack item) {
        return item.hasItemMeta()
                && item.getPersistentDataContainer().get(AbstractItem.ITEM_KEY, PersistentDataType.STRING) != null;
    }

    public static boolean isCorrectKey(ItemStack item, String crateType) {
        if (item == null) return false;
        if (!item.hasItemMeta()) return false;
        String keyType = item.getPersistentDataContainer().get(AbstractItem.ITEM_KEY, PersistentDataType.STRING);
        return keyType != null && keyType.equals(crateType + "_key");
    }

    public static void removePhysicalKey(Player player) {
        ItemStack keyItem = player.getInventory().getItemInMainHand();
        if (keyItem.getAmount() > 1) {
            keyItem.setAmount(keyItem.getAmount() - 1);
        } else {
            player.getInventory().removeItem(keyItem);
        }
    }
}