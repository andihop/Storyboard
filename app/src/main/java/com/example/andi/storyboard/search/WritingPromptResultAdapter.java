package com.example.andi.storyboard.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.andi.storyboard.R;
import com.example.andi.storyboard.datatype.Story;
import com.example.andi.storyboard.datatype.WritingPrompt;

import java.util.ArrayList;

public class WritingPromptResultAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<WritingPrompt> writing_prompt;

    public WritingPromptResultAdapter(Context context, ArrayList<WritingPrompt> writingprompts) {
        this.context = context;
        this.writing_prompt = writingprompts;
    }

    public int getCount() {
        return writing_prompt.size();
    }

    public WritingPrompt getItem(int id) {
        return writing_prompt.get(id);
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

        final WritingPrompt item = (WritingPrompt) getItem(position);

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
