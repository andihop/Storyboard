package com.example.andi.storyboard.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.andi.storyboard.R;
import com.example.andi.storyboard.datatype.Story;
import com.example.andi.storyboard.firebase.FireStoreOps;
import com.example.andi.storyboard.search.StoriesResultAdapter;

import java.util.ArrayList;

public class TabsFragment extends Fragment {

    int position;
    TextView tab1;

    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        TabsFragment tabsFragment = new TabsFragment();
        tabsFragment.setArguments(bundle);
        return tabsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tabs, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // RECENT STORIES
        if (position == 0) {
//            tab1 = view.findViewById(R.id.main_page_tab);
//            tab1.setText("Tab 1");

            ArrayList<Story> stories = FireStoreOps.stories;
            ListView featured_stories_listview = view.findViewById(R.id.featured_stories_list);

            StoriesResultAdapter mAdapter = new StoriesResultAdapter(getContext(), stories);
            featured_stories_listview.setAdapter(mAdapter);


        }
        // WRITING PROMPTS
        else if (position == 1) {
//            tab1 = view.findViewById(R.id.main_page_tab);
//            tab1.setText("Tab 2");
        }
        // FEATURED STORIES
        else if (position == 2) {
//            tab1 = view.findViewById(R.id.main_page_tab);
//            tab1.setText("Tab 3");
        }
    }
}
