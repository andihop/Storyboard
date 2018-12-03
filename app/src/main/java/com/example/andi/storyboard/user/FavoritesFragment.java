package com.example.andi.storyboard.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.andi.storyboard.R;
import com.example.andi.storyboard.datatype.Story;
import com.example.andi.storyboard.datatype.WritingPrompt;
import com.example.andi.storyboard.firebase.FireStoreOps;
import com.example.andi.storyboard.search.StoriesResultAdapter;
import com.example.andi.storyboard.search.WritingPromptResultAdapter;
import com.example.andi.storyboard.viewstory.StoryReadActivity;
import com.example.andi.storyboard.viewstory.WritingPromptReadActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {

    FirebaseAuth auth;

    int position;
    FragmentManager fM;
    FragmentTransaction fT;

    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        FavoritesFragment favoritesFragment = new FavoritesFragment();
        favoritesFragment.setArguments(bundle);
        return favoritesFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();

        fM = getFragmentManager();
        position = getArguments().getInt("position");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tabs, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // FAVORITE STORIES
        if (position == 0) {
            FireStoreOps.favoriteStories.clear();
            ArrayList<Story> favoriteStories = FireStoreOps.favoriteStories;
            // says R.id.featured_stories_list to reference the list view in fragment_tabs.xml
            ListView favorite_stories_listview = view.findViewById(R.id.featured_stories_list_tabs);

            final StoriesResultAdapter mAdapter = new StoriesResultAdapter(getContext(), favoriteStories);
            favorite_stories_listview.setAdapter(mAdapter);

            FireStoreOps.getFavoriteStories(auth.getCurrentUser().getUid(), mAdapter);
            favorite_stories_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent intent = new Intent(getContext(), StoryReadActivity.class);
                    intent.putExtra("title", mAdapter.getItem(i).getTitle());
                    intent.putExtra("author", mAdapter.getItem(i).getAuthorName());
                    intent.putExtra("text", mAdapter.getItem(i).getText());
                    intent.putExtra("summary", mAdapter.getItem(i).getSummary());
                    intent.putExtra("views", "" + mAdapter.getItem(i).getViews());
                    intent.putExtra("created_on", mAdapter.getItem(i).getCreated_On().toString());
                    intent.putExtra("last_update", mAdapter.getItem(i).getLast_Updated().toString());
                    intent.putExtra("documentID", mAdapter.getItem(i).getDocumentID());
                    intent.putExtra("in_progress", mAdapter.getItem(i).getIn_Progress());
                    intent.putExtra("is_private", mAdapter.getItem(i).getIs_Private());
                    intent.putExtra("genre", mAdapter.getItem(i).getGenre());
                    intent.putExtra("userID", mAdapter.getItem(i).getAuthorID());

                    startActivity(intent);
                }
            });
        }
        // FAVORITE PROMPTS
        else if (position == 1) {
            FireStoreOps.favoritePrompts.clear();
            ArrayList<WritingPrompt> favoritePrompts = FireStoreOps.favoritePrompts;
            ListView favorite_prompts_listview = view.findViewById(R.id.featured_stories_list_tabs);

            final WritingPromptResultAdapter mAdapter = new WritingPromptResultAdapter(getContext(), favoritePrompts);
            favorite_prompts_listview.setAdapter(mAdapter);

            FireStoreOps.getFavoritePrompts(auth.getCurrentUser().getUid(), mAdapter);
            favorite_prompts_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent intent = new Intent(getContext(), WritingPromptReadActivity.class);
                    intent.putExtra("text", mAdapter.getItem(i).toString());
                    intent.putExtra("date", mAdapter.getItem(i).getPostedTime().toString());
                    intent.putExtra("author", mAdapter.getItem(i).getPrompt_author());
                    intent.putExtra("prompt", mAdapter.getItem(i).getText());

                    startActivity(intent);
                }
            });
        }
    }
}