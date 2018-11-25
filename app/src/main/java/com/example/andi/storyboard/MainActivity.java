package com.example.andi.storyboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.andi.storyboard.create.CreateMaterialChoosingActivity;
import com.example.andi.storyboard.login.LoginActivity;
import com.example.andi.storyboard.search.FilterByGenreSearchActivity;
import com.google.firebase.firestore.FirebaseFirestore;

//Jee Kang Query database edits


import com.google.firebase.auth.*;


public class MainActivity extends AppCompatActivity {

    public static final int SEARCH_STORIES_REQUEST = 0;
    public static final int RESULT_STORIES_REQUEST = 1;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        auth = FirebaseAuth.getInstance();
//        if (auth.getCurrentUser() == null) {
//            finish();
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO: Figure out how to get stuff from firestore before inflating new layouts in another activity
        //FireStoreOps.searchByRef(getString(R.string.collection_stories), getString(R.string.collection_authors), "S4TEFok6UlrLTa64RHv3", getString(R.string.stories_field_author));

        FloatingActionButton new_story_button = findViewById(R.id.new_story_button);
        new_story_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CreateMaterialChoosingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == SEARCH_STORIES_REQUEST && resultCode == RESULT_OK){
            // TODO: change fragment of this activity to be the search filter fragment
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void setUp() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        /*FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);*/

        /*
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("author", "test_Author2");
        data.put("genre", "test_genre2");
        data.put("text", "test_text2");
        firestore.collection("stories").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                System.out.println("dasdsadsa");
                Log.d("stories", "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        });
        */

        //Test get stories by author

        //Get author using document ID
        //document path was obtained by analyzing the firestore database online.

        //DocumentReference author = firestore.collection("authors").document("S4TEFok6UlrLTa64RHv3");

        //use the author document reference to retrieve list of all stories that have a reference to specified author

        /*
        final ArrayList<QueryDocumentSnapshot> storiesList = new ArrayList<QueryDocumentSnapshot>();
        firestore.collection("stories")
                .whereEqualTo("author", author)
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

                            }
                        } else {
                            Log.d("storiesByAuthors", "Error getting documents: ", task.getException());
                        }
                    }
                });
        */

        //Test get stories by Genre

        //Get genre using document ID
        //document path was obtained by analyzing the firestore database online.
        //DocumentReference genre = firestore.collection("genres").document("E9FH2veRiT4vgoBEk2ZB");

        //use the genre document reference to retrieve list of all stories that have a reference to specified genre
        //Note difference, uses whereArrayContains() instead of whereEqualTo

        /*
        final ArrayList<QueryDocumentSnapshot> storiesListFromGenre = new ArrayList<QueryDocumentSnapshot>();
        firestore.collection("stories")
                .whereArrayContains("genres", genre)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                storiesList.add(document);
                                Log.d("storiesByGenres", document.getId() + " => " + document.getData());
                                Log.i("Title", document.get("title").toString());
                                Log.i("Text", document.get("text").toString());

                            }
                        } else {
                            Log.d("storiesByGenres", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    */

    /*
    Returns a list of stories in QueryDocumentSnapshot format for a given author.
    Author must be converted to a document reference object before calling function.
    Convert author to document reference by calling author.getReference() if needed.
     */
    /*
    public ArrayList<QueryDocumentSnapshot> storiesByAuthors(DocumentReference author) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        final ArrayList<QueryDocumentSnapshot> storiesList = new ArrayList<QueryDocumentSnapshot>();
        firestore.collection("stories")
                .whereEqualTo("author", author)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                storiesList.add(document);
                                Log.d("storiesByAuthors", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("storiesByAuthors", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return storiesList;
        */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;

        switch(id) {
            case R.id.action_settings:
                return true;
            case R.id.search_button:
                intent = new Intent(getBaseContext(), FilterByGenreSearchActivity.class);
                startActivityForResult(intent, SEARCH_STORIES_REQUEST);
                break;
            case R.id.profile_button:
                return true;
            case R.id.signout_button:
                auth.signOut();
                intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
