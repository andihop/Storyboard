package com.example.andi.storyboard;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FireStoreOps {
    public static ArrayList<Story> stories = new ArrayList<>();

    //public enum COLLECTION {AUTHORS, GENRES, STORIES};
    //public enum GENRE {HORROR, FANTASY, ROMANCE, DRAMA, SCIENCE_FICTION, DYSTOPIAN, TRAGEDY, ACTION_ADVENTURE, COMEDY, THRILLER};

    public static void searchByRef(final String collectionName, String documentCollection, String documentID, final String field) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference ref = firestore.collection(documentCollection).document(documentID);
        stories.clear();

        final ArrayList<QueryDocumentSnapshot> storiesList = new ArrayList<QueryDocumentSnapshot>();
            firestore.collection(collectionName)
                    .whereEqualTo(field , ref)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    storiesList.add(document);
                                    Log.d("storiesByAuthors", document.getId() + " => " + document.getData());
                                    Log.i("Title", document.get("title").toString());
                                    Log.i("Text", document.get("text").toString());
                                    stories.add(new Story(document.get("title").toString(), document.get("title").toString(), document.get("text").toString(), document.get("title").toString()));

                                }
                            } else {
                                Log.d("storiesByAuthors", "Error getting documents: ", task.getException());
                            }
                        }
                    });
    }
}
