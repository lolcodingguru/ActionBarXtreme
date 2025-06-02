package com.me.actionbarxtreme.commands;

import com.me.actionbarxtreme.ActionBarXtreme;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.me.actionbarxtreme.handlers.onPlayerBan;
import com.me.actionbarxtreme.handlers.onDragonDeath;
import com.me.actionbarxtreme.handlers.onWitherDeath;
import com.me.actionbarxtreme.handlers.onPlayerKick;
import com.me.actionbarxtreme.handlers.onElderGuardianDeath;
import com.me.actionbarxtreme.handlers.onWardenDeath;
import com.me.actionbarxtreme.handlers.onPlayerKilledPlayer;



public class eventForce {

    public ActionBarXtreme plugin;
    public onPlayerBan onPlayerBan;
    public onDragonDeath onDragonDeath;
    public onWitherDeath onWitherDeath;
    public onPlayerKick onPlayerKick;
    public onElderGuardianDeath onElderGuardianDeath;
    public onWardenDeath onWardenDeath;
    public onPlayerKilledPlayer onPlayerKilledPlayer;


    public eventForce(ActionBarXtreme plugin, onPlayerBan onPlayerBan, onDragonDeath onDragonDeath, onWitherDeath onWitherDeath, onPlayerKick onPlayerKick, onElderGuardianDeath onElderGuardianDeath, onWardenDeath onWardenDeath, onPlayerKilledPlayer onPlayerKilledPlayer) {
        this.plugin = plugin;
        this.onPlayerBan = onPlayerBan;
        this.onDragonDeath = onDragonDeath;
        this.onWitherDeath = onWitherDeath;
        this.onPlayerKick = onPlayerKick;
        this.onElderGuardianDeath = onElderGuardianDeath;
        this.onWardenDeath = onWardenDeath;
        this.onPlayerKilledPlayer = onPlayerKilledPlayer;
    }


    public void forceEventPlayerKilledPlayer(CommandSender commandSender ,Player killed, Player killer) {

        if(commandSender.hasPermission("actionbarxtreme.forceevent.onplayerkilledplayer")) {
            if(plugin.getConfig().getBoolean("Events.OnPlayerKilledPlayer.allowForceEvent")) {
                onPlayerKilledPlayer.PlayerKilledPlayerAnnounceEvent(killed, killer);
            } else {
                commandSender.sendMessage("§cForcing this event is disabled in the configuration file!");
            }
        } else {
            commandSender.sendMessage("§cYou do not have permission to force this event.");
        }

    }

    public void forceEventAll(CommandSender commandSender, String eventToForce) {

        switch(eventToForce.toUpperCase()){

            case "ONPLAYERBAN":
                if(commandSender.hasPermission("actionbarxtreme.forceevent.onplayerban") || commandSender.hasPermission("actionbarxtreme.forceevent.*")) {
                    if(plugin.getConfig().getBoolean("Events.OnPlayerBan.allowForceEvent")) {
                        onPlayerBan.playerBanAnnounce();
                        commandSender.sendMessage("§a[ABX] Forced event successfully: §eOnPlayerBan");
                    } else {
                        commandSender.sendMessage("§c[ABX] Forcing this event is disabled in the configuration file!");
                    }
                } else {
                    commandSender.sendMessage("§cYou do not have permission to force this event.");
                }
                break;

            case "ONDRAGONDEATH":
                if(commandSender.hasPermission("actionbarxtreme.forceevent.ondragondeath") || commandSender.hasPermission("actionbarxtreme.forceevent.*")) {
                    if(plugin.getConfig().getBoolean("Events.OnDragonDeath.allowForceEvent")) {
                        onDragonDeath.DragonDeathAnnounceEvent();
                        commandSender.sendMessage("§a[ABX] Forced event: §eOnDragonDeath");
                    } else {
                        commandSender.sendMessage("§c[ABX] Forcing this event is disabled in the configuration file!");
                    }
                } else {
                    commandSender.sendMessage("§cYou do not have permission to force this event.");
                }
                break;



            case "ONWITHERDEATH":
                if(commandSender.hasPermission("actionbarxtreme.forceevent.onwitherdeath")) {
                    if(plugin.getConfig().getBoolean("Events.OnWitherDeath.allowForceEvent")) {
                        onWitherDeath.WitherDeathAnnounceEvent();
                        commandSender.sendMessage("§a[ABX] Forced event successfully: §eOnWitherDeath");
                    } else {
                        commandSender.sendMessage("§c[ABX] Forcing this event is disabled in the configuration file!");
                    }
                } else {
                    commandSender.sendMessage("§cYou do not have permission to force this event.");
                }
                break;

            case "ONPLAYERKICK":
                if(commandSender.hasPermission("actionbarxtreme.forceevent.onplayerkick")) {
                    if(plugin.getConfig().getBoolean("Events.OnPlayerKick.allowForceEvent")) {
                        onPlayerKick.PlayerKickAnnounceEvent();
                        commandSender.sendMessage("§a[ABX] Forced event successfully: §eOnPlayerKick");
                    } else {
                        commandSender.sendMessage("§c[ABX] Forcing this event is disabled in the configuration file!");
                    }
                } else {
                    commandSender.sendMessage("§cYou do not have permission to force this event.");
                }
                break;

            case "ONELDERGUARDIANDEATH":
                if(commandSender.hasPermission("actionbarxtreme.forceevent.onelderguardiandeath")) {
                    if(plugin.getConfig().getBoolean("Events.OnElderGuardianDeath.allowForceEvent")) {
                        onElderGuardianDeath.ElderGuardianDeathAnnounceEvent();
                        commandSender.sendMessage("§a[ABX] Forced event successfully: §eOnElderGuardianDeath");
                    } else {
                        commandSender.sendMessage("§c[ABX] Forcing this event is disabled in the configuration file!");
                    }
                } else {
                    commandSender.sendMessage("§cYou do not have permission to force this event.");
                }
                break;

            case "ONWARDENDEATH":
                if(commandSender.hasPermission("actionbarxtreme.forceevent.onwardendeath")) {
                    if(plugin.getConfig().getBoolean("Events.OnWardenDeath.allowForceEvent")) {
                        onWardenDeath.WardenDeathAnnounceEvent();
                        commandSender.sendMessage("§a[ABX] Forced event successfully: §eOnWardenDeath");
                    } else {
                        commandSender.sendMessage("§c[ABX] Forcing this event is disabled in the configuration file!");
                    }
                } else {
                    commandSender.sendMessage("§cYou do not have permission to force this event.");
                }
                break;

            default:
                if(commandSender.hasPermission("actionbarxtreme.forceevent")) {
                    commandSender.sendMessage("§c[ABX] Invalid event name.");
                } else {
                    commandSender.sendMessage("§c[ABX] You do not have permission to force this event.");
                }
                break;
        }


    }
}