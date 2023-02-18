package me.kyrobi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Calendar;

public class CommandHandler implements CommandExecutor {

    Main plugin;

    public CommandHandler(final Main plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args){

        //if(commandSender instanceof Player) {
            //final Player playerSender = (Player) commandSender; //Cast commandSender to player


            if (args.length == 1) {
                long time;
                String uuid;
                String prefix = plugin.getConfig().getString("prefix");
                Player player;
                DatabaseHandler database = new DatabaseHandler();

                //Get the player the player is requesting
                player = Bukkit.getPlayer(args[0]);

                //If the player is not online, we check if they have previously saved data
                if (player == null) {
                    //If previously data doesn't exist, we made sure they don't exist and then exit
                    if (Bukkit.getOfflinePlayer(args[0]).getFirstPlayed() == 0) {
                        commandSender.sendMessage((ChatColor.translateAlternateColorCodes('&', prefix)) + ChatColor.AQUA + args[0] + ChatColor.RED + " has never joined the server!");
                        //commandSender.sendMessage("test");
                        return false;
                    }

                    //Previously player data exist, we try to fetch data from database
                    else {
                        //First check if it exists in the database. If so, we print it first
                        if (database.ifExist(Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString())) {
                            uuid = Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString();
                            time = database.getTime(uuid);
                            commandSender.sendMessage((ChatColor.translateAlternateColorCodes('&', prefix)) + ChatColor.AQUA + args[0] + ChatColor.GREEN + " joined on " + millisecondsToDate(time) + ChatColor.GRAY + " \n(" + millisecondsToTimeStamp(time) + " ago)") ;
                            System.out.println("Offline player exists in the database");
                            return false;
                        }

                        //If it's not in the database, we pull from playerdata and then write it to database
                        commandSender.sendMessage((ChatColor.translateAlternateColorCodes('&', prefix)) + ChatColor.AQUA + args[0] + ChatColor.GREEN + " joined on " + millisecondsToDate(Bukkit.getOfflinePlayer(args[0]).getFirstPlayed()) );
                        if (!database.ifExist(Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString())) {
                            System.out.println("Offline player does not in the database");
                            database.insert(Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString(), Bukkit.getOfflinePlayer(args[0]).getFirstPlayed());
                        }
                    }
                    return false;
                }

                //Assuming the player is online and exist, we pull data from the database
                time = database.getTime(player.getUniqueId().toString());
                //System.out.println("PLAYER ONLINE RETURNS " + database.getTime(player.getUniqueId().toString()));
                commandSender.sendMessage((ChatColor.translateAlternateColorCodes('&', prefix)) + ChatColor.AQUA + args[0] + ChatColor.GREEN + " joined on " + millisecondsToDate(time) + ChatColor.GRAY + " \n(" + millisecondsToTimeStamp(time) + " ago)");
                System.out.println("Getting data of online player");

            } else {
                String prefix = this.plugin.getConfig().getString("prefix");
                commandSender.sendMessage((ChatColor.translateAlternateColorCodes('&', prefix)) + ChatColor.RED + "Usage: /jointime (username)");
            }
            return false;
        //}
        //return false;
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

    private String millisecondsToTimeStamp(long milliSeconds) {

        Calendar calendar = Calendar.getInstance();
        long currentMillis = calendar.getTimeInMillis();

        double different = currentMillis - milliSeconds; // long upTime = bean.getUptime(); Time in ms

        double secondsInMilli = 1000;
        double minutesInMilli = secondsInMilli * 60;
        double hoursInMilli = minutesInMilli * 60;
        double daysInMilli = hoursInMilli * 24;
        double monthsInMilli = daysInMilli * 30.4375; //Roughly than many days in a month including leap year and months containing 30 / 31 days
        double yearsInMilli = monthsInMilli * 12;

        double years = different / yearsInMilli;
        different = different % yearsInMilli;

        double months = different / monthsInMilli;
        different = different % monthsInMilli;

        double days = different / daysInMilli;


        //System.out.println((int)years + "y " + (int)months + "m " + (int)days +"d");
        String elaspedTime = (int)years + "y " + (int)months + "m " + (int)days +"d";
        return elaspedTime;
    }
}
