package com.example.andi.storyboard.viewstory;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.example.andi.storyboard.user.ProfileActivity;
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

        setTitle(getIntent().getStringExtra("Comments"));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        AlertDialog.Builder alert = new AlertDialog.Builder(StoryCommentsActivity.this);

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
