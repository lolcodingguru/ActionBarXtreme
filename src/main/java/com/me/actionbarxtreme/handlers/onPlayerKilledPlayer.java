/* package com.me.actionbarxtreme.handlers;

import com.me.actionbarxtreme.ActionBarXtreme;
import com.me.actionbarxtreme.barMethods.permBarOverrideAnnounce;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.entity.Player;

public class onPlayerKilledPlayer implements Listener {

    private final ActionBarXtreme plugin;
    private final permBarOverrideAnnounce plugin1;

    public onPlayerKilledPlayer(ActionBarXtreme plugin, permBarOverrideAnnounce plugin1) {
        this.plugin = plugin;
        this.plugin1 = plugin1;
    }


    public void PlayerKilledPlayerAnnounceEvent(Player killer, Player killed) {



        plugin1.actionbarAnnounce(plugin.getConfig().getInt("Events.OnPlayerKilledPlayer.Duration"), plugin.getConfig().getString("Events.OnPlayerKilledPlayer.Message").replace("%killer%", killer.getName()).replace("%killed%", killed.getName()));
    }

    @EventHandler
    public void onPlayerKilledPlayerEvent(EntityDeathEvent event) {

        if(plugin.getConfig().getBoolean("Events.OnPlayerKilledPlayer.Enable")
                && event.getEntity().getKiller() != null
                && event.getEntity().getKiller() instanceof Player
                && event.getEntity().getType().equals(EntityType.PLAYER)
                && !event.getEntity().getKiller().equals(event.getEntity())) {
            Player killer = event.getEntity().getKiller();
            Player killed = (Player) event.getEntity();
                        PlayerKilledPlayerAnnounceEvent(killer, killed);

        }
    }
}
*/