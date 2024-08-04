package me.viann.playerStats.managers;

import org.bukkit.ChatColor;

public class PlayerClass {
    private final double maxHealth;
    private final double movementSpeed;
    private final double attackDamage;
    private final ChatColor scoreboardColor;

    public PlayerClass(double maxHealth, double movementSpeed, double attackDamage, ChatColor scoreboardColor) {
        this.maxHealth = maxHealth;
        this.movementSpeed = movementSpeed;
        this.attackDamage = attackDamage;
        this.scoreboardColor = scoreboardColor;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public double getMovementSpeed() {
        return movementSpeed;
    }

    public double getAttackDamage() {
        return attackDamage;
    }

    public ChatColor getScoreboardColor() {
        return scoreboardColor;
    }
}
