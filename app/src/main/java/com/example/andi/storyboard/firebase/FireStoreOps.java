package com.example.andi.storyboard.firebase;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.BaseAdapter;

import com.example.andi.storyboard.datatype.Author;
import com.example.andi.storyboard.datatype.Chapter;
import com.example.andi.storyboard.datatype.Genre;
import com.example.andi.storyboard.datatype.Story;
import com.example.andi.storyboard.datatype.WritingPrompt;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.google.android.gms.tasks.OnSuccessListener;


public class FireStoreOps {
    public static ArrayList<Story> stories = new ArrayList<>();
    public static ArrayList<Author> authors = new ArrayList<>();
    public static ArrayList<Genre> genres = new ArrayList<>();
    public static ArrayList<Chapter> chapters = new ArrayList<>();
    public static ArrayList<String> comments = new ArrayList<>();
    public static ArrayList<WritingPrompt> writingprompts = new ArrayList<>();

    public static Story story = new Story("", "", "", "", "", 0, new Date(), new Date(), "", true, true);
    private FirebaseAuth auth;

    //public enum COLLECTION {AUTHORS, GENRES, STORIES};
    public enum GENRE {
        HORROR, FANTASY, ROMANCE, DRAMA, SCIENCE_FICTION, DYSTOPIAN, TRAGEDY, ACTION_ADVENTURE, COMEDY, THRILLER
    }

    //If genre collection is changed in firestore, update this accordingly.
    public static Map<String, String> genreDocumentIDs = new HashMap<String, String>() {
        {
            put("horror", "E9FH2veRiT4vgoBEk2ZB");
            put("science fiction", "4l80oPdFF3rVMvFnAjbY");
            put("comedy", "9b4TUmqa1iBmYbJVNgwq");
            put("dystopian", "UellLRhI8rSq1mYKtJTJ");
            put("drama", "UqOCQA19tB1wRfBHhuTD");
            put("tragedy", "c6OvU1Jq2SbPsqrc8yHO");
            put("action adventure", "cJVtAe2nSsuFM9s3b688");
            put("thriller", "l7zHf7EmIHbnERWZFiiF");
            put("fantasy", "md7Bjt5MCOazoXa84nAk");
            put("romance", "oDwoSIoSTFUAALzVDqQ5");
            put("default", "JBoe4I23t53OvwYwgR54");
        }

    };

