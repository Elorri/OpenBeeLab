package com.example.android.openbeelab.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.openbeelab.Utility;
import com.example.android.openbeelab.sync.BeeSyncAdapter;

/**
 * Created by Elorri on 02/12/2015.
 */
public class BeeDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 7;
    static final String DATABASE_NAME = "openbeelab.db";
    private Context context;


    public BeeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + BeeContract.UserEntry.TABLE_NAME +
                "(" +BeeContract.UserEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +BeeContract.UserEntry.COLUMN_NAME+" TEXT(50) NOT NULL, "
                +BeeContract.UserEntry.COLUMN_JSON_ID +" TEXT(50) NOT NULL, "
                +BeeContract.UserEntry.COLUMN_DATABASE+" TEXT(50) NOT NULL)";

        final String SQL_CREATE_APIARY_USER_TABLE = "CREATE TABLE " + BeeContract.ApiaryUserEntry
                .TABLE_NAME +
                "(" +BeeContract.ApiaryUserEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +BeeContract.ApiaryUserEntry.COLUMN_USER_ID+" INTEGER NOT NULL, "
                +BeeContract.ApiaryUserEntry.COLUMN_APIARY_ID+" INTEGER NOT NULL)";

        final String SQL_CREATE_APIARY_TABLE = "CREATE TABLE " + BeeContract.ApiaryEntry
                .TABLE_NAME +
                "(" +BeeContract.ApiaryEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +BeeContract.ApiaryEntry.COLUMN_JSON_ID +" TEXT(50) NOT NULL, "
                +BeeContract.ApiaryEntry.COLUMN_NAME+" TEXT(50) NOT NULL,"
                +"UNIQUE (" + BeeContract.ApiaryEntry.COLUMN_JSON_ID + ", " +
                BeeContract.ApiaryEntry.COLUMN_NAME + ") ON CONFLICT REPLACE)";


        final String SQL_CREATE_BEEHOUSE_TABLE = "CREATE TABLE " + BeeContract.BeehouseEntry
                .TABLE_NAME +
                "(" +BeeContract.BeehouseEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +BeeContract.BeehouseEntry.COLUMN_JSON_ID +" TEXT(50) NOT NULL, "
                +BeeContract.BeehouseEntry.COLUMN_NAME+" TEXT(50) NOT NULL, "
                //TODO uncomment this line
                //+BeeContract.BeehouseEntry.COLUMN_APIARY_ID +" INTEGER NOT NULL, "
                +BeeContract.BeehouseEntry.COLUMN_APIARY_ID +" INTEGER, "
                +BeeContract.BeehouseEntry.COLUMN_JSON_APIARY_ID +" TEXT(50))";
        
        
        final String SQL_CREATE_MEASURE_TABLE = "CREATE TABLE " + BeeContract.MeasureEntry.TABLE_NAME +
                "(" +BeeContract.MeasureEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +BeeContract.MeasureEntry.COLUMN_NAME+" TEXT(50) NOT NULL, "
                +BeeContract.MeasureEntry.COLUMN_TIMESTAMP+" TEXT(50), "
                +BeeContract.MeasureEntry.COLUMN_WEEK_ID +" TEXT(10) NOT NULL, "
                +BeeContract.MeasureEntry.COLUMN_VALUE+" REAL NOT NULL, "
                +BeeContract.MeasureEntry.COLUMN_UNIT+" TEXT(10) NOT NULL, "
                +BeeContract.MeasureEntry.COLUMN_BEEHOUSE_ID+" INTEGER NOT NULL)";

        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_APIARY_TABLE);
        db.execSQL(SQL_CREATE_APIARY_USER_TABLE);
        db.execSQL(SQL_CREATE_BEEHOUSE_TABLE);
        db.execSQL(SQL_CREATE_MEASURE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BeeContract.UserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BeeContract.ApiaryEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BeeContract.ApiaryUserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BeeContract.BeehouseEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BeeContract.MeasureEntry.TABLE_NAME);
        Utility.setUserStatus(context, BeeSyncAdapter.STATUS_USERS_UNKNOWN );
        onCreate(db);
    }


}
