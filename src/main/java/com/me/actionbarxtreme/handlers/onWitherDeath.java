/*package com.me.actionbarxtreme.handlers;

import com.me.actionbarxtreme.ActionBarXtreme;
import com.me.actionbarxtreme.barMethods.permBarOverrideAnnounce;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class onWitherDeath implements Listener {

    private final ActionBarXtreme plugin;
    private final permBarOverrideAnnounce plugin1;

    public onWitherDeath(ActionBarXtreme plugin, permBarOverrideAnnounce plugin1) {
        this.plugin = plugin;
        this.plugin1 = plugin1;
    }

    public void WitherDeathAnnounceEvent() {
        plugin1.actionbarAnnounce(plugin.getConfig().getInt("Events.OnWitherDeath.Duration"), plugin.getConfig().getString("Events.OnWitherDeath.Message"));
    }

    @EventHandler
    public void onWitherDeathEvent(EntityDeathEvent event) {
        if(plugin.getConfig().getBoolean("Events.OnWitherDeath.Enable")) {
            if (event.getEntity() instanceof Wither) {
                WitherDeathAnnounceEvent();
            }
        }
    }



}
*/