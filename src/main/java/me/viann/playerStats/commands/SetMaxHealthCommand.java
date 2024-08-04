package me.viann.playerStats.commands;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetMaxHealthCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("Usage: /setmaxhealth <player> <amount>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found");
            return true;
        }

        try {
            double maxHealth = Double.parseDouble(args[1]);
            AttributeInstance attribute = target.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (attribute != null) {
                attribute.setBaseValue(maxHealth);
                sender.sendMessage("Set " + target.getName() + "'s max health to " + maxHealth);
            } else {
                sender.sendMessage("Failed to set max health");
            }
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid number format");
        }
        return true;
    }
}
