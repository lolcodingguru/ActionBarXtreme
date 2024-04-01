package com.me.actionbarxtreme.barMethods;

import com.me.actionbarxtreme.ActionBarXtreme;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class permBarOverrideAnnounce implements Runnable {

    private final ActionBarXtreme plugin;
    public BukkitTask task;
    private TextComponent component;
    private int duration;

    public permBarOverrideAnnounce(ActionBarXtreme plugin) {
        this.plugin = plugin;
    }

    public void actionbarAnnounce(int duration, String message) {

        this.duration = duration * 20;

        if (message == null || message.isEmpty()) {
            return;
        }

        message = plugin.getConfig().getString("prefix") + message;

        plugin.permActionBar.stop();

        try {
            task.cancel();
        } catch (Exception ignored) {}

        task = plugin.getServer().getScheduler().runTaskTimer(plugin, this, 0, 1);

        if (plugin.LegacyColors) {
            component = new TextComponent(ChatColor.translateAlternateColorCodes('&', message));
        } else {
            message = message.replaceAll("&", "§");
            component = new TextComponent(message);
        }

        if (plugin.getConfig().getBoolean("Announcements.soundEffect")) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }
        }

    }

    public void cancelTask() {
            task.cancel();
    }

    @Override
    public synchronized void run() {

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
        }

        duration--;
        if (duration <= 0) {
            try {
                task.cancel();
            } catch (Exception ignored) {}
            plugin.permActionBar.start();
        }
    }

}