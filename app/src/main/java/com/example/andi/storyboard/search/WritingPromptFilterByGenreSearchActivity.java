package com.example.andi.storyboard.search;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.andi.storyboard.create.CreateMaterialChoosingActivity;
import com.example.andi.storyboard.main.MainActivity;
import com.example.andi.storyboard.R;
import com.example.andi.storyboard.user.SettingsActivity;

import java.util.ArrayList;

public class WritingPromptFilterByGenreSearchActivity extends AppCompatActivity {
    private ArrayList<ConstraintLayout> filterLayoutList;
    private ArrayList<CheckBox> filterCheckBoxList;
    private ArrayList<String> filterGenreNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_by_genre_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        filterLayoutList = new ArrayList<>();
        filterCheckBoxList = new ArrayList<>();
        filterGenreNameList = new ArrayList<>();

        filterLayoutList.add((ConstraintLayout) findViewById(R.id.horror_filter));
        filterCheckBoxList.add((CheckBox) findViewById(R.id.horror_filter_checkbox));
        filterGenreNameList.add("horror");

        filterLayoutList.add((ConstraintLayout) findViewById(R.id.fantasy_filter));
        filterCheckBoxList.add((CheckBox) findViewById(R.id.fantast_filter_checkbox));
        filterGenreNameList.add("fantasy");

        filterLayoutList.add((ConstraintLayout) findViewById(R.id.romance_filter));
        filterCheckBoxList.add((CheckBox) findViewById(R.id.romance_filter_checkbox));
        filterGenreNameList.add("romance");

        filterLayoutList.add((ConstraintLayout) findViewById(R.id.drama_filter));
        filterCheckBoxList.add((CheckBox) findViewById(R.id.drama_filter_checkbox));
        filterGenreNameList.add("drama");

        filterLayoutList.add((ConstraintLayout) findViewById(R.id.science_fiction_filter));
        filterCheckBoxList.add((CheckBox) findViewById(R.id.science_fiction_filter_checkbox));
        filterGenreNameList.add("science fiction");

        filterLayoutList.add((ConstraintLayout) findViewById(R.id.dystopian_filter));
        filterCheckBoxList.add((CheckBox) findViewById(R.id.dystopian_filter_checkbox));
        filterGenreNameList.add("dystopian");

        filterLayoutList.add((ConstraintLayout) findViewById(R.id.tragedy_filter));
        filterCheckBoxList.add((CheckBox) findViewById(R.id.tragedy_filter_checkbox));
        filterGenreNameList.add("tragedy");

        filterLayoutList.add((ConstraintLayout) findViewById(R.id.action_adventure_filter));
        filterCheckBoxList.add((CheckBox) findViewById(R.id.action_adventure_filter_checkbox));
        filterGenreNameList.add("action adventure");

        filterLayoutList.add((ConstraintLayout) findViewById(R.id.comedy_filter));
        filterCheckBoxList.add((CheckBox) findViewById(R.id.comedy_filter_checkbox));
        filterGenreNameList.add("comedy");

        filterLayoutList.add((ConstraintLayout) findViewById(R.id.thriller_filter));
        filterCheckBoxList.add((CheckBox) findViewById(R.id.thriller_filter_checkbox));
        filterGenreNameList.add("thriller");

        for (int i = 0; i < filterLayoutList.size(); i++) {
            final int curr = i;
            ConstraintLayout layout = filterLayoutList.get(i);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox checkBox = filterCheckBoxList.get(curr);
                    boolean isChecked = checkBox.isChecked();
                    checkBox.setChecked(!isChecked);
                    System.out.println(checkBox.isChecked());
                }
            });
        }

        Button searchButton = (Button) findViewById(R.id.story_search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ListFilteredWritingPromptActivity.class);
                ArrayList<String> genreFilterlist = new ArrayList<>();

                for (int i = 0; i < filterCheckBoxList.size(); i++) {
                    if (filterCheckBoxList.get(i).isChecked()) {
                        genreFilterlist.add(filterGenreNameList.get(i));
                    }
                }
                intent.putStringArrayListExtra("genre_filters", genreFilterlist);
                System.out.println(genreFilterlist.toString());
                startActivityForResult(intent, MainActivity.SEARCH_STORIES_REQUEST);
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
        AlertDialog.Builder alert = new AlertDialog.Builder(WritingPromptFilterByGenreSearchActivity.this);

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
