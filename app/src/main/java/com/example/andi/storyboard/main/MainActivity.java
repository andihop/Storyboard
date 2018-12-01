package com.example.andi.storyboard.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.andi.storyboard.R;
import com.example.andi.storyboard.create.CreateMaterialChoosingActivity;
import com.example.andi.storyboard.login.LoginActivity;
import com.example.andi.storyboard.search.SearchChoosingActivity;
import com.example.andi.storyboard.user.FavoritesActivity;
import com.example.andi.storyboard.user.ProfileActivity;
import com.google.firebase.firestore.FirebaseFirestore;

//Jee Kang Query database edits


import com.google.firebase.auth.*;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final int SEARCH_STORIES_REQUEST = 0;
    public static final int RESULT_STORIES_REQUEST = 1;
    FirebaseAuth auth;

    TabsAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager tabsPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            finish();
        }

        setContentView(com.example.andi.storyboard.R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(com.example.andi.storyboard.R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView username_greeting = (TextView) findViewById(com.example.andi.storyboard.R.id.username_greeting);
        String username = auth.getCurrentUser().getDisplayName();
        if (username == null || username == "") {
            username_greeting.setText("Hello, Storyboarder!");
        } else {
            username_greeting.setText("Hello, " + username + "!");
        }

        tabsPager = (ViewPager) findViewById(R.id.tabspager);
        adapter = new TabsAdapter(getSupportFragmentManager());
        tabsPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(tabsPager);

        FloatingActionButton new_story_button = findViewById(com.example.andi.storyboard.R.id.new_story_button);
        new_story_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CreateMaterialChoosingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPause() {
        try {
            adapter = null;
            tabsPager.setAdapter(null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        try {
            tabsPager = (ViewPager) findViewById(R.id.tabspager);
            adapter = new TabsAdapter(getSupportFragmentManager());
            tabsPager.setAdapter(adapter);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SEARCH_STORIES_REQUEST && resultCode == RESULT_OK){
            // TODO: change fragment of this activity to be the search filter fragment
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.example.andi.storyboard.R.menu.menu_main, menu);
        return true;
    }

    public void setUp() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;

        //I forgot to add a commit message
        switch(id) {
            case com.example.andi.storyboard.R.id.action_settings:
                return true;
            case com.example.andi.storyboard.R.id.search_button:
                intent = new Intent(getBaseContext(), SearchChoosingActivity.class);
                startActivityForResult(intent, SEARCH_STORIES_REQUEST);
                break;
            case com.example.andi.storyboard.R.id.profile_button:
                intent = new Intent(getBaseContext(), ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.signout_button:
                auth.signOut();
                intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.view_favorites:
                intent = new Intent(getBaseContext(), FavoritesActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
