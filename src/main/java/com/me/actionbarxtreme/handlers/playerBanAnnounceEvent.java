package com.me.actionbarxtreme.handlers;

import com.me.actionbarxtreme.ActionBarXtreme;
import com.me.actionbarxtreme.permBarOverrideAnnounce;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class playerBanAnnounceEvent implements Listener {

    private  ActionBarXtreme plugin1;

    public playerBanAnnounceEvent(ActionBarXtreme plugin1) {
        this.plugin1 = plugin1;
    }

    private permBarOverrideAnnounce plugin;

    public playerBanAnnounceEvent(permBarOverrideAnnounce plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerBanEvent(PlayerKickEvent event) {

        Player player = event.getPlayer();
        if(player.isBanned()) {
            plugin.actionbarAnnounce(3, player.getDisplayName() + ChatColor.RED + ChatColor.BOLD + " has been banned! Don't Cheat!");
        }
    }

}
