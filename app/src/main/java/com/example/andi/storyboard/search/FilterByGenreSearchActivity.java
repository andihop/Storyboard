package com.example.andi.storyboard.search;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.andi.storyboard.MainActivity;
import com.example.andi.storyboard.R;

import java.util.ArrayList;

public class FilterByGenreSearchActivity extends Activity {
    private ArrayList<ConstraintLayout> filterLayoutList;
    private ArrayList<CheckBox> filterCheckBoxList;
    private ArrayList<String> filterGenreNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_search);

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
                Intent intent = new Intent(getBaseContext(), ListFilteredStoriesActivity.class);
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

        Button backButton = (Button) findViewById(R.id.story_search_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
