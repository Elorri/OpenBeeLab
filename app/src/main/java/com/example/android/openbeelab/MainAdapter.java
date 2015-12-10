package com.example.android.openbeelab;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Elorri on 03/12/2015.
 */
public class MainAdapter extends CursorAdapter {

    public MainAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public static class ViewHolder {
        public final TextView beehouseImageView;
        public final TextView beehouseTextView;

        public ViewHolder(View view) {
            beehouseImageView = (TextView) view.findViewById(R.id.beehouse_img);
            beehouseTextView = (TextView) view.findViewById(R.id.beehouse_name);
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
        viewHolder.beehouseImageView.setBackgroundDrawable(context.getResources()
                .getDrawable(Utility.getIconResourceForBeehouseCondition(cursor.getDouble
                        (MainFragment.COL_BEEHOUSE_WEIGHT))));
        viewHolder.beehouseImageView.setText(String.format(context.getString(R.string.weight),
                cursor.getString(MainFragment.COL_BEEHOUSE_WEIGHT)));
//        ((ImageView) view).setImageResource(Utility.getIconResourceForBeehouseCondition(cursor
//                .getDouble(1)));
        viewHolder.beehouseTextView.setText(cursor.getString(MainFragment.COL_BEEHOUSE_NAME));
    }
}
