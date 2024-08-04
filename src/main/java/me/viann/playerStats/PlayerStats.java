package me.viann.playerStats;

import me.viann.playerStats.commands.SetClassCommand;
import me.viann.playerStats.commands.SetHealthCommand;
import me.viann.playerStats.commands.SetMaxHealthCommand;
import me.viann.playerStats.commands.SetSpeedCommand;
import me.viann.playerStats.managers.PlayerClass;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class PlayerStats extends JavaPlugin {

    private final Map<String, PlayerClass> playerClasses = new HashMap<>();
    private File classesFile;
    private FileConfiguration classesConfig;

    @Override
    public void onEnable() {
        // Save the default config if it doesn't exist
        saveDefaultConfig();

        // Initialize the file and configuration object
        classesFile = new File(getDataFolder(), "classes.yml");
        if (!classesFile.exists()) {
            saveResource("classes.yml", false); // Save default classes.yml if it doesn't exist
        }

        // Initialize the FileConfiguration
        classesConfig = YamlConfiguration.loadConfiguration(classesFile);

        // Ensure that the classesConfig is loaded
        if (classesConfig == null) {
            getLogger().severe("Failed to load classes.yml configuration.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    public void registerCommands() {
        Objects.requireNonNull(this.getCommand("setspeed")).setExecutor(new SetSpeedCommand());
        Objects.requireNonNull(this.getCommand("setmaxhealth")).setExecutor(new SetMaxHealthCommand());
        Objects.requireNonNull(this.getCommand("sethealth")).setExecutor(new SetHealthCommand());
        Objects.requireNonNull(this.getCommand("setclass")).setExecutor(new SetClassCommand(this));
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
            playerClasses.put(className, new PlayerClass(maxHealth, movementSpeed, attackDamage));
        }
    }

    public PlayerClass getPlayerClass(String className) {
        return playerClasses.get(className);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.getLogger().info("PlayerStats has been unloaded");
    }
}
