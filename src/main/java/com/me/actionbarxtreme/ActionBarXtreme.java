package com.me.actionbarxtreme;

import com.me.actionbarxtreme.commands.maincmd;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.ArrayList;

public class ActionBarXtreme extends JavaPlugin {
    private List<ChatColor> colors;
    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        getCommand("abx").setExecutor(new maincmd(this));

        PermActionBar permActionBar = new PermActionBar(this);
        colors = permActionBar.loadColorsFromConfig();

        if (colors.isEmpty()) {
            getLogger().warning("No colors defined in configuration file. Action bar may not work properly.");
            return;
        }

        Bukkit.getLogger().info("ActionBarXtreme has been successfully enabled!");
        getServer().getScheduler().runTaskTimer(this, permActionBar, 0, getConfig().getInt("duration")); // Every how many ticks
    }

    public class PermActionBar implements Runnable {
        private BukkitTask task;
        private ActionBarXtreme plugin;
        public boolean enabled = getConfig().getBoolean("EnablePermanentActionBar"); // Toggle variable in config (false is default)
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
                    // Invalid color string, ignore it
                }
            }
            return colorList;
        }

        public PermActionBar(ActionBarXtreme plugin) {
            this.plugin = plugin;
            this.colors = loadColorsFromConfig();
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
                return; // If the toggle is off, do nothing
            }

            if (colors.isEmpty()) {
                getLogger().warning("No colors defined in configuration file. Action bar may not work properly.");
                return;
            }
            // Select color based on tick counter
            ChatColor color = colors.get(tickCounter % colors.size());

            // Increment tick counter
            tickCounter++;

            // Send the action bar message to all players with the selected color

            for (Player player : plugin.getServer().getOnlinePlayers()) {

                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(color + getActionBarMessage()));


            }
        }

        public void start() {
            // Schedule the task and hold a reference to the returned BukkitTask object
            task = plugin.getServer().getScheduler().runTaskTimer(plugin, this, 0, plugin.getConfig().getInt("duration"));
        }
        public void stop() {
            // Cancel the task if it's running
            if (task != null) {
                Bukkit.getLogger().info("[ABX] Detected ActionBar still running! Disabling...");
                task.cancel();
                Bukkit.getLogger().info("[ABX] ActionBar disabled.");
                task = null;
            }
        }


    }

    @Override
    public void onDisable () {
        PermActionBar PermActionBar = new PermActionBar(this);
        PermActionBar.stop();

        Bukkit.getLogger().info("ActionBarXtreme has been successfully disabled.");
    }

}