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
public class User {
    public long id; //can be null
    public final String name;
    public final String database;
    private static String LOG_TAG=User.class.getSimpleName();

    public User(long id, String name, String database) {
        this.id = id;
        this.name = name;
        this.database = database;
    }

    public User(String name, String database) {
        this.name = name;
        this.database = database;
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BeeContract.UserEntry.COLUMN_NAME, this.name);
        contentValues.put(BeeContract.UserEntry.COLUMN_DATABASE, this.database);
        return contentValues;
    }

    public static ContentValues[] getContentValuesArray(List<User> users) {
        ContentValues[] values = new ContentValues[users.size()];
        int i = 0;
        for (User user : users) {
            values[i] = user.toContentValues();
            i++;
        }
        return values;
    }

    public static void resetDB(Context context){
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        context.getContentResolver().delete(BeeContract.UserEntry
                .CONTENT_URI, null, null);
    }

    public static void syncDB(Context context, List<User> users) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        ContentValues[] usersContentValues = User.getContentValuesArray(users);
        int inserted = 0;
        if (usersContentValues.length > 0)
            inserted = context.getContentResolver().bulkInsert(BeeContract.UserEntry
                    .CONTENT_URI, usersContentValues);
        Log.e(LOG_TAG, "SyncDB Complete. " + inserted + " Inserted");
    }


    /**
     * Retrieve a list of User object from a cursor that contains the following columns. Order
     * is important.
     * 0 : COLUMN_ID
     * 1 : COLUMN_NAME
     * 2 : COLUMN_DATABASE
     *
     * @param cursor
     * @return List<User>
     */
    public static List<User> getUsers(Cursor cursor) {
        final int COLUMN_ID = 0;
        final int COLUMN_NAME = 1;
        final int COLUMN_DATABASE = 2;


        List<User> users = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            long id = Long.valueOf(cursor.getString(COLUMN_ID));
            Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "id "+id);
            int _id=cursor.getColumnIndex(BeeContract.UserEntry._ID);
            id=cursor.getLong(_id);
            Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "_id "+_id + "id "+id);
            String name = cursor.getString(COLUMN_NAME);
            Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "name "+name);
            String database = cursor.getString(COLUMN_DATABASE);
            Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "database "+database);
            users.add(new User(id, name, database));
        }
        return users;
    }

    /**
     * Retrieve an array of option label for the listPreference settings view from a
     * cursor  that contains the following columns. Order is important.
     * 0 : COLUMN_ID
     * 1 : COLUMN_NAME
     *
     * @param cursor
     * @return List<User>
     */
    public static String[] toCharSequenceOptionLabel(Cursor cursor) {
        final int COL_USER_ID = 0;
        final int COL_USER_NAME = 1;


        String[] optionsLabel = new String[cursor.getCount()];
        int i=0;
        while (cursor.moveToNext()) {
            optionsLabel[i]=cursor.getString(COL_USER_NAME);
            Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "optionsValue "+cursor.getString(COL_USER_NAME));
            i++;
        }
        return optionsLabel;
    }

    /**
     * Retrieve an array of option label for the listPreference settings view from a
     * cursor  that contains the following columns. Order is important.
     * 0 : COLUMN_ID
     * 1 : COLUMN_NAME
     *
     * @param cursor
     * @return List<User>
     */
    public static String[] toCharSequenceOptionValue(Cursor cursor) {
        final int COL_USER_ID = 0;
        final int COL_USER_NAME = 1;
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "getCount "+cursor.getCount());


        String[] optionsValue = new String[cursor.getCount()];
        int i=0;
        while (cursor.moveToNext()) {
            optionsValue[i]=cursor.getString(COL_USER_ID);
            Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "optionsValue "+cursor.getString(COL_USER_ID));
            i++;
        }
        return optionsValue;
    }


    
}
