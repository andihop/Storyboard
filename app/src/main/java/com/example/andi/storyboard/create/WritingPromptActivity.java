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
import android.widget.TextView;
import android.widget.Toast;

import com.example.andi.storyboard.R;
import com.example.andi.storyboard.firebase.FireStoreOps;
import com.example.andi.storyboard.user.SettingsActivity;
import com.example.andi.storyboard.viewstory.StoryReadActivity;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WritingPromptActivity extends AppCompatActivity {
    Button mSelectGenres, mPostPrompt;
    TextView mGenresSelected;
    String[] listGenres;
    String writingPrompt, tag;
    boolean[] checkedGenres;

    EditText mWritingPrompt;
    RadioGroup mRadioGroup;
    RadioButton mRadioButton;

    // genres selected by user
    ArrayList<Integer> mUserGenres = new ArrayList<>();
    ArrayList<String> genresToPost = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.writing_prompt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSelectGenres = (Button) findViewById(R.id.btn_select_genres);
        mPostPrompt = (Button) findViewById(R.id.post);
        mGenresSelected = (TextView) findViewById(R.id.display_selected_genres);
        mWritingPrompt = (EditText) findViewById(R.id.prompt);

        // radio buttons for tagging prompt's status
        mRadioGroup = findViewById(R.id.tag_group);

        // setting default radio button "in-progress"
        mRadioGroup.check(R.id.tag_in_progress);

        listGenres = getResources().getStringArray(R.array.genre_list);
        checkedGenres = new boolean[listGenres.length];

        // selecting genres
        mSelectGenres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(WritingPromptActivity.this);
                mBuilder.setTitle(R.string.dialog_title);
                mBuilder.setMultiChoiceItems(listGenres, checkedGenres, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            if (!mUserGenres.contains(which)) {
                                mUserGenres.add(which);
                            }
                        } else if (mUserGenres.contains(which)) {
                            mUserGenres.remove((Integer) which);
                        }
                    }
                });

                mBuilder.setCancelable(false);

                mBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String genre = "";
                        genresToPost.clear();

                        for (int i = 0; i < mUserGenres.size(); i++) {
                            genre = genre + listGenres[mUserGenres.get(i)];
                            genresToPost.add(listGenres[mUserGenres.get(i)]);

                            if (i != (mUserGenres.size() - 1)) {
                                genre = genre + ", ";
                            }
                        }

                        mGenresSelected.setText(genre);
                    }
                });

                mBuilder.setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                mBuilder.setNeutralButton(R.string.clear_all, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!genresToPost.isEmpty()) {
                            genresToPost.clear();
                        }

                        for (int i = 0; i < checkedGenres.length; i++) {
                            checkedGenres[i] = false;
                            mUserGenres.clear();
                            mGenresSelected.setText("");
                        }
                    }
                });

                // showing dialog
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        mPostPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // retrieve writing prompt as a string
                writingPrompt = mWritingPrompt.getText().toString();

                AlertDialog.Builder alert = new AlertDialog.Builder(WritingPromptActivity.this);

                // validate that the prompt has no empty fields

                // if no genres selected
                if (genresToPost.isEmpty()) {
                    // alert that says "please select genres"
                    alert.setTitle(R.string.post_prompt);
                    alert.setMessage(R.string.please_select_genre);
                    alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    // if no text typed in writing text field
                    if (writingPrompt.trim().length() == 0) {
                        // please fill out the writing prompt
                        alert.setTitle(R.string.post_prompt);
                        alert.setMessage(R.string.please_write_prompt);
                        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    } else {
                        // retrieve prompt's tag status
                        mRadioButton = findViewById(mRadioGroup.getCheckedRadioButtonId());
                        tag = mRadioButton.getText().toString();

                        alert.setTitle(R.string.post_prompt);
                        alert.setMessage(R.string.confirm_prompt_post);

                        // user clicks "yes" to post the prompt
                        alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            // storing in fire base
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Date currentTime = Calendar.getInstance().getTime();
                                final Timestamp ts = new Timestamp(currentTime);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // change UI elements here
                                        FireStoreOps.createWritingPrompt(FirebaseAuth.getInstance().getCurrentUser().getUid(), ts, writingPrompt, genresToPost, tag);
                                    }
                                });

                                dialog.dismiss();

                                Toast.makeText(WritingPromptActivity.this, R.string.prompt_post_success, Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        });

                        // user clicks "no" so the prompt is not posted
                        alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                Toast.makeText(WritingPromptActivity.this, R.string.prompt_not_posted, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                alert.show();
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
        AlertDialog.Builder alert = new AlertDialog.Builder(WritingPromptActivity.this);

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
