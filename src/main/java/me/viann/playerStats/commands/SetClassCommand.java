package me.viann.playerStats.commands;

import me.viann.playerStats.PlayerStats;
import me.viann.playerStats.managers.PlayerClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetClassCommand implements CommandExecutor {
    private final PlayerStats plugin;

    public SetClassCommand(PlayerStats plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("Usage: /setclass <player> <class>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found");
            return true;
        }

        String className = args[1];
        PlayerClass playerClass = plugin.getPlayerClass(className);
        if (playerClass == null) {
            sender.sendMessage("Class not found");
            return true;
        }

        plugin.setPlayerClassAssignment(target.getUniqueId(), className);
        setPlayerAttributes(target, playerClass);
        sender.sendMessage("Set " + target.getName() + " to class " + className);
        return true;
    }

    private void setPlayerAttributes(Player player, PlayerClass playerClass) {
        AttributeInstance maxHealthAttr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealthAttr != null) {
            maxHealthAttr.setBaseValue(playerClass.getMaxHealth());
        }

        AttributeInstance movementSpeedAttr = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (movementSpeedAttr != null) {
            movementSpeedAttr.setBaseValue(playerClass.getMovementSpeed());
        }

        AttributeInstance attackDamageAttr = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (attackDamageAttr != null) {
            attackDamageAttr.setBaseValue(playerClass.getAttackDamage());
        }
    }
}
