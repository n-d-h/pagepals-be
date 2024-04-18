package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.webhook.EmbedObject;
import com.pagepal.capstone.dtos.webhook.JSONObject;
import com.pagepal.capstone.dtos.webhook.Webhook;
import com.pagepal.capstone.entities.postgre.Account;
import com.pagepal.capstone.entities.postgre.Customer;
import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.services.WebhookService;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class WebhookImpl implements WebhookService {
    @Override
    public void sendWebhook(Webhook webhook, Boolean isWarning) throws IOException {
        if (webhook.getContent() == null && webhook.getEmbeds().isEmpty()) {
            throw new IllegalArgumentException("Set content or add at least one EmbedObject");
        }

        JSONObject json = new JSONObject();

        json.put("content", webhook.getContent());
        json.put("username", webhook.getUsername());
        json.put("avatar_url", webhook.getAvatarUrl());
        json.put("tts", webhook.getTts());

        if (!webhook.getEmbeds().isEmpty()) {
            List<JSONObject> embedObjects = new ArrayList<>();

            for (EmbedObject embed : webhook.getEmbeds()) {
                JSONObject jsonEmbed = new JSONObject();

                jsonEmbed.put("title", embed.getTitle());
                jsonEmbed.put("description", embed.getDescription());
                jsonEmbed.put("url", embed.getUrl());

                if (embed.getColor() != null) {
                    int rgb;

                    if(isWarning == Boolean.TRUE) {
                        rgb = 16711680;
                    } else {
                        rgb = 65280;
                    }

                    jsonEmbed.put("color", rgb);
                }

                EmbedObject.Footer footer = embed.getFooter();
                EmbedObject.Image image = embed.getImage();
                EmbedObject.Thumbnail thumbnail = embed.getThumbnail();
                EmbedObject.Author author = embed.getAuthor();
                List<EmbedObject.Field> fields = embed.getFields();

                if (footer != null) {
                    JSONObject jsonFooter = new JSONObject();

                    jsonFooter.put("text", footer.text());
                    jsonFooter.put("icon_url", footer.iconUrl());
                    jsonEmbed.put("footer", jsonFooter);
                }

                if (image != null) {
                    JSONObject jsonImage = new JSONObject();

                    jsonImage.put("url", image.url());
                    jsonEmbed.put("image", jsonImage);
                }

                if (thumbnail != null) {
                    JSONObject jsonThumbnail = new JSONObject();

                    jsonThumbnail.put("url", thumbnail.url());
                    jsonEmbed.put("thumbnail", jsonThumbnail);
                }

                if (author != null) {
                    JSONObject jsonAuthor = new JSONObject();

                    jsonAuthor.put("name", author.name());
                    jsonAuthor.put("url", author.url());
                    jsonAuthor.put("icon_url", author.iconUrl());
                    jsonEmbed.put("author", jsonAuthor);
                }

                List<JSONObject> jsonFields = new ArrayList<>();
                for (EmbedObject.Field field : fields) {
                    JSONObject jsonField = new JSONObject();

                    jsonField.put("name", field.name());
                    jsonField.put("value", field.value());
                    jsonField.put("inline", field.inline());

                    jsonFields.add(jsonField);
                }

                jsonEmbed.put("fields", jsonFields.toArray());
                embedObjects.add(jsonEmbed);
            }

            json.put("embeds", embedObjects.toArray());
        }

        String webhookUrl = webhook.getWebhookUrl();
        URL url = new URL(webhookUrl);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Gelox_");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        OutputStream stream = connection.getOutputStream();
        stream.write(json.toString().getBytes());
        stream.flush();
        stream.close();

        connection.getInputStream().close();
        connection.disconnect();
    }

    @Override
    public void sendWebhookWithData(Account account, Map<String, String> content, Boolean isReader, Boolean isWarning) {
        try {
            Webhook webhook = new Webhook();
            webhook.setContent("PagePal Notification");
            webhook.setWebhookUrl("https://discord.com/api/webhooks/1206078678518206504/6VsXDrAJgmzOVXXwFLbYa7ZO2iD5xQ4n0UsrFzeCBaj03UHJ8gUPvu-7DLVgMr3zHHDU");
            webhook.setUsername("PagePal");
            webhook.setAvatarUrl("https://via.placeholder.com/150");
            EmbedObject embedObject = new EmbedObject();

            if (isReader == Boolean.TRUE) {
                Reader reader = account.getReader();
                String nickname = reader.getNickname();
                String imageAvatar = reader.getAvatarUrl();
                embedObject.setAuthor(nickname, null, imageAvatar);
            } else {
                Customer customer = account.getCustomer();
                String fullName = customer.getFullName();
                String imageAvatar = customer.getImageUrl();
                embedObject.setAuthor(fullName, null, imageAvatar);
            }

            for (Map.Entry<String, String> entry : content.entrySet()) {
                embedObject.addField(entry.getKey(), entry.getValue(), false);
            }

            webhook.addEmbed(embedObject);
            this.sendWebhook(webhook, isWarning);
        } catch (Exception e) {
            System.out.println("Webhooks fails");
        }
    }
}
