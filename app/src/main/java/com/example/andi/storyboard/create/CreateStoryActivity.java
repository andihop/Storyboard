package com.example.andi.storyboard.create;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.andi.storyboard.main.MainActivity;
import com.example.andi.storyboard.R;
import com.example.andi.storyboard.firebase.FireStoreOps;
import com.example.andi.storyboard.viewstory.StoryReadActivity;
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
        AlertDialog.Builder alert = new AlertDialog.Builder(CreateStoryActivity.this);

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
