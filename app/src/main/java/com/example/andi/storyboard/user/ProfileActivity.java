package com.example.andi.storyboard.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.andi.storyboard.R;
import com.example.andi.storyboard.datatype.Story;
import com.example.andi.storyboard.firebase.FireStoreOps;
import com.example.andi.storyboard.search.StoriesResultAdapter;
import com.example.andi.storyboard.viewstory.StoryReadActivity;
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
        FireStoreOps.featuredProfileStories.clear();
        ArrayList<Story> stories = FireStoreOps.featuredProfileStories;
        FireStoreOps.recentProfileStories.clear();
        ArrayList<Story> recentStories = FireStoreOps.recentProfileStories;
        featuredStoryList = findViewById(R.id.featured_stories_list);
        recentStoryList = findViewById(R.id.recent_stories_list);

        final StoriesResultAdapter mAdapter = new StoriesResultAdapter(this, stories);
        featuredStoryList.setAdapter(mAdapter);

        FireStoreOps.getFeaturedUserStories(auth.getCurrentUser().getUid(),auth, mAdapter);
        featuredStoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(ProfileActivity.this, StoryReadActivity.class);
                intent.putExtra("title", mAdapter.getItem(i).getTitle());
                intent.putExtra("author", mAdapter.getItem(i).getAuthorName());
                intent.putExtra("text", mAdapter.getItem(i).getText());
                intent.putExtra("summary", mAdapter.getItem(i).getSummary());
                intent.putExtra("views", "" + mAdapter.getItem(i).getViews());
                intent.putExtra("created_on", mAdapter.getItem(i).getCreated_On().toString());
                intent.putExtra("last_update", mAdapter.getItem(i).getLast_Updated().toString());
                intent.putExtra("documentID", mAdapter.getItem(i).getDocumentID());
                intent.putExtra("in_progress", mAdapter.getItem(i).getIn_Progress());
                intent.putExtra("is_private", mAdapter.getItem(i).getIs_Private());
                intent.putExtra("genre", mAdapter.getItem(i).getGenre());
                intent.putExtra("userID", mAdapter.getItem(i).getAuthorID());

                startActivity(intent);
            }
        });

        final StoriesResultAdapter mRecentAdapter = new StoriesResultAdapter(this, recentStories);
        recentStoryList.setAdapter(mRecentAdapter);

        FireStoreOps.getRecentUserStories(auth.getCurrentUser().getUid(),auth, mAdapter);
        recentStoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(ProfileActivity.this, StoryReadActivity.class);
                intent.putExtra("title", mAdapter.getItem(i).getTitle());
                intent.putExtra("author", mAdapter.getItem(i).getAuthorName());
                intent.putExtra("text", mAdapter.getItem(i).getText());
                intent.putExtra("summary", mAdapter.getItem(i).getSummary());
                intent.putExtra("views", "" + mAdapter.getItem(i).getViews());
                intent.putExtra("created_on", mAdapter.getItem(i).getCreated_On().toString());
                intent.putExtra("last_update", mAdapter.getItem(i).getLast_Updated().toString());
                intent.putExtra("documentID", mAdapter.getItem(i).getDocumentID());
                intent.putExtra("in_progress", mAdapter.getItem(i).getIn_Progress());
                intent.putExtra("is_private", mAdapter.getItem(i).getIs_Private());
                intent.putExtra("genre", mAdapter.getItem(i).getGenre());
                intent.putExtra("userID", mAdapter.getItem(i).getAuthorID());

                startActivity(intent);
            }
        });




        //recentStoryList = findViewById(R.id.recent_stories_list);


        viewArchive = (Button) findViewById(R.id.btn_story_archive);

        //Set the username, # subscribers and # stories
        username.setText(auth.getCurrentUser().getDisplayName());
        //numSubscribers
        //numStories.setText(auth.getCurrentUser().collection("stories").size());

        viewArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ListUserStoriesActivity.class));
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
