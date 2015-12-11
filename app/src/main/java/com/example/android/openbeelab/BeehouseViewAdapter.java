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

    private static final int VIEW_TYPE_COUNT = 2;

    public static final int VIEW_TYPE_INFO = 0;
    public static final int VIEW_TYPE_DATAWIZ_OVER_LAST_30_DAYS = 1;


    public BeehouseViewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = null;
        int viewType = getItemViewType(cursor.getPosition());
        switch (viewType)  {
            case VIEW_TYPE_INFO:
                view = LayoutInflater.from(context).inflate(R.layout.beehouse_info_item, parent, false);
//                ViewHolderDates viewHolder = new ViewHolderDates(view);
//                view.setTag(viewHolder);
                break;
            case VIEW_TYPE_DATAWIZ_OVER_LAST_30_DAYS:
                view = LayoutInflater.from(context).inflate(R.layout.beehouse_datawiz_last30days, parent, false);
//                ViewHolderDates viewHolder = new ViewHolderDates(view);
//                view.setTag(viewHolder);
                break;
        }
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int viewType = getItemViewType(cursor.getPosition());
        switch (viewType) {
            case VIEW_TYPE_INFO:
                break;
            case VIEW_TYPE_DATAWIZ_OVER_LAST_30_DAYS:
                break;
        }
    }


    @Override
    public int getItemViewType(int position) {
        return (position == 0 ) ? VIEW_TYPE_INFO : VIEW_TYPE_DATAWIZ_OVER_LAST_30_DAYS;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

}
