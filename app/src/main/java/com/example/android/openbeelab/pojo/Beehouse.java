package com.example.android.openbeelab.pojo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.android.openbeelab.db.BeeContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elorri on 06/12/2015.
 */
public class Beehouse {
    public long id; //can be null
    public final String name;
    public final long userId;
    public final String apiaryName;
    public final double currentWeight;


    private static String LOG_TAG = Beehouse.class.getSimpleName();


    public Beehouse(long id, String name, long userId, String apiaryName, double currentWeight) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.apiaryName = apiaryName;
        this.currentWeight = currentWeight;
    }

    public Beehouse(String name, long userId, String apiaryName, double currentWeight) {
        this.name = name;
        this.userId = userId;
        this.apiaryName = apiaryName;
        this.currentWeight = currentWeight;
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BeeContract.BeehouseEntry.COLUMN_NAME, this.name);
        contentValues.put(BeeContract.BeehouseEntry.COLUMN_USER_ID, this.userId);
        contentValues.put(BeeContract.BeehouseEntry.COLUMN_APIARY_NAME, this.apiaryName);
        contentValues.put(BeeContract.BeehouseEntry.COLUMN_CURRENT_WEIGHT, this.currentWeight);
        return contentValues;
    }

    public static ContentValues[] getContentValuesArray(List<Beehouse> beehouses) {
        ContentValues[] values = new ContentValues[beehouses.size()];
        int i = 0;
        for (Beehouse beehouse : beehouses) {
            values[i] = beehouse.toContentValues();
            i++;
        }
        return values;
    }

    public static void resetDB(Context context){
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        context.getContentResolver().delete(BeeContract.BeehouseEntry
                .CONTENT_URI, null, null);
    }

    public static void syncDB(Context context, List<Beehouse> beehouses) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        ContentValues[] beehousesContentValues = Beehouse.getContentValuesArray(beehouses);
        int inserted = 0;
        if (beehousesContentValues.length > 0)
            inserted = context.getContentResolver().bulkInsert(BeeContract.BeehouseEntry
                    .CONTENT_URI, beehousesContentValues);
        Log.e(LOG_TAG, "SyncDB Complete. " + inserted + " Inserted");
    }


    /**
     * Retrieve a list of Beehouse object from a cursor that contains the following columns. Order
     * is important.
     * 0 : COLUMN_ID
     * 1 : COLUMN_NAME
     * 2 : COLUMN_USER_ID
     * 3 : COLUMN_APIARY_NAME
     * 4 : COLUMN_CURRENT_WEIGHT
     *
     * @param cursor
     * @return List<Beehouse>
     */
    public static List<Beehouse> getBeehouses(Cursor cursor) {
        final int COLUMN_ID = 0;
        final int COLUMN_NAME = 1;
        final int COLUMN_USER_ID = 2;
        final int COLUMN_APIARY_NAME = 3;
        final int COLUMN_CURRENT_WEIGHT = 4;


        List<Beehouse> beehouses = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            long id = cursor.getLong(COLUMN_ID);
            String name = cursor.getString(COLUMN_NAME);
            long userId = cursor.getLong(COLUMN_USER_ID);
            String apiaryName = cursor.getString(COLUMN_APIARY_NAME);
            double currentWeight = cursor.getDouble(COLUMN_CURRENT_WEIGHT);
            beehouses.add(new Beehouse(id, name, userId, apiaryName, currentWeight));
        }
        return beehouses;
    }


}
