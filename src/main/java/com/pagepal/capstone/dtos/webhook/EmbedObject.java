package com.pagepal.capstone.dtos.webhook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmbedObject {
    private String title;
    private String description;
    private String url;
    private Color color = new Color(34, 139, 34);
    private Footer footer;
    private Thumbnail thumbnail;
    private Image image;
    private Author author;
    private List<Field> fields = new ArrayList<>();

    public EmbedObject setTitle(String title) {
        this.title = title;
        return this;
    }

    public EmbedObject setDescription(String description) {
        this.description = description;
        return this;
    }

    public EmbedObject setUrl(String url) {
        this.url = url;
        return this;
    }

    public EmbedObject setColor(Color color) {
        this.color = color;
        return this;
    }

    public EmbedObject setFooter(String text, String icon) {
        this.footer = new Footer(text, icon);
        return this;
    }

    public EmbedObject setThumbnail(String url) {
        this.thumbnail = new Thumbnail(url);
        return this;
    }

    public EmbedObject setImage(String url) {
        this.image = new Image(url);
        return this;
    }

    public EmbedObject setAuthor(String name, String url, String icon) {
        this.author = new Author(name, url, icon);
        return this;
    }

    public EmbedObject addField(String name, String value, boolean inline) {
        this.fields.add(new Field(name, value, inline));
        return this;
    }

    public record Footer(String text, String iconUrl) {
    }

    public record Thumbnail(String url) {
    }

    public record Image(String url) {
    }

    public record Author(String name, String url, String iconUrl) {
    }

    public record Field(String name, String value, boolean inline) {
    }
}
