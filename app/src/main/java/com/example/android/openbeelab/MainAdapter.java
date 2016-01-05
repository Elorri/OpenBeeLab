package com.example.android.openbeelab;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Elorri on 03/12/2015.
 */
public class MainAdapter extends CursorAdapter {

    public MainAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public static class ViewHolder {
        public final ImageView apiaryImageView;
        public final TextView apiaryTextView;

        public ViewHolder(View view) {
            apiaryImageView = (ImageView) view.findViewById(R.id.apiary_icon);
            apiaryTextView = (TextView) view.findViewById(R.id.apiary_name);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.apiaryTextView.setText(cursor.getString(MainFragment.COL_APIARY_NAME));
    }
}
