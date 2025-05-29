package com.me.actionbarxtreme.utils;

// import org.bukkit.Bukkit;
//import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
//import org.bukkit.entity.Player;
// import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class tabComplete implements TabCompleter {


    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> tabCompletions = new ArrayList<>();

        if (strings.length == 1) {
            tabCompletions.addAll(Arrays.asList("reload", "announce", "announceToPlayer"/*"forceeventannounce"*/));
            return tabCompletions;
        } else if (strings[0].equalsIgnoreCase("announce") || strings[0].equalsIgnoreCase("broadcast") || strings[0].equalsIgnoreCase("bc")) {
            if (strings.length == 2) {
                tabCompletions.addAll(Arrays.asList("3s", "4s", "5s", "6s", "7s", "8s", "9s", "10s"));
                return tabCompletions;
            }
        } else if (strings[0].equalsIgnoreCase("announceToPlayer")) {
            if (strings.length == 2) {
                // Return list of online player names
                return null;
            } else if (strings.length == 3) {
                tabCompletions.addAll(Arrays.asList("3s", "4s", "5s", "6s", "7s", "8s", "9s", "10s"));
                return tabCompletions;
            }
        }

        /*else if (strings[0].equalsIgnoreCase("forceeventannounce")) {
            if (strings.length == 2) {
                return List.of("onplayerban", "ondragondeath", "onwitherdeath", "onplayerkick", "onelderguardiandeath", "onwardendeath", "onplayerkilledplayer");
            }
        } else if (strings[0].equalsIgnoreCase("forceeventannounce") && strings[1].equalsIgnoreCase("onplayerkilledplayer")) {
            if (strings.length == 3) {
                return getOnlinePlayerNames();
            } else if (strings.length == 4) {
                return getOnlinePlayerNames();
            }
        }
        */
        return null;
    }
/*
    private List<String> getOnlinePlayerNames() {
        List<String> playerNames = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerNames.add(player.getName());
        }
        return playerNames;
    }
    */
}
