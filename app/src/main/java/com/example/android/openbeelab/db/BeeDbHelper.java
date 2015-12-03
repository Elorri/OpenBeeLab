package com.example.android.openbeelab.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Elorri on 02/12/2015.
 */
public class BeeDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "openbeelab.db";


    public BeeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FOOD_TABLE = "CREATE TABLE " + BeeContract.MeasureEntry.TABLE_NAME +
                "(" +BeeContract.MeasureEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +BeeContract.MeasureEntry.COLUMN_NAME+" TEXT(50) NOT NULL, "
                +BeeContract.MeasureEntry.COLUMN_TIMESTAMP+" TEXT(50), "
                +BeeContract.MeasureEntry.COLUMN_WEEK_ID +" TEXT(10) NOT NULL, "
                +BeeContract.MeasureEntry.COLUMN_VALUE+" REAL NOT NULL, "
                +BeeContract.MeasureEntry.COLUMN_UNIT+" TEXT(10) NOT NULL)";
        db.execSQL(SQL_CREATE_FOOD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BeeContract.MeasureEntry.TABLE_NAME);
        onCreate(db);
    }
}
