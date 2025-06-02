package com.me.actionbarxtreme.handlers;

import com.me.actionbarxtreme.ActionBarXtreme;
import com.me.actionbarxtreme.barMethods.permBarOverrideAnnounce;
import com.me.actionbarxtreme.utils.logging;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class onPlayerKick implements Listener {

    private final ActionBarXtreme plugin;
    private final permBarOverrideAnnounce plugin1;
    public onPlayerKick(ActionBarXtreme plugin, permBarOverrideAnnounce plugin1) {
        this.plugin = plugin;
        this.plugin1 = plugin1;
    }

    public void PlayerKickAnnounceEvent() {
        String message = plugin.getConfig().getString("Events.OnPlayerKick.Message");
        plugin1.actionbarAnnounce(plugin.getConfig().getInt("Events.OnPlayerKick.Duration"), message);
    }

    @EventHandler
    public void onPlayerKickEvent(PlayerKickEvent event) {
        if (plugin.getConfig().getBoolean("Events.OnPlayerKick.Enable")) {
            if(!event.getPlayer().isBanned()) {
                String kickMessage = event.getReason();
                PlayerKickAnnounceEvent();
            }
        }
    }

}

