package com.example.android.openbeelab;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Elorri on 11/12/2015.
 */
public class BeehouseOverviewAdapter extends CursorAdapter {

    private static final int VIEW_TYPE_COUNT = 2;

    public static final int VIEW_TYPE_INFO = 0;
    public static final int VIEW_TYPE_DATAWIZ_OVER_LAST_30_DAYS = 1;


    //Should match BeeProvider.getCursorBeehouseInfo Cursor
    static final int COL_VIEW_TYPE_INFO_BEEHOUSE_ID = 0; //needed for the cursor loader
    static final int COL_VIEW_TYPE_INFO_LAST_UPDATE = 1;
    static final int COL_VIEW_TYPE_INFO_CURRENT_WEIGHT = 2;
    static final int COL_VIEW_TYPE_INFO_WEIGHT_UNIT = 3;
    static final int COL_VIEW_TYPE_INFO_NAME = 4;
    static final int COL_VIEW_TYPE_INFO_APIARY_NAME = 5;


    public BeehouseOverviewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public static class ViewHolderGeneral {
        public final TextView beehouseLastUpdateTextView;
        public final TextView currentWeightTextView;
        public final TextView currentWeightUnitTextView;
        public final TextView beehouseNameApiaryNameTextView;

        public ViewHolderGeneral(View view) {
            beehouseLastUpdateTextView = (TextView) view.findViewById(R.id.beehouseLastUpdate);
            currentWeightTextView = (TextView) view.findViewById(R.id.currentWeight);
            currentWeightUnitTextView = (TextView) view.findViewById(R.id.currentWeightUnit);
            beehouseNameApiaryNameTextView = (TextView) view.findViewById(R.id.beehouseNameApiaryName);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = null;
        int viewType = getItemViewType(cursor.getPosition());
        switch (viewType)  {
            case VIEW_TYPE_INFO:
                view = LayoutInflater.from(context).inflate(R.layout.beehouse_info_item, parent, false);
                ViewHolderGeneral viewHolderGeneral = new ViewHolderGeneral(view);
                view.setTag(viewHolderGeneral);
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
                //TODO should be an int later getInt(COL_VIEW_TYPE_INFO_LAST_UPDATE)
                String beehouseLastUpdate= cursor.getString(COL_VIEW_TYPE_INFO_LAST_UPDATE);
                float currentWeight=cursor.getFloat(COL_VIEW_TYPE_INFO_CURRENT_WEIGHT);
                String currentWeightUnit=cursor.getString(COL_VIEW_TYPE_INFO_WEIGHT_UNIT);
                String beehouseNameApiaryName=cursor.getString(COL_VIEW_TYPE_INFO_NAME)+"\n"+cursor
                        .getString(COL_VIEW_TYPE_INFO_APIARY_NAME);

                String beehouseLastUpdateString = Utility.getFriendlyDayName(beehouseLastUpdate);
                String currentWeightString = String.format(context.getString(R.string.beehouse_weight), currentWeight);

                ViewHolderGeneral viewHolder = (ViewHolderGeneral) view.getTag();
                viewHolder.beehouseLastUpdateTextView.setText(beehouseLastUpdateString);
                viewHolder.currentWeightTextView.setText(currentWeightString);
                viewHolder.currentWeightUnitTextView.setText(currentWeightUnit);
                viewHolder.beehouseNameApiaryNameTextView.setText(beehouseNameApiaryName);
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
