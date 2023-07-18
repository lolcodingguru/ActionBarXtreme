package com.me.actionbarxtreme;

import com.me.actionbarxtreme.commands.maincmd;
import com.me.actionbarxtreme.handlers.playerBanAnnounceEvent;
import com.me.actionbarxtreme.utils.updateCheck;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.logging.Level;

public class ActionBarXtreme extends JavaPlugin {
    public BukkitTask task;
    public playerBanAnnounceEvent playerBanAnnounceEvent;
    public PermActionBar permActionBar;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        playerBanAnnounceEvent = new playerBanAnnounceEvent(this);

        maincmd commandExecutor = new maincmd(this, new permBarOverrideAnnounce(ActionBarXtreme.this));
        getCommand("abx").setExecutor(commandExecutor);
        Bukkit.getPluginManager().registerEvents(new playerBanAnnounceEvent(this), this);

        this.permActionBar = new PermActionBar(this);
        List<ChatColor> colors = permActionBar.loadColorsFromConfig();

        permActionBar.start();

        Bukkit.getLogger().info("ActionBarXtreme has been successfully enabled!");

        new updateCheck(this, 111234).getLatestVersion(version -> {
            Bukkit.getLogger().info("Asking Spigot API if ActionBarXtreme (ABX) is up to date?...");
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                Bukkit.getLogger().info("Spigot API says ABX is up to date.");
            } else {
                Bukkit.getLogger().warning("Spigot API says ABX is outdated! Newest version: " + version + ", Your version: " + getDescription().getVersion() + ", Please Update Here: https://www.spigotmc.org/resources/111234");

        	}
        });
    }

    public class PermActionBar implements Runnable {
        private final ActionBarXtreme plugin;
        public boolean enabled = getConfig().getBoolean("EnablePermanentActionBar");
        private int tickCounter = 0;
        List<ChatColor> colors;

        public List<ChatColor> loadColorsFromConfig() {
            List<ChatColor> colorList = new ArrayList<>();
            List<String> colorStrings = plugin.getConfig().getStringList("ChatColor");
            for (String colorString : colorStrings) {
                try {
                    ChatColor color = ChatColor.valueOf(colorString.toUpperCase().replace(" ", "_"));
                    colorList.add(color);
                } catch (IllegalArgumentException e) {

                }
            }
            return colorList;
        }

        public PermActionBar(ActionBarXtreme plugin) {
            this.plugin = plugin;
            this.colors = loadColorsFromConfig();
        }

        public void start() {
            task = getServer().getScheduler().runTaskTimer(plugin, this, 0, getConfig().getInt("duration"));

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

        String actionBarMessage = formatActionBarMessage(
                getConfig().getBoolean("isBold"),
                getConfig().getBoolean("isItalic"),
                getConfig().getBoolean("isUnderline"),
                getConfig().getBoolean("isStrikethrough"),
                getConfig().getBoolean("isMagic"));

        public String getActionBarMessage() {
            return this.actionBarMessage;
        }

        private String formatActionBarMessage(boolean isBold, boolean isItalic, boolean isUnderline, boolean isStrikeThrough, boolean isMagic) {
            StringBuilder sb = new StringBuilder();
            sb.append(getConfig().getString("actionBarMessage"));

            if (getConfig().getBoolean("isBold")) {
                sb.insert(0, ChatColor.BOLD);
            }
            if (getConfig().getBoolean("isItalic")) {
                sb.insert(0, ChatColor.ITALIC);
            }
            if (getConfig().getBoolean("isUnderline")) {
                sb.insert(0, ChatColor.UNDERLINE);
            }
            if (getConfig().getBoolean("isStrikethrough")) {
                sb.insert(0, ChatColor.STRIKETHROUGH);
            }
            if (getConfig().getBoolean("isMagic")) {
                sb.insert(0, ChatColor.MAGIC);
            }

            return sb.toString();
        }


        public void run () {

            if (!enabled) {
                return;
            }

            if(colors.isEmpty()){
                Bukkit.getLogger().log(Level.SEVERE, "[ABX] Color list is empty in configuration file. Please add at least 1 color before restarting. Disabling Plugin to prevent crash and errors.");
                Bukkit.getPluginManager().disablePlugin(plugin);
                return;
            }

            ChatColor color = colors.get(tickCounter % colors.size());

            tickCounter++;


            for (Player player : plugin.getServer().getOnlinePlayers()) {

                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(color + getActionBarMessage()));


            }
        }
    }

    @Override
    public void onDisable () {
        permActionBar.setEnabled(false);
        Bukkit.getLogger().info("ActionBarXtreme has been successfully disabled.");
    }

}