package com.example.android.openbeelab;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.android.openbeelab.db.BeeContract;
import com.example.android.openbeelab.pojo.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Elorri on 08/12/2015.
 */
public class TestUtilities extends AndroidTestCase {

    private static final String[] USERS_COLUMNS = {
            BeeContract.UserEntry._ID,
            BeeContract.UserEntry.COLUMN_NAME,
            BeeContract.UserEntry.COLUMN_DATABASE
    };



    private static final String[] BEEHOUSES_COLUMNS = {
            BeeContract.BeehouseEntry._ID,
            BeeContract.BeehouseEntry.COLUMN_NAME,
            BeeContract.BeehouseEntry.COLUMN_USER_ID,
            BeeContract.BeehouseEntry.COLUMN_APIARY_NAME,
            BeeContract.BeehouseEntry.COLUMN_CURRENT_WEIGHT
    };


    private static final String[] MEASURES_COLUMNS = {
            BeeContract.MeasureEntry._ID,
            BeeContract.MeasureEntry.COLUMN_NAME,
            BeeContract.MeasureEntry.COLUMN_TIMESTAMP,
            BeeContract.MeasureEntry.COLUMN_WEEK_ID,
            BeeContract.MeasureEntry.COLUMN_VALUE,
            BeeContract.MeasureEntry.COLUMN_UNIT,
            BeeContract.MeasureEntry.COLUMN_BEEHOUSE_ID
    };




    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }








public static final int BULK_INSERT_RECORDS_TO_INSERT=3;
    public static ContentValues[] createListUserWithoutIdContentValues() {
        List<User> users=new ArrayList<>(3);
        users.add(new User("Pierre","la_mine"));
        users.add(new User("Pierre","la_mine_dev"));
        users.add(new User("Fred","fred_db"));
        return User.getContentValuesArray(users);
    }

    
    
    
    
    public void testShowUserTable() {
        // Test the sort_by content provider query
        Cursor cursor = mContext.getContentResolver().query(
                BeeContract.UserEntry.CONTENT_URI,
                USERS_COLUMNS,
                null,
                null,
                null
        );
        int i = 0;
        while (cursor.moveToNext()) {
            Log.e("Lifecycle", i + ("$"
                    + cursor.getString(cursor.getColumnIndex(BeeContract.UserEntry._ID)) + "|"
                    + cursor.getString(cursor.getColumnIndex(BeeContract.UserEntry.COLUMN_NAME)) + "|"
                    + cursor.getString(cursor.getColumnIndex(BeeContract.UserEntry.COLUMN_DATABASE))));
            i++;
        }
        Log.e("Lifecycle", i + "record displayed");
    }

    public void testShowBeehouseTable() {
        // Test the sort_by content provider query
        Cursor cursor = mContext.getContentResolver().query(
                BeeContract.BeehouseEntry.CONTENT_URI,
                BEEHOUSES_COLUMNS,
                null,
                null,
                null
        );
        int i = 0;
        while (cursor.moveToNext()) {
            Log.e("Lifecycle", i + ("$" 
                    + cursor.getString(cursor.getColumnIndex(BeeContract.BeehouseEntry._ID)) + "|" 
                    + cursor.getString(cursor.getColumnIndex(BeeContract.BeehouseEntry.COLUMN_NAME)) + "|"
                    + cursor.getString(cursor.getColumnIndex(BeeContract.BeehouseEntry.COLUMN_USER_ID)) + "|"
                    + cursor.getString(cursor.getColumnIndex(BeeContract.BeehouseEntry.COLUMN_APIARY_NAME)) + "|"
                    + cursor.getString(cursor.getColumnIndex(BeeContract.BeehouseEntry.COLUMN_CURRENT_WEIGHT))));
            i++;
        }
        Log.e("Lifecycle", i + "record displayed");
    }


    public void testShowMeasureTable() {
        // Test the sort_by content provider query
        Cursor cursor = mContext.getContentResolver().query(
                BeeContract.MeasureEntry.CONTENT_URI,
                MEASURES_COLUMNS,
                null,
                null,
                null
        );
        int i = 0;
        while (cursor.moveToNext()) {
            Log.e("Lifecycle", i + ("$"
                    + cursor.getString(cursor.getColumnIndex(BeeContract.MeasureEntry._ID)) + "|"
                    + cursor.getString(cursor.getColumnIndex(BeeContract.MeasureEntry.COLUMN_NAME)) + "|"
                    + cursor.getString(cursor.getColumnIndex(BeeContract.MeasureEntry.COLUMN_TIMESTAMP)) + "|"
                    + cursor.getString(cursor.getColumnIndex(BeeContract.MeasureEntry.COLUMN_WEEK_ID)) + "|"
                    + cursor.getString(cursor.getColumnIndex(BeeContract.MeasureEntry.COLUMN_NAME)) + "|"
                    + cursor.getString(cursor.getColumnIndex(BeeContract.MeasureEntry.COLUMN_VALUE)) + "|"
                    + cursor.getString(cursor.getColumnIndex(BeeContract.MeasureEntry.COLUMN_UNIT)) + "|"
                    + cursor.getString(cursor.getColumnIndex(BeeContract.MeasureEntry.COLUMN_BEEHOUSE_ID))));
            i++;
        }
        Log.e("Lifecycle", i + "record displayed");
    }


    /*
    Students: The functions we provide inside of TestProvider use this utility class to test
    the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
    CTS tests.
    Note that this only tests that the onChange function is called; it does not test that the
    correct Uri is returned.
 */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
