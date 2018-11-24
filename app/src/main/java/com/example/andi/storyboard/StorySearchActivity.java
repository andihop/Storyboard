package com.example.andi.storyboard;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.example.andi.storyboard.MainActivity;
import com.example.andi.storyboard.R;
import com.example.andi.storyboard.StoriesActivity;

public class StorySearchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_search);

        Button searchButton = (Button) findViewById(R.id.story_search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), StoriesActivity.class);
                startActivityForResult(intent, MainActivity.SEARCH_STORIES_REQUEST);
            }
        });

        Button backButton = (Button) findViewById(R.id.story_search_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
