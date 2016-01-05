package com.example.android.openbeelab;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.CursorAdapter;
/**
 * Created by Elorri on 03/01/2016.
 */
public class BeehousesAdapter extends CursorAdapter {

    public BeehousesAdapter(Context context, Cursor c, int flags) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.beehouses_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String beehouseName=cursor.getString(BeehousesFragment.COL_BEEHOUSE_NAME);
        float weight=cursor.getFloat(BeehousesFragment.COL_BEEHOUSE_WEIGHT);
        String weightString=String.format(context.getString(R.string.beehouses_weight), weight);
        int weightDrawableId=Utility.getIconResourceForBeehouseCondition(weight);

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.beehouseImageView.setBackgroundDrawable(context.getResources().getDrawable(weightDrawableId));
        viewHolder.beehouseImageView.setText(weightString);
        viewHolder.beehouseTextView.setText(beehouseName);
    }
}
