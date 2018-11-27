package com.example.andi.storyboard.datatype;

import java.util.ArrayList;
import java.util.Date;

public class WritingPrompt {
    private String prompt;
    private ArrayList<String> genres;
    private Date time;
    private String prompt_author;
    private String tag;

    public WritingPrompt(String prompt, ArrayList<String> genres, Date time, String prompt_author, String tag) {
        this.prompt = prompt;
        this.genres = genres;
        this.time = time;
        this.prompt_author = prompt_author;
        this.tag = tag;

    }

    @Override
    public String toString() {
        String genre = "";
        for (int i = 0; i < genres.size() - 1; i++) {
            genre += genres.get(i) +", ";
        }
        genre += genres.get(genres.size() - 1);
        return  prompt + "\n"+
                "\nAuthor: " + prompt_author +
                "\nGenre(s): " + genre;
    }

    public String getText() {
        return prompt;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public Date getPostedTime() {
        return time;
    }

    public String getTag() {
        return tag;
    }




}


