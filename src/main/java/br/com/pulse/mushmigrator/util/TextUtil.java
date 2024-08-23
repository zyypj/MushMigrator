package br.com.pulse.mushmigrator.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public final class TextUtil {
    private TextUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> color(List<String> list) {
        list.replaceAll(TextUtil::color);
        return list;
    }

    public static void sendMessage(Player player, String message) {
        player.sendMessage(color(message));
    }
}
