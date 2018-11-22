package com.example.andi.storyboard;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Story {
    private String title;
    private String genre;
    private String authorName;
    private String text;
    private String summary;
    private long views;
    private Date created_on;
    private Date last_updated;


    //For backwards compatibility with previous version.
    public Story(String title, String authorName, String text, String genre) {
        this.title = title;
        this.authorName = authorName;
        this.text = text;
        this.genre = genre;
        this.summary = "";
        this.views = 0;
        this.created_on = new Date();
        this.last_updated = new Date();
    }

    public Story(String title, String authorName, String text, String genre, String summary, long views, Date created, Date last) {
        this.title = title;
        this.authorName = authorName;
        this.text = text;
        this.genre = genre;
        this.summary = summary;
        this.views = views;
        this.created_on = created;
        this.last_updated = last;
    }

    @Override
    public String toString() {
        return "Title: " + title +
                "\nAuthor: " + authorName +
                "\nGenre: " + genre;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getText() {
        return text;
    }

    public long getViews() {
        return views;
    }

    public String getSummary() {
        return summary;
    }

    public Date getCreated_On() {
        return created_on;
    }

    public Date getLast_Updated() {
        return last_updated;
    }



}

