/*
 * Credits Notice
 *
 * This plugin uses the following third-party library and dependency:
 *
 * ConfigUpdater library, which was developed by tchristofferson. GitHub: https://github.com/tchristofferson/Config-Updater
 *
 * I would like to express my thanks to tchristofferson for developing this library and for making it easier to work with the mess that is Spigot's configuration system.
 *
 * Please note that this plugin and I are not affiliated with or endorsed by tchristofferson or the ConfigUpdater library.
 * Similarly, tchristofferson and the ConfigUpdater library are not affiliated with or endorsed by me or this plugin.
 */
package com.me.actionbarxtreme;

import com.me.actionbarxtreme.barMethods.permBarOverrideAnnounce;
import com.me.actionbarxtreme.commands.eventForce;
import com.me.actionbarxtreme.commands.maincmd;
import com.me.actionbarxtreme.handlers.onPlayerKick;
import com.me.actionbarxtreme.handlers.onDragonDeath;
import com.me.actionbarxtreme.handlers.onPlayerBan;
import com.me.actionbarxtreme.utils.logging;
import com.me.actionbarxtreme.utils.updateCheck;
import com.me.actionbarxtreme.handlers.onDragonRespawn;
import com.me.actionbarxtreme.handlers.onWitherDeath;
import com.me.actionbarxtreme.handlers.onElderGuardianDeath;
import com.me.actionbarxtreme.handlers.onWardenDeath;
import com.me.actionbarxtreme.handlers.onPlayerKilledPlayer;
import com.me.actionbarxtreme.utils.tabComplete;
import com.tchristofferson.configupdater.ConfigUpdater;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ActionBarXtreme extends JavaPlugin {
    public BukkitTask task;
    public onPlayerKick onPlayerKick;
    public onPlayerBan onPlayerBan;
    public onDragonDeath onDragonDeath;
    public PermActionBar permActionBar;
    public onDragonRespawn onDragonRespawn;
    public onWitherDeath onWitherDeath;
    public onElderGuardianDeath onElderGuardianDeath;
    public onWardenDeath onWardenDeath;
    public onPlayerKilledPlayer onPlayerKilledPlayer;
    public permBarOverrideAnnounce permBarOverrideAnnounce;


    public boolean LegacyColors = false;
    public boolean wardenSupported = false;


    @Override
    public void onEnable() {

        logging.log(logging.LogLevel.OUTLINE, "*****************************************************************");
        logging.log(logging.LogLevel.INFO, "[ABX] ActionBarXtreme is enabling...");
        logging.log(logging.LogLevel.INFO, "[ABX] Plugin version: " + getDescription().getVersion());
        logging.log(logging.LogLevel.INFO, "[ABX] Loading files...");

        File file = new File(getDataFolder(), "config.yml");



        if(file.exists()) {
            try {
                logging.log(logging.LogLevel.INFO, "[ABX] Updating config.yml file...");
                ConfigUpdater.update(this, "config.yml", file, Collections.emptyList());
                logging.log(logging.LogLevel.INFO, "[ABX] config.yml file up to date!");
            } catch (NullPointerException | IOException e) {
                if(e.getClass().equals(IOException.class)) {
                    e.printStackTrace();
                } else {
                    logging.log(logging.LogLevel.OUTLINE , "**************************************************");
                    logging.log(logging.LogLevel.WARNING,"[ABX] Caught NullPointerException while updating config.yml file.");
                    logging.log(logging.LogLevel.WARNING,"[ABX] If you are reloading the plugin, this is not an issue and you may safely ignore this warning.");
                    logging.log(logging.LogLevel.WARNING,"[ABX] If you have reloaded the server using /reload, that may be the cause, please restart your server instead.");
                    logging.log(logging.LogLevel.WARNING,"[ABX] In any other case, this is a bug! Please report it.");
                    logging.log(logging.LogLevel.WARNING,"[ABX] You should never reload your server using /reload as it can cause issues with plugins.");
                    logging.log(logging.LogLevel.WARNING,"[ABX] If you continue experiencing issues, please restart your server.");
                    logging.log(logging.LogLevel.OUTLINE , "**************************************************");
                }
            }
        } else {
            logging.log(logging.LogLevel.INFO, "[ABX] Did not detect config.yml file. Generating...");
            getConfig().options().copyDefaults();
            saveDefaultConfig();
            logging.log(logging.LogLevel.INFO, "[ABX] config.yml file generated!");
        }

        reloadConfig();
        logging.log(logging.LogLevel.INFO, "[ABX] Files loaded successfully!");


        String serverVersion = Bukkit.getVersion();

        if (serverVersion.contains("1.7") || serverVersion.contains("1.8")) {
            logging.log(logging.LogLevel.ERROR, "[ABX] Detected unsupported version " + serverVersion + ". Disabling plugin to prevent fatal errors and crashes." +
                    "\n If you are not on 1.8 or below, this is a bug! Please report it");
            logging.log(logging.LogLevel.OUTLINE, "*****************************************************************");
            Bukkit.getPluginManager().disablePlugin(this);
        } else if (serverVersion.contains("1.9") || serverVersion.contains("1.10") || serverVersion.contains("1.11") || serverVersion.contains("1.12")) {

            LegacyColors = true;
            logging.log(logging.LogLevel.WARNING , "[ABX] Detecting legacy supported version " + serverVersion + ". Alternating Colors are not supported. " +
                    "\n If you are not on 1.9-1.11, this is a bug! Please report it");
        }

        permBarOverrideAnnounce permBarOverrideAnnounce = new permBarOverrideAnnounce(ActionBarXtreme.this);

        onPlayerBan = new onPlayerBan(this, permBarOverrideAnnounce);
        onDragonDeath = new onDragonDeath(this, permBarOverrideAnnounce);
        onDragonRespawn = new onDragonRespawn(this, permBarOverrideAnnounce);
        onWitherDeath = new onWitherDeath(this, permBarOverrideAnnounce);
        onPlayerKick = new onPlayerKick(this, permBarOverrideAnnounce);
        onElderGuardianDeath = new onElderGuardianDeath(this, permBarOverrideAnnounce);
        onWardenDeath = new onWardenDeath(this, permBarOverrideAnnounce);
        onPlayerKilledPlayer = new onPlayerKilledPlayer(this, permBarOverrideAnnounce);
        tabComplete tabComplete = new tabComplete();


        logging.log(logging.LogLevel.INFO, "[ABX] Loading events and commands...");

        CommandExecutor MainCommandExectuer = new maincmd(this, new permBarOverrideAnnounce
                (ActionBarXtreme.this), new eventForce(this,
                onPlayerBan, onDragonRespawn, onDragonDeath, onWitherDeath, onPlayerKick,
                onElderGuardianDeath, onWardenDeath, onPlayerKilledPlayer));

        getCommand("abx").setExecutor(MainCommandExectuer);
        getCommand("abx").setTabCompleter(tabComplete);
        Bukkit.getPluginManager().registerEvents(onPlayerKick, this);
        Bukkit.getPluginManager().registerEvents(onPlayerBan, this);
        Bukkit.getPluginManager().registerEvents(onDragonDeath, this);
        Bukkit.getPluginManager().registerEvents(onDragonRespawn, this);
        Bukkit.getPluginManager().registerEvents(onWitherDeath, this);
        Bukkit.getPluginManager().registerEvents(onElderGuardianDeath, this);
        Bukkit.getPluginManager().registerEvents(onWardenDeath, this);
        Bukkit.getPluginManager().registerEvents(onPlayerKilledPlayer, this);

        logging.log(logging.LogLevel.INFO, "[ABX] Events and commands loaded successfully!");

        this.permActionBar = new PermActionBar(this);
        List<ChatColor> colors = permActionBar.loadColorsFromConfig();

        permActionBar.start();

        if(serverVersion.contains("1.19") || serverVersion.contains("1.20") || serverVersion.contains("1.21")) {
            wardenSupported = true;
        }


        new updateCheck(this, 111234).getLatestVersion(version -> {
            logging.log(logging.LogLevel.INFO, "Asking Spigot API if ActionBarXtreme (ABX) is up to date?...");
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                logging.log(logging.LogLevel.INFO,"Spigot API says ABX is up to date.");
            } else {
                logging.log(logging.LogLevel.WARNING, "Spigot API says ABX is outdated! Newest version: " + version + ", Your version: " + getDescription().getVersion() + ", Please Update Here: ");

        	}
        });

        logging.log(logging.LogLevel.SUCCESS, "[ABX] ActionBarXtreme has been successfully enabled!");
        logging.log(logging.LogLevel.OUTLINE, "*****************************************************************");
    }

    public void reload(CommandSender commandSender) {
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

        public void start() {
            task = getServer().getScheduler().runTaskTimer(plugin, this, 0, getConfig().getInt("PermanentActionBar.duration"));

        }

        public void stop() {
            if (task != null) {
                task.cancel();
                task = null;
            }
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
            if (enabled) {
                start();
            } else {
                stop();
            }
        }

        String actionBarMessage = formatActionBarMessage();

        public TextComponent getActionBarMessage() {

            if (plugin.LegacyColors) {
                return new TextComponent(ChatColor.translateAlternateColorCodes('&', this.actionBarMessage));
            } else

            if(colors.isEmpty()){
                colors.add(ChatColor.WHITE);
            }

            ChatColor color = colors.get(tickCounter % colors.size());

            tickCounter++;

            this.actionBarMessage = this.actionBarMessage.replaceAll("&", "§");
            return new TextComponent(TextComponent.fromLegacyText(color + this.actionBarMessage));
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

            for (Player player : plugin.getServer().getOnlinePlayers()) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, getActionBarMessage());

            }
        }
    }

    @Override
    public void onDisable () {

        logging.log(logging.LogLevel.OUTLINE, "*****************************************************************");
        logging.log(logging.LogLevel.INFO, "[ABX] ActionBarXtreme is disabling...");

        logging.log(logging.LogLevel.INFO, "[ABX] Cancelling Permanent ActionBar task...");
        permActionBar.setEnabled(false);
        logging.log(logging.LogLevel.INFO, "[ABX] Permanent ActionBar task cancelled!");

        logging.log(logging.LogLevel.INFO, "[ABX] Cancelling any running announce tasks...");
        permBarOverrideAnnounce.cancelTask();

        logging.log(logging.LogLevel.SUCCESS ,"ActionBarXtreme has been successfully disabled.");
        logging.log(logging.LogLevel.OUTLINE, "*****************************************************************");
    }

}