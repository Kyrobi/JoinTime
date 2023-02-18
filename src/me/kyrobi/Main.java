package me.kyrobi;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    public ConsoleCommandSender console;

    @Override
    public void onEnable(){
        console = Bukkit.getServer().getConsoleSender();
        this.getCommand("jointime").setExecutor((CommandExecutor)new CommandHandler(this)); //Registers the command
        this.getCommand("jointimeupdate").setExecutor((CommandExecutor)new UpdateCommand(this)); //Registers the command
        this.saveDefaultConfig();

        DatabaseHandler sqlite = new DatabaseHandler();
        new OnJoin(this);

        //Check if the database exists. If not, create a new one
        File dbFile = new File("");
        File folderDirectory = new File(dbFile.getAbsolutePath() + File.separator + "plugins" + File.separator + "JoinTime" + File.separator + "storage.db");

        boolean exists = folderDirectory.exists();
        if(!exists){
            System.out.println("DATABASE FILE NOT EXIST!");
            System.out.println(folderDirectory);
            sqlite.createNewDatabase();
        }

        console.sendMessage("[Jointime] Loaded!");
    }

    public void onDisable(){
        console.sendMessage("[Jointime] Disabled!");
    }
}
