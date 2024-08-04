package me.viann.playerStats.commands;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetSpeedCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("Usage: /setspeed <player> <speed>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found");
            return true;
        }

        try {
            double speed = Double.parseDouble(args[1]);
            if (speed < 0 || speed > 1) {
                sender.sendMessage("Speed must be between 0 and 1");
                return true;
            }

            AttributeInstance attribute = target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
            if (attribute != null) {
                attribute.setBaseValue(speed);
                sender.sendMessage("Set " + target.getName() + "'s speed to " + speed);
            } else {
                sender.sendMessage("Failed to set speed");
            }
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid number format");
        }
        return true;
    }
}
