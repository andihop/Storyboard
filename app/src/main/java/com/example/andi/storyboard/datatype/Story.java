package com.example.andi.storyboard.datatype;

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
    private String documentID;
    private Boolean is_private;
    private Boolean in_progress;
    private String authorID;

    //For backwards compatibility with previous version.
    /*
    public Story(String title, String authorName, String text, String genre) {
        this.title = title;
        this.authorName = authorName;
        this.text = text;
        this.genre = genre;
        this.summary = "";
        this.views = 0;
        this.created_on = new Date();
        this.last_updated = new Date();
        this.documentID =
    }
    */

    public Story(String title, String authorName, String text, String genre, String summary,
                 long views, Date created, Date last, String documentID, Boolean is_private, Boolean in_progress, String authorID) {
        this.title = title;
        this.authorName = authorName;
        this.text = text;
        this.genre = genre;
        this.summary = summary;
        this.views = views;
        this.created_on = created;
        this.last_updated = last;
        this.documentID = documentID;
        this.is_private = is_private;
        this.in_progress = in_progress;
        this.authorID = authorID;
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

    public String getDocumentID() {
        return documentID;
    }

    public Boolean getIs_Private() {
        return is_private;
    }

    public Boolean getIn_Progress() {
        return in_progress;
    }
    public String getAuthorID() {
        return authorID;
    }



}


