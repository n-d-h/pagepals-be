package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.webhook.Webhook;

import java.io.IOException;

public interface WebhookService {
    /**
     * Example of using a webhook to send a message to a Discord channel.
     * <pre>{@code
     *      private final WebhookService webhookService;
     *      try {
     *          Webhook webhook = new Webhook();
     *          webhook.setContent("Hello, World!");
     *          webhook.setWebhookUrl("https://discord.com/api/webhooks/1234567890/ABCDEFGHIJKLMN");
     *          webhook.setUsername("Webhook");
     *          webhook.setAvatarUrl("https://via.placeholder.com/150");
     *          EmbedObject embedObject = new EmbedObject();
     *          embedObject.setAuthor("Author", null, "https://via.placeholder.com/150");
     *          embedObject.addField("Field 1", "Value 1", false);
     *          embedObject.addField("Field 2", "Value 2", false);
     *          embedObject.addField("Field 3", "Value 3", false);
     *          embedObject.setFooter("Footer", "https://via.placeholder.com/150");
     *          webhook.addEmbed(embedObject);
     *          webhookService.sendWebhook(webhook);
     *      } catch (Exception e) {
     *          System.out.println("Webhooks fails");
     *      }
     * }
     * </pre>
     */
    void sendWebhook(Webhook webhook) throws IOException;
}
