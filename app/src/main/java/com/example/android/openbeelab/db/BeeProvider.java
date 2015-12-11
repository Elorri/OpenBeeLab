package com.example.android.openbeelab.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Elorri on 02/12/2015.
 */
public class BeeProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private BeeDbHelper mOpenHelper;

    //will match content://com.example.android.openbeelab/user/
    public static final int USER = 100;

    //will match content://com.example.android.openbeelab/beehouse/
    public static final int BEEHOUSE = 200;

    //will match content://com.example.android.openbeelab/{userDb}/unknown/beehouse/
    public static final int USER_BEEHOUSES_UNKNOWN = 201;

    //will match content://com.example.android.openbeelab/{userDb}/{userId}/beehouse/
    public static final int USER_BEEHOUSES = 202;

    //will match /{userDb}/{userId}/{beehouseId}/beehouse_view/
    public static final int BEEHOUSE_VIEW = 203;


    //will match content://com.example.android.openbeelab/measure/
    public static final int MEASURE = 300;

    //will match /{userDb}/{userId}/{beehouseId}/measure/weight_over_period
    public static final int WEIGHT_OVER_PERIOD = 301;

    public static final String UNKNOWN = "unknown";

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BeeContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, BeeContract.PATH_USER, USER);
        matcher.addURI(authority, BeeContract.PATH_BEEHOUSE, BEEHOUSE);
        matcher.addURI(authority, BeeContract.PATH_MEASURE, MEASURE);
        matcher.addURI(authority, "*/#/#/" + BeeContract.PATH_BEEHOUSE_VIEW, BEEHOUSE_VIEW);
        matcher.addURI(authority, "*/#/#/" + BeeContract.PATH_MEASURE + "/" + BeeContract
                .PATH_WEIGHT_OVER_PERIOD, WEIGHT_OVER_PERIOD);
        matcher.addURI(authority, "*/" + UNKNOWN + "/" + BeeContract.PATH_BEEHOUSE, USER_BEEHOUSES_UNKNOWN);
        matcher.addURI(authority, "*/#/" + BeeContract.PATH_BEEHOUSE, USER_BEEHOUSES);


        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new BeeDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "" + uri.toString());
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case USER_BEEHOUSES_UNKNOWN: {
                MatrixCursor cursorEmpty = null;
                String[] cursorCursorEmpty_columns = {"_id", "first_column"};
                cursorEmpty = new MatrixCursor(cursorCursorEmpty_columns);
                retCursor = cursorEmpty;
                break;
            }
            case USER_BEEHOUSES: {
                String userId = BeeContract.BeehouseEntry.getUserIdFromBeehousesViewUri(uri);
                Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "" + userId);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        BeeContract.BeehouseEntry.TABLE_NAME,
                        projection,
                        BeeContract.BeehouseEntry.COLUMN_USER_ID + "=?",
                        new String[]{userId},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case BEEHOUSE_VIEW: {
                MatrixCursor cursorEmpty = null;
                String[] cursorCursorEmpty_columns = {"_id", "first_column"};
                cursorEmpty = new MatrixCursor(cursorCursorEmpty_columns);
                cursorEmpty.addRow(new Object[]{1, "dummy"});
                cursorEmpty.addRow(new Object[]{2, "dummy"});
                retCursor = cursorEmpty;
                break;
            }

            case WEIGHT_OVER_PERIOD: {
                String beehouseId = BeeContract.MeasureEntry
                        .getBeehouseIdFromWeightOverPeriodViewUri(uri);
                Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "" + beehouseId);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        BeeContract.MeasureEntry.TABLE_NAME,
                        projection,
                        BeeContract.MeasureEntry.COLUMN_BEEHOUSE_ID + "=?",
                        new String[]{beehouseId},
                        null,
                        null,
                        sortOrder);
                break;
            }
            case USER: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        BeeContract.UserEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case BEEHOUSE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        BeeContract.BeehouseEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MEASURE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        BeeContract.MeasureEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case MEASURE:
                return BeeContract.MeasureEntry.CONTENT_TYPE;
            case WEIGHT_OVER_PERIOD:
                return BeeContract.MeasureEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case MEASURE: {
                long _id = db.insert(BeeContract.MeasureEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = BeeContract.MeasureEntry.buildMeasureUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "" + uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case USER:
                rowsDeleted = db.delete(
                        BeeContract.UserEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BEEHOUSE:
                rowsDeleted = db.delete(
                        BeeContract.BeehouseEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MEASURE:
                rowsDeleted = db.delete(
                        BeeContract.MeasureEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MEASURE:
                rowsUpdated = db.update(BeeContract.MeasureEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;
        switch (match) {
            case USER:
                returnCount = insertInBulk(BeeContract.UserEntry.TABLE_NAME, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case BEEHOUSE:
                returnCount = insertInBulk(BeeContract.BeehouseEntry.TABLE_NAME, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case MEASURE:
                returnCount = insertInBulk(BeeContract.MeasureEntry.TABLE_NAME, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }


    private int insertInBulk(String tableName, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : values) {
                long _id = db.insert(tableName, null, value);
                Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "_id " + _id);
                if (_id != -1) {
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
            return returnCount;
        } finally {
            db.endTransaction();
        }
    }
}
