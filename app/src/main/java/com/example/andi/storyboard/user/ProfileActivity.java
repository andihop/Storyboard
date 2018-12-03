package com.example.andi.storyboard.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.example.andi.storyboard.search.WritingPromptFilterByGenreSearchActivity;
import com.example.andi.storyboard.viewstory.StoryReadActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Array.getLength;

public class ProfileActivity extends AppCompatActivity {

    private final String TAG = "ProfileActivity";
    private TextView username, numSubscribers, numStories;
    private ImageView propic;
    private FirebaseAuth auth;
    FirebaseFirestore db;
    private Button viewArchive;
    StoriesResultAdapter mAdapter;
    private ArrayList<String> storiesList;
    private ListView featuredStoryList;
    private ListView recentStoryList;
    private String user;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        String userTemp = getIntent().getStringExtra("username");
        String uidTemp = getIntent().getStringExtra("uid");

        if (userTemp != null && uidTemp != null) {
            user = userTemp;
            uid = uidTemp;
        } else {
            user = auth.getCurrentUser().getDisplayName();
            uid = auth.getCurrentUser().getUid();
        }


        setContentView(R.layout.activity_profile);
        username = (TextView) findViewById(R.id.username);
        numSubscribers = (TextView) findViewById(R.id.numSubscribers);
        numStories = (TextView) findViewById(R.id.numStories);

        viewArchive = (Button) findViewById(R.id.btn_story_archive);

        //Set the username, # subscribers and # stories
        username.setText(user);
        FireStoreOps.storyCountArr.clear();
        FireStoreOps.getSubNum(uid,numSubscribers);
        FireStoreOps.getStoryNum(uid,auth, numStories);

        //numStories.setText(auth.getCurrentUser().collection("stories").size());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(username.getText() + "'s Profile");

        FireStoreOps.featuredProfileStories.clear();
        ArrayList<Story> stories = FireStoreOps.featuredProfileStories;

        FireStoreOps.recentProfileStories.clear();
        ArrayList<Story> recentStories = FireStoreOps.recentProfileStories;

        featuredStoryList = findViewById(R.id.featured_stories_list);
        recentStoryList = findViewById(R.id.recent_stories_list);

        final StoriesResultAdapter mAdapter = new StoriesResultAdapter(this, stories);
        featuredStoryList.setAdapter(mAdapter);

        FireStoreOps.getFeaturedUserStories(uid,auth, mAdapter);
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

        FireStoreOps.getRecentUserStories(uid,auth, mRecentAdapter);
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

/*        CollectionReference storiesList = db.collection("authors");
        DocumentReference docRef = db.collection("authors").document(auth.getCurrentUser().getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                int storyCount = 0;
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        List<DocumentReference> list = (List<DocumentReference>) doc.get("stories");

                        if (list == null) {
                            numStories.setText("0");
                        } else {
                            numStories.setText("" + list.size());
                        }

                    }

                }
            }
        });

        CollectionReference subscriberList = db.collection("subscription");
        subscriberList.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int subscriberCount = 0;

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        if (doc.get("author").equals(auth.getCurrentUser().getUid())) {
                            subscriberCount++;
                        }
                    }
                    numSubscribers.setText("" + subscriberCount);
                }
            }
        });*/

        viewArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ListUserStoriesActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("username", user);
                Log.i("user", user);
                startActivity(intent);
            }
        });

        Button subscribe = findViewById(R.id.subscribeButton);

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireStoreOps.subscribe(uid, getApplicationContext());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_side_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;
        AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);

        switch (id) {
            case R.id.action_settings:
                intent = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.back_button:
                finish();
                break;
            case R.id.about_us:
                alert.setTitle("About Us");
                alert.setMessage(R.string.about_us);
                alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();
                break;
            case R.id.contact_us:
                alert.setTitle("Contact Us");
                alert.setMessage(R.string.contact_us);
                alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
