package com.example.andi.storyboard.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.andi.storyboard.R;
import com.example.andi.storyboard.datatype.Author;
import com.example.andi.storyboard.datatype.Story;

import java.util.ArrayList;

public class AuthorResultAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<Author> authors;

    public AuthorResultAdapter(Context context, ArrayList<Author> authors) {
        this.context = context;
        this.authors = authors;
    }

    public int getCount() {
        return authors.size();
    }

    public Author getItem(int id) {
        return authors.get(id);
    }

    public long getItemId(int id) {
        return id;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        RelativeLayout itemRelativeLayout = (RelativeLayout) convertView;

        if (itemRelativeLayout == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemRelativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.story_result_item, null);
        }

        final Author item = (Author) getItem(position);

        final TextView titleView = (TextView) itemRelativeLayout.findViewById(R.id.story_result_item);

        titleView.setText(" \n" + item.toString() + "\n");

        ViewHolder holder = new ViewHolder();
        holder.position = position;
        holder.mItemLayout = itemRelativeLayout;
        holder.mTitleView = titleView;
        itemRelativeLayout.setTag(holder);

        return itemRelativeLayout;
    }

    public static class ViewHolder {
        public int position;
        public RelativeLayout mItemLayout;
        public TextView mTitleView;
    }


}
