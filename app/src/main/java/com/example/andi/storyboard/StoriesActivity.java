package com.example.andi.storyboard;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

        setTitle("Story Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAdapter = new StoriesResultAdapter(getApplicationContext(), stories);

        resultsList.setAdapter(mAdapter);

        FireStoreOps.searchByMultipleGenres(getIntent().getStringArrayListExtra("genre_filters"), mAdapter);
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

                startActivity(intent);
            }
        });

        //Test Query
        //FireStoreOps.createStory("Sample 2", "Sample Text","null", "4hQDx7MZsoTvwdOp8EEB");
        //FireStoreOps.searchByRef("stories","authors","S4TEFok6UlrLTa64RHv3", "Author", mAdapter);
        //FireStoreOps.getAllStories(mAdapter);

        //FireStoreOps.getStory("iIU7KOxtGTsUZ9LeKS5v",mAdapter);
        //FireStoreOps.createStory("Sample 3", "Sample Text 3","null", "4hQDx7MZsoTvwdOp8EEB", "sample summary");
        //FireStoreOps.editStory("iIU7KOxtGTsUZ9LeKS5v", null, null, "science fiction" , "new summary");
        //FireStoreOps.getStory("iIU7KOxtGTsUZ9LeKS5v",mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FireStoreOps.stories.clear();
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
