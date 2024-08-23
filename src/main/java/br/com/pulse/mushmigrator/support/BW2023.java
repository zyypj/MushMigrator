package br.com.pulse.mushmigrator.support;

import br.com.pulse.mushmigrator.core.LayoutItem;
import com.tomkeuper.bedwars.api.BedWars;
import com.tomkeuper.bedwars.api.arena.shop.ICategoryContent;
import com.tomkeuper.bedwars.api.language.Language;
import com.tomkeuper.bedwars.api.shop.IPlayerQuickBuyCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Iterator;

public class BW2023 implements IBedWars{
    BedWars instance = Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider();

    public Object getArenaByPlayer(Player player) {
        return instance.getArenaUtil().getArenaByPlayer(player);
    }

    public String getShopIndexName(Player player) {
        return Language.getMsg(player, "shop-items-messages.inventory-name");
    }

    public ItemStack setNmsTag(ItemStack item, String key, String value) {
        return this.instance.getVersionSupport().setTag(item, key, value);
    }

    public String getNmsTag(ItemStack item, String key) {
        return this.instance.getVersionSupport().getTag(item, key);
    }

    public void setQuickBuyCache(Player player, String[] items) {
        IPlayerQuickBuyCache cache = this.instance.getShopUtil().getPlayerQuickBuyCache().getQuickBuyCache(player.getUniqueId());
        Iterator<String> it = Arrays.stream(items).iterator();
        for (int i = 19; i < 44; i++) {
            if (i != 26 && i != 27 && i != 35 && i != 36) {
                String item = it.next();
                if (item == null || item.equals("null")) {
                    cache.setElement(i, (ICategoryContent)null);
                } else {
                    LayoutItem layoutItem = LayoutItem.matchItem(item);
                    if (layoutItem != null) {
                        String category = layoutItem.getCategory();
                        cache.setElement(i, category);
                    } else {
                        cache.setElement(i, (ICategoryContent)null);
                    }
                }
            }
        }
        cache.pushChangesToDB();
    }
}