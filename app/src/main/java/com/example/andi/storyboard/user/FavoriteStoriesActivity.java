package com.example.andi.storyboard.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.andi.storyboard.R;

public class FavoriteStoriesActivity extends Fragment {

    @android.support.annotation.Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @android.support.annotation.Nullable ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.display_favorite_stories, container, false);

        return view;
    }
}
