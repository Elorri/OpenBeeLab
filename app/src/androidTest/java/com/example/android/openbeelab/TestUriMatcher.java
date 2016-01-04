package com.example.android.openbeelab;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.android.openbeelab.db.BeeContract.MeasureEntry;
import com.example.android.openbeelab.db.BeeProvider;

/**
 * Created by Elorri on 03/12/2015.
 */
public class TestUriMatcher extends AndroidTestCase {
    private static final String DATABASE = "la_mine";
    private static final String USER_ID = "1";
    private static final String APIARY_ID = "la_mine_rucher_01";
    private static final String BEHOUSE_ID = "fa06a84f86d39c84f80eb136ce06141e";


    public void testUriMatcher() {
        UriMatcher testMatcher = BeeProvider.buildUriMatcher();

        assertEquals("Error:", testMatcher.match(MeasureEntry.CONTENT_URI), BeeProvider.MEASURE);
        Uri uri = MeasureEntry.buildWeightOverPeriodViewUri(USER_ID, APIARY_ID, BEHOUSE_ID);
        Log.e("jj", uri.toString());
        assertEquals("Error:", testMatcher.match(uri),BeeProvider.WEIGHT_OVER_PERIOD);
        //uri = BeeContract.BeehouseEntry.buildBeehousesByApiaryViewUri(DATABASE, USER_ID);
        Log.e("jj", uri.toString());
        assertEquals("Error:", testMatcher.match(uri), BeeProvider.USER_BEEHOUSES_BY_APIARY);
    }
}
