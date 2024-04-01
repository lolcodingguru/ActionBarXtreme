/* package com.me.actionbarxtreme.handlers;

import com.me.actionbarxtreme.ActionBarXtreme;
import com.me.actionbarxtreme.barMethods.permBarOverrideAnnounce;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class onPlayerBan implements Listener {
    public permBarOverrideAnnounce plugin;
    public ActionBarXtreme plugin1;

    public onPlayerBan(ActionBarXtreme plugin1, permBarOverrideAnnounce plugin) {
        this.plugin = plugin;
        this.plugin1 = plugin1;
    }



    @EventHandler
    public void onPlayerBanEvent(PlayerKickEvent event) {

        if (plugin1.getConfig().getBoolean("Events.OnPlayerBan.Enable")) {
            Player player = event.getPlayer();
            if (player.isBanned()) {
                playerBanAnnounce();
            }
        }
    }

    public void playerBanAnnounce() {
        plugin.actionbarAnnounce(plugin1.getConfig().getInt("Events.OnPlayerBan.Duration"), plugin1.getConfig().getString("Events.OnPlayerBan.Message"));
    }

}
*/