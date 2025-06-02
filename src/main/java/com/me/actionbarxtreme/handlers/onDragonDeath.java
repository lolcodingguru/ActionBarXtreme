package com.me.actionbarxtreme.handlers;

import com.me.actionbarxtreme.ActionBarXtreme;
import com.me.actionbarxtreme.barMethods.permBarOverrideAnnounce;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class onDragonDeath implements Listener {

    public ActionBarXtreme plugin;
    public permBarOverrideAnnounce plugin1;

    public onDragonDeath(ActionBarXtreme plugin, permBarOverrideAnnounce plugin1) {
        this.plugin = plugin;
        this.plugin1 = plugin1;
    }

    @EventHandler
    public void onDragonDeathEvent(EntityDeathEvent event) {
        if(plugin.getConfig().getBoolean("Events.OnDragonDeath.Enable")) {
            if (event.getEntity() instanceof EnderDragon) {
                DragonDeathAnnounceEvent();
            }
        }
     }

     public void DragonDeathAnnounceEvent() {
        plugin1.actionbarAnnounce(plugin.getConfig().getInt("Events.OnDragonDeath.Duration"), plugin.getConfig().getString("Events.OnDragonDeath.Message"));
    }

}