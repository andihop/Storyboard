package com.example.andi.storyboard.viewstory;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.andi.storyboard.EditStoryActivity;
import com.example.andi.storyboard.firebase.FireStoreOps;
import com.example.andi.storyboard.R;
import com.example.andi.storyboard.main.TabsAdapter;
import com.example.andi.storyboard.main.TabsFragment;
import com.example.andi.storyboard.user.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class StoryReadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_story_read);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String str = getIntent().getStringExtra("title");
        getSupportActionBar().setTitle(str);

        String inProg = "Completed!";
        if (getIntent().getBooleanExtra("in_progress", true)) {
            inProg = "In Progress";
        }
        FloatingActionButton editButton = (FloatingActionButton) findViewById(R.id.edit_button);

        if (getIntent().getStringExtra("userID").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            editButton.setVisibility(View.VISIBLE);
        } else {
            editButton.setVisibility(View.INVISIBLE);
        }

        str = "By " + getIntent().getStringExtra("author") + "\nCreated on " + getIntent().getStringExtra("created_on") + "\nLast update on " + getIntent().getStringExtra("last_update")
                + "\nStatus: " + inProg + "\n" + getIntent().getStringExtra("views") + " views\n\nSummary:\n" + getIntent().getStringExtra("summary") + "\n\n" +
                getIntent().getStringExtra("text");

        TextView textView = (TextView) findViewById(R.id.story_text);

        textView.setText(str);

        FloatingActionButton commentButton = (FloatingActionButton) findViewById(R.id.comment_button);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), StoryCommentsActivity.class);
                // TODO: intent filter to get the list of comments for current story being read
                intent.putExtra("title", getIntent().getStringExtra("title"));
                intent.putExtra("documentID", getIntent().getStringExtra("documentID"));
                startActivity(intent);
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), EditStoryActivity.class);
                // TODO: intent filter to get the list of comments for current story being read
                intent.putExtra("title", getIntent().getStringExtra("title"));
                intent.putExtra("documentID", getIntent().getStringExtra("documentID"));
                intent.putExtra("summary", getIntent().getStringExtra("summary"));
                intent.putExtra("text", getIntent().getStringExtra("text"));
                intent.putExtra("genre", getIntent().getStringExtra("genre"));
                intent.putExtra("is_private", getIntent().getStringExtra("is_private"));
                intent.putExtra("in_progress", getIntent().getBooleanExtra("in_progress", true));
                startActivity(intent);



            }
        });
    }

    @Override
    protected void onDestroy() {
        Log.d("StoryReadActivity", "views" + getIntent().getStringExtra("views"));
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                // change UI elements here
                FireStoreOps.incrementViewCount(getIntent().getStringExtra("documentID"));
                FireStoreOps.updateTopTen(getIntent().getStringExtra("documentID"), Integer.parseInt(getIntent().getStringExtra("views")) + 1);
                FireStoreOps.updateRecentStoriesRead(FirebaseAuth.getInstance().getCurrentUser().getUid(), getIntent().getStringExtra("documentID"), Calendar.getInstance().getTime());
            }
        });
        super.onDestroy();
        // TODO: add count to number of views to current story being read
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
        AlertDialog.Builder alert = new AlertDialog.Builder(StoryReadActivity.this);

        switch (id) {
            case R.id.action_settings:
                return true;
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
