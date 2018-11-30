package com.example.andi.storyboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.andi.storyboard.firebase.FireStoreOps;
import com.example.andi.storyboard.main.MainActivity;

public class EditStoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_story);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        final EditText title = findViewById(R.id.title);
        final EditText summary = findViewById(R.id.summary);
        final EditText text = findViewById(R.id.body);
        final Spinner genre = findViewById(R.id.genreSpinner);
        final RadioGroup privacyGroup = findViewById(R.id.privacy_rg);
        final RadioGroup progressGroup = findViewById(R.id.progress_rg);

        ///////////
        /*
         SEND INTENT TO THIS ACTIVITY BY PLACING THESE INTO INTENT VIA PUT EXTRA
         */
        ///////////
        final String documentId = getIntent().getStringExtra("documentID").toString();
        String intentTitle = getIntent().getStringExtra("title").toString();
        String intentSummary = getIntent().getStringExtra("summary").toString();
        String intentText = getIntent().getStringExtra("text").toString();
        String intentGenre = getIntent().getStringExtra("genre").toString();
        Boolean isPrivate = getIntent().getBooleanExtra("is_private", true);
        Boolean inProgress = getIntent().getBooleanExtra("in_progress", true);

        String[] genreArray = getResources().getStringArray(R.array.genre_list);
        int genrePos = 0;
        for (int i = 0; i < genreArray.length; i++) {
            if (genreArray[i].equalsIgnoreCase(intentGenre)) {
                genrePos = i;
            }
        }


        title.setText(getIntent().getStringExtra("title").toString());
        summary.setText(getIntent().getStringExtra("summary").toString());
        text.setText(getIntent().getStringExtra("text").toString());
        genre.setSelection(genrePos);

        if (isPrivate) {
            ((RadioButton)findViewById(R.id.private_radio)).setChecked(true);
        } else {
            ((RadioButton)findViewById(R.id.public_radio)).setChecked(true);
        }

        if (inProgress) {
            ((RadioButton)findViewById(R.id.in_progress)).setChecked(true);
        } else {
            ((RadioButton)findViewById(R.id.completed)).setChecked(true);
        }


        android.widget.Button fab = (android.widget.Button) findViewById(R.id.create_story_create_button);
        fab.setText("Finish Edit");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isPrivate = true;
                boolean inProgress = true;
                if (title.getText().toString().trim().length() == 0 ||
                        summary.getText().toString().trim().length() == 0 ||
                        text.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please fill out all fields before submitting.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (!((RadioButton)findViewById(privacyGroup.getCheckedRadioButtonId())).getText().equals("Private")) {
                        isPrivate = false;
                    }

                    if (!((RadioButton)findViewById(progressGroup.getCheckedRadioButtonId())).getText().equals("In Progress")) {
                        inProgress = false;
                    }
                    FireStoreOps.editStory(documentId,title.getText().toString(), text.getText().toString(),genre.getSelectedItem().toString(),summary.getText().toString(), isPrivate,inProgress);

                    Toast.makeText(getApplicationContext(), "Submitted!",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                }

            }
        });
    }

}
