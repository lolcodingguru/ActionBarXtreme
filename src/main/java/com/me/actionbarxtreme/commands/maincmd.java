package com.me.actionbarxtreme.commands;

import com.me.actionbarxtreme.ActionBarXtreme;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class maincmd implements CommandExecutor {

    private final ActionBarXtreme plugin;

    public maincmd(ActionBarXtreme plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(strings.length == 1) {
            if (strings[0].equalsIgnoreCase("reload")) {
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
                commandSender.sendMessage(ChatColor.AQUA + "Available Commands:\n/abx reload - Reloads the plugin.");
            }
        } else {
            commandSender.sendMessage(ChatColor.AQUA + "Available Commands:\n/abx reload - Reloads the plugin");
        }
        return false;
    }
}