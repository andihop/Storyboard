package com.example.andi.storyboard.viewstory;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.andi.storyboard.R;
import com.example.andi.storyboard.create.CreateStoryActivity;
import com.example.andi.storyboard.firebase.FireStoreOps;

public class WritingPromptReadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_read);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Writing Prompt");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String str = "Posted On: " + getIntent().getStringExtra("date") + "\n\n" + "The Prompt:\n\n" +
                getIntent().getStringExtra("text");

        TextView textView = (TextView) findViewById(R.id.story_text);

        textView.setText(str);

        FloatingActionButton commentButton = (FloatingActionButton) findViewById(R.id.comment_button);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CreateStoryActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
