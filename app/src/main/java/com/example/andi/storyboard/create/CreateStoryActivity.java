package com.example.andi.storyboard.create;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.andi.storyboard.MainActivity;
import com.example.andi.storyboard.R;
import com.example.andi.storyboard.firebase.FireStoreOps;
import com.google.firebase.auth.FirebaseAuth;

public class CreateStoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_story);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText title = findViewById(R.id.title);
        final EditText summary = findViewById(R.id.summary);
        final EditText text = findViewById(R.id.body);
        final Spinner genre = findViewById(R.id.genreSpinner);
        final RadioGroup privacyGroup = findViewById(R.id.privacy_rg);
        final RadioGroup progressGroup = findViewById(R.id.progress_rg);


        Button button = (Button) findViewById(R.id.create_story_create_button);
        button.setOnClickListener(new View.OnClickListener() {
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

                    FireStoreOps.createStory(title.getText().toString(), text.getText().toString(),
                            genre.getSelectedItem().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            summary.getText().toString(), isPrivate,inProgress);
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
