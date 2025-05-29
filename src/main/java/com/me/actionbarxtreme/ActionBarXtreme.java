/*
 * Credits Notice
 *
 * This plugin uses the following third-party libraries and dependencies:
 *
 * bStats library, which was developed by Bastion @ GitHub.
 * https://github.com/Bastian, https://github.com/Bastian/bStats, https://bStats.org.
 * This plugin and I are not affiliated with or endorsed by bStats.
 * Similarly, bStats is not affiliated with or endorsed by me or this plugin.
 *
 * ConfigUpdater library, which was developed by tchristofferson.
 * GitHub: https://github.com/tchristofferson/Config-Updater, https://github.com/tchristofferson
 *
 * I would like to express my thanks to tchristofferson for developing this library and for making it easier to work with the mess that is Spigot's configuration system.
 *
 * This plugin and I are not affiliated with or endorsed by tchristofferson or the ConfigUpdater library.
 * Similarly, tchristofferson and the ConfigUpdater library are not affiliated with or endorsed by me or this plugin.
 */
package com.me.actionbarxtreme;

import com.me.actionbarxtreme.barMethods.permBarOverrideAnnounce;
/*import com.me.actionbarxtreme.commands.eventForce;*/
import com.me.actionbarxtreme.commands.maincmd;
/*import com.me.actionbarxtreme.handlers.onPlayerKick;
import com.me.actionbarxtreme.handlers.onDragonDeath;
import com.me.actionbarxtreme.handlers.onPlayerBan;*/
import com.me.actionbarxtreme.utils.logging;
import com.me.actionbarxtreme.utils.updateCheck;
/*import com.me.actionbarxtreme.handlers.onWitherDeath;
import com.me.actionbarxtreme.handlers.onElderGuardianDeath;
import com.me.actionbarxtreme.handlers.onWardenDeath;
import com.me.actionbarxtreme.handlers.onPlayerKilledPlayer;*/
import com.me.actionbarxtreme.utils.tabComplete;
import com.tchristofferson.configupdater.ConfigUpdater;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ActionBarXtreme extends JavaPlugin implements Listener {

    public BukkitTask task;
    /*public onPlayerKick onPlayerKick;
    public onPlayerBan onPlayerBan;
    public onDragonDeath onDragonDeath;
    public onWitherDeath onWitherDeath;
    public onElderGuardianDeath onElderGuardianDeath;
    public onWardenDeath onWardenDeath;
    public onPlayerKilledPlayer onPlayerKilledPlayer;*/


    public permBarOverrideAnnounce permBarOverrideAnnounce;
    public PermActionBar permActionBar;
    public boolean LegacyColors = false;
    public boolean wardenSupported = false;
    public static boolean isReloading;
    private Listener listener;

    @Override
    public void onEnable() {

        if(isReloading) {
            logging.log(logging.LogLevel.DEBUG, "[ABX] Reloading ActionBarXtreme...");
        }
        logging.log(logging.LogLevel.OUTLINE, "*****************************************************************");
        logging.log(logging.LogLevel.INFO, "[ABX] ActionBarXtreme is enabling...");
        logging.log(logging.LogLevel.INFO, "[ABX] Plugin version: " + getDescription().getVersion());
        logging.log(logging.LogLevel.DEBUG, "[ABX] Server version: " + Bukkit.getVersion());

        logging.log(logging.LogLevel.INFO, "[ABX] Loading bStats...");
        /* Metrics metrics = new Metrics(this, 19264); */
        logging.log(logging.LogLevel.INFO, "[ABX] bStats loaded successfully!");


        logging.log(logging.LogLevel.INFO, "[ABX] Loading files...");

        File file = new File(getDataFolder(), "config.yml");



        if(file.exists()) {
            try {
                logging.log(logging.LogLevel.INFO, "[ABX] Updating config.yml file...");
                ConfigUpdater.update(this, "config.yml", file, Collections.emptyList());
                logging.log(logging.LogLevel.INFO, "[ABX] config.yml file up to date!");
            } catch (NullPointerException ignored) {

            } catch (Exception e) {
                logging.log(logging.LogLevel.ERROR, "[ABX] Caught ERROR while updating config.yml file.");
                logging.log(logging.LogLevel.ERROR, "[ABX] This is likely due to wrong format in config.yml file.\n" +
                        "If you have modified the config.yml file, please make sure it is in the correct format.\n" +
                        "You can find the correct format in the default config.yml file provided in the plugin page.\n" +
                        "If issue persists even if config.yml file is in correct format, please report it.\n");
                logging.log(logging.LogLevel.ERROR, "[ABX] ABX will shut down.\n");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
                }

        } else {
            logging.log(logging.LogLevel.INFO, "[ABX] Did not detect config.yml file. Generating...");
            getConfig().options().copyDefaults();
            saveDefaultConfig();
            logging.log(logging.LogLevel.INFO, "[ABX] config.yml file generated!");
        }
        isReloading = false;

        try {
            reloadConfig();
        } catch (Exception e) {
            logging.log(logging.LogLevel.ERROR, "[ABX] Caught ERROR while reloading config.yml file.");
            logging.log(logging.LogLevel.ERROR, "[ABX] This may be due to wrong format in config.yml file.\n" +
                    "If you have modified the config.yml file, please make sure it is in the correct format.\n" +
                    "You can find the correct format in the default config.yml file provided in the plugin page.\n" +
                    "If issue persists even if config.yml file is in correct format, please report it.\n");
            logging.log(logging.LogLevel.ERROR, "[ABX] ABX will shut down.\n");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        logging.log(logging.LogLevel.INFO, "[ABX] Files loaded successfully!");


        String serverVersion = Bukkit.getVersion();

        // If version is 1.22, show a warning message that this version of the plugin has not yet been tested on this version, proceed with caution

        if (serverVersion.contains("1.22")) {
            logging.log(logging.LogLevel.WARNING, "[ABX] This version of the plugin has not been tested on " + serverVersion + ". Proceed with caution." +
                    "\n If you are not on 1.22.x, this is a bug! Please report it");
        } else


        if (serverVersion.contains("1.7") || serverVersion.contains("1.8") || serverVersion.contains("1.9")) {
            logging.log(logging.LogLevel.ERROR, "[ABX] Detected unsupported version " + serverVersion + ". Disabling plugin to prevent fatal errors and crashes." +
                    "\n If you are not on 1.9.x or below, this is a bug! Please report it");
            logging.log(logging.LogLevel.OUTLINE, "*****************************************************************");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        } else if (serverVersion.contains("1.10") || serverVersion.contains("1.11")) {
            LegacyColors = true;
            logging.log(logging.LogLevel.WARNING , "[ABX] Detecting legacy supported version " + serverVersion + ". Alternating Colors are not supported. " +
                    "\n If you are not on 1.10.x-1.11.x, this is a bug! Please report it");
        }

        permBarOverrideAnnounce permBarOverrideAnnounce = new permBarOverrideAnnounce(ActionBarXtreme.this);

        /*onPlayerBan = new onPlayerBan(this, permBarOverrideAnnounce);
        onDragonDeath = new onDragonDeath(this, permBarOverrideAnnounce);
        onWitherDeath = new onWitherDeath(this, permBarOverrideAnnounce);
        onPlayerKick = new onPlayerKick(this, permBarOverrideAnnounce);
        onElderGuardianDeath = new onElderGuardianDeath(this, permBarOverrideAnnounce);
        onWardenDeath = new onWardenDeath(this, permBarOverrideAnnounce);
        onPlayerKilledPlayer = new onPlayerKilledPlayer(this, permBarOverrideAnnounce);*/


        tabComplete tabComplete = new tabComplete();


        logging.log(logging.LogLevel.INFO, "[ABX] Loading" + /*"events and" +*/ " commands...");

        CommandExecutor MainCommandExectuer = new maincmd(this, new permBarOverrideAnnounce
                (ActionBarXtreme.this)/*, new eventForce(this,
                onPlayerBan, onDragonDeath, onWitherDeath, onPlayerKick,
                onElderGuardianDeath, onWardenDeath, onPlayerKilledPlayer)*/);

        getCommand("abx").setExecutor(MainCommandExectuer);
        getCommand("abx").setTabCompleter(tabComplete);

        listener = new Listener() {
            @EventHandler
            public void onPlayerJoin(PlayerJoinEvent event) {
                permActionBar.stop(event.getPlayer());
                logging.log(logging.LogLevel.DEBUG, "Player joined: " + event.getPlayer().getName()); // Debug message
                if (permActionBar.enabled) {
                    logging.log(logging.LogLevel.DEBUG, "Starting Permanent ActionBar for player: " + event.getPlayer().getName()); // Debug message
                    permActionBar.start(event.getPlayer());
                    logging.log(logging.LogLevel.DEBUG, "Permanent ActionBar started for player: " + event.getPlayer().getName()); // Debug message
                }
            }

            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent event) {
                logging.log(logging.LogLevel.DEBUG, "Player quit: " + event.getPlayer().getName()); // Debug message
                logging.log(logging.LogLevel.DEBUG, "Stopping Permanent ActionBar for player: " + event.getPlayer().getName());
                permActionBar.stop(event.getPlayer());
                logging.log(logging.LogLevel.DEBUG, "Stopped Permanent ActionBar for player: " + event.getPlayer().getName()); // Debug message
            }
        };

        getServer().getPluginManager().registerEvents(listener, this);


        /*Bukkit.getPluginManager().registerEvents(onPlayerKick, this);
        Bukkit.getPluginManager().registerEvents(onPlayerBan, this);
        Bukkit.getPluginManager().registerEvents(onDragonDeath, this);
        Bukkit.getPluginManager().registerEvents(onWitherDeath, this);
        Bukkit.getPluginManager().registerEvents(onElderGuardianDeath, this);
        Bukkit.getPluginManager().registerEvents(onWardenDeath, this);
        Bukkit.getPluginManager().registerEvents(onPlayerKilledPlayer, this);*/



        logging.log(logging.LogLevel.INFO, "[ABX] Commands loaded successfully!");

        this.permActionBar = new PermActionBar(this);
        List<ChatColor> colors = permActionBar.loadColorsFromConfig();

        permActionBar.startAll();

        if(serverVersion.contains("1.19") || serverVersion.contains("1.20") || serverVersion.contains("1.21")) {
            wardenSupported = true;
        }


        new updateCheck(this, 111234).getLatestVersion(version -> {
            logging.log(logging.LogLevel.INFO, "Asking Spigot API if ActionBarXtreme (ABX) is up to date?...");
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                logging.log(logging.LogLevel.INFO,"Spigot API says ABX is up to date.");
            } else {
                logging.log(logging.LogLevel.WARNING, "Spigot API says ABX is outdated! Newest version: " + version + ", Your version: " + getDescription().getVersion() + ", Please Update Here: https://www.spigotmc.org/resources/111234/");

        	}
        });

        logging.log(logging.LogLevel.SUCCESS, "[ABX] ActionBarXtreme has been successfully enabled!");
        logging.log(logging.LogLevel.OUTLINE, "*****************************************************************");
    }

    public void reload(CommandSender commandSender) {
        try {
            // Set reloading flag
            isReloading = true;

            // Cancel all tasks
            if (task != null) {
                task.cancel();
                task = null;
            }

            // Cancel any active announcements
            if (permBarOverrideAnnounce != null) {
                permBarOverrideAnnounce.cancelTask();
            }

            // Unregister all listeners
            HandlerList.unregisterAll((Plugin) this);
            if (listener != null) {
                HandlerList.unregisterAll(listener);
            }

            // Reload the configuration
            reloadConfig();

            // Re-enable the plugin
            getPluginLoader().disablePlugin(this);
            getPluginLoader().enablePlugin(this);

            // Notify success
            commandSender.sendMessage(ChatColor.LIGHT_PURPLE + "[ABX] " + ChatColor.RESET + ChatColor.GREEN + "Plugin successfully reloaded.");
            logging.log(logging.LogLevel.INFO, "[ABX] Plugin was reloaded successfully");
        } catch (Exception e) {
            // Log any errors that occur during reload
            logging.log(logging.LogLevel.ERROR, "Error during plugin reload: " + e.getMessage());
            commandSender.sendMessage(ChatColor.LIGHT_PURPLE + "[ABX] " + ChatColor.RESET + ChatColor.RED + "Error reloading plugin. Check console for details.");
        } finally {
            // Always reset the reloading flag
            isReloading = false;
        }
    }

    public class PermActionBar implements Runnable {
        private final ActionBarXtreme plugin;
        public boolean enabled = getConfig().getBoolean("PermanentActionBar.Enable");
        private List<ChatColor> colors;

        // Track color counters PER PLAYER
        private final Map<UUID, Integer> playerColorCounters = new HashMap<>();
        private final Map<Player, BukkitTask> playerTasks = new HashMap<>();

        // Store the message format once to avoid repeated config lookups
        private final String baseMessage;
        private final boolean useBold;
        private final boolean useItalic;
        private final boolean useUnderline;
        private final boolean useStrikethrough; 
        private final boolean useMagic;
        private final int duration;

        public List<ChatColor> loadColorsFromConfig() {
            List<ChatColor> colorList = new ArrayList<>();
            List<String> colorStrings = plugin.getConfig().getStringList("PermanentActionBar.ChatColor");

            for (String colorString : colorStrings) {
                try {
                    ChatColor color = ChatColor.valueOf(colorString.toUpperCase().replace(" ", "_"));
                    colorList.add(color);
                } catch (IllegalArgumentException ignored) {
                    // Invalid color, ignore
                }
            }

            // Ensure we have at least one color
            if (colorList.isEmpty()) {
                colorList.add(ChatColor.WHITE);
            }

            return colorList;
        }

        public PermActionBar(ActionBarXtreme plugin) {
            this.plugin = plugin;
            this.colors = loadColorsFromConfig();

            // Cache message elements from config to avoid repeated lookups
            this.baseMessage = getConfig().getString("PermanentActionBar.actionBarMessage");
            this.useBold = getConfig().getBoolean("PermanentActionBar.MessageStyles.isBold");
            this.useItalic = getConfig().getBoolean("PermanentActionBar.MessageStyles.isItalic");
            this.useUnderline = getConfig().getBoolean("PermanentActionBar.MessageStyles.isUnderline");
            this.useStrikethrough = getConfig().getBoolean("PermanentActionBar.MessageStyles.isStrikethrough");
            this.useMagic = getConfig().getBoolean("PermanentActionBar.MessageStyles.isMagic");
            this.duration = getConfig().getInt("PermanentActionBar.duration");
        }

        /**
         * Start sending permanent action bar to a player with a dedicated task
         */
        public void start(Player player) {
            if (player == null || !player.isOnline()) return;

            // Stop any existing task first
            stop(player);

            // Reset this player's color counter
            playerColorCounters.put(player.getUniqueId(), 0);

            // Create an isolated Runnable just for this player to avoid shared state issues
            BukkitTask task = plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    if (!player.isOnline()) {
                        // Player went offline, cancel task
                        stop(player);
                        return;
                    }

                    // Send message with this player's current color
                    sendActionBarToPlayer(player);
                }
            }, 0, duration);

            // Store the task for later management
            playerTasks.put(player, task);
            logging.log(logging.LogLevel.DEBUG, "Started dedicated action bar task for: " + player.getName());
        }

        /**
         * Send the action bar to a specific player with their tracked color
         */
        private void sendActionBarToPlayer(Player player) {
            if (!enabled || !player.isOnline()) return;

            // Get this player's color counter
            UUID playerId = player.getUniqueId();
            int colorIndex = playerColorCounters.getOrDefault(playerId, 0);

            // Get the next color for this player
            ChatColor color = colors.get(colorIndex % colors.size());

            // Increment the player's color counter
            playerColorCounters.put(playerId, colorIndex + 1);

            // Format message with this player's current color
            TextComponent message;
            if (plugin.LegacyColors) {
                message = new TextComponent(ChatColor.translateAlternateColorCodes('&', formatActionBarMessage()));
            } else {
                String formattedMsg = formatActionBarMessage().replaceAll("&", "§");
                message = new TextComponent(TextComponent.fromLegacyText(color + formattedMsg));
            }

            // Send directly to this player
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, message);
        }

        /**
         * Stop sending permanent action bar to a player
         */
        public void stop(Player player) {
            if (player == null) return;

            BukkitTask task = playerTasks.remove(player);
            if (task != null) {
                task.cancel();
                logging.log(logging.LogLevel.DEBUG, "Stopped action bar task for player: " + player.getName());
            }

            // Clean up player's color counter
            playerColorCounters.remove(player.getUniqueId());
        }

        /**
         * Start for all online players
         */
        public void startAll() {
            logging.log(logging.LogLevel.DEBUG, "Starting action bar for all players");
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                start(player);
            }
        }

        /**
         * Stop for all players
         */
        public void stopAll() {
            logging.log(logging.LogLevel.DEBUG, "Stopping action bar for all players");

            // Copy to avoid concurrent modification
            List<Player> players = new ArrayList<>(playerTasks.keySet());
            for (Player player : players) {
                stop(player);
            }

            // Clear all data structures
            playerTasks.clear();
            playerColorCounters.clear();
        }

        /**
         * Enable or disable the action bar system
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
            if (enabled) {
                startAll();
            } else {
                stopAll();
            }
        }

        /**
         * Resets the tick counter for a specific player
         */
        public void resetTickCounter(Player player) {
            if (player != null) {
                playerColorCounters.put(player.getUniqueId(), 0);
                logging.log(logging.LogLevel.DEBUG, "Reset tick counter for player: " + player.getName());
            }
        }

        /**
         * Resets all tick counters
         */
        public void resetTickCounter() {
            playerColorCounters.clear();
            logging.log(logging.LogLevel.DEBUG, "Reset all tick counters");
        }

        /**
         * Format the action bar message with styles from config
         */
        private String formatActionBarMessage() {
            StringBuilder sb = new StringBuilder();
            sb.append(baseMessage);

            if (useBold) sb.insert(0, ChatColor.BOLD);
            if (useItalic) sb.insert(0, ChatColor.ITALIC);
            if (useUnderline) sb.insert(0, ChatColor.UNDERLINE);
            if (useStrikethrough) sb.insert(0, ChatColor.STRIKETHROUGH);
            if (useMagic) sb.insert(0, ChatColor.MAGIC);

            return sb.toString();
        }

        /**
         * This run method is only used as a fallback and should rarely be called
         * since we now use dedicated tasks per player
         */
        @Override
        public void run() {
            // This is now just a fallback - each player has their own dedicated task
            if (!enabled) return;

            for (Player player : plugin.getServer().getOnlinePlayers()) {
                if (player.isOnline() && !playerTasks.containsKey(player)) {
                    // If a player somehow doesn't have a task, create one
                    start(player);
                }
            }
        }
    }

    @Override
    public void onDisable () {

        logging.log(logging.LogLevel.OUTLINE, "*****************************************************************");
        logging.log(logging.LogLevel.INFO, "[ABX] ActionBarXtreme is disabling...");

        logging.log(logging.LogLevel.INFO, "[ABX] Cancelling Permanent ActionBar task...");
        try {
            if (permActionBar != null) {
                permActionBar.setEnabled(false);
            }
        } catch (Exception e) {
            logging.log(logging.LogLevel.ERROR, "[ABX] Error cancelling permanent action bar: " + e.getMessage());
        }

        logging.log(logging.LogLevel.INFO, "[ABX] Permanent ActionBar task cancelled!");

        logging.log(logging.LogLevel.INFO, "[ABX] Cancelling all other tasks...");
        try {
            if (permBarOverrideAnnounce != null) {
                permBarOverrideAnnounce.cancelTask();
            }
            if (listener != null) {
                HandlerList.unregisterAll(listener);
            }
        } catch (Exception e) {
            logging.log(logging.LogLevel.ERROR, "[ABX] Error cancelling other tasks: " + e.getMessage());
        }

        logging.log(logging.LogLevel.INFO, "[ABX] All other tasks cancelled!");

        logging.log(logging.LogLevel.SUCCESS ,"ActionBarXtreme has been successfully disabled.");
        logging.log(logging.LogLevel.OUTLINE, "*****************************************************************");
    }

}