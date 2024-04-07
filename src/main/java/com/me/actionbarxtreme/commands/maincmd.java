package com.me.actionbarxtreme.commands;

import com.me.actionbarxtreme.ActionBarXtreme;
import com.me.actionbarxtreme.barMethods.permBarOverrideAnnounce;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
// import org.bukkit.entity.Player;


public class maincmd implements CommandExecutor {

    private final permBarOverrideAnnounce plugin1;
    private final ActionBarXtreme plugin;
//    private final eventForce eventForce;

    public maincmd(ActionBarXtreme plugin, permBarOverrideAnnounce plugin1/*eventForce eventForce*/) {
        this.plugin = plugin;
        this.plugin1 = plugin1;
//        this.eventForce = eventForce;
    }

    String AvailableCommands =
            ChatColor.AQUA + "Available Commands:\n" +
                    ChatColor.RESET + "/abx reload" + ChatColor.GRAY + " - Reloads the plugin.\n" +
                    ChatColor.RESET + "/abx announce " + ChatColor.YELLOW + "[duration] <message>" + ChatColor.GRAY + " - Announces a message to ALL players through the actionbar." +
                    ChatColor.RESET + "/abx announceToPlayer " + ChatColor.YELLOW + "<player> [duration] <message>" + ChatColor.GRAY + " - Announces a message to a specific player through the actionbar."
            /* + ChatColor.RESET + "/abx forceeventannounce " + ChatColor.YELLOW + "<event>" + ChatColor.GRAY + " - Forces an event to announce."*/;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(strings.length == 0) {
            commandSender.sendMessage(AvailableCommands);
            return true;
        }

        if (strings.length >= 3 && strings[0].equalsIgnoreCase("announceToPlayer")) {
            if (commandSender.hasPermission("abx.announceToPlayer")) {
                if (plugin.getConfig().getBoolean("Announcements.Enable")) {
                    int duration = plugin.getConfig().getInt("Announcements.defaultDuration");
                    Player player = plugin.getServer().getPlayer(strings[1]);

                    if(player == null) {
                        commandSender.sendMessage(ChatColor.AQUA + "[ABX] " + ChatColor.RESET + ChatColor.RED + "Player not found.");
                        return true;
                    }

                    if (strings[2].matches("\\d+s")) {

                        duration = Integer.parseInt(strings[2].substring(0, strings[2].length() - 1));

                        strings = java.util.Arrays.copyOfRange(strings, 3, strings.length);

                    } else {
                        strings = java.util.Arrays.copyOfRange(strings, 2, strings.length);
                        commandSender.sendMessage(ChatColor.AQUA + "[ABX] " + ChatColor.RESET + ChatColor.RED + "Duration not specified, using default duration of " + plugin.getConfig().getInt("Announcements.defaultDuration") + " seconds.");
                    }


                    String message = String.join(" ", strings);
                    plugin1.actionbarAnnounceToPlayer(player, duration, message);
                } else {
                    commandSender.sendMessage(ChatColor.AQUA + "[ABX] " + ChatColor.RESET + ChatColor.RED + "Announcements are disabled in the configuration file.");
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "Insufficient Permission.");
            }
            return true;
        } else if (strings.length == 1 && strings[0].equalsIgnoreCase("announceToPlayer")) {
            commandSender.sendMessage(ChatColor.AQUA + "[ABX] " + ChatColor.RESET + ChatColor.RED + "You must specify a player and a message.");
            return true;
        } else if (strings.length==2 && strings[0].equalsIgnoreCase("announceToPlayer")) {
            commandSender.sendMessage(ChatColor.AQUA + "[ABX] " + ChatColor.RESET + ChatColor.RED + "You must specify a message.");
            return true;
        }

        else if (strings.length >= 2 && strings[0].equalsIgnoreCase("announce") ||
                strings[0].equalsIgnoreCase("broadcast") ||
                strings[0].equalsIgnoreCase("bc") &&
                        strings[1] != null) {
            if (commandSender.hasPermission("abx.announce")) {
                if (plugin.getConfig().getBoolean("Announcements.Enable")) {
                    int duration = plugin.getConfig().getInt("Announcements.defaultDuration");
                    if (strings[1].matches("\\d+s")) {

                        duration = Integer.parseInt(strings[1].substring(0, strings[1].length() - 1));

                        strings = java.util.Arrays.copyOfRange(strings, 2, strings.length);

                    } else {
                        strings = java.util.Arrays.copyOfRange(strings, 1, strings.length);
                        commandSender.sendMessage(ChatColor.AQUA + "[ABX] " + ChatColor.RESET + ChatColor.RED + "Duration not specified, using default duration of " + plugin.getConfig().getInt("Announcements.defaultDuration") + " seconds.");
                    }

                    String message = String.join(" ", strings);
                    plugin1.actionbarAnnounce(duration, message);
                } else {
                    commandSender.sendMessage(ChatColor.AQUA + "[ABX] " + ChatColor.RESET + ChatColor.RED + "Announcements are disabled in the configuration file.");
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "Insufficient Permission.");
            }
            return true;
        } else if (strings.length == 1 && strings[0].equalsIgnoreCase("announce")) {
            commandSender.sendMessage(ChatColor.AQUA + "[ABX] " + ChatColor.RESET + ChatColor.RED + "You must specify a message.");
            return true;
        }

        if (strings.length == 1) {
            if (strings[0].equalsIgnoreCase("reload")) {
                if (commandSender.hasPermission("abx.reload")) {

                    if (plugin.task != null) {
                        plugin.task.cancel();
                    }
                    commandSender.sendMessage(ChatColor.AQUA + "[ABX] " + ChatColor.RESET + ChatColor.GREEN + "Reloading...");
                    plugin.reload(commandSender);
                } else {
                    commandSender.sendMessage(ChatColor.RED + "Insuffiecient Permission.");
                }
            } else {
                commandSender.sendMessage(AvailableCommands);
            }

/*            case 2:

                if(strings[0].equalsIgnoreCase("forceeventannounce") && strings[1].equalsIgnoreCase("onplayerkilledplayer")) {

                    commandSender.sendMessage(ChatColor.AQUA + "[ABX] " + ChatColor.RESET + ChatColor.RED + "You must specify 2 players." +
                            "\n/abx forceeventannounce onplayerkilledplayer <killed> <killer>");

                } else if (strings[0].equalsIgnoreCase("forceeventannounce")) {
                    eventForce.forceEventAll(commandSender, strings[1]);
                } else {
                    commandSender.sendMessage(AvailableCommands);
                }


                break;

            case 4:

                if(strings[0].equalsIgnoreCase("forceeventannounce") && strings[1].equalsIgnoreCase("onplayerkilledplayer")) {

                    Player killer = plugin.getServer().getPlayer(strings[2]);
                    Player killed = plugin.getServer().getPlayer(strings[3]);

                    if (killer != null || killed != null) {
                        eventForce.forceEventPlayerKilledPlayer(commandSender, killed, killer);
                    } else {
                        commandSender.sendMessage(ChatColor.RED + "Player not found.");
                    }
                } else {
                    commandSender.sendMessage(AvailableCommands);
                }
                break;
*/
        } else {
            commandSender.sendMessage(AvailableCommands);
        }

            return false;
        }
    }
