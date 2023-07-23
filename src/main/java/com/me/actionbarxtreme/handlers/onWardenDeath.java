package com.me.actionbarxtreme.handlers;

import com.me.actionbarxtreme.ActionBarXtreme;
import com.me.actionbarxtreme.barMethods.permBarOverrideAnnounce;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.entity.Warden;


public class onWardenDeath implements Listener {

    private final ActionBarXtreme plugin;
    private final permBarOverrideAnnounce plugin1;

    public onWardenDeath(ActionBarXtreme plugin, permBarOverrideAnnounce plugin1) {
        this.plugin = plugin;
        this.plugin1 = plugin1;
    }


    @EventHandler
    public void onWardenDeathEvent(EntityDeathEvent event) {
        if (plugin.wardenSupported && event.getEntity() instanceof Warden && plugin.getConfig().getBoolean("Events.OnWardenDeath.Enable")) {
                WardenDeathAnnounceEvent();
            }
        }


    public void WardenDeathAnnounceEvent() {
        plugin1.actionbarAnnounce(plugin.getConfig().getInt("Events.OnWardenDeath.Duration"), plugin.getConfig().getString("Events.OnWardenDeath.Message"));
    }
}
