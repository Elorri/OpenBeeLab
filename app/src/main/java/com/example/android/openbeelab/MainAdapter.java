package com.example.android.openbeelab;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by Elorri on 03/12/2015.
 */
public class MainAdapter extends CursorAdapter {

    public MainAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    //No need for a ViewHolder class because the view parameter of bindView method gives us the root view, and that's the one we want to set. No need to travel the view tree in this case

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_item, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        view.setBackgroundDrawable(context.getResources()
                .getDrawable(Utility.getIconResourceForBeehouseCondition(cursor.getDouble
                        (MainFragment.COL_BEHOUSE_WEIGHT))));
//        ((ImageView) view).setImageResource(Utility.getIconResourceForBeehouseCondition(cursor
//                .getDouble(1)));
    }
}