    //When a story is selected and read from the search, increment view count by using this method:
    public static void incrementViewCount(String documentID) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference ref = firestore.collection("stories").document(documentID);
        final Map<String, Object> storyMap = new HashMap<String, Object>();
        ref.get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            storyMap.put("views", ((long) task.getResult().get("views")) + 1);
                            task.getResult().getReference().update(storyMap);

                        }
                    }
                }
        );
    }

    //Pass in user ID via auth.getCurrentUser().getUid()
    //Pass in auth as well, so only if current user = profile user the private stories are returned.
    //otherwise, only public ones are returned.
    public static void getUserStories(final String userID, final FirebaseAuth auth, final BaseAdapter mAdapter) {
        Log.d("getUserStories", "Entered Function");

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference authorRef = firestore.collection("authors").document(userID);

        firestore.collection("stories").whereEqualTo("author", authorRef).orderBy("title")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            stories.clear();
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("getAllStories", document.getId() + " => " + document.getData());
                                document.getDocumentReference("author").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull final Task<DocumentSnapshot> task_author) {
                                        if (task_author.isSuccessful()) {
                                            ((List<DocumentReference>) document.get("genres")).get(0).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                      @Override
                                                                                                                                      public void onComplete(@NonNull Task<DocumentSnapshot> task_genre) {
                                                                                                                                          if (task_genre.isSuccessful()) {
                                                                                                                                              if ((Boolean) document.get("is_private")) {
                                                                                                                                                  if (userID.equals(auth.getCurrentUser().getUid())) {
                                                                                                                                                      stories.add(new Story(document.get("title").toString(), task_author.getResult().get("username").toString(), document.get("text").toString(),
                                                                                                                                                              task_genre.getResult().get("type").toString(), document.get("summary").toString(), document.getLong("views"), document.getDate("Created_On"),
                                                                                                                                                              document.getDate("Last_Updated"), document.getId(), (Boolean) document.get("is_private"), (Boolean) document.get("in_progress")));
                                                                                                                                                      Log.i("UserStory", "testing");
                                                                                                                                                      mAdapter.notifyDataSetChanged();
                                                                                                                                                  }
                                                                                                                                              } else {
                                                                                                                                                  stories.add(new Story(document.get("title").toString(), task_author.getResult().get("username").toString(), document.get("text").toString(),
                                                                                                                                                          task_genre.getResult().get("type").toString(), document.get("summary").toString(), document.getLong("views"), document.getDate("Created_On"),
                                                                                                                                                          document.getDate("Last_Updated"), document.getId(), (Boolean) document.get("is_private"), (Boolean) document.get("in_progress")));
                                                                                                                                                  Log.i("UserStory", "testing");
                                                                                                                                                  mAdapter.notifyDataSetChanged();
                                                                                                                                              }
                                                                                                                                          }
                                                                                                                                      }
                                                                                                                                  }
                                            );
                                        }
                                    }
                                });
                            }

                        } else {
                            Log.d("userStories", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    //Pass in user ID via auth.getCurrentUser().getUid()
    //Pass in auth as well, so only if current user = profile user the private stories are returned.
    //otherwise, only public ones are returned.
    //Returns at most 5 most recentlyupdated stories in the static stories list.
    public static void getRecentUserStories(final String userID, final FirebaseAuth auth, final BaseAdapter mAdapter) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference authorRef = firestore.collection("authors").document(userID);
        firestore.collection("stories").whereEqualTo("author", authorRef).orderBy("Last_Updated", Query.Direction.DESCENDING).limit(5)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            stories.clear();
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("getAllStories", document.getId() + " => " + document.getData());
                                document.getDocumentReference("author").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull final Task<DocumentSnapshot> task_author) {
                                        if (task_author.isSuccessful()) {
                                            ((List<DocumentReference>) document.get("genres")).get(0).get().addOnCompleteListener(
                                                    new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task_genre) {
                                                            if (task_genre.isSuccessful()) {
                                                                if ((Boolean) document.get("is_private")) {
                                                                    if (userID.equals(auth.getCurrentUser().getUid())) {
                                                                        stories.add(new Story(document.get("title").toString(), task_author.getResult().get("username").toString(), document.get("text").toString(),
                                                                                task_genre.getResult().get("type").toString(), document.get("summary").toString(), document.getLong("views"), document.getDate("Created_On"),
                                                                                document.getDate("Last_Updated"), document.getId(), (Boolean) document.get("is_private"), (Boolean) document.get("in_progress")));
                                                                        Log.i("UserStory", "testing");
                                                                        mAdapter.notifyDataSetChanged();
                                                                    }
                                                                } else {
                                                                    stories.add(new Story(document.get("title").toString(), task_author.getResult().get("username").toString(), document.get("text").toString(),
                                                                            task_genre.getResult().get("type").toString(), document.get("summary").toString(), document.getLong("views"), document.getDate("Created_On"),
                                                                            document.getDate("Last_Updated"), document.getId(), (Boolean) document.get("is_private"), (Boolean) document.get("in_progress")));
                                                                    Log.i("UserStory", "testing");
                                                                    mAdapter.notifyDataSetChanged();
                                                                }
                                                            }
                                                        }
                                                    }
                                            );
                                        }
                                    }
                                });
                            }

                        } else {
                            Log.d("userStories", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    //Pass in user ID via auth.getCurrentUser().getUid()
    //Pass in auth as well, so only if current user = profile user the private stories are returned.
    //otherwise, only public ones are returned.
    public static void getFeaturedUserStories(final String userID, final FirebaseAuth auth, final BaseAdapter mAdapter) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference authorRef = firestore.collection("authors").document(userID);

        firestore.collection("stories").whereEqualTo("author", authorRef).orderBy("views", Query.Direction.DESCENDING).limit(3)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            stories.clear();
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("getAllStories", document.getId() + " => " + document.getData());
                                document.getDocumentReference("author").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull final Task<DocumentSnapshot> task_author) {
                                        if (task_author.isSuccessful()) {
                                            ((List<DocumentReference>) document.get("genres")).get(0).get().addOnCompleteListener(
                                                    new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task_genre) {
                                                            if (task_genre.isSuccessful()) {
                                                                if ((Boolean) document.get("is_private")) {
                                                                    if (userID.equals(auth.getCurrentUser().getUid())) {
                                                                        stories.add(new Story(document.get("title").toString(), task_author.getResult().get("username").toString(), document.get("text").toString(),
                                                                                task_genre.getResult().get("type").toString(), document.get("summary").toString(), document.getLong("views"), document.getDate("Created_On"),
                                                                                document.getDate("Last_Updated"), document.getId(), (Boolean) document.get("is_private"), (Boolean) document.get("in_progress")));
                                                                        Log.i("UserStory", document.get("views").toString() + " " + document.get("title").toString());
                                                                        mAdapter.notifyDataSetChanged();
                                                                    }
                                                                } else {
                                                                    stories.add(new Story(document.get("title").toString(), task_author.getResult().get("username").toString(), document.get("text").toString(),
                                                                            task_genre.getResult().get("type").toString(), document.get("summary").toString(), document.getLong("views"), document.getDate("Created_On"),
                                                                            document.getDate("Last_Updated"), document.getId(), (Boolean) document.get("is_private"), (Boolean) document.get("in_progress")));
                                                                    Log.i("UserStory", document.get("views").toString() + " " + document.get("title").toString());
                                                                    mAdapter.notifyDataSetChanged();
                                                                }
                                                            }
                                                        }
                                                    }
                                            );
                                        }
                                    }
                                });
                            }

                        } else {
                            Log.d("userStories", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    //Get the top ten stories from firebase
    //The top ten stories are based on view count, and are updated every time oneone views a story
    public static void getTopTenStories(final BaseAdapter mAdapter) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("toptenstories").orderBy("views", Query.Direction.DESCENDING).limit(10).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task_get_top_ten) {
                        // we have the top ten stories by reference, hopefully.
                        if (task_get_top_ten.isSuccessful()) {
                            stories.clear();
                            // for each "Document" in the collection
                            for (final QueryDocumentSnapshot document : task_get_top_ten.getResult()) {
                                // dereference the story
                                Log.d("getTopTenStories", document.getId() + " => " + document.getData());
                                document.getDocumentReference("storyid").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull final Task<DocumentSnapshot> task_story) {
                                        // we have dereferenced a story.
                                        if (task_story.isSuccessful()) {
                                            // get the author
                                            final DocumentSnapshot story = task_story.getResult();
                                            Log.d("getTopTenStories", story.getId() + " => " + story.getData());
                                            story.getDocumentReference("author").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull final Task<DocumentSnapshot> task_author) {
                                                    //we have dereferenced the author of a story.
                                                    if (task_author.isSuccessful()) {
                                                        final DocumentSnapshot author = task_author.getResult();
                                                        Log.d("getTopTenStories", author.getId() + " => " + author.getData());
                                                        //get the genre of the story.
                                                        ((List<DocumentReference>) story.get("genres")).get(0).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull final Task<DocumentSnapshot> task_genre) {
                                                                //we have dereferenced the generes of a story
                                                                if (task_genre.isSuccessful()) {
                                                                    final DocumentSnapshot genre = task_genre.getResult();
                                                                    Log.d("getTopTenStories", genre.getId() + " => " + genre.getData());
                                                                    // add that story to our list of stories
                                                                    stories.add(new Story(story.get("title").toString(), author.get("username").toString(), story.get("text").toString(),
                                                                            genre.get("type").toString(), story.get("summary").toString(), document.getLong("views"), story.getDate("Created_On"),
                                                                            story.getDate("Last_Updated"), story.getId(), (Boolean) story.get("is_private"), (Boolean) story.get("in_progress")));
                                                                    Log.i("getTopTenStories", "retrieved story");
                                                                    mAdapter.notifyDataSetChanged();
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        } else {
                            Log.d("getTopTenStories", "Error getting documents: ", task_get_top_ten.getException());
                        }
                    }
                });
    }


    //Get story using story documentID
    public static void getStory(String documentID, final BaseAdapter mAdapter) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference ref = firestore.collection("stories").document(documentID);

        ref.get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            final DocumentSnapshot doc = task.getResult();
                            doc.getDocumentReference("author").get().addOnCompleteListener(
                                    new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull final Task<DocumentSnapshot> task_author) {
                                            if (task_author.isSuccessful()) {

                                                ((List<DocumentReference>) doc.get("genres")).get(0).get().addOnCompleteListener(
                                                        new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task_genre) {
                                                                if (task_genre.isSuccessful()) {
                                                                    story = new Story(doc.get("title").toString(), task_author.getResult().get("username").toString(), doc.get("text").toString(),
                                                                            task_genre.getResult().get("type").toString(), doc.get("summary").toString(), (long) doc.get("views"), (Date) doc.get("Created_On"), (Date) doc.get("Last_Updated"), doc.getId(), (Boolean) doc.get("is_private"), (Boolean) doc.get("in_progress"));

                                                                    mAdapter.notifyDataSetChanged();

                                                                }
                                                            }
                                                        }
                                                );

                                            }
                                        }
                                    });

                        }
                    }
                }
        );
    }

    //Returns ALL stories, private and public. no real reason to use this method.
    public static void getAllStories(final BaseAdapter mAdapter) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("stories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            stories.clear();
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("getAllStories", document.getId() + " => " + document.getData());
                                document.getDocumentReference("author").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull final Task<DocumentSnapshot> task_author) {
                                        if (task_author.isSuccessful()) {
                                            ((List<DocumentReference>) document.get("genres")).get(0).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                      @Override
                                                                                                                                      public void onComplete(@NonNull Task<DocumentSnapshot> task_genre) {
                                                                                                                                          if (task_genre.isSuccessful()) {
                                                                                                                                              stories.add(new Story(document.get("title").toString(), task_author.getResult().get("username").toString(), document.get("text").toString(),
                                                                                                                                                      task_genre.getResult().get("type").toString(), document.get("summary").toString(), document.getLong("views"), document.getDate("Created_On"), document.getDate("Last_Updated"), document.getId(), (Boolean) document.get("is_private"), (Boolean) document.get("in_progress")));
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
                                                       Log.i("Name", document.get("username").toString());
                                                       authors.add(new Author(document.get("username").toString(), document.getReference()));
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
                                                                                                                                                  try {
                                                                                                                                                      if (!(Boolean) document.get("is_private")) {
                                                                                                                                                          stories.add(new Story(document.get("title").toString(), task_author.getResult().get("username").toString(), document.get("text").toString(),
                                                                                                                                                                  task_genre.getResult().get("type").toString(), document.get("summary").toString(), document.getLong("views"), document.getDate("Created_On"), document.getDate("Last_Updated"), document.getId(), (boolean) document.get("is_private"), (boolean) document.get("in_progress")));
                                                                                                                                                          Log.i("test", "testing");
                                                                                                                                                          mAdapter.notifyDataSetChanged();
                                                                                                                                                      }
                                                                                                                                                  } catch (Exception e) {
                                                                                                                                                      //Accounts for empty authors.
                                                                                                                                                  }
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
                                                                                                                                                      try {
                                                                                                                                                          stories.add(new Story(document.get("title").toString(), task_author.getResult().get("username").toString(), document.get("text").toString(),
                                                                                                                                                                  task_genre.getResult().get("type").toString(), document.get("summary").toString(), document.getLong("views"), document.getDate("Created_On"), document.getDate("Last_Updated"), document.getId(), (boolean) document.get("is_private"), (boolean) document.get("in_progress")));
                                                                                                                                                          Log.i("test", "testing");
                                                                                                                                                          mAdapter.notifyDataSetChanged();
                                                                                                                                                      } catch (Exception e) {
                                                                                                                                                          //Accounts for empty arrays
                                                                                                                                                      }

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
                                        Log.i("Name", document.get("username").toString());
                                        authors.add(new Author(document.get("username").toString(), document.getReference()));
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

    //Search by  multiple genres, see firebase database for exact string genre matches with.
    // genres:
    /*
    "horror"
    "science fiction"
    "comedy"
    "dystopian"
    "drama"
    "tragedy"
    "action adventure"
    "fantasy"
    "romance"
    "default"
    */

    public static void searchByMultipleGenres(final List<String> genreList, final BaseAdapter mAdapter) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("stories")
                .get()
                .addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    stories.clear();
                                    for (final QueryDocumentSnapshot document : task.getResult()) {
                                        document.getDocumentReference("author").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull final Task<DocumentSnapshot> task_author) {
                                                if (task_author.isSuccessful()) {
                                                    ((List<DocumentReference>) document.get("genres")).get(0).get().addOnCompleteListener(
                                                            new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task_genre) {
                                                                    if (task_genre.isSuccessful()) {
                                                                        try {
                                                                            if (genreList.contains(task_genre.getResult().get("type").toString())) {
                                                                                if (!(Boolean) document.get("is_private")) {
                                                                                    stories.add(new Story(document.get("title").toString(), task_author.getResult().get("username").toString(), document.get("text").toString(),
                                                                                            task_genre.getResult().get("type").toString(), document.get("summary").toString(), document.getLong("views"), document.getDate("Created_On"), document.getDate("Last_Updated"), document.getId(), (boolean) document.get("is_private"), (boolean) document.get("in_progress")));
                                                                                    Log.i("genre", "match");
                                                                                    mAdapter.notifyDataSetChanged();
                                                                                }
                                                                            }
                                                                        } catch (Exception e) {
                                                                            //Accounts for empty arrays
                                                                        }

                                                                    }
                                                                }
                                                            }
                                                    );

                                                }
                                            }
                                        });


                                    }
                                }
                            }
                        }
                );
    }

    //Author is created when user signs up.
    //userID : auth.getCurrentUser().getUid();
    public static void createAuthor(String username, String userID) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Map<String, Object> newAuthor = new HashMap<String, Object>();
        newAuthor.put("username", username);
        newAuthor.put("userID", userID);
        firestore.collection("authors").document(userID).set(newAuthor);
    }

    //Story cannot be created without an author.
    //documentID : documentID of author, can pass in auth.getCurrentUser().getUid();
    public static void createStory(String title, String text, String genreInput, String documentID, String summary, Boolean is_private, Boolean in_progress) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String genre = "default";

        for (String g : genreDocumentIDs.keySet()) {
            if (g.equalsIgnoreCase(genreInput)) {
                genre = g;
            }
        }

        final DocumentReference authorRef = firestore.collection("authors").document(documentID);
        final DocumentReference genreRef = firestore.collection("genres").document(genreDocumentIDs.get(genre));
        List<DocumentReference> genres = new ArrayList<DocumentReference>();
        genres.add(genreRef);

        Map<String, Object> newStory = new HashMap<String, Object>();
        newStory.put("title", title);
        newStory.put("genres", genres);
        newStory.put("author", authorRef);
        newStory.put("text", text);
        newStory.put("summary", summary);
        newStory.put("Created_On", new Date());
        newStory.put("Last_Updated", new Date());
        newStory.put("views", (long) 0);
        newStory.put("is_private", is_private);
        newStory.put("in_progress", in_progress);

        firestore.collection("stories").add(newStory).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(final DocumentReference documentReference) {
                Log.d("stories", "DocumentSnapshot added with ID: " + documentReference.getId());
                authorRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.get("stories") == null) {
                            List<DocumentReference> storyList = new ArrayList<DocumentReference>();
                            storyList.add(documentReference);
                            Map<String, Object> newStoryList = new HashMap<String, Object>();
                            newStoryList.put("stories", storyList);

                            documentSnapshot.getReference().update(newStoryList);

                        } else {
                            List<DocumentReference> storyList = new ArrayList<DocumentReference>();
                            storyList = (List<DocumentReference>) documentSnapshot.get("stories");
                            storyList.add(documentReference);
                            Map<String, Object> newStoryList = new HashMap<String, Object>();
                            newStoryList.put("stories", storyList);
                            documentSnapshot.getReference().update(newStoryList);
                        }
                    }
                });

                genreRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.get("stories") == null) {
                            List<DocumentReference> storyList = new ArrayList<DocumentReference>();
                            storyList.add(documentReference);
                            Map<String, Object> newStoryList = new HashMap<String, Object>();
                            newStoryList.put("stories", storyList);

                            documentSnapshot.getReference().update(newStoryList);

                        } else {
                            List<DocumentReference> storyList = new ArrayList<DocumentReference>();
                            storyList = (List<DocumentReference>) documentSnapshot.get("stories");
                            storyList.add(documentReference);
                            Map<String, Object> newStoryList = new HashMap<String, Object>();
                            newStoryList.put("stories", storyList);
                            documentSnapshot.getReference().update(newStoryList);
                        }
                    }
                });

            }
        });
    }

    //If field is not being updated, use NULL as parameter.
    //IE, if title is not being changed, 2nd parameter will be null.
    // document ID : documentID of story to edit
    public static void editStory(String documentID, String title, String text, String genreInput, String summary, Boolean is_private, Boolean in_progress) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        final DocumentReference storyRef = firestore.collection("stories").document(documentID);

        final Map<String, Object> storyMap = new HashMap<String, Object>();
        storyMap.put("Last_Updated", new Date());
        if (title != null) {
            storyMap.put("title", title);
        }
        if (text != null) {
            storyMap.put("text", text);
        }
        if (summary != null) {
            storyMap.put("summary", summary);
        }
        if (in_progress != null) {
            storyMap.put("in_progress", in_progress);
        }
        if (is_private != null) {
            storyMap.put("is_private", is_private);
        }
        if (genreInput != null) {
            String genre = "default";

            for (String g : genreDocumentIDs.keySet()) {
                if (g.equalsIgnoreCase(genreInput)) {
                    genre = g;
                }
            }

            final DocumentReference genreRef = firestore.collection("genres").document(genreDocumentIDs.get(genre));
            List<DocumentReference> genres = new ArrayList<DocumentReference>();
            genres.add(genreRef);
            storyMap.put("genres", genres);

            storyRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    DocumentReference oldGenre = ((List<DocumentReference>) documentSnapshot.get("genres")).get(0);
                    oldGenre.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            List<DocumentReference> storyList = new ArrayList<DocumentReference>();
                            storyList = (List<DocumentReference>) documentSnapshot.get("stories");
                            storyList.remove(storyRef);
                            Map<String, Object> newStoryList = new HashMap<String, Object>();
                            newStoryList.put("stories", storyList);
                            documentSnapshot.getReference().update(newStoryList);
                            Log.i("Remove Old Genre", "removed");

                        }
                    });
                }
            });
            genreRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.get("stories") == null) {
                        List<DocumentReference> storyList = new ArrayList<DocumentReference>();
                        storyList.add(storyRef);
                        Map<String, Object> newStoryList = new HashMap<String, Object>();
                        newStoryList.put("stories", storyList);

                        documentSnapshot.getReference().update(newStoryList);
                        Log.i("Added New Genre", "added");
                        storyRef.update(storyMap);

                    } else {
                        List<DocumentReference> storyList = new ArrayList<DocumentReference>();
                        storyList = (List<DocumentReference>) documentSnapshot.get("stories");
                        storyList.add(storyRef);
                        Map<String, Object> newStoryList = new HashMap<String, Object>();
                        newStoryList.put("stories", storyList);
                        documentSnapshot.getReference().update(newStoryList);
                        Log.i("Added New Genre", "added");
                        storyRef.update(storyMap);


                    }
                }
            });
        }
        if (genreInput == null) {
            storyRef.update(storyMap);
        }
    }

    //Story cannot be created without an author.
    /*
    public static void createStory(String title, String text, String genreInput, String documentID) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String genre = "default";


        for (String g : genreDocumentIDs.keySet()) {
            if (g.equalsIgnoreCase(genreInput)) {
                genre = g;
            }
        }

        final DocumentReference authorRef = firestore.collection("authors").document(documentID);
        final DocumentReference genreRef = firestore.collection("genres").document(genreDocumentIDs.get(genre));
        List<DocumentReference> genres = new ArrayList<DocumentReference>();
        genres.add(genreRef);

        Map<String, Object> newStory = new HashMap<String, Object>();
        newStory.put("title", title);
        newStory.put("genres", genres);
        newStory.put("author", authorRef);
        newStory.put("text", text);
        newStory.put("summary", "");
        newStory.put("Created_On", new Date());
        newStory.put("Last_Updated", new Date());
        newStory.put("views", (long) 0);

        firestore.collection("stories").add(newStory).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(final DocumentReference documentReference) {
                Log.d("stories", "DocumentSnapshot added with ID: " + documentReference.getId());
                authorRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.get("stories") == null) {
                            List<DocumentReference> storyList = new ArrayList<DocumentReference>();
                            storyList.add(documentReference);
                            Map<String, Object> newStoryList = new HashMap<String, Object>();
                            newStoryList.put("stories", storyList);

                            documentSnapshot.getReference().update(newStoryList);

                        } else {
                            List<DocumentReference> storyList = new ArrayList<DocumentReference>();
                            storyList = (List<DocumentReference>) documentSnapshot.get("stories");
                            storyList.add(documentReference);
                            Map<String, Object> newStoryList = new HashMap<String, Object>();
                            newStoryList.put("stories", storyList);
                            documentSnapshot.getReference().update(newStoryList);
                        }
                    }
                });

                genreRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.get("stories") == null) {
                            List<DocumentReference> storyList = new ArrayList<DocumentReference>();
                            storyList.add(documentReference);
                            Map<String, Object> newStoryList = new HashMap<String, Object>();
                            newStoryList.put("stories", storyList);

                            documentSnapshot.getReference().update(newStoryList);

                        } else {
                            List<DocumentReference> storyList = new ArrayList<DocumentReference>();
                            storyList = (List<DocumentReference>) documentSnapshot.get("stories");
                            storyList.add(documentReference);
                            Map<String, Object> newStoryList = new HashMap<String, Object>();
                            newStoryList.put("stories", storyList);
                            documentSnapshot.getReference().update(newStoryList);
                        }
                    }
                });

            }
        });
    }
    */

    //documentID = story ID
    public static void getChapters(String documentID, final BaseAdapter mAdapter) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("chapters").whereEqualTo("storyID", documentID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    chapters.clear();
                    for (final QueryDocumentSnapshot document : task.getResult()) {
                        chapters.add(new Chapter(document.get("title").toString(), document.get("text").toString()));
                        Log.i("chapter", document.get("text").toString());
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });

    }

    //text = contents
    //chapterNumber = number of chapter
    // title = title
    // document ID = story ID
    public static void createChapter(String title, String text, long chapterNumber, final String documentID) {
        Map<String, Object> newChapter = new HashMap<String, Object>();
        newChapter.put("text", text);
        newChapter.put("title", title);
        newChapter.put("storyID", documentID);
        newChapter.put("chapter_number", chapterNumber);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("chapters").add(newChapter).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                  @Override
                                                                                  public void onSuccess(final DocumentReference documentReference) {

                                                                                  }
                                                                              }
        );

    }

    //Document ID = chapter ID
    public static void editChapter(String title, String text, final String documentID) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        final DocumentReference chapterRef = firestore.collection("chapters").document(documentID);

        Map<String, Object> newChapter = new HashMap<String, Object>();
        newChapter.put("text", text);
        newChapter.put("title", title);
        chapterRef.update(newChapter);

    }

    /*
    public static void editStory(String documentID, String title, String text, String genreInput) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        final DocumentReference storyRef = firestore.collection("stories").document(documentID);

        final Map<String, Object> storyMap = new HashMap<String, Object>();
        storyMap.put("Last_Updated", new Date());
        if (title != null) {
            storyMap.put("title", title);
        }
        if (text != null) {
            storyMap.put("text", text);
        }
        if (genreInput != null) {
            String genre = "default";

            for (String g : genreDocumentIDs.keySet()) {
                if (g.equalsIgnoreCase(genreInput)) {
                    genre = g;
                }
            }

            final DocumentReference genreRef = firestore.collection("genres").document(genreDocumentIDs.get(genre));
            List<DocumentReference> genres = new ArrayList<DocumentReference>();
            genres.add(genreRef);
            storyMap.put("genres",genres);

            storyRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    DocumentReference oldGenre = ((List<DocumentReference>) documentSnapshot.get("genres")).get(0);
                    oldGenre.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            List<DocumentReference> storyList = new ArrayList<DocumentReference>();
                            storyList = (List<DocumentReference>) documentSnapshot.get("stories");
                            storyList.remove(storyRef);
                            Map<String, Object> newStoryList = new HashMap<String, Object>();
                            newStoryList.put("stories", storyList);
                            documentSnapshot.getReference().update(newStoryList);
                            Log.i("Remove Old Genre", "removed");

                        }
                    });
                }
            });
            genreRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.get("stories") == null) {
                        List<DocumentReference> storyList = new ArrayList<DocumentReference>();
                        storyList.add(storyRef);
                        Map<String, Object> newStoryList = new HashMap<String, Object>();
                        newStoryList.put("stories", storyList);

                        documentSnapshot.getReference().update(newStoryList);
                        Log.i("Added New Genre", "added");
                        storyRef.update(storyMap);

                    } else {
                        List<DocumentReference> storyList = new ArrayList<DocumentReference>();
                        storyList = (List<DocumentReference>) documentSnapshot.get("stories");
                        storyList.add(storyRef);
                        Map<String, Object> newStoryList = new HashMap<String, Object>();
                        newStoryList.put("stories", storyList);
                        documentSnapshot.getReference().update(newStoryList);
                        Log.i("Added New Genre", "added");
                        storyRef.update(storyMap);


                    }
                }
            });
        }
        if (genreInput == null) {
            storyRef.update(storyMap);
        }
    }
    */


    //Add a comment to the story. Requires the commenting user's author referenceID,
    // and the story reference ID
    public static void addComment(final String text, String storyID, String authorID, final BaseAdapter mAdapter) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        final DocumentReference storyRef = firestore.collection("stories").document(storyID);
        final DocumentReference authorRef = firestore.collection("authors").document(authorID);


        Map<String, Object> newComment = new HashMap<String, Object>();
        newComment.put("author", authorRef);
        newComment.put("story", storyRef);
        newComment.put("text", text);
        firestore.collection("comments").add(newComment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                comments.add(text);
                mAdapter.notifyDataSetChanged();
                Log.d("authors", "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        });
    }

    //Get all comments associated with a story, Document ID is the reference ID of the story in question
    public static void getComments(String documentID, final BaseAdapter mAdapter) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        final DocumentReference storyRef = firestore.collection("stories").document(documentID);


        firestore.collection("comments").whereEqualTo("story", storyRef)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   comments.clear();
                                                   for (final QueryDocumentSnapshot document : task.getResult()) {

                                                       comments.add(document.get("text").toString());
                                                       Log.i("comment", document.get("text").toString());
                                                       mAdapter.notifyDataSetChanged();
                                                   }
                                               }
                                           }
                                       }
                );
    }

    public static void createWritingPrompt(String documentID, final Timestamp stamp, final String prompt, final ArrayList<String> genres, final String tag) {
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        final DocumentReference authorRef = firestore.collection("authors").document(documentID);
        authorRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                  @Override
                                                  public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                      if (task.isSuccessful()) {
                                                          Map<String, Object> newPrompt = new HashMap<String, Object>();
                                                          newPrompt.put("user", task.getResult().get("username").toString());
                                                          newPrompt.put("time_posted", stamp);
                                                          newPrompt.put("prompt", prompt);
                                                          newPrompt.put("tag", tag);
                                                          newPrompt.put("categories", genres);
                                                          firestore.collection("writing_prompts").add(newPrompt).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                                                                          @Override
                                                                                                                                          public void onSuccess(final DocumentReference documentReference) {

                                                                                                                                          }
                                                                                                                                      }
                                                          );
                                                      }
                                                  }
                                              }
        );


    }

    public static void searchWritingPromptByMultipleGenres(final List<String> genreList, final BaseAdapter mAdapter) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("writing_prompts")
                .get()
                .addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    writingprompts.clear();
                                    for (final QueryDocumentSnapshot document : task.getResult()) {
                                        Log.i("WP", "get writing prompt");
                                        ArrayList<String> categories = (ArrayList<String>) document.get("categories");


                                        for (int i = 0; i < categories.size(); i++) {
                                            if (genreList.contains(categories.get(i))) {
                                                writingprompts.add(new WritingPrompt(document.get("prompt").toString(), categories, document.getDate("time_posted"),
                                                        document.get("user").toString(), document.get("tag").toString()));
                                                Log.i("writing prompt", "match");
                                                mAdapter.notifyDataSetChanged();

                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                );
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
