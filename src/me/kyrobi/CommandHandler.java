package me.kyrobi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.UUID;

public class CommandHandler implements CommandExecutor {

    private Main plugin;

    public CommandHandler(final Main plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args){

        if(commandSender instanceof Player) {
            final Player playerSender = (Player) commandSender; //Cast commandSender to player


            if (args.length == 1) {
                long time;
                String uuid;
                String prefix = this.plugin.getConfig().getString("prefix");
                Player player;
                DatabaseHandler database = new DatabaseHandler();

                //Get the player the player is requesting
                player = Bukkit.getPlayer(args[0]);

                //If the player is not online, we check if they have previously saved data
                if (player == null) {
                    //If previously data doesn't exist, we made sure they don't exist and then exit
                    if (Bukkit.getOfflinePlayer(args[0]).getFirstPlayed() == 0) {
                        playerSender.sendMessage((ChatColor.translateAlternateColorCodes('&', prefix)) + ChatColor.AQUA + args[0] + ChatColor.RED + " has never joined the server!");
                        return true;
                    }

                    //Previously player data exist, we try to fetch data from database
                    else {

                        //First check if it exists in the database. If so, we print it first
                        if (database.ifExist(Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString())) {
                            uuid = Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString();
                            time = database.getTime(uuid);
                            playerSender.sendMessage((ChatColor.translateAlternateColorCodes('&', prefix)) + ChatColor.AQUA + args[0] + ChatColor.GREEN + " joined on " + millisecondsToDate(time));
                            return true;
                        }

                        //If it's not in the database, we pull from playerdata and then write it to database
                        playerSender.sendMessage((ChatColor.translateAlternateColorCodes('&', prefix)) + ChatColor.AQUA + args[0] + ChatColor.GREEN + " joined on " + millisecondsToDate(Bukkit.getOfflinePlayer(args[0]).getFirstPlayed()));
                        if (!database.ifExist(Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString())) {
                            database.insert(Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString(), Bukkit.getOfflinePlayer(args[0]).getFirstPlayed());
                        }
                    }
                    return true;
                }

                //Assuming the player is online and exist, we pull data from the database
                time = database.getTime(player.getUniqueId().toString());
                //System.out.println("PLAYER ONLINE RETURNS " + database.getTime(player.getUniqueId().toString()));
                playerSender.sendMessage((ChatColor.translateAlternateColorCodes('&', prefix)) + ChatColor.AQUA + args[0] + ChatColor.GREEN + " joined on " + millisecondsToDate(time));
            } else {
                String prefix = this.plugin.getConfig().getString("prefix");
                playerSender.sendMessage((ChatColor.translateAlternateColorCodes('&', prefix)) + ChatColor.RED + "Usage: /jointime (username)");
            }
            return false;
        }
        return false;
    }


    private String millisecondsToDate(long timeInMillis) {

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        final int mYear = calendar.get(1);
        int mMonth = calendar.get(2);
        ++mMonth;
        final int mDay = calendar.get(5);
        String date = "";
        String monthToName = null;
        switch (mMonth) {
            case 1: {
                monthToName = "January";
                break;
            }
            case 2: {
                monthToName = "February";
                break;
            }
            case 3: {
                monthToName = "March";
                break;
            }
            case 4: {
                monthToName = "April";
                break;
            }
            case 5: {
                monthToName = "May";
                break;
            }
            case 6: {
                monthToName = "June";
                break;
            }
            case 7: {
                monthToName = "July";
                break;
            }
            case 8: {
                monthToName = "August";
                break;
            }
            case 9: {
                monthToName = "September";
                break;
            }
            case 10: {
                monthToName = "October";
                break;
            }
            case 11: {
                monthToName = "November";
                break;
            }
            case 12: {
                monthToName = "December";
                break;
            }
            default: {
                monthToName = "Error";
                break;
            }
        }
        if (mYear == 1969) {
            date = " has never joined or it's an invalid username.";
            return date;
        }
        date = String.valueOf(String.valueOf(monthToName)) + " " + mDay + ", " + mYear;
        return date;
    }
}
