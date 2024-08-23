package br.com.pulse.mushmigrator.util;

import br.com.pulse.mushmigrator.Main;
import br.com.pulse.mushmigrator.libs.SkullUtils;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder {

    private ItemStack item;

    public ItemBuilder(String material) {
        this.item = ((XMaterial)XMaterial.matchXMaterial(material).get()).parseItem();
    }

    public ItemBuilder setDisplayName(String name) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(TextUtil.color(name));
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setLore(TextUtil.color(lore));
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setTexture(String identifier) {
        ItemMeta meta = this.item.getItemMeta();
        if (!(meta instanceof org.bukkit.inventory.meta.SkullMeta))
            return this;
        this.item.setItemMeta((ItemMeta) SkullUtils.applySkin(meta, identifier));
        return this;
    }

    public ItemBuilder setTag(String key, String value) {
        this.item = Main.getInstance().getBedWars().setNmsTag(this.item, key, value);
        return this;
    }

    public ItemStack build() {
        ItemMeta meta = this.item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        this.item.setItemMeta(meta);
        return this.item;
    }

    public static ItemBuilder fromConfig(ConfigurationSection section) {
        ItemBuilder builder = new ItemBuilder(section.getString("material"));
        if (section.contains("displayname"))
            builder.setDisplayName(section.getString("displayname"));
        if (section.contains("lore"))
            builder.setLore(section.getStringList("lore"));
        if (section.contains("texture"))
            builder.setTexture(section.getString("texture"));
        return builder;
    }

    public static String getTag(ItemStack item, String key) {
        String tag = Main.getInstance().getBedWars().getNmsTag(item, key);
        return (tag == null) ? "" : tag;
    }

}
