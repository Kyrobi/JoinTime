package me.kyrobi;

import com.google.common.base.Stopwatch;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class OnJoin implements Listener {

    Main plugin;
    public OnJoin(Main plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    //Adds players to the database on join if they aren't on it already
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e){
        new BukkitRunnable(){
            public void run(){
                DatabaseHandler database = new DatabaseHandler();

                Player player = e.getPlayer();
                String uuid = player.getUniqueId().toString();
                long playerFirstJoin;

                if(!database.ifExist(uuid)){
                    System.out.println("UUID NOT IN DATABASE");
                    playerFirstJoin = ((OfflinePlayer) player).getFirstPlayed();
                    database.insert(uuid, playerFirstJoin);
                }
            }

        }.runTaskAsynchronously(plugin);
    }
}

/*
new BukkitRunnable(){
            public void run(){

            }

        }.runTaskAsynchronously(plugin);
 */