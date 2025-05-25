package cz.mattheo.utils.version;

import cz.mattheo.Main;
import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VersionChecker {

    private final Main plugin;
    private final String currentVersion;
    private final String gitHubURL;

    public VersionChecker(){
        this.plugin = Main.getInstance();
        this.currentVersion = plugin.getDescription().getVersion();
        this.gitHubURL= "https://api.github.com/repos/codedbymattheo/advancedmaintenance/releases/latest";
    }

    public void checkForUpdates(){
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try{
                URL url = new URL(gitHubURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(reader);
                reader.close();

                String latestVersion = (String) json.get("tag_name");

                if(!currentVersion.equalsIgnoreCase(latestVersion)){
                    Bukkit.getLogger().warning("There is a new version available: " + latestVersion + " you're using version (" + currentVersion + ")");
                }else{
                    Bukkit.getLogger().warning("You have latest version of this plugin! (" + currentVersion + ")");
                }
           }catch (Exception e){
               e.printStackTrace();
           }
        });
    }
}
