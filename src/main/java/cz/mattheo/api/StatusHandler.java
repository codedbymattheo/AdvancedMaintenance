package cz.mattheo.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import cz.mattheo.Main;

import java.io.IOException;
import java.io.OutputStream;

public class StatusHandler implements HttpHandler {

    private final Main plugin;

    public StatusHandler(){
        this.plugin = Main.getInstance();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(!exchange.getRequestMethod().equalsIgnoreCase("GET")){
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String json = "{\n" +
                "  \"maintenance\": " + plugin.isMaintenanceEnabled() + ",\n" +
                "  \"server\": \"" + plugin.getServer().getName() + "\",\n" +
                "  \"players_online\": " + plugin.getServer().getOnlinePlayers().size() + "\n" +
                "}";

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, json.length());

        try(OutputStream os = exchange.getResponseBody()){
            os.write(json.getBytes());
        }
    }
}
