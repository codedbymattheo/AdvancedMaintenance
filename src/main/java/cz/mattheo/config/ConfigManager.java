package cz.mattheo.config;

import cz.mattheo.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {

    private final Main plugin;
    private FileConfiguration messages;

    public ConfigManager(){
        this.plugin = Main.getInstance();
    }

    public void loadMessages(){
        File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if(!messagesFile.exists()){
            plugin.saveResource("messages.yml", false);
        }

        messages = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public String getMessage(String key){
        if(messages == null){
            return key;
        }
        return messages.getString(key, "§cMessage '" + key + "' not found!");
    }

    public FileConfiguration getConfig(){
        return plugin.getConfig();
    }

}
