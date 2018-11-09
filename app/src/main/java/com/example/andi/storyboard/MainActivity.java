package com.example.andi.storyboard;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

//Jee Kang Query database edits
import com.google.firebase.firestore.*;
import com.google.android.gms.tasks.*;
import java.util.ArrayList;
import android.support.annotation.*;


import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import com.google.firebase.database.collection.*;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firestore.v1beta1.Document;
import com.google.firestore.v1beta1.WriteResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.new_story_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action - test for githu", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                setUp();

            }
        });
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
        DocumentReference author = firestore.collection("authors").document("S4TEFok6UlrLTa64RHv3");

        //use the author document reference to retrieve list of all stories that have a reference to specified author
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

    }

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
    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
