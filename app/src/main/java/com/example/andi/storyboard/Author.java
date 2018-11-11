package com.example.andi.storyboard;

import com.google.firebase.firestore.DocumentReference;

public class Author {
    private String name;
    private DocumentReference ref;

    public Author(String name, DocumentReference ref) {
        this.name = name;
        this.ref = ref;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public DocumentReference getRef() {
        return ref;
    }
}
