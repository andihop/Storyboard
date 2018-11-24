package com.example.andi.storyboard.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.andi.storyboard.datatype.Story;
import com.example.andi.storyboard.R;

import java.util.ArrayList;

public class StoriesResultAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<Story> stories;

    public StoriesResultAdapter(Context context, ArrayList<Story> stories) {
        this.context = context;
        this.stories = stories;
    }

    public int getCount() {
        return stories.size();
    }

    public Story getItem(int id) {
        return stories.get(id);
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

        final Story item = (Story) getItem(position);

        final TextView titleView = (TextView) itemRelativeLayout.findViewById(R.id.story_result_item);

        titleView.setText(item.toString());

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
