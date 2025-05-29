package com.me.actionbarxtreme.barMethods;

import com.me.actionbarxtreme.ActionBarXtreme;
import com.me.actionbarxtreme.utils.logging;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class permBarOverrideAnnounce {

    private final ActionBarXtreme plugin;
    private AnnounceTask globalTask = null;
    private Map<UUID, AnnounceTask> playerTasks = new HashMap<>();

    public permBarOverrideAnnounce(ActionBarXtreme plugin) {
        this.plugin = plugin;
    }

    /**
     * Announce to a specific player via action bar
     */
    public void actionbarAnnounceToPlayer(Player player, int duration, String message) {
        if (player == null || message == null || message.isEmpty()) {
            return;
        }

        // Double-check player is online on the same thread
        if (!player.isOnline()) {
            return;
        }

        // Format the message
        final String finalMessage = plugin.getConfig().getString("prefix") + message;

        // Stop any existing player-specific task for this player
        AnnounceTask existingTask = playerTasks.get(player.getUniqueId());
        if (existingTask != null) {
            existingTask.cancel();
            playerTasks.remove(player.getUniqueId());
        }

        // Stop the permanent action bar for this player
        plugin.permActionBar.stop(player);

        // Create a new single-player announcement task
        SinglePlayerAnnounceTask task = new SinglePlayerAnnounceTask(player, duration, finalMessage);
        playerTasks.put(player.getUniqueId(), task);
        task.start();

        // Play sound if enabled
        if (plugin.getConfig().getBoolean("Announcements.soundEffect")) {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }

        logging.log(logging.LogLevel.DEBUG, "Started player-specific announcement for: " + player.getName());
    }

    /**
     * Announce to all players via action bar
     */
    public void actionbarAnnounce(int duration, String message) {
        if (message == null || message.isEmpty()) {
            return;
        }

        // Format the message
        final String finalMessage = plugin.getConfig().getString("prefix") + message;

        // Cancel any existing global announcement task
        if (globalTask != null) {
            globalTask.cancel();
            globalTask = null;
        }

        // Stop all permanent action bars
        plugin.permActionBar.stopAll();

        // Create a new all-players announcement task
        globalTask = new AllPlayersAnnounceTask(duration, finalMessage);
        globalTask.start();

        // Play sound if enabled
        if (plugin.getConfig().getBoolean("Announcements.soundEffect")) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            }
        }

        logging.log(logging.LogLevel.DEBUG, "Started global announcement for all players");
    }

    /**
     * Cancel all running announcement tasks
     */
    public void cancelTask() {
        // Cancel the global task
        if (globalTask != null) {
            globalTask.cancel();
            globalTask = null;
        }

        // Cancel all player-specific tasks
        for (AnnounceTask task : playerTasks.values()) {
            task.cancel();
        }
        playerTasks.clear();

        logging.log(logging.LogLevel.DEBUG, "Cancelled all announcement tasks");
    }

    /**
     * Abstract base class for announcement tasks
     */
    private abstract class AnnounceTask {
        protected final String message;
        protected final int durationTicks;
        protected int remainingTicks;
        protected BukkitTask task = null;
        protected TextComponent component;

        public AnnounceTask(int durationSeconds, String message) {
            this.durationTicks = durationSeconds * 20;
            this.remainingTicks = this.durationTicks;
            this.message = message;

            // Format the message once to avoid doing it every tick
            if (plugin.LegacyColors) {
                this.component = new TextComponent(ChatColor.translateAlternateColorCodes('&', message));
            } else {
                this.component = new TextComponent(message.replaceAll("&", "§"));
            }
        }

        /**
         * Start the announcement task
         */
        public void start() {
            if (task != null) return; // Already running

            task = plugin.getServer().getScheduler().runTaskTimer(plugin, this::run, 0, 1);
            logging.log(logging.LogLevel.DEBUG, "Started announcement task");
        }

        /**
         * Cancel the announcement task
         */
        public void cancel() {
            if (task == null) return; // Not running

            task.cancel();
            task = null;
            logging.log(logging.LogLevel.DEBUG, "Canceled announcement task");

            // Clean up after cancellation
            onComplete();
        }

        /**
         * Run method called every tick
         */
        protected void run() {
            // Send the message to relevant players
            sendMessage();

            // Decrement the timer
            remainingTicks--;

            // Check if the announcement is complete
            if (remainingTicks <= 0) {
                cancel();
            }
        }

        /**
         * Send the message to relevant players
         */
        protected abstract void sendMessage();

        /**
         * Actions to perform when the announcement completes
         */
        protected abstract void onComplete();

        /**
         * Check if this task affects the given player
         */
        protected abstract boolean affectsPlayer(Player player);
    }

    /**
     * Announcement task for a single player
     */
    private class SinglePlayerAnnounceTask extends AnnounceTask {
        private final Player player;
        private final UUID playerId;

        public SinglePlayerAnnounceTask(Player player, int durationSeconds, String message) {
            super(durationSeconds, message);
            // Null check for player as safety
            if (player == null) {
                throw new IllegalArgumentException("Player cannot be null");
            }
            this.player = player;
            this.playerId = player.getUniqueId();
        }

        @Override
        protected void sendMessage() {
            try {
                if (player != null && player.isOnline()) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
                } else {
                    // Player went offline, cancel the task
                    cancel();
                }
            } catch (Exception e) {
                // Log the error and cancel the task
                logging.log(logging.LogLevel.ERROR, "Error sending action bar message to player: " + e.getMessage());
                cancel();
            }
        }

        @Override
        protected void onComplete() {
            // Remove this task from player tasks map
            playerTasks.remove(playerId);

            // Check if there's an active global announcement
            if (globalTask != null && globalTask.task != null) {
                // Add this player to the global announcement instead of restarting permanent action bar
                logging.log(logging.LogLevel.DEBUG, "Player " + player.getName() + " joining ongoing global announcement");
                // No need to do anything else - the global announcement will handle this player now
            } else {
                // Reset the tickCounter for this player
                plugin.permActionBar.resetTickCounter(player);

                // Restart permanent action bar for this player if they're online
                if (player.isOnline()) {
                    plugin.permActionBar.start(player);
                }
            }
        }

        @Override
        protected boolean affectsPlayer(Player p) {
            return p.getUniqueId().equals(playerId);
        }
    }

    /**
     * Announcement task for all players
     */
    private class AllPlayersAnnounceTask extends AnnounceTask {
        // Keep track of players who should receive this announcement
        private final Set<UUID> activePlayerIds = new HashSet<>();

        public AllPlayersAnnounceTask(int durationSeconds, String message) {
            super(durationSeconds, message);

            // Initialize with all current online players
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                activePlayerIds.add(player.getUniqueId());
            }
        }

        @Override
        protected void sendMessage() {
            if (activePlayerIds.isEmpty()) {
                cancel();
                return;
            }

            // Set to keep track of which players were processed
            Set<UUID> processedPlayers = new HashSet<>();

            for (Player player : plugin.getServer().getOnlinePlayers()) {
                UUID playerId = player.getUniqueId();
                processedPlayers.add(playerId);

                // Skip players with a targeted announcement
                if (playerTasks.containsKey(playerId)) {
                    continue;
                }

                // Add this player to our active set if not already there
                if (!activePlayerIds.contains(playerId)) {
                    activePlayerIds.add(playerId);
                    logging.log(logging.LogLevel.DEBUG, "Added player " + player.getName() + " to global announcement");
                }

                // Send the message with error handling
                try {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
                } catch (Exception e) {
                    logging.log(logging.LogLevel.ERROR, "Error in global action bar for player " + player.getName() + ": " + e.getMessage());
                    // Remove problematic player from active set
                    activePlayerIds.remove(playerId);
                }
            }

            // Remove players who are no longer online
            activePlayerIds.retainAll(processedPlayers);
        }

        @Override
        protected void onComplete() {
            // Clear global task reference
            if (globalTask == this) {
                globalTask = null;
            }

            // Reset the global tickCounter
            plugin.permActionBar.resetTickCounter();

            // Restart permanent action bars for all players except those with player-specific announcements
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                if (!playerTasks.containsKey(player.getUniqueId())) {
                    plugin.permActionBar.start(player);
                }
            }
        }

        @Override
        protected boolean affectsPlayer(Player player) {
            // Global announcements affect all players EXCEPT those with targeted announcements
            return !playerTasks.containsKey(player.getUniqueId());
        }
    }
}