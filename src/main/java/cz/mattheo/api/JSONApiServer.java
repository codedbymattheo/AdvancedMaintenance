package cz.mattheo.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import cz.mattheo.Main;

import java.io.IOException;
import java.net.InetSocketAddress;

public class JSONApiServer {

    private final Main plugin;
    private HttpServer server;

    public JSONApiServer(){
        this.plugin = Main.getInstance();
    }

    public void start(){
        int port = plugin.getConfigManager().getConfig().getInt("api.port");
        boolean enabled = plugin.getConfigManager().getConfig().getBoolean("api.enabled");

        if(!enabled){
            plugin.getLogger().info("JSON API is disabled in config");
            return;
        }

        try{
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext(plugin.getConfigManager().getConfig().getString("api.json-path"), new StatusHandler());
            server.setExecutor(null);
            server.start();

            plugin.getLogger().info("✅ JSON API is running on port: " + port);
        }catch (IOException e){
            plugin.getLogger().severe("❌ JSON API can't run: " + e.getMessage());
        }
    }

    public void stop(){
        if(server != null){
            server.stop(0);
            plugin.getLogger().info("🛑 JSON API stopped.");
        }
    }

}
