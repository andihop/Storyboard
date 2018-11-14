package com.example.andi.storyboard;

import com.google.firebase.firestore.DocumentReference;

public class Genre {
    private String type;
    private DocumentReference ref;

    public Genre(String type, DocumentReference ref) {
        this.type = type;
        this.ref = ref;
    }

    @Override
    public String toString() {
        return type;
    }

    public String getGenre() {
        return type;
    }

    public DocumentReference getRef() {
        return ref;
    }
}