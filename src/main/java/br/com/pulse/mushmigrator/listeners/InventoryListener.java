package br.com.pulse.mushmigrator.listeners;

import br.com.pulse.mushmigrator.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class InventoryListener implements Listener {

    private final Main plugin;
    private final HashMap<UUID, Long> cooldowns = new HashMap<UUID, Long>();

    public InventoryListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onShopOpen(InventoryOpenEvent e) {
        Player player = (Player)e.getPlayer();
        Object arena = this.plugin.getBedWars().getArenaByPlayer(player);
        if (arena == null)
            return;
        if (e.getView().getTitle().equals(this.plugin.getBedWars().getShopIndexName(player))) {
            // Create the ItemStack for the Mush Migrator
            ItemStack migratorItem = new ItemStack(Material.PAPER);
            ItemMeta meta = migratorItem.getItemMeta();
            meta.setDisplayName("§c§lMush Migrator");
            meta.setLore(Arrays.asList("§7Migre sua loja do §c§lMushMC", "", "§eClique para migrar"));
            migratorItem.setItemMeta(meta);

            // Set the item in the specified slots
            e.getInventory().setItem(52, migratorItem);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
            return;
        Player player = (Player)e.getWhoClicked();
        UUID playerUUID = player.getUniqueId();
        Object arena = this.plugin.getBedWars().getArenaByPlayer(player);
        if (arena == null)
            return;

        if (e.getSlot() == 52 && e.getCurrentItem().getType() == Material.PAPER) {
            long currentTime = System.currentTimeMillis();
            if (cooldowns.containsKey(playerUUID)) {
                long lastClickTime = cooldowns.get(playerUUID);
                if (currentTime - lastClickTime < 5 * 60 * 1000) { // 5 minutos em milissegundos
                    player.sendMessage("§cVocê precisa esperar 5 minutos antes de clicar novamente.");
                    return;
                }
            }

            cooldowns.put(playerUUID, currentTime);
            player.closeInventory();
            this.plugin.getMigrator().migrate(player);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent e) {
        if (this.plugin.getMigrator().isMigrating(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§cVocê não pode interagir com o NPC enquanto está migrando.");
        }
    }
}
