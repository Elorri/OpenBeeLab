package com.example.android.openbeelab.pojo;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.android.openbeelab.db.BeeContract;

import java.util.List;

/**
 * Created by Elorri on 20/12/2015.
 */
public class Apiary {
    private static final String LOG_TAG = Apiary.class.getSimpleName();
    public long id; //can be null
    public final String name;

    public Apiary(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Apiary(String name) {
        this.name = name;
    }

    public static void syncDB(Context context, List<Apiary> apiaries) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        ContentValues[] apiariesContentValues = Apiary.getContentValuesArray(apiaries);
        int inserted = 0;
        if (apiariesContentValues.length > 0)
            inserted = context.getContentResolver().bulkInsert(BeeContract.UserEntry
                    .CONTENT_URI, apiariesContentValues);
        Log.e(LOG_TAG, "SyncDB Complete. " + inserted + " Inserted");
    }

    private static ContentValues[] getContentValuesArray(List<Apiary> apiaries) {
            ContentValues[] values = new ContentValues[apiaries.size()];
            int i = 0;
            for (Apiary apiary : apiaries) {
                values[i] = apiary.toContentValues();
                i++;
            }
            return values;
    }

    private ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BeeContract.UserEntry.COLUMN_NAME, this.name);
        return contentValues;
    }
}
