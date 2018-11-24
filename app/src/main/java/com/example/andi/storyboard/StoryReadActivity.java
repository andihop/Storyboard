package com.example.andi.storyboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class StoryReadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_read);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String str = getIntent().getStringExtra("title");
        getSupportActionBar().setTitle(str);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String inProg = "Completed!";
        if (getIntent().getBooleanExtra("in_progress", true)) {
            inProg = "In Progress";
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
    }

    @Override
    protected void onDestroy() {
        FireStoreOps.incrementViewCount(getIntent().getStringExtra("documentID"));
        super.onDestroy();
        // TODO: add count to number of views to current story being read
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
