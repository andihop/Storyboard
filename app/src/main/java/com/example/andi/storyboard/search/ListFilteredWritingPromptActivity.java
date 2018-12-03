package com.example.andi.storyboard.search;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.andi.storyboard.R;
import com.example.andi.storyboard.create.CreateMaterialChoosingActivity;
import com.example.andi.storyboard.datatype.Story;
import com.example.andi.storyboard.datatype.WritingPrompt;
import com.example.andi.storyboard.firebase.FireStoreOps;
import com.example.andi.storyboard.user.SettingsActivity;
import com.example.andi.storyboard.viewstory.StoryReadActivity;
import com.example.andi.storyboard.viewstory.WritingPromptReadActivity;

import java.util.ArrayList;

public class ListFilteredWritingPromptActivity extends AppCompatActivity  {

    WritingPromptResultAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_filtered_stories);
        Toolbar toolbar = (Toolbar) findViewById(com.example.andi.storyboard.R.id.toolbar);
        setSupportActionBar(toolbar);

        FireStoreOps.writingprompts.clear();
        ArrayList<WritingPrompt> writing_prompts = FireStoreOps.writingprompts;
        ListView resultsList = findViewById(R.id.stories_result_list);

        setTitle("Writing Prompts");

        mAdapter = new WritingPromptResultAdapter(getApplicationContext(), writing_prompts);

        resultsList.setAdapter(mAdapter);

        FireStoreOps.searchWritingPromptByMultipleGenres(getIntent().getStringArrayListExtra("genre_filters"), mAdapter);
        resultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(), WritingPromptReadActivity.class);
                intent.putExtra("text", mAdapter.getItem(i).toString());
                intent.putExtra("date", mAdapter.getItem(i).getPostedTime().toString());

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
        AlertDialog.Builder alert = new AlertDialog.Builder(ListFilteredWritingPromptActivity.this);

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
