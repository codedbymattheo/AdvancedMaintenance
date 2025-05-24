package cz.mattheo;

import cz.mattheo.api.DiscordWebhook;
import cz.mattheo.config.ConfigManager;
import cz.mattheo.scheduler.MaintenanceScheduler;
import cz.mattheo.storage.SQLiteLogger;
import cz.mattheo.utils.CommandExecutor;
import cz.mattheo.utils.EventExecutor;
import cz.mattheo.utils.version.VersionChecker;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@Setter
public final class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    private boolean maintenanceEnabled = getConfigManager().getConfig().getBoolean("maintenance");

    private ConfigManager configManager;
    private SQLiteLogger sqLiteLogger;
    private MaintenanceScheduler maintenanceScheduler;
    private DiscordWebhook discordWebhook;
    private VersionChecker versionChecker;

    @Override
    public void onEnable() {
        getLogger().info("✅ AdvancedMaintenance has been enabled!");

        instance = this;
        saveDefaultConfig();

        configManager = new ConfigManager();
        configManager.loadMessages();

        sqLiteLogger = new SQLiteLogger();

        maintenanceScheduler = new MaintenanceScheduler();
        discordWebhook = new DiscordWebhook();

        versionChecker = new VersionChecker();
        versionChecker.checkForUpdates();

        registerUtils();
    }

    @Override
    public void onDisable() {
        getLogger().info("❌ AdvancedMaintenance has been disabled!");
        if(sqLiteLogger != null) sqLiteLogger.close();
    }

    private void registerUtils(){
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.autoRegister();

        EventExecutor eventExecutor = new EventExecutor();
        eventExecutor.autoEventRegister();
    }

    public void setMaintenanceMode(boolean maintenance){
        this.maintenanceEnabled = maintenance;
        getConfigManager().getConfig().set("maintenance", maintenance);
        saveConfig();
    }

}
