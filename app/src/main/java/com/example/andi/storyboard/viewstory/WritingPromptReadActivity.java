package com.example.andi.storyboard.viewstory;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.andi.storyboard.R;
import com.example.andi.storyboard.create.CreateStoryActivity;
import com.example.andi.storyboard.datatype.WritingPrompt;
import com.example.andi.storyboard.firebase.FireStoreOps;
import com.example.andi.storyboard.user.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WritingPromptReadActivity extends AppCompatActivity {

    ScaleAnimation scaleAnimation;
    BounceInterpolator bounceInterpolator;
    ToggleButton buttonFavorite;

    FirebaseFirestore firestore;
    DocumentReference favoritesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firestore = FirebaseFirestore.getInstance();
        ArrayList<WritingPrompt> favoritePrompts = FireStoreOps.favoritePrompts;

        setContentView(R.layout.activity_prompt_read);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Writing Prompt");

        // favorites button
        buttonFavorite = (ToggleButton) findViewById(R.id.btn_favorite);

        String str = "Posted On: " + getIntent().getStringExtra("date") + "\n\n" + "The Prompt:\n\n" +
                getIntent().getStringExtra("text");

        TextView textView = (TextView) findViewById(R.id.story_text);

        textView.setText(str);

        // if story is already in user's favorites, set checked status of favorite icon to true
        FireStoreOps.getFavoriteStories(FirebaseAuth.getInstance().getCurrentUser().getUid(), null);

        Boolean storyIsInFavorites = false;

        for (int i = 0; i < favoritePrompts.size(); i++) {
            if (favoritePrompts.get(i).getPrompt_author().equals(getIntent().getStringExtra("author"))
                    && favoritePrompts.get(i).getText().equals(getIntent().getStringExtra("prompt"))
                    && favoritePrompts.get(i).getPostedTime().toString().equals(getIntent().getStringExtra("date"))) {
                storyIsInFavorites = true;
                break;
            }
        }

        if (storyIsInFavorites) {
            buttonFavorite.setChecked(true);
        } else {
            buttonFavorite.setChecked(false);
        }

        // animation to make the favorites button bounce when clicked
        scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(500);
        bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);
        buttonFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean isChecked) {
                //animation
                compoundButton.startAnimation(scaleAnimation);

                favoritesRef = firestore.collection("favorites").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                //checking whether user has favorites list or not
                favoritesRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            //if user does not have favorite prompts, add the the favorite prompts array field
                            if (task.getResult().get("prompts") == null) {
                                ArrayList<DocumentReference> prompts = new ArrayList<>();
                                Map<String, Object> favorites = new HashMap<>();
                                favorites.put("prompts", prompts);
                                favoritesRef.set(favorites);
                            }
                        }
                    }
                });

                firestore.collection("writing_prompts")
                        .whereEqualTo("prompt", getIntent().getStringExtra("prompt"))
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (!task.getResult().getDocuments().isEmpty()) {
                                        DocumentSnapshot result = task.getResult().getDocuments().get(0);
                                        String doc = result.getId();

                                        // add to favorites, remove from favorites
                                        if (isChecked) {
                                            favoritesRef.update("prompts",
                                                    FieldValue.arrayUnion(firestore.collection("writing_prompts")
                                                            .document(doc)));
                                            Toast.makeText(getApplicationContext(), "Prompt added to favorites!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            favoritesRef.update("prompts",
                                                    FieldValue.arrayRemove(firestore.collection("writing_prompts")
                                                            .document(doc)));
                                            Toast.makeText(getApplicationContext(), "Prompt removed from favorites.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                });
            }
        });

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
        AlertDialog.Builder alert = new AlertDialog.Builder(WritingPromptReadActivity.this);

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
