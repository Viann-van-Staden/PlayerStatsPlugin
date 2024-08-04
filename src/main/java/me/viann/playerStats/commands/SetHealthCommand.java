package me.viann.playerStats.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetHealthCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("Usage: /sethealth <player> <amount>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found");
            return true;
        }

        try {
            double health = Double.parseDouble(args[1]);
            target.setHealth(health);
            sender.sendMessage("Set " + target.getName() + "'s health to " + health);
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid number format");
        } catch (IllegalArgumentException e) {
            sender.sendMessage("Health value out of range");
        }
        return true;
    }
}
