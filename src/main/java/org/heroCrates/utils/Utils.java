package org.heroCrates.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class Utils {
    public static Component colorize(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message)
                .decoration(TextDecoration.ITALIC, false);
    }
}
