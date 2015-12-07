package com.example.android.openbeelab.pojo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.android.openbeelab.db.BeeContract;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elorri on 24/11/2015.
 */
public class Measure {
    private static final String LOG_TAG = Measure.class.getSimpleName();
    public double id; //can be null
    public final String name;
    public String timestamp; //can be null
    public final String weekId;
    public final double value;
    public final String unit;
    public final long beehouseId;

    public Measure(long id, String name, String timestamp, String weekId, double value, String
            unit, long beehouseId) {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
        this.weekId = weekId;
        this.value = value;
        this.unit = unit;
        this.beehouseId = beehouseId;
    }


    public Measure(String name, String weekId, double value, String unit, long beehouseId) {
        this.name = name;
        this.weekId = weekId;
        this.value = value;
        this.unit = unit;
        this.beehouseId = beehouseId;
    }


    public String getShortDesc() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(1);
        numberFormat.setRoundingMode(RoundingMode.CEILING);
        return weekId + "  " + numberFormat.format(value) + " kg";
    }

    @Override
    public String toString() {
        return "id:" + id + " - name:" + name + " - " +
                "timestamp:" + timestamp + " - weekId:" + weekId + " - value:" + value + " - unit:" + unit;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BeeContract.MeasureEntry.COLUMN_NAME, this.name);
        contentValues.put(BeeContract.MeasureEntry.COLUMN_WEEK_ID, this.weekId);
        contentValues.put(BeeContract.MeasureEntry.COLUMN_VALUE, this.value);
        contentValues.put(BeeContract.MeasureEntry.COLUMN_UNIT, this.unit);
        contentValues.put(BeeContract.MeasureEntry.COLUMN_BEEHOUSE_ID, this.beehouseId);
        return contentValues;
    }

    public static ContentValues[] getContentValuesArray(List<Measure> measures) {
        ContentValues[] values = new ContentValues[measures.size()];
        int i = 0;
        for (Measure measure : measures) {
            values[i] = measure.toContentValues();
            i++;
        }
        return values;
    }


    /**
     * Retrieve a list of Measure object from a cursor that contains the following columns. Order
     * is important.
     * 0 : COLUMN_ID
     * 1 : COLUMN_NAME
     * 2 : COLUMN_TIMESTAMP
     * 3 : COLUMN_WEEK_ID
     * 4 : COLUMN_VALUE
     * 5 : COLUMN_UNIT
     * 6 : COLUMN_BEEHOUSE_ID
     *
     * @param cursor
     * @return List<Measure>
     */
    public static List<Measure> getMeasures(Cursor cursor) {
        final int COLUMN_ID = 0;
        final int COLUMN_NAME = 1;
        final int COLUMN_TIMESTAMP = 2;
        final int COLUMN_WEEK_ID = 3;
        final int COLUMN_VALUE = 4;
        final int COLUMN_UNIT = 5;
        final int COLUMN_BEEHOUSE_ID = 6;

        List<Measure> measures = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            long id = cursor.getLong(COLUMN_ID);
            String name = cursor.getString(COLUMN_NAME);
            String timestamp = cursor.getString(COLUMN_TIMESTAMP);
            String weekId = cursor.getString(COLUMN_WEEK_ID);
            double value = cursor.getDouble(COLUMN_VALUE);
            String unit = cursor.getString(COLUMN_UNIT);
            long beehouseId = cursor.getLong(COLUMN_BEEHOUSE_ID);
            measures.add(new Measure(id, name, timestamp, weekId, value, unit, beehouseId));
        }
        return measures;
    }

    public static void resetDB(Context context){
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        context.getContentResolver().delete(BeeContract.MeasureEntry
                .CONTENT_URI, null, null);
    }

    public static void syncDB(Context context, List<Measure> measures) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        if (measures != null) {
            ContentValues[] measuresContentValues = Measure.getContentValuesArray(measures);
            int inserted = 0;
            if (measuresContentValues.length > 0)
                inserted = context.getContentResolver().bulkInsert(BeeContract.MeasureEntry
                        .CONTENT_URI, measuresContentValues);
            Log.e(LOG_TAG, "SyncDB Complete. " + inserted + " Inserted");
        }
    }
}
