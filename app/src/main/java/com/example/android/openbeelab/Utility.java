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
}
