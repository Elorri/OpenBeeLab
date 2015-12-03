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


    public static final String PATH_APIARY = "apiary";
    private static final String PATH_BEEHOUSE = "beehouse";
    public static final String PATH_WEIGHT_OVER_PERIOD = "weight_over_period";
    public static final String PATH_MEASURE = "measure";


    public static final class MeasureEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEASURE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEASURE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEASURE;

        public static final String TABLE_NAME = "measure";

        public static final String COLUMN_NAME = "name"; //e.g. "global weight"
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_VALUE = "value"; //e.g. "45.0"
        public static final String COLUMN_TAG = "weekId";//e.g. "2014W29"

        public static Uri buildMeasureUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildWeightOverPeriodViewUri(String user_id, String apiary_id, String beehouse_id) {
            return BASE_CONTENT_URI.buildUpon()
                    .appendPath(user_id)
                    .appendPath(apiary_id)
                    .appendPath(beehouse_id)
                    .appendPath(PATH_MEASURE)
                    .appendPath(PATH_WEIGHT_OVER_PERIOD)
                    .build();
        }
    }

    public static final class ApiaryEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_APIARY)
                .build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_APIARY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_APIARY;

        public static final String TABLE_NAME = "apiary";

        public static Uri buildApiariesViewUri(String user_id) {
            return BASE_CONTENT_URI.buildUpon().appendPath(user_id).appendPath(PATH_APIARY).build();
        }

        public static String getUserIdFromApiariesViewUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }


    public static final class BeehouseEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_BEEHOUSE)
                .build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_APIARY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_APIARY;

        public static final String TABLE_NAME = "behouse";

        public static final String COLUMN_CURRENT_WEIGHT = "movie_id";
    }



}
