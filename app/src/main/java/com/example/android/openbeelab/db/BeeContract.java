package com.example.android.openbeelab.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Elorri on 02/12/2015.
 */
public class BeeContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.openbeelab";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_USER = "user";
    public static final String PATH_BEEHOUSE = "beehouse";
    public static final String PATH_WEIGHT_OVER_PERIOD = "weight_over_period";
    public static final String PATH_MEASURE = "measure";



    public static final class UserEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER)
                .build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        public static final String TABLE_NAME = "user";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DATABASE = "database";
    }


    public static final class BeehouseEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_BEEHOUSE)
                .build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BEEHOUSE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BEEHOUSE;

        public static final String TABLE_NAME = "behouse";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_APIARY_NAME = "apiary_name";
        public static final String COLUMN_CURRENT_WEIGHT = "current_weight";


        public static Uri buildBeehousesViewUri(String userDB, String userId) {
            return BASE_CONTENT_URI.buildUpon()
                    .appendPath(userDB)
                    .appendPath(userId)
                    .appendPath(PATH_BEEHOUSE)
                    .build();
        }

//        public static String getUserIdFromBeehousesViewUri(Uri uri) {
//            return uri.getPathSegments().get(1);
//        }
    }


    public static final class MeasureEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEASURE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEASURE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEASURE;

        public static final String TABLE_NAME = "measure";

        public static final String COLUMN_NAME = "name"; //e.g. "global weight"
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_WEEK_ID = "weekId";//e.g. "2014W29"
        public static final String COLUMN_VALUE = "value"; //e.g. "45.0"
        public static final String COLUMN_UNIT = "unit"; //e.g. "Kg"
        public static final String COLUMN_BEEHOUSE_ID = "beehouse_id"; //e.g. "89"




        public static Uri buildMeasureUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildWeightOverPeriodViewUri(String userDb, String userId, String
                beehouseId) {
            return BASE_CONTENT_URI.buildUpon()
                    .appendPath(userDb)
                    .appendPath(userId)
                    .appendPath(beehouseId)
                    .appendPath(PATH_MEASURE)
                    .appendPath(PATH_WEIGHT_OVER_PERIOD)
                    .build();
        }
    }





}
