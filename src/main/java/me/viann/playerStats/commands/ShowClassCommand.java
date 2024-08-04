package me.viann.playerStats.commands;

import me.viann.playerStats.PlayerStats;
import me.viann.playerStats.managers.PlayerClass;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ShowClassCommand implements CommandExecutor {
    private final PlayerStats plugin;

    public ShowClassCommand(PlayerStats plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Usage: /showclass <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found");
            return true;
        }

        String className = plugin.getPlayerClassAssignment(target.getUniqueId());
        if (className == null) {
            sender.sendMessage(target.getName() + " has no class assigned.");
            return true;
        }

        sender.sendMessage(target.getName() + "'s class is " + className);
        return true;
    }
}
