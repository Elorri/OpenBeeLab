package com.example.android.openbeelab.pojo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.android.openbeelab.db.BeeContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elorri on 20/12/2015.
 */
public class Apiary {
    private static final String LOG_TAG = Apiary.class.getSimpleName();
    public long id; //can be null
    public final String jsonId;
    public final String name;
    public List<Long> usersIds;
    private final String[] usersJsonIds;


    public Apiary(long id, String jsonId, String name, String[] usersJsonIds) {
        this.id = id;
        this.jsonId = jsonId;
        this.name = name;
        this.usersJsonIds = usersJsonIds;
    }

    public Apiary(String jsonId, String name, String[] usersJsonIds) {
        this.jsonId = jsonId;
        this.name = name;
        this.usersJsonIds = usersJsonIds;
    }

    public static void syncDB(Context context, List<Apiary> apiaries) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        ContentValues[] apiariesContentValues = Apiary.getContentValuesArray(apiaries);
        int inserted = 0;
        if (apiariesContentValues.length > 0)
            inserted = context.getContentResolver().bulkInsert(BeeContract.ApiaryEntry
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
        contentValues.put(BeeContract.ApiaryEntry.COLUMN_JSON_ID, this.jsonId);
        contentValues.put(BeeContract.ApiaryEntry.COLUMN_NAME, this.name);
        return contentValues;
    }

    public static void resetDB(Context context) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        context.getContentResolver().delete(BeeContract.ApiaryEntry
                .CONTENT_URI, null, null);
    }

    public static List<Apiary> fetchUsersIds(Context context, List<Apiary> apiaries) {
        for (Apiary apiary : apiaries) {
            List<Long> ids = new ArrayList<>();
            for (String jsonId : apiary.usersJsonIds) {
                Long userId = User.findIdByJsonId(context, jsonId);
                if (userId != null)
                    ids.add(userId);
            }
            apiary.usersIds = ids;
        }
        return apiaries;
    }


    public static List<Apiary> fetchIds(Context context, List<Apiary> apiaries) {
        for (Apiary apiary : apiaries) {
            apiary.id = findIdByJsonId(context, apiary.jsonId);
        }
        return apiaries;
    }

    public static Long findIdByJsonId(Context context, String jsonApiaryId) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2]
                + "jsonApiaryId " + jsonApiaryId);
        final int COLUMN_ID = 0;
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2]
                + "BeeContract.ApiaryEntry.CONTENT_URI " + BeeContract.ApiaryEntry.CONTENT_URI);
        Cursor cursor = context.getContentResolver().query(
                BeeContract.ApiaryEntry.CONTENT_URI,
                new String[]{BeeContract.ApiaryEntry._ID},
                BeeContract.ApiaryEntry.COLUMN_JSON_ID + "=?",
                new String[]{jsonApiaryId},
                null
        );

        if (cursor.moveToNext())
            return cursor.getLong(COLUMN_ID);
        else
            return null; //Case where an jsonApiaryId ex 'apiary:rucher_004' is found in json
            // beehouse details, but not in apiary list.
        //TODO add apiary  apiary:rucher_004 in json lamine apiary list

    }
}
