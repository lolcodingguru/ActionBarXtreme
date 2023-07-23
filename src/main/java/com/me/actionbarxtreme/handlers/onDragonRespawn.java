package com.me.actionbarxtreme.handlers;

import com.me.actionbarxtreme.ActionBarXtreme;
import com.me.actionbarxtreme.barMethods.permBarOverrideAnnounce;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class onDragonRespawn implements Listener {

    private final ActionBarXtreme plugin;
    private final permBarOverrideAnnounce plugin1;

    public onDragonRespawn(ActionBarXtreme plugin, permBarOverrideAnnounce plugin1) {
        this.plugin = plugin;
        this.plugin1 = plugin1;
    }

    @EventHandler
    public void onDragonRespawnEvent(EntitySpawnEvent event) {
        if (event.getEntity() instanceof org.bukkit.entity.EnderDragon && plugin.getConfig().getBoolean("Events.OnDragonRespawn.Enable")) {
            DragonRespawnEvent();
        }
    }

    public void DragonRespawnEvent() {
        String message = plugin.getConfig().getString("Events.OnDragonRespawn.Message");
        plugin1.actionbarAnnounce(plugin.getConfig().getInt("Events.OnDragonRespawn.Duration"), message);
    }
}
