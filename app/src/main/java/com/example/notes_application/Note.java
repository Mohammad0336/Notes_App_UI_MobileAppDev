package com.example.notes_application;
public class Note {
    private String title;
    private String description;
    private int color;

    public Note(String title, String description, int color) {
        this.title = title;
        this.description = description;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getColor() {
        return color;
    }
}

