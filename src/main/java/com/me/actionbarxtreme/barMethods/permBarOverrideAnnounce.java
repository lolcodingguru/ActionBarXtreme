package com.me.actionbarxtreme.barMethods;

import com.me.actionbarxtreme.ActionBarXtreme;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
// a
public class permBarOverrideAnnounce implements Runnable {

    private Player targetPlayer;
    private final ActionBarXtreme plugin;
    public BukkitTask task;
    private TextComponent component;
    private int duration;

    public permBarOverrideAnnounce(ActionBarXtreme plugin) {
        this.plugin = plugin;
    }

    public void actionbarAnnounceToPlayer(Player player, int duration, String message) {
        this.targetPlayer = player;
        final int finalDuration = duration * 20;
        this.duration = finalDuration;

        if (message == null || message.isEmpty()) {
            return;
        }

        final String finalMessage = plugin.getConfig().getString("prefix") + message;

        plugin.permActionBar.stop(player);

        try {
            task.cancel();
        } catch (Exception ignored) {}

        task = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            if (plugin.LegacyColors) {
                component = new TextComponent(ChatColor.translateAlternateColorCodes('&', finalMessage));
            } else {
                component = new TextComponent(finalMessage.replaceAll("&", "§"));
            }

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);

            this.duration--;
            if (this.duration <= 0) {
                try {
                    task.cancel();
                } catch (Exception ignored) {}
                plugin.permActionBar.start(player);
            }
        }, 0, 1);

        if (plugin.getConfig().getBoolean("Announcements.soundEffect")) {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }
    }

    public void actionbarAnnounce(int duration, String message) {

        this.targetPlayer = null; // Clear the target player
        this.duration = duration * 20;

        if (message == null || message.isEmpty()) {
            return;
        }

        message = plugin.getConfig().getString("prefix") + message;

        plugin.permActionBar.stopAll();

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
        if (targetPlayer != null) {
            if (targetPlayer.isOnline()) {
                targetPlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
            }
        } else {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
            }
        }

        duration--;
        if (duration <= 0) {
            try {
                task.cancel();
            } catch (Exception ignored) {}
            if(targetPlayer != null) {
            plugin.permActionBar.start(targetPlayer);
            } else {
                plugin.permActionBar.startAll();
            }
        }
    }

}