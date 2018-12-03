package com.example.andi.storyboard.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.andi.storyboard.create.CreateStoryActivity;
import com.google.firebase.auth.FirebaseAuth;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.andi.storyboard.R;


public class FavoritesMain extends AppCompatActivity {

    private static final String tag = "FavoritesMain";

    private FavoritesAdapter mFavoritesAdapter;

    private ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_favorites);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String toDisplay = FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "'s Favorites!";

        TextView username_greeting = (TextView) findViewById(com.example.andi.storyboard.R.id.username_greeting);
        username_greeting.setText(toDisplay);

        mViewPager = (ViewPager) findViewById(R.id.tabs_pager);
        mFavoritesAdapter = new FavoritesAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mFavoritesAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onPause() {
        try {
            mFavoritesAdapter = null;
            mViewPager.setAdapter(null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        try {
            mViewPager = (ViewPager) findViewById(R.id.tabs_pager);
            mFavoritesAdapter = new FavoritesAdapter(getSupportFragmentManager());
            mViewPager.setAdapter(mFavoritesAdapter);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
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
        AlertDialog.Builder alert = new AlertDialog.Builder(FavoritesMain.this);

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
