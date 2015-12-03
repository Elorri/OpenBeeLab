package com.example.android.openbeelab;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Elorri on 03/12/2015.
 */
public class Utility {

    public static String getPreferredUserDB(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_userDB_key),
                context.getString(R.string.pref_userDB_option_default));
    }


    /**
     * Helper method to provide the icon resource id according to the weather condition id returned
     * by the OpenWeatherMap call.
     *
     * @param weatherId from OpenWeatherMap API response
     * @return resource id for the corresponding icon. -1 if no relation is found.
     */
    public static int getIconResourceForBeehouseCondition(double weight) {
        if (weight <= 30) {
            return R.drawable.ic_weight_critical;
        } else if (weight > 30 && weight <= 60) {
            return R.drawable.ic_weight_low;
        } else if (weight > 60 && weight <= 90) {
            return R.drawable.ic_weight_medium;
        } else if (weight > 90) {
            return R.drawable.ic_weight_high;
        }
        return -1;
    }
}
