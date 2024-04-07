package com.pagepal.capstone.dtos.webhook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Webhook {
    private String content;
    private String webhookUrl;
    private String username;
    private String avatarUrl;
    private Boolean tts;
    private List<EmbedObject> embeds = new ArrayList<>();

    public void addEmbed(EmbedObject embed) {
        this.embeds.add(embed);
    }
}
