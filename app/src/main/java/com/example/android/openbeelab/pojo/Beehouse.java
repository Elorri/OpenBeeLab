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
    public final String jsonId;
    public final String name;
    public final String jsonApiaryId;
    public Long apiaryId;
    private final String database;


    private static String LOG_TAG = Beehouse.class.getSimpleName();


    public Beehouse(long id, String jsonId, String name, String jsonApiaryId, String database) {
        this.id = id;
        this.jsonId = jsonId;
        this.name = name;
        this.jsonApiaryId = jsonApiaryId;
        this.database=database;
    }

    public Beehouse(String jsonId, String name, String jsonApiaryId, String database) {
        this.jsonId = jsonId;
        this.name = name;
        this.jsonApiaryId = jsonApiaryId;
        this.database=database;
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BeeContract.BeehouseEntry.COLUMN_JSON_ID, this.jsonId);
        contentValues.put(BeeContract.BeehouseEntry.COLUMN_NAME, this.name);
        contentValues.put(BeeContract.BeehouseEntry.COLUMN_JSON_APIARY_ID, this.jsonApiaryId);
        //TODO remove this if when we will force every beehouse apiary_id to exist in the apiary
        //TODO list
        if (this.apiaryId != null)
            contentValues.put(BeeContract.BeehouseEntry.COLUMN_APIARY_ID, this.apiaryId);
        contentValues.put(BeeContract.BeehouseEntry.COLUMN_DATABASE, this.database);
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

    public static void resetDB(Context context) {
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
     * 3 : COLUMN_APIARY_ID
     * 4 : COLUMN_DATABASE
     *
     * @param cursor
     * @return List<Beehouse>
     */
    public static List<Beehouse> getBeehouses(Cursor cursor) {
        final int COLUMN_ID = 0;
        final int COLUMN_JSON_ID = 1;
        final int COLUMN_NAME = 2;
        final int COLUMN_JSON_APIARY_ID = 3;
        final int COLUMN_DATABASE = 3;


        List<Beehouse> beehouses = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            long id = cursor.getLong(COLUMN_ID);
            String jsonId = cursor.getString(COLUMN_JSON_ID);
            String name = cursor.getString(COLUMN_NAME);
            String jsonApiaryId = cursor.getString(COLUMN_JSON_APIARY_ID);
            String database = cursor.getString(COLUMN_DATABASE);
            beehouses.add(new Beehouse(id, jsonId, name, jsonApiaryId, database));
        }
        return beehouses;
    }


    public static List<Beehouse> fetchApiaryId(Context context, List<Beehouse> beehouses) {
        for (Beehouse beehouse : beehouses) {
            Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2]
                    + "beehouse.jsonApiaryId " + beehouse.jsonApiaryId);
            beehouse.apiaryId = Apiary.findIdByJsonId(context, beehouse.jsonApiaryId);
        }
        return beehouses;

    }
}
