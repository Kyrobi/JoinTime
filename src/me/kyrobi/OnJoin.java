package me.kyrobi;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoin implements Listener {

    public OnJoin(Main plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    //Adds players to the database on join if they aren't on it already
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e){

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
}
