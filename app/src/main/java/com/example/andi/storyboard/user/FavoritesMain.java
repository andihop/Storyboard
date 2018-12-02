package com.example.andi.storyboard.user;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
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

}
