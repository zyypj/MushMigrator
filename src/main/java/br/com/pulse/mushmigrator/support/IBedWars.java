package br.com.pulse.mushmigrator.support;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IBedWars {

    Object getArenaByPlayer(Player paramPlayer);

    String getShopIndexName(Player paramPlayer);

    ItemStack setNmsTag(ItemStack paramItemStack, String paramString1, String paramString2);

    String getNmsTag(ItemStack paramItemStack, String paramString);

    void setQuickBuyCache(Player paramPlayer, String[] paramArrayOfString);
}
