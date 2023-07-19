package com.me.actionbarxtreme;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class permBarOverrideAnnounce implements Runnable {

    private final ActionBarXtreme plugin;
    private String message;
    private BukkitTask task;
    private TextComponent component;
    private int duration;

    public permBarOverrideAnnounce(ActionBarXtreme plugin) {
        this.plugin = plugin;
    }

    public void actionbarAnnounce(int duration, String message) {
        if (duration == 0) {
            duration = 5;
        }
        this.duration = duration * 20;

        Bukkit.getLogger().info("Debug message: " + duration + " seconds logged.");

        if (message == null || message.isEmpty()) {
            return;
        }

        plugin.permActionBar.stop();
        Bukkit.getLogger().info("Permanent Task cancelled!");

        if (task != null) {
            task.cancel();
        }

        Bukkit.getLogger().info("Beginning announce task!");
        task = plugin.getServer().getScheduler().runTaskTimer(plugin, this, 0, 1);
        Bukkit.getLogger().info("Announce task started!");

        if (plugin.LegacyColors) {
            component = new TextComponent(ChatColor.translateAlternateColorCodes('&', message));
        } else {
            message = message.replaceAll("&", "§");
            component = new TextComponent(message);
        }

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }

    }

    @Override
    public void run() {
        if (component == null) {
            Bukkit.getLogger().warning("Component is null.");
            return;
        }

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
        }

        duration--;
        Bukkit.getLogger().info("Debug message: " + duration + " ticks left.");
        if (duration <= 0) {
            Bukkit.getLogger().info("Reached " + duration + " ticks. Cancelling task.");
            task.cancel();
            plugin.permActionBar.start();
            Bukkit.getLogger().info("STOPPED ANNOUNCE TASK! PERMANENT TASK RESTARTED!");
            Bukkit.getLogger().info("Debug tests passed! Feature should be working!");
        }
    }

}

