package com.example.android.openbeelab;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.android.openbeelab.db.BeeContract;
import com.example.android.openbeelab.db.BeeDbHelper;

/**
 * Created by Elorri on 08/12/2015.
 */
public class TestProvider extends AndroidTestCase {


    /*
This helper function deletes all records from database tables using the database
functions only.  This is designed to be used to reset the state of the database until the
delete functionality is available in the ContentProvider.
*/
    public void deleteAllRecordsFromDB() {
        BeeDbHelper dbHelper = new BeeDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(BeeContract.UserEntry.TABLE_NAME, null, null);
        db.delete(BeeContract.BeehouseEntry.TABLE_NAME, null, null);
        db.delete(BeeContract.MeasureEntry.TABLE_NAME, null, null);
        db.close();
    }

    /*
        Student: Refactor this function to use the deleteAllRecordsFromProvider functionality once
        you have implemented delete functionality there.
     */
    public void deleteAllRecords() {
        deleteAllRecordsFromDB();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }




    public void testUserBulkInsert() {

        ContentValues[] bulkInsertContentValues = TestUtilities.createListUserWithoutIdContentValues();

        // Register a content observer for our bulk insert.
        TestUtilities.TestContentObserver userObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(BeeContract.UserEntry.CONTENT_URI, true, userObserver);

        int insertCount = mContext.getContentResolver().bulkInsert(BeeContract.UserEntry.CONTENT_URI,
                bulkInsertContentValues);

        // Students:  If this fails, it means that you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in your BulkInsert
        // ContentProvider method.
        userObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(userObserver);

        assertEquals(insertCount, TestUtilities.BULK_INSERT_RECORDS_TO_INSERT);

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                BeeContract.UserEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), TestUtilities.BULK_INSERT_RECORDS_TO_INSERT);

        // and let's make sure they match the ones we created
        cursor.moveToFirst();
        for ( int i = 0; i < TestUtilities.BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext() ) {
            TestUtilities.validateCurrentRecord(" Error " + i, cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }
}
