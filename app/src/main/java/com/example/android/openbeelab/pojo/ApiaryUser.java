package com.example.android.openbeelab.pojo;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.android.openbeelab.db.BeeContract;

/**
 * Created by Elorri on 03/01/2016.
 */
public class ApiaryUser {


    private static final String LOG_TAG = ApiaryUser.class.getSimpleName();

    public static void syncDB(Context context, Apiary apiary) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        ContentValues[] apiaryUserContentValues = ApiaryUser.getContentValuesArray(apiary);
        int inserted = 0;
        if (apiaryUserContentValues.length > 0)
            inserted = context.getContentResolver().bulkInsert(BeeContract.ApiaryUserEntry
                    .CONTENT_URI, apiaryUserContentValues);
        Log.e(LOG_TAG, "SyncDB Complete. " + inserted + " Inserted");
    }

    private static ContentValues[] getContentValuesArray(Apiary apiary) {
        ContentValues[] values = new ContentValues[apiary.usersIds.size()];
        int i = 0;
        for (Long userId : apiary.usersIds) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(BeeContract.ApiaryUserEntry.COLUMN_USER_ID, userId);
            contentValues.put(BeeContract.ApiaryUserEntry.COLUMN_APIARY_ID, apiary.id);
            values[i] = contentValues;
            i++;
        }
        return values;
    }


    public static void resetDB(Context context) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        context.getContentResolver().delete(BeeContract.ApiaryUserEntry
                .CONTENT_URI, null, null);
    }
}
