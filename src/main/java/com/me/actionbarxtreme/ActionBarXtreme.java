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

        // If version is 1.21 or above, show a warning message that this version of the plugin has not yet been tested on this version, proceed with caution

        if (serverVersion.contains("1.21")) {
            logging.log(logging.LogLevel.WARNING, "[ABX] This version of the plugin has not been tested on " + serverVersion + ". Proceed with caution." +
                    "\n If you are not on 1.21.x, this is a bug! Please report it");
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

       /* listener = new Listener() {
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
        };*/

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
         isReloading = true;
        HandlerList.unregisterAll(listener);
        getPluginLoader().disablePlugin(this);
        getPluginLoader().enablePlugin(this);
        commandSender.sendMessage(ChatColor.LIGHT_PURPLE + "[ABX] " + ChatColor.RESET + ChatColor.GREEN + "Plugin sucessfully reloaded.");
        logging.log(logging.LogLevel.INFO,"[ABX] Plugin was reloaded");
    }

    public class PermActionBar implements Runnable {
        private final ActionBarXtreme plugin;
        public boolean enabled = getConfig().getBoolean("PermanentActionBar.Enable");
        private int tickCounter = 0;
        List<ChatColor> colors;
        Map<Player, BukkitTask> tasks = new HashMap<>();

        public List<ChatColor> loadColorsFromConfig() {
            List<ChatColor> colorList = new ArrayList<>();
            List<String> colorStrings = plugin.getConfig().getStringList("PermanentActionBar.ChatColor");
            for (String colorString : colorStrings) {
                try {
                    ChatColor color = ChatColor.valueOf(colorString.toUpperCase().replace(" ", "_"));
                    colorList.add(color);
                } catch (IllegalArgumentException ignored) {

                }
            }
            return colorList;
        }

        public PermActionBar(ActionBarXtreme plugin) {
            this.plugin = plugin;
            this.colors = loadColorsFromConfig();
        }

        public void start(Player player) {
            tasks.put(player, getServer().getScheduler().runTaskTimer(plugin, this, 0, getConfig().getInt("PermanentActionBar.duration")));
            logging.log(logging.LogLevel.DEBUG,"Started task for player: " + player.getName()); // Debug message

        }

        public void stop(Player player) {
            BukkitTask task = tasks.get(player);
            if(task != null) {
                task.cancel();
                tasks.remove(player);
                logging.log(logging.LogLevel.DEBUG,"TStopped task for player: " + player.getName()); // Debug message

            }
            logging.log(logging.LogLevel.DEBUG,"Stopped task for player: " + player.getName()); // Debug message
        }

        public void startAll() {
            logging.log(logging.LogLevel.DEBUG,"startAll method Called"); // Debug message
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                start(player);
            }
            logging.log(logging.LogLevel.DEBUG,"Started task for ALL players"); // Debug message
        }

        public void stopAll() {
            logging.log(logging.LogLevel.DEBUG,"stopAll method Called"); // Debug message

            for (Player player : plugin.getServer().getOnlinePlayers()) {
                stop(player);
            }
            logging.log(logging.LogLevel.DEBUG,"Stopped task for ALL players"); // Debug message
        }


        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
            if (enabled) {
                startAll();
            } else {
                stopAll();
            }
        }


        public TextComponent getActionBarMessage() {

            if (plugin.LegacyColors) {
                return new TextComponent(ChatColor.translateAlternateColorCodes('&', formatActionBarMessage()));
            } else

            if(colors.isEmpty()){
                colors.add(ChatColor.WHITE);
            }

            ChatColor color = colors.get(tickCounter % colors.size());

            tickCounter++;
            String actionBarMessage = formatActionBarMessage().replaceAll("&", "§");
            return new TextComponent(TextComponent.fromLegacyText(color + actionBarMessage));
        }



        private String formatActionBarMessage() {
            StringBuilder sb = new StringBuilder();
            sb.append(getConfig().getString("PermanentActionBar.actionBarMessage"));

            if (getConfig().getBoolean("PermanentActionBar.MessageStyles.isBold")) {
                sb.insert(0, ChatColor.BOLD);
            }
            if (getConfig().getBoolean("PermanentActionBar.MessageStyles.isItalic")) {
                sb.insert(0, ChatColor.ITALIC);
            }
            if (getConfig().getBoolean("PermanentActionBar.MessageStyles.isUnderline")) {
                sb.insert(0, ChatColor.UNDERLINE);
            }
            if (getConfig().getBoolean("PermanentActionBar.MessageStyles.isStrikethrough")) {
                sb.insert(0, ChatColor.STRIKETHROUGH);
            }
            if (getConfig().getBoolean("PermanentActionBar.MessageStyles.isMagic")) {
                sb.insert(0, ChatColor.MAGIC);
            }

            return sb.toString();
        }

        @Override
        public void run () {

            if (!enabled) {
                return;
            }

            Player player = tasks.keySet().stream().filter(Player::isOnline).findFirst().orElse(null);
            if (player != null) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, getActionBarMessage());

            }
        }
    }

    @Override
    public void onDisable () {

        logging.log(logging.LogLevel.OUTLINE, "*****************************************************************");
        logging.log(logging.LogLevel.INFO, "[ABX] ActionBarXtreme is disabling...");

        logging.log(logging.LogLevel.INFO, "[ABX] Cancelling Permanent ActionBar task...");
        try {
            permActionBar.setEnabled(false);
        } catch (Exception ignored) {}

        logging.log(logging.LogLevel.INFO, "[ABX] Permanent ActionBar task cancelled!");

        logging.log(logging.LogLevel.INFO, "[ABX] Cancelling all other tasks...");
        try {
            permBarOverrideAnnounce.cancelTask();
            HandlerList.unregisterAll(listener);
        } catch (Exception ignored) {}

        logging.log(logging.LogLevel.INFO, "[ABX] All other tasks cancelled!");

        logging.log(logging.LogLevel.SUCCESS ,"ActionBarXtreme has been successfully disabled.");
        logging.log(logging.LogLevel.OUTLINE, "*****************************************************************");
    }

}