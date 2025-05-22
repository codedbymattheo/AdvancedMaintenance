package cz.mattheo.listeners;

import cz.mattheo.Main;
import cz.mattheo.utils.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

@Event
public class PlayerLogin implements Listener {

    private final Main plugin;

    public PlayerLogin(){
        this.plugin = Main.getInstance();
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e){
        if(plugin.isMaintenanceEnabled() && !e.getPlayer().hasPermission("maintenance.bypass")){
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, plugin.getConfigManager().getMessage("kick-message"));
        }
    }
}
