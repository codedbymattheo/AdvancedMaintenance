package cz.mattheo.scheduler;

import cz.mattheo.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class MaintenanceScheduler {

    private final Main plugin;
    private BukkitTask countdownTask;
    private long secondsLeft;
    private String reason;

    public MaintenanceScheduler(){
        this.plugin = Main.getInstance();
    }

    public void schedule(int minutes, String reason){
        if(countdownTask != null){
            cancel();
        }

        this.secondsLeft = minutes * 60;
        this.reason = reason;

        plugin.getSqLiteLogger().logAction("SYSTEM", "SCHEDULED", "Maintenance in " + minutes + " min" + (reason != null ? " – " + reason : ""));

        if(reason != null){
            broadcast(plugin.getConfigManager().getMessage("scheduler-broadcast-with-reason").replace("%min%", Integer.toString(minutes)).replace("%reason%", reason));
        }else{
            broadcast(plugin.getConfigManager().getMessage("scheduler-broadcast-without-reason").replace("%min%", Integer.toString(minutes)));
        }



        countdownTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
           if(secondsLeft == 0){
               plugin.setMaintenanceMode(true);
               broadcast(plugin.getConfigManager().getMessage("maintenance-schedule-start"));

               plugin.getSqLiteLogger().logAction("SYSTEM", "AUTO_ENABLED", reason != null ? reason : "Planned");

               Bukkit.getOnlinePlayers().stream()
                       .filter(p -> !p.hasPermission("maintenance.bypass"))
                       .forEach(p -> p.kickPlayer(plugin.getConfigManager().getMessage("kick-message")));
                cancel();
                return;
           }
            if (shouldAnnounce(secondsLeft)) {
                broadcast(plugin.getConfigManager().getMessage("maintenance-in").replace("%min%", Long.toString(secondsLeft / 60)).replace("%sec%", Long.toString(secondsLeft % 60)));
            }

           secondsLeft--;
        }, 0L, 20L);
    }

    public void cancel(){
        if(countdownTask != null){
            countdownTask.cancel();
            countdownTask = null;
            broadcast(plugin.getConfigManager().getMessage("maintenance-scheduled-canceled"));
            plugin.getSqLiteLogger().logAction("SYSTEM", "CANCELLED", reason != null ? reason : "Without reason");
        }
    }

    private boolean shouldAnnounce(long seconds){
        return seconds == 300 || seconds == 60 || seconds == 30 || seconds == 10 || seconds <= 5;
    }

    private void broadcast(String msg){
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendMessage(msg);
        }
    }

    public boolean isScheduled(){
        return countdownTask != null;
    }
}
