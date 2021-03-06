package com.example.andi.storyboard.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.andi.storyboard.R;
import com.example.andi.storyboard.datatype.Story;
import com.example.andi.storyboard.firebase.FireStoreOps;
import com.example.andi.storyboard.search.StoriesResultAdapter;
import com.example.andi.storyboard.search.WritingPromptFilterByGenreSearchActivity;
import com.example.andi.storyboard.viewstory.StoryReadActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ListUserStoriesActivity extends AppCompatActivity  {

    StoriesResultAdapter mAdapter;
    private FirebaseAuth auth;
    private String user;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_filtered_stories);
        FireStoreOps.stories.clear();
        ArrayList<Story> stories = FireStoreOps.stories;
        ListView resultsList = findViewById(R.id.stories_result_list);

        auth = FirebaseAuth.getInstance();
        String userTemp = getIntent().getStringExtra("username");
        String uidTemp = getIntent().getStringExtra("uid");
        if (userTemp != null && uidTemp != null) {
            user = userTemp;
            uid = uidTemp;
        } else {
            user = auth.getCurrentUser().getDisplayName();
            uid = auth.getCurrentUser().getUid();
        }
        Log.i("user", user);


        setTitle("Stories by " + user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAdapter = new StoriesResultAdapter(getApplicationContext(), stories);

        resultsList.setAdapter(mAdapter);

        FireStoreOps.getUserStories(uid,FirebaseAuth.getInstance(), mAdapter);
        resultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getBaseContext(), StoryReadActivity.class);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FireStoreOps.stories.clear();
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
        AlertDialog.Builder alert = new AlertDialog.Builder(ListUserStoriesActivity.this);

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
