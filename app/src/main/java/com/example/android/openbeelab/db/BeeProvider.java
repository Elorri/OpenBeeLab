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

    //will match content://com.example.android.openbeelab/apiary/
    public static final int APIARY = 200;

    //will match content://com.example.android.openbeelab/{userDb}/unknown/apiary/
    public static final int USER_APIARIES_UNKNOWN = 201;

    //will match content://com.example.android.openbeelab/{userDb}/{userId}/apiary/
    public static final int USER_APIARIES = 202;

    //will match content://com.example.android.openbeelab/beehouse/
    public static final int BEEHOUSE = 300;

    //will match content://com.example.android.openbeelab/{userDb}/unknown/{apiaryId}/beehouse/
    public static final int USER_BEEHOUSES_UNKNOWN = 301;

    //will match content://com.example.android.openbeelab/{userDb}/{userId}/{apiaryId}/beehouse/
    public static final int USER_BEEHOUSES_BY_APIARY = 302;

    //will match content://com.example.android.openbeelab/{userDb}/{userId}/beehouse/
    public static final int USER_BEEHOUSES = 303;

    //will match /{userDb}/{userId}/{beehouseId}/beehouse_view/
    public static final int BEEHOUSE_VIEW = 304;

    //will match content://com.example.android.openbeelab/{userDb}/beehouse/
    public static final int BEEHOUSE_BY_DATABASE = 305;


    //will match content://com.example.android.openbeelab/measure/
    public static final int MEASURE = 400;

    //will match /{userDb}/{userId}/{beehouseId}/measure/weight_over_period
    public static final int WEIGHT_OVER_PERIOD = 401;


    //will match content://com.example.android.openbeelab/apiary_user/
    public static final int APIARY_USER = 500;

    public static final String UNKNOWN = "unknown";

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BeeContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, BeeContract.PATH_USER, USER);
        matcher.addURI(authority, BeeContract.PATH_APIARY, APIARY);
        matcher.addURI(authority, BeeContract.PATH_APIARY_USER, APIARY_USER);
        matcher.addURI(authority, BeeContract.PATH_BEEHOUSE, BEEHOUSE);
        matcher.addURI(authority, BeeContract.PATH_MEASURE, MEASURE);
        matcher.addURI(authority, "*/#/#/" + BeeContract.PATH_BEEHOUSE_VIEW, BEEHOUSE_VIEW);
        matcher.addURI(authority, "*/#/#/" + BeeContract.PATH_MEASURE + "/" + BeeContract.PATH_WEIGHT_OVER_PERIOD, WEIGHT_OVER_PERIOD);
        matcher.addURI(authority, "*/#/#/" + BeeContract.PATH_BEEHOUSE, USER_BEEHOUSES_BY_APIARY);
        matcher.addURI(authority, "*/#/" + BeeContract.PATH_APIARY, USER_APIARIES);
        matcher.addURI(authority, "*/#/" + BeeContract.PATH_BEEHOUSE, USER_BEEHOUSES);
        matcher.addURI(authority, "*/" + UNKNOWN + "/" + BeeContract.PATH_APIARY, USER_APIARIES_UNKNOWN);
        matcher.addURI(authority, "*/" + UNKNOWN + "/#/" + BeeContract.PATH_BEEHOUSE, USER_BEEHOUSES_UNKNOWN);
        matcher.addURI(authority, "*/" + BeeContract.PATH_BEEHOUSE, BEEHOUSE_BY_DATABASE);


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
            case USER_APIARIES_UNKNOWN: {
                Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] +
                        "USER_APIARIES_UNKNOWN");
                MatrixCursor cursorEmpty = null;
                String[] cursorCursorEmpty_columns = {"_id", "first_column"};
                cursorEmpty = new MatrixCursor(cursorCursorEmpty_columns);
                retCursor = cursorEmpty;
                break;
            }
            case USER_APIARIES: {
                Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] +
                        "USER_APIARIES");
                String userId = BeeContract.ApiaryEntry.getUserIdFromApiariesViewUri(uri);
                String database = BeeContract.ApiaryEntry.getDatabaseFromApiariesViewUri(uri);
                Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "" + userId);
                retCursor = mOpenHelper.getReadableDatabase().query(BeeContract.ApiaryEntry.TABLE_NAME + " inner join "
                                + BeeContract.ApiaryUserEntry.TABLE_NAME + " on "
                                + BeeContract.ApiaryEntry.TABLE_NAME + "."
                                + BeeContract.ApiaryEntry._ID + " = "
                                + BeeContract.ApiaryUserEntry.TABLE_NAME + "."
                                + BeeContract.ApiaryUserEntry.COLUMN_APIARY_ID,
                        projection,
                        BeeContract.ApiaryUserEntry.COLUMN_USER_ID + "=? and "
                                + BeeContract.ApiaryEntry.COLUMN_DATABASE + "=?",
                        new String[]{userId, database},
                        null,
                        null,
                        BeeContract.ApiaryEntry.COLUMN_NAME + " asc"
                );
                break;
            }

            case USER_BEEHOUSES_UNKNOWN: {
                Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] +
                        "USER_BEEHOUSES_UNKNOWN");
                MatrixCursor cursorEmpty = null;
                String[] cursorCursorEmpty_columns = {"_id", "first_column"};
                cursorEmpty = new MatrixCursor(cursorCursorEmpty_columns);
                retCursor = cursorEmpty;
                break;
            }
            case USER_BEEHOUSES_BY_APIARY: {
                Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] +
                        "USER_BEEHOUSES_BY_APIARY");
                String apiaryId = BeeContract.BeehouseEntry.getApiaryIdFromBeehousesViewUri(uri);
                String database = BeeContract.BeehouseEntry.getDatabaseFromBeehousesViewUri(uri);
