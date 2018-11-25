package com.example.andi.storyboard.create;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andi.storyboard.R;
import com.example.andi.storyboard.firebase.FireStoreOps;
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

        mSelectGenres = (Button) findViewById(R.id.btn_select_genres);
        mPostPrompt = (Button) findViewById(R.id.post);
        mGenresSelected = (TextView) findViewById(R.id.display_selected_genres);
        mWritingPrompt = (EditText) findViewById(R.id.prompt);

        // radio buttons for tagging prompt's status
        mRadioGroup = findViewById(R.id.tag_group);

        listGenres = getResources().getStringArray(R.array.genre_list);
        checkedGenres = new boolean[listGenres.length];

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
                            mUserGenres.remove((Integer)which);
                        }
                    }
                });

                mBuilder.setCancelable(false);

                mBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String genre = ""; genresToPost.clear();

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
                // retrieve prompt's tag status
                mRadioButton = findViewById(mRadioGroup.getCheckedRadioButtonId());
                tag = mRadioButton.getText().toString();

                AlertDialog.Builder alert = new AlertDialog.Builder(WritingPromptActivity.this);
                alert.setTitle(R.string.post_prompt);
                alert.setMessage(R.string.confirm_prompt_post);
                alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /* NEED TO STORE THE FOLLOWING TO FIRE BASE:
                         *
                         *      writingPrompt, tag, and genresToPost
                         *
                         */

                        // MAKE STORE HERE
                        Date currentTime = Calendar.getInstance().getTime();
                        Timestamp ts = new Timestamp(currentTime);

                        ///////////////////////////////////////////////////// NEED TO ADJUST "placeholderUser" PARAMETER WITH ACTUAL USER NAME

                        FireStoreOps.createWritingPrompt(FirebaseAuth.getInstance().getCurrentUser().getUid(), ts, writingPrompt, genresToPost, tag);

                        dialog.dismiss();

                        Toast.makeText(WritingPromptActivity.this, R.string.prompt_post_success, Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });

                alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Toast.makeText(WritingPromptActivity.this, R.string.prompt_not_posted, Toast.LENGTH_SHORT).show();
                    }
                });

                alert.show();
            }
        });
    }
}
