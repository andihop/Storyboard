package com.example.andi.storyboard;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.BaseAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FireStoreOps {
    public static ArrayList<Story> stories = new ArrayList<>();
    public static ArrayList<Author> authors = new ArrayList<>();
    public static ArrayList<Genre> genres = new ArrayList<>();

    //public enum COLLECTION {AUTHORS, GENRES, STORIES};
    //public enum GENRE {HORROR, FANTASY, ROMANCE, DRAMA, SCIENCE_FICTION, DYSTOPIAN, TRAGEDY, ACTION_ADVENTURE, COMEDY, THRILLER};

    public static void getAllStories(final BaseAdapter mAdapter) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("stories")
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (final QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("getAllStories", document.getId() + " => " + document.getData());
                        document.getDocumentReference("author").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull final Task<DocumentSnapshot> task_author) {
                                if (task_author.isSuccessful()) {
                                    stories.clear();
                                    ((List<DocumentReference>) document.get("genres")).get(0).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                          @Override
                                          public void onComplete(@NonNull Task<DocumentSnapshot> task_genre) {
                                              if (task_genre.isSuccessful()) {
                                                  stories.add(new Story(document.get("title").toString(), task_author.getResult().get("name").toString(), document.get("text").toString(),
                                                          task_genre.getResult().get("type").toString()));
                                                  Log.i("test", "testing");
                                                  mAdapter.notifyDataSetChanged();
                                              }
                                          }
                                      }
                                    );
                                }
                            }
                        });
                    }

                } else {
                    Log.d("storiesByAuthors", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public static void getAllAuthors(final BaseAdapter mAdapter) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("authors")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            authors.clear();
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("searchByRef (authors)", document.getId() + " => " + document.getData());
                                Log.i("Name", document.get("name").toString());
                                authors.add(new Author(document.get("name").toString(), document.getReference()));
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
                );
    }

    public static void getAllGenres(final BaseAdapter mAdapter) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("authors")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                           if (task.isSuccessful()) {
                               genres.clear();
                               for (final QueryDocumentSnapshot document : task.getResult()) {
                                   Log.d("searchByRef (genres)", document.getId() + " => " + document.getData());
                                   Log.i("Type", document.get("type").toString());
                                   genres.add(new Genre(document.get("type").toString(), document.getReference()));
                                   mAdapter.notifyDataSetChanged();
                               }
                           }
                       }
                   }
                );
    }

    //collectionName: collection to be searched, ie want to query stories by author
    //documentCollection: collection containing reference to be searched by, ie. author when searching stories by author
    //documentID: search reference's document ID.
    public static void searchByRef(final String collectionName, String documentCollection, String documentID, final String field, final BaseAdapter mAdapter) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference ref = firestore.collection(documentCollection).document(documentID);
        if ("stories".compareToIgnoreCase(collectionName) == 0 && "authors".compareToIgnoreCase(documentCollection) == 0) {
            firestore.collection("stories")
            .whereEqualTo("author", ref)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        stories.clear();
                        for (final QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("storiesByAuthors", document.getId() + " => " + document.getData());
                            document.getDocumentReference("author").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull final Task<DocumentSnapshot> task_author) {
                                    if (task_author.isSuccessful()) {
                                        ((List<DocumentReference>) document.get("genres")).get(0).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                              @Override
                                              public void onComplete(@NonNull Task<DocumentSnapshot> task_genre) {
                                                  if (task_genre.isSuccessful()) {
                                                      stories.add(new Story(document.get("title").toString(), task_author.getResult().get("name").toString(), document.get("text").toString(),
                                                              task_genre.getResult().get("type").toString()));
                                                      Log.i("test", "testing");
                                                      mAdapter.notifyDataSetChanged();
                                                  }
                                              }
                                          }
                                        );
                                    }
                                }
                            });
                        }

                    } else {
                        Log.d("storiesByAuthors", "Error getting documents: ", task.getException());
                    }
                }
            });
        } else {
            firestore.collection(collectionName)
                    .whereArrayContains(field, ref)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                if ("stories".compareToIgnoreCase(collectionName) == 0) {
                                    stories.clear();

                                    for (final QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("searchByRef (stories)", document.getId() + " => " + document.getData());
                                        Log.i("Title", document.get("title").toString());
                                        document.getDocumentReference("author").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull final Task<DocumentSnapshot> task_author) {
                                                if (task_author.isSuccessful()) {
                                                    ((List<DocumentReference>) document.get("genres")).get(0).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                          @Override
                                                          public void onComplete(@NonNull Task<DocumentSnapshot> task_genre) {
                                                              if (task_genre.isSuccessful()) {
                                                                  stories.add(new Story(document.get("title").toString(), task_author.getResult().get("name").toString(), document.get("text").toString(),
                                                                          task_genre.getResult().get("type").toString()));
                                                                  Log.i("test", "testing");
                                                                  mAdapter.notifyDataSetChanged();

                                                              }
                                                          }
                                                      }
                                                    );

                                                }
                                            }
                                        });


                                    }
                                } else if ("authors".compareToIgnoreCase(collectionName) == 0) {
                                    authors.clear();
                                    for (final QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("searchByRef (authors)", document.getId() + " => " + document.getData());
                                        Log.i("Name", document.get("name").toString());
                                        authors.add(new Author(document.get("name").toString(), document.getReference()));
                                        mAdapter.notifyDataSetChanged();
                                    }
                                } else if ("genres".compareToIgnoreCase(collectionName) == 0) {
                                    genres.clear();
                                    for (final QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("searchByRef (genres)", document.getId() + " => " + document.getData());
                                        Log.i("Type", document.get("type").toString());
                                        genres.add(new Genre(document.get("type").toString(), document.getReference()));
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }

                            } else {
                                Log.d("searchByRef", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    /////
    /////

    ///Below functions are not used, kept for reference / just in case.

    /////
    //Use when you have document ID of author
    /*
    public static void searchStoriesByAuthor(String documentID, final BaseAdapter mAdapter) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference ref = firestore.collection("authors").document(documentID);
        stories.clear();

        firestore.collection("stories")
                .whereEqualTo("author", ref)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("storiesByAuthors", document.getId() + " => " + document.getData());
                                Log.i("Title", document.get("title").toString());
                                document.getDocumentReference("author").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull final Task<DocumentSnapshot> task_author) {
                                        if (task_author.isSuccessful()) {
                                            ((List<DocumentReference>) document.get("genres")).get(0).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task_genre) {
                                                    if (task_genre.isSuccessful()) {
                                                    stories.add(new Story(document.get("title").toString(), task_author.getResult().get("name").toString(),document.get("text").toString(),
                                                            task_genre.getResult().get("type").toString()));
                                                        Log.i("test", "testing");
                                                        mAdapter.notifyDataSetChanged();
                                                }
                                            }}
                                            );
                                        }}});
                            }

                        } else {
                            Log.d("storiesByAuthors", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    //Use when you have Document Reference of Author
    public static void searchStoriesByAuthor(DocumentReference ref, final BaseAdapter mAdapter) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        stories.clear();

        firestore.collection("stories")
                .whereEqualTo("author", ref)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("storiesByAuthors", document.getId() + " => " + document.getData());
                                Log.i("Title", document.get("title").toString());
                                document.getDocumentReference("author").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull final Task<DocumentSnapshot> task_author) {
                                        if (task_author.isSuccessful()) {
                                            ((List<DocumentReference>) document.get("genres")).get(0).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task_genre) {
                                                    if (task_genre.isSuccessful()) {
                                                        stories.add(new Story(document.get("title").toString(), task_author.getResult().get("name").toString(),document.get("text").toString(),
                                                                task_genre.getResult().get("type").toString()));
                                                        Log.i("test", "testing");
                                                        mAdapter.notifyDataSetChanged();

                                                    }
                                                }}
                                            );

                                        }}});


                            }


                        } else {
                            Log.d("storiesByAuthors", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
*/
}