//                retCursor = mOpenHelper.getReadableDatabase().query(
//                        BeeContract.BeehouseEntry.TABLE_NAME,
//                        projection,
//                        BeeContract.BeehouseEntry.COLUMN_APIARY_ID + "=? and "
//                                + BeeContract.BeehouseEntry.COLUMN_DATABASE + "=?",
//                        new String[]{apiaryId, database},
//                        null,
//                        null,
//                        BeeContract.BeehouseEntry.COLUMN_NAME + " asc"
//                );


                retCursor = mOpenHelper.getReadableDatabase().rawQuery(
                        "select * from "
                                + BeeContract.BeehouseEntry.TABLE_NAME + " inner join (select "
                                + BeeContract.MeasureEntry.COLUMN_BEEHOUSE_ID + ", max("
                                + BeeContract.MeasureEntry.COLUMN_WEEK_ID + ")as "
                                + BeeContract.MeasureEntry.COLUMN_WEEK_ID + ", "
                                + BeeContract.BeehouseEntry.VIEW_CURRENT_WEIGHT + " as "
                                + BeeContract.BeehouseEntry.VIEW_CURRENT_WEIGHT + " from(select b."
                                + BeeContract.BeehouseEntry._ID + ", "
                                + BeeContract.MeasureEntry.COLUMN_BEEHOUSE_ID + ", "
                                + BeeContract.MeasureEntry.COLUMN_WEEK_ID + ", "
                                + BeeContract.MeasureEntry.COLUMN_VALUE + " as "
                                + BeeContract.BeehouseEntry.VIEW_CURRENT_WEIGHT + " from "
                                + BeeContract.MeasureEntry.TABLE_NAME + "  inner join(select  "
                                + BeeContract.BeehouseEntry.TABLE_NAME + ".* from "
                                + BeeContract.BeehouseEntry.TABLE_NAME + " where "
                                + BeeContract.BeehouseEntry.COLUMN_APIARY_ID + " =? and "
                                + BeeContract.BeehouseEntry.COLUMN_DATABASE + " =?) b on "
                                + BeeContract.MeasureEntry.COLUMN_BEEHOUSE_ID + " =b."
                                + BeeContract.BeehouseEntry._ID + ") group by "
                                + BeeContract.MeasureEntry.COLUMN_BEEHOUSE_ID + ") w on "
                                + BeeContract.BeehouseEntry._ID + "=w."
                                + BeeContract.MeasureEntry.COLUMN_BEEHOUSE_ID,
                        new String[]{apiaryId, database}
                );
