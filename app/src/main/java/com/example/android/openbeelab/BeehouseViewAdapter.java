package com.example.android.openbeelab;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by Elorri on 11/12/2015.
 */
public class BeehouseViewAdapter extends CursorAdapter {

    public BeehouseViewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.beehouse_info_item, parent,
                false);
//        ViewHolder viewHolder = new ViewHolder(view);
//        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }

}
