package com.example.andi.storyboard.viewstory;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.andi.storyboard.firebase.FireStoreOps;
import com.example.andi.storyboard.R;
import com.example.andi.storyboard.search.*;
import com.google.firebase.auth.FirebaseAuth;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

public class StoryCommentsActivity extends AppCompatActivity {

    private CommentAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_comments);
        ListView resultsList = findViewById(R.id.comment_list);

        setTitle(getIntent().getStringExtra("title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText commentEdit = (EditText) findViewById(R.id.edit_comment);

        //need this before calling add comment
        mAdapter = new CommentAdapter(getApplicationContext(), FireStoreOps.comments);

        ImageView addCommentButton = (ImageView) findViewById(R.id.add_comment_button);
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FireStoreOps.addComment(commentEdit.getText().toString(),getIntent().getStringExtra("documentID"), FirebaseAuth.getInstance().getCurrentUser().getUid(), mAdapter);

                // TODO: figure out reference id for current story and user reading that story
                // for firestoreops method
                commentEdit.getText().clear();
                //Source for getting rid of keyboard on adding comment:
                //https://stackoverflow.com/questions/2342620/how-to-hide-keyboard-after-typing-in-edittext-in-android

                InputMethodManager inputManager =
                        (InputMethodManager) getApplicationContext().
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        /*
        ArrayList<String> comments = new ArrayList<>();
        comments.add("comment 1");
        comments.add("comment \n\n\n\n\n\n\n\n");
        comments.add("comment \n\n\n\n\n\n\n\n");
        comments.add("comment \n\n\n\n\n\n\n\n");
        comments.add("comment \n\n\n\n\n\n\n\n");
        comments.add("comment \n\n\n\n\n\n\n\n");
        */


        mAdapter = new CommentAdapter(getApplicationContext(), FireStoreOps.comments);

        resultsList.setAdapter(mAdapter);
        FireStoreOps.getComments(getIntent().getStringExtra("documentID"), mAdapter);
    }

    class CommentAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<String> comments;

        public CommentAdapter(Context context, ArrayList<String> stories) {
            this.context = context;
            this.comments = stories;
        }

        public int getCount() {
            return comments.size();
        }

        public String getItem(int id) {
            return comments.get(id);
        }

        public long getItemId(int id) {
            return id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            RelativeLayout itemRelativeLayout = (RelativeLayout) convertView;

            if (itemRelativeLayout == null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                itemRelativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.story_comment_item, null);
            }

            final String item = (String) getItem(position);

            final TextView titleView = (TextView) itemRelativeLayout.findViewById(R.id.story_comment_item);

            titleView.setText(item.toString());

            StoriesResultAdapter.ViewHolder holder = new StoriesResultAdapter.ViewHolder();
            holder.position = position;
            holder.mItemLayout = itemRelativeLayout;
            holder.mTitleView = titleView;
            itemRelativeLayout.setTag(holder);

            return itemRelativeLayout;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}