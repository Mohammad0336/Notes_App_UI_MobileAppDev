package com.example.notes_application;
import java.util.Arrays;

public class Note {

    private int id;
    private String title;
    private String description;
    private int color;
    private byte[] image;

    public Note(int id, String title, String description, int color, byte[] image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.color = color;
        this.image = image;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", color=" + color +
                ", image=" + Arrays.toString(image) +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}