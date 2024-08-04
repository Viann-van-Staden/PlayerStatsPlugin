package me.viann.playerStats.managers;

public class PlayerClass {
    private final double maxHealth;
    private final double movementSpeed;
    private final double attackDamage;

    public PlayerClass(double maxHealth, double movementSpeed, double attackDamage) {
        this.maxHealth = maxHealth;
        this.movementSpeed = movementSpeed;
        this.attackDamage = attackDamage;
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
}
