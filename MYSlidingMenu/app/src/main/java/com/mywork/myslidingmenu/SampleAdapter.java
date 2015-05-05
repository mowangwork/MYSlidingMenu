package com.mywork.myslidingmenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ĭ on 2015/5/4.
 */

public class SampleAdapter extends ArrayAdapter<SampleAdapter.SampleItem> {

    public static class SampleItem {
        public String tag;
        public int iconRes;
        public SampleItem(String tag, int iconRes) {
            this.tag = tag;
            this.iconRes = iconRes;
        }
    }
    public SampleAdapter(Context context) {
        super(context, 0);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
        }
        ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
        icon.setImageResource(getItem(position).iconRes);
        TextView title = (TextView) convertView.findViewById(R.id.row_title);
        title.setText(getItem(position).tag);

        return convertView;
    }

}