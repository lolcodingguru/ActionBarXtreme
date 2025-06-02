package com.me.actionbarxtreme.handlers;

import com.me.actionbarxtreme.ActionBarXtreme;
import com.me.actionbarxtreme.barMethods.permBarOverrideAnnounce;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.entity.Guardian;

public class onElderGuardianDeath implements Listener {

    private final ActionBarXtreme plugin;
    private final permBarOverrideAnnounce plugin1;

    public onElderGuardianDeath(ActionBarXtreme plugin, permBarOverrideAnnounce plugin1) {
        this.plugin = plugin;
        this.plugin1 = plugin1;
    }

    public void ElderGuardianDeathAnnounceEvent() {
        plugin1.actionbarAnnounce(plugin.getConfig().getInt("Events.OnElderGuardianDeath.Duration"), plugin.getConfig().getString("Events.OnElderGuardianDeath.Message"));
    }

    @EventHandler
    public void onElderGuardianDeathEvent(EntityDeathEvent event) {
        if(plugin.getConfig().getBoolean("Events.OnElderGuardianDeath.Enable")) {
            if (event.getEntity() instanceof Guardian && ((Guardian) event.getEntity()).isElder()) {
                ElderGuardianDeathAnnounceEvent();
            }
        }
    }


}
