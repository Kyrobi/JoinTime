package me.kyrobi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;

public class UpdateCommand implements CommandExecutor {

    private Main plugin;

    public UpdateCommand(final Main plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args){

        //if(commandSender instanceof Player) {
        //final Player playerSender = (Player) commandSender; //Cast commandSender to player

        if(!(commandSender instanceof ConsoleCommandSender)){
            commandSender.sendMessage( ChatColor.RED + "You cannot use this command!");
            return false;
        }
        if (args.length == 2) {

            new BukkitRunnable() {
                public void run()
                {
                    String uuid = args[0];
                    long time = Long.parseLong(args[1]);

                    DatabaseHandler database = new DatabaseHandler();
                    System.out.println("UUID: " + uuid + " Time: " + time);
                    database.update(uuid, time);
                }
            }.runTaskAsynchronously(plugin);

        } else {
            String prefix = this.plugin.getConfig().getString("prefix");
            commandSender.sendMessage(ChatColor.RED + "Usage: /jointimeupdate <uuid> <time in ms>");
        }
        return false;
    }
}
