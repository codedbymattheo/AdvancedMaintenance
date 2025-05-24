package cz.mattheo.api;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import cz.mattheo.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DiscordWebhook {

    private final Main plugin;
    private final String webhookUrl;

    public DiscordWebhook(){
        this.plugin = Main.getInstance();
        this.webhookUrl = plugin.getConfigManager().getConfig().getString("discord.webhook-url");
    }

    public void sendEmbed(String title, Player author, String reason, int color){
        boolean enabled = plugin.getConfigManager().getConfig().getBoolean("discord.enabled");
        WebhookClient webhookClient = WebhookClient.withUrl(webhookUrl);
        if(enabled){
            if(webhookUrl != null){
                WebhookEmbed embed = new WebhookEmbedBuilder()
                        .setTitle(new WebhookEmbed.EmbedTitle(title, null))
                        .setAuthor(new WebhookEmbed.EmbedAuthor(author.getName(), " https://crafatar.com/avatars/" + author.getUniqueId(), null))
                        .setDescription(reason)
                        .setColor(color)
                        .build();

                webhookClient.send(embed);
            }else{
                Bukkit.getLogger().warning("Webhook URL is not set!");
                return;
            }
        }
    }

}
