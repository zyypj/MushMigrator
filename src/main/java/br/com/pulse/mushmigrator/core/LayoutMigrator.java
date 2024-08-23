package br.com.pulse.mushmigrator.core;

import br.com.pulse.mushmigrator.Main;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class LayoutMigrator {

    private final Main plugin;
    private final Set<UUID> migratingPlayers = new HashSet<>();

    public LayoutMigrator(Main plugin) {
        this.plugin = plugin;
    }

    public void migrate(Player player) {
        if (migratingPlayers.contains(player.getUniqueId())) {
            player.sendMessage("§cVocê já está migrando. Aguarde a migração atual terminar.");
            return;
        }

        migratingPlayers.add(player.getUniqueId());
        applyLayout(player, player.getName());
    }

    private void applyLayout(Player player, String playerName) {
        player.sendMessage("§eMigração iniciada...");
        player.sendMessage("§eProcurando seu perfil no MushMC.");
        fetchProfile(playerName, response -> {
            if (response == null || !response.get("success").getAsBoolean()) {
                player.sendMessage("§cPerfil não encontrado no MushMC!");
                return;
            }

            JsonObject quickBuyLayout = response.getAsJsonObject("response").getAsJsonObject("quick_buy_layout");
            if (quickBuyLayout == null) {
                player.sendMessage("§cLayout não encontrado no perfil do mushmc!");
                return;
            }

            JsonArray itemsArray = quickBuyLayout.getAsJsonArray("items");
            if (itemsArray == null) {
                player.sendMessage("§cItens de layout não encontrados no perfil do mushmc!");
                return;
            }

            String[] items = new String[21];
            for (int i = 0; i < items.length; i++) {
                JsonElement itemElement = itemsArray.get(i);
                if (itemElement != null && !itemElement.isJsonNull()) {
                    items[i] = itemElement.getAsString();
                } else {
                    items[i] = "null";
                }
            }

            this.plugin.getBedWars().setQuickBuyCache(player, items);
            player.sendMessage("§aMigração concluída com sucesso!");
            migratingPlayers.remove(player.getUniqueId());
        });
    }

    private void fetchProfile(String playerName, Consumer<JsonObject> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String urlString = "https://mush.com.br/api/player/name/" + playerName + "/bedwars";
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");

                Bukkit.getLogger().info("Requesting URL: " + urlString);

                if (connection.getResponseCode() == 200) {
                    InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                    JsonObject json = (new JsonParser()).parse(reader).getAsJsonObject();
                    Bukkit.getLogger().info("Response from API: " + json.toString());
                    callback.accept(json);
                } else {
                    Bukkit.getLogger().info("Failed to fetch profile: HTTP " + connection.getResponseCode());
                    callback.accept(null);
                }
                connection.disconnect();
            } catch (IOException ex) {
                ex.printStackTrace();
                callback.accept(null);
            }
        });
    }

    public boolean isMigrating(UUID playerUUID) {
        return migratingPlayers.contains(playerUUID);
    }
}