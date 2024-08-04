package me.viann.playerStats;

import me.viann.playerStats.commands.SetClassCommand;
import me.viann.playerStats.commands.ShowClassCommand;
import me.viann.playerStats.managers.PlayerClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerStats extends JavaPlugin {
    private Map<String, PlayerClass> playerClasses = new HashMap<>();
    private Map<UUID, String> playerClassAssignments = new HashMap<>();
    private File classesFile;
    private FileConfiguration classesConfig;
    private Scoreboard scoreboard;

    @Override
    public void onEnable() {
        saveDefaultConfig(); // Save the default config if it doesn't exist

        // Initialize the file and configuration object
        classesFile = new File(getDataFolder(), "classes.yml");
        if (!classesFile.exists()) {
            saveResource("classes.yml", false); // Save default classes.yml if it doesn't exist
        }
        classesConfig = YamlConfiguration.loadConfiguration(classesFile);

        // Ensure that the classesConfig is loaded
        if (classesConfig == null) {
            getLogger().severe("Failed to load classes.yml configuration.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        loadClasses(); // Load classes from the configuration

        // Initialize scoreboard
        initializeScoreboard();

        // Register commands
        this.getCommand("setclass").setExecutor(new SetClassCommand(this));
        this.getCommand("showclass").setExecutor(new ShowClassCommand(this));
    }

    private void loadClasses() {
        if (classesConfig == null) {
            getLogger().severe("Classes configuration is not loaded.");
            return;
        }

        // Clear existing classes in case of reloading
        playerClasses.clear();

        for (String className : classesConfig.getKeys(false)) {
            double maxHealth = classesConfig.getDouble(className + ".max_health");
            double movementSpeed = classesConfig.getDouble(className + ".movement_speed");
            double attackDamage = classesConfig.getDouble(className + ".attack_damage");
            int colorCode = classesConfig.getInt(className + ".scoreboard_color");
            ChatColor scoreboardColor = getColorFromCode(colorCode);

            getLogger().info("Loaded class " + className + " with color " + scoreboardColor.name());

            playerClasses.put(className, new PlayerClass(maxHealth, movementSpeed, attackDamage, scoreboardColor));
        }
    }

    private ChatColor getColorFromCode(int code) {
        switch (code) {
            case 0: return ChatColor.BLACK;
            case 1: return ChatColor.DARK_BLUE;
            case 2: return ChatColor.DARK_GREEN;
            case 3: return ChatColor.DARK_AQUA;
            case 4: return ChatColor.DARK_RED;
            case 5: return ChatColor.DARK_PURPLE;
            case 6: return ChatColor.GOLD;
            case 7: return ChatColor.GRAY;
            case 8: return ChatColor.DARK_GRAY;
            case 9: return ChatColor.BLUE;
            default: return ChatColor.BLACK; // Default to WHITE if invalid code
        }
    }

    private void initializeScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager != null) {
            scoreboard = manager.getNewScoreboard();
        }

        // Create or update teams for each class
        for (String className : playerClasses.keySet()) {
            Team team = scoreboard.getTeam(className);
            if (team == null) {
                team = scoreboard.registerNewTeam(className);
                getLogger().info("Created new team for class: " + className);
            } else {
                getLogger().info("Updating existing team for class: " + className);
            }
            PlayerClass playerClass = playerClasses.get(className);
            if (playerClass != null) {
                ChatColor color = playerClass.getScoreboardColor();
                team.setColor(color);
                team.setPrefix(className + " "); // Optional: Set prefix or suffix if needed
                getLogger().info("Set color " + color.name() + " for team " + className);
            } else {
                getLogger().warning("PlayerClass is null for class " + className);
            }
        }

        // Set the default scoreboard for online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayerScoreboard(player);
        }
    }

    public PlayerClass getPlayerClass(String className) {
        return playerClasses.get(className);
    }

    public String getPlayerClassAssignment(UUID playerUUID) {
        return playerClassAssignments.get(playerUUID);
    }

    public void setPlayerClassAssignment(UUID playerUUID, String className) {
        playerClassAssignments.put(playerUUID, className);

        // Update the player's team to reflect the class change
        Player player = Bukkit.getPlayer(playerUUID);
        if (player != null && scoreboard != null) {
            updatePlayerScoreboard(player);
        }
    }

    private void updatePlayerScoreboard(Player player) {
        // Remove player from all teams
        for (Team team : scoreboard.getTeams()) {
            team.removeEntry(player.getName());
        }

        // Add player to the appropriate team
        String className = playerClassAssignments.get(player.getUniqueId());
        if (className != null) {
            Team newTeam = scoreboard.getTeam(className);
            if (newTeam != null) {
                newTeam.addEntry(player.getName());
                player.setScoreboard(scoreboard);
                getLogger().info("Updated scoreboard for player " + player.getName() + " to class " + className);
            } else {
                getLogger().warning("No team found for class " + className);
            }
        } else {
            getLogger().warning("No class assignment found for player " + player.getName());
        }
    }
}
