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
    private WebhookClient webhookClient;

    public DiscordWebhook(){
        this.plugin = Main.getInstance();
        this.webhookUrl = plugin.getConfigManager().getConfig().getString("discord.webhook-url");
        if(webhookUrl != null && !webhookUrl.isEmpty()){
            this.webhookClient = WebhookClient.withUrl(webhookUrl);
        }else{
            this.webhookClient = null;
            plugin.getLogger().warning("Discord webhook URL isn't set or it's empty!");
        }
    }

    public void sendEmbed(String title, Player author, String reason, int color){
        if(!plugin.getConfigManager().getConfig().getBoolean("discord.enabled")){
            return;
        }

        if(webhookClient == null){
            plugin.getLogger().warning("WebhookClient isn't initialized! Check config.yml");
            return;
        }

        if(author == null || reason == null){
            plugin.getLogger().warning("Author or reason of embed is empty!");
            return;
        }

        webhookClient = WebhookClient.withUrl(webhookUrl);
        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setTitle(new WebhookEmbed.EmbedTitle(title, null))
                .setAuthor(new WebhookEmbed.EmbedAuthor(author.getName(), "https://crafatar.com/avatars/" + author.getUniqueId().toString().replace("-", ""), null))
                .setDescription(reason)
                .setColor(color)
                .build();

        webhookClient.send(embed);

        }



}
