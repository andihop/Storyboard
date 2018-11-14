package com.example.andi.storyboard;

public class Story {
    private String title;
    private String genre;
    private String authorName;
    private String text;

    public Story(String title, String authorName, String text, String genre) {
        this.title = title;
        this.authorName = authorName;
        this.text = text;
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Title: " + title +
                "\nAuthor: " + authorName +
                "\nGenre: " + genre;
    }
}
