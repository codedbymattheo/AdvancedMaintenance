package cz.mattheo.commands;

import cz.mattheo.Main;
import cz.mattheo.utils.Command;
import cz.mattheo.utils.Executor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@Executor
public class MaintenanceCommand extends Command {

    private final Main plugin;

    public MaintenanceCommand(){
        super("maintenance", new String[]{}, null);
        this.plugin = Main.getInstance();
    }

    @Override
    public void execute(CommandSender sender, String[] args){
        if(!(sender instanceof Player)){
            return;
        }
        Player p = (Player) sender;

        if(!p.hasPermission("maintenance.admin")){
            p.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return;
        }

        if(args.length == 0){
            p.sendMessage(plugin.getConfigManager().getMessage("usage"));
            return;
        }

        String reason = args.length >= 2 ? String.join(" ", Arrays.copyOfRange(args, 1, args.length)) : null;

        switch(args[0].toLowerCase()){
            case "on":
                plugin.setMaintenanceEnabled(true);
                p.sendMessage(plugin.getConfigManager().getMessage("maintenance-enabled"));
                plugin.getSqLiteLogger().logAction(p.getName(), "ENABLED", reason);
                plugin.getDiscordWebhook().sendEmbed("🔧 Maintenance", p, "Maintenance has been switched on", 53572);
                return;

            case "off":
                plugin.setMaintenanceEnabled(false);
                p.sendMessage(plugin.getConfigManager().getMessage("maintenance-disabled"));
                plugin.getSqLiteLogger().logAction(p.getName(), "DISABLED", reason);
                plugin.getDiscordWebhook().sendEmbed("🔧 Maintenance", p,"Maintenance has been switched off", 16711680);
                return;

            case "status":
                boolean status = plugin.isMaintenanceEnabled();
                p.sendMessage(status ?
                        plugin.getConfigManager().getMessage("maintenance-status-on") :
                        plugin.getConfigManager().getMessage("maintenance-status-off"));
                return;

            case "schedule":
                if(args.length < 2){
                    p.sendMessage(plugin.getConfigManager().getMessage("maintenance-usage"));
                    return;
                }
                try{
                    int minutes = Integer.parseInt(args[1]);
                    String schReason = args.length >= 3 ? String.join(" ", Arrays.copyOfRange(args, 2, args.length)) : null;
                    plugin.getMaintenanceScheduler().schedule(minutes, schReason);
                    p.sendMessage(plugin.getConfigManager().getMessage("maintenance-scheduled-to").replace("%min%", Integer.toString(minutes)));
                    plugin.getDiscordWebhook().sendEmbed("🔧 Maintenance", p,"There will be maintenance in the next " + minutes + " minutes.", 16755968);
                }catch (NumberFormatException e){
                    p.sendMessage(plugin.getConfigManager().getMessage("invalid-number-format-scheduler"));
                }
                return;

            case "cancel":
                if(plugin.getMaintenanceScheduler().isScheduled()){
                    plugin.getMaintenanceScheduler().cancel();
                    p.sendMessage(plugin.getConfigManager().getMessage("maintenance-scheduled-canceled"));
                    plugin.getDiscordWebhook().sendEmbed("🔧 Maintenance", p, "Maintenance has been canceled!", 16755968);

                }else{
                    p.sendMessage(plugin.getConfigManager().getMessage("there-is-no-maintenance"));
                }
                return;

            default:
                p.sendMessage(plugin.getConfigManager().getMessage("usage"));
                return;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
