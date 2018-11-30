package com.example.andi.storyboard.user;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.andi.storyboard.R;
import com.example.andi.storyboard.datatype.Story;
import com.example.andi.storyboard.firebase.FireStoreOps;
import com.example.andi.storyboard.search.StoriesResultAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private final String TAG = "ProfileActivity";
    private TextView username, numSubscribers, numStories;
    private TextView feature_1, feature_2, feature_3, feature_4, feature_5;
    private TextView recent_1, recent_2, recent_3, recent_4, recent_5;
    private ImageView propic;
    private FirebaseAuth auth;
    private Button viewArchive;
    StoriesResultAdapter mAdapter;
    private ListView featuredStoryList;
    private ListView recentStoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        username = (TextView) findViewById(R.id.username);
        setTitle(username + "'s Profile");
        numSubscribers = (TextView) findViewById(R.id.numSubscribers);
        numStories = (TextView) findViewById(R.id.numStories);

        ArrayList<Story> stories = FireStoreOps.stories;
        //featuredStoryList = findViewById(R.id.featured_stories_list);
        //recentStoryList = findViewById(R.id.recent_stories_list);

        mAdapter = new StoriesResultAdapter(getApplicationContext(), stories);

        feature_1 = (TextView) findViewById(R.id.featured_story_1);
        feature_2 = (TextView) findViewById(R.id.featured_story_2);
        feature_3 = (TextView) findViewById(R.id.featured_story_3);
        feature_4 = (TextView) findViewById(R.id.featured_story_4);
        feature_5 = (TextView) findViewById(R.id.featured_story_5);
        recent_1 = (TextView) findViewById(R.id.recent_story_1);
        recent_2 = (TextView) findViewById(R.id.recent_story_2);
        recent_3 = (TextView) findViewById(R.id.recent_story_3);
        recent_4 = (TextView) findViewById(R.id.recent_story_4);
        recent_5 = (TextView) findViewById(R.id.recent_story_5);
        propic = (ImageView) findViewById(R.id.profilepic);
        viewArchive = (Button) findViewById(R.id.btn_story_archive);



        //Set the username, # subscribers and # stories
        username.setText(auth.getCurrentUser().getDisplayName());
        //numSubscribers
        //numStories.setText(auth.getCurrentUser().collection("stories").size());


        featuredStoryList.setAdapter(mAdapter);

        FireStoreOps.getFeaturedUserStories(auth.getCurrentUser().getUid(), auth, mAdapter);
        featuredStoryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        feature_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        feature_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        feature_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        feature_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        feature_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        recent_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        recent_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        recent_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        recent_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        recent_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        viewArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, StoryArchiveActivity.class));
            }
        });
    }

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
