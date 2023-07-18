package com.me.actionbarxtreme.commands;

import com.me.actionbarxtreme.ActionBarXtreme;
import com.me.actionbarxtreme.permBarOverrideAnnounce;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class maincmd implements CommandExecutor {

    private permBarOverrideAnnounce plugin1;
    private final ActionBarXtreme plugin;

    public maincmd(ActionBarXtreme plugin, permBarOverrideAnnounce plugin1) {
        this.plugin = plugin;
        this.plugin1 = plugin1;
    }

    String AvailableCommands = ChatColor.AQUA + "Available Commands:\n/abx reload - Reloads the plugin.\n/abx announce [duration] <message> - Announces a message to all players through the actionbar.";

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {


        switch(strings.length) {

            case 0:
                commandSender.sendMessage(AvailableCommands);
                break;

            case 1:
                if(strings[0].equalsIgnoreCase("reload")) {
                    if(commandSender.hasPermission("abx.reload")) {

                        if(plugin.task!=null) {
                            plugin.task.cancel();
                        }
                        plugin.reloadConfig();
                        plugin.getPluginLoader().disablePlugin(plugin);
                        plugin.getPluginLoader().enablePlugin(plugin);
                        commandSender.sendMessage(ChatColor.LIGHT_PURPLE + "[ABX] " + ChatColor.RESET + ChatColor.GREEN + "Plugin sucessfully reloaded.");
                        Bukkit.getLogger().info("[ABX] Plugin was reloaded");
                    } else {
                        commandSender.sendMessage(ChatColor.RED + "Insuffiecient Permission.");
                    }
                } else {
                    commandSender.sendMessage(AvailableCommands);
                }
                break;

            case 2:

            default:
                if(strings[0].equalsIgnoreCase("announce") ||
                        strings[0].equalsIgnoreCase("broadcast") ||
                        strings[0].equalsIgnoreCase("bc") &&
                                strings[1] != null) {

                    if(commandSender.hasPermission("abx.announce")) {
                        int duration = 3;
                        if (strings[1].matches("\\d+s")) {

                            duration = Integer.parseInt(strings[1].substring(0, strings[1].length() - 1));

                            strings = java.util.Arrays.copyOfRange(strings, 2, strings.length);
                        } else {
                            strings = java.util.Arrays.copyOfRange(strings, 1, strings.length);
                            commandSender.sendMessage( ChatColor.AQUA + "[ABX] " + ChatColor.RESET + ChatColor.RED + "Duration not specified, using default duration of 5 seconds.");
                        }

                        String message = String.join(" ", strings);
                        plugin1.actionbarAnnounce(duration, "&4&l[Broadcast] &r&c" + message);
                    } else {
                        commandSender.sendMessage(ChatColor.RED + "Insufficient Permission.");
                    }
                } else {
                    commandSender.sendMessage(AvailableCommands);
                }
        }

        return false;
    }
}