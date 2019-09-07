package dev.anullihate.nemissentials;

import org.itxtech.nemisys.Client;
import org.itxtech.nemisys.Player;
import org.itxtech.nemisys.event.EventHandler;
import org.itxtech.nemisys.event.Listener;
import org.itxtech.nemisys.event.player.PlayerChatEvent;
import org.itxtech.nemisys.event.server.QueryRegenerateEvent;
import org.itxtech.nemisys.plugin.PluginBase;

public class Main extends PluginBase implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(this, this);

    }

    @EventHandler
    public void onQueryRegenerate(QueryRegenerateEvent event) {
        int count = 0;
        for (Client client : getServer().getClients().values()) {
            count += client.getMaxPlayers();
        }
        event.setMaxPlayerCount(count);
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String msgFormat = getConfig().getString("chat_format")
                .replace("%server%", player.getClient().getDescription())
                .replace("%player%", player.getName())
                .replace("%message%", event.getMessage()).replaceAll("§", "\u00A7");

        if (getConfig().getStringList("chat_disabled_from").contains(player.getClient().getDescription())) {
            return;
        }

        for (Client client : getServer().getClients().values()) {
            if (getConfig().getStringList("chat_disabled_to").contains(client.getDescription())) {
                continue;
            }

            if (!client.getDescription().equals(player.getClient().getDescription())) {
                client.getPlayers().forEach((u, pl) -> {
                    pl.sendMessage(msgFormat);
                });
            }
        }
    }
}
