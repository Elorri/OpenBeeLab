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


    public static final String PATH_MEASURE = "measure";
    public static final String WEIGHT_OVER_PERIOD = "weight_over_period";


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
        public static final String COLUMN_TAG = "tag";//e.g. "2014W29"

        public static Uri buildMeasureUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