//                select * from beehouse inner join (select beehouse_id, max(weekId) as weekId, weight as weight from (select b._id, beehouse_id, weekId, value as weight from measure inner join (select beehouse.* from beehouse where apiary_id=9 and database='la_mine') b on beehouse_id=b._id) group by beehouse_id) w on beehouse._id=w.beehouse_id

                break;
            }
            case USER_BEEHOUSES: {
                Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] +
                        "USER_BEEHOUSES");
                String userId = BeeContract.BeehouseEntry.getUserIdFromBeehousesViewUri(uri);
                Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "" + userId);
                retCursor = mOpenHelper.getReadableDatabase().rawQuery("Select 89 as "
                                + BeeContract.BeehouseEntry.VIEW_CURRENT_WEIGHT + " from "
                                + BeeContract.BeehouseEntry.TABLE_NAME + " inner join "
                                + BeeContract.ApiaryEntry.TABLE_NAME + " on "
                                + BeeContract.BeehouseEntry.TABLE_NAME + "."
                                + BeeContract.BeehouseEntry.COLUMN_APIARY_ID + " = "
                                + BeeContract.ApiaryEntry.TABLE_NAME + "."
                                + BeeContract.ApiaryEntry._ID,
                        //TODO remove this comment
                        //           +" where "+BeeContract.ApiaryEntry.COLUMN_USER_ID + "=? ",
                        new String[]{userId});
                break;
            }

            case BEEHOUSE_VIEW: {
                Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] +
                        "BEEHOUSE_VIEW");
                MatrixCursor cursorEmpty = null;
                String[] cursorCursorEmpty_columns = {"_id", "first_column"};
                cursorEmpty = new MatrixCursor(cursorCursorEmpty_columns);
                cursorEmpty.addRow(new Object[]{1, "dummy"});
                cursorEmpty.addRow(new Object[]{2, "dummy"});
                retCursor = cursorEmpty;
                break;
            }

            case BEEHOUSE_BY_DATABASE: {
                Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] +
                        "BEEHOUSE_BY_DATABASE");
                String database = BeeContract.BeehouseEntry.getDatabaseFromBeehousesByDatabaseViewUri(uri);
                Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "" + database);

                retCursor = mOpenHelper.getReadableDatabase().rawQuery("Select "
                                + BeeContract.BeehouseEntry._ID + ","
                                + BeeContract.BeehouseEntry.COLUMN_JSON_ID + ","
                                + BeeContract.BeehouseEntry.COLUMN_NAME + ","
                                + BeeContract.BeehouseEntry.TABLE_NAME + "."
                                + BeeContract.BeehouseEntry.COLUMN_APIARY_ID + ","
                                + BeeContract.BeehouseEntry.COLUMN_JSON_APIARY_ID + " from "
                                + BeeContract.BeehouseEntry.TABLE_NAME + " inner join (select "
                                + BeeContract.ApiaryUserEntry.COLUMN_APIARY_ID + " from "
                                + BeeContract.ApiaryUserEntry.TABLE_NAME + " inner join (select "
                                + BeeContract.UserEntry._ID + " from "
                                + BeeContract.UserEntry.TABLE_NAME + " where  "
                                + BeeContract.UserEntry.COLUMN_DATABASE + "=?) u on "
                                + BeeContract.ApiaryUserEntry.TABLE_NAME + "."
                                + BeeContract.ApiaryUserEntry.COLUMN_USER_ID + "= u."
                                + BeeContract.UserEntry._ID + ") a on "
                                + BeeContract.BeehouseEntry.TABLE_NAME + "."
                                + BeeContract.BeehouseEntry.COLUMN_APIARY_ID + "= a."
                                + BeeContract.ApiaryUserEntry.COLUMN_APIARY_ID,
                        new String[]{database});
                break;
            }

            case WEIGHT_OVER_PERIOD: {
                Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] +
                        "WEIGHT_OVER_PERIOD");
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
                Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] +
                        "USER");
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
            case APIARY: {
                Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "APIARY");
                retCursor = mOpenHelper.getReadableDatabase().query(
                        BeeContract.ApiaryEntry.TABLE_NAME,
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
                Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "BEEHOUSE");
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
                Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "MEASURE");
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
            case APIARY:
                rowsDeleted = db.delete(
                        BeeContract.ApiaryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case USER:
                rowsDeleted = db.delete(
                        BeeContract.UserEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case APIARY_USER:
                rowsDeleted = db.delete(
                        BeeContract.ApiaryUserEntry.TABLE_NAME, selection, selectionArgs);
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
            case APIARY:
                returnCount = insertInBulk(BeeContract.ApiaryEntry.TABLE_NAME, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case APIARY_USER:
                returnCount = insertInBulk(BeeContract.ApiaryUserEntry.TABLE_NAME, values);
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
