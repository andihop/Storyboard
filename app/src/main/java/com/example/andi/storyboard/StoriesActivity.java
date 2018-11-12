package com.example.andi.storyboard;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class StoriesActivity extends AppCompatActivity  {

    StoriesResultAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stories);
        ArrayList<Story> stories = FireStoreOps.stories;
        ListView resultsList = findViewById(R.id.stories_result_list);

        mAdapter = new StoriesResultAdapter(getApplicationContext(), stories);

        resultsList.setAdapter(mAdapter);

        //Test Query
        //FireStoreOps.createStory("Sample", "Sample Text","null", "4hQDx7MZsoTvwdOp8EEB");
        FireStoreOps.searchByRef("stories","authors","S4TEFok6UlrLTa64RHv3", "Author", mAdapter);

    }


}
