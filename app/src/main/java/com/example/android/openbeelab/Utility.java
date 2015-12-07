package com.example.android.openbeelab;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.example.android.openbeelab.sync.BeeSyncAdapter;

/**
 * Created by Elorri on 03/12/2015.
 */
public class Utility {

    public static String getPreferredDatabase(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_database_key),
                context.getString(R.string.pref_database_option_default));
    }

    public static String getPreferredUserId(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_userId_key),
                context.getString(R.string.pref_userId_option_default));
    }


    /**
     * Helper method to provide the icon resource id according to the behouse weight condition
     *
     * @param weight from OpenWeatherMap API response
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

    /**
     * Compare the second Uri to the first and return true if equals, false if not
     * @param uri1 first uri
     * @param uri2 second uri to compare to the first
     * @return true if the 2 uris are equals, false otherwise
     */
    public static boolean compareUris(Uri uri1, Uri uri2) {
        return uri1.toString().equals(uri2.toString());
    }

    /**
     * Returns true if the network is available or about to become available.
     *
     * @param c Context used to get the ConnectivityManager
     * @return true if the network is available
     */
    static public boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm =  (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&  activeNetwork.isConnectedOrConnecting();
    }


    /**
     *
     * @param c Context used to get the SharedPreferences
     * @return the location status integer type
     */
    @SuppressWarnings("ResourceType")
    @BeeSyncAdapter.UserDbStatus
    public static int getUserDbStatus(Context c) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        return sp.getInt(c.getString(R.string.pref_user_db_status_key), BeeSyncAdapter
                .USER_DB_STATUS_SERVEUR_UNKNOWN);
    }

    /**
     * Sets the location status into shared preference.  This function should not be called from
     * the UI thread because it uses commit to write to the shared preferences. Nb:if call from
     * UI thread use, apply instead.
     *
     * @param c              Context to get the PreferenceManager from.
     * @param userDbStatus The IntDef value to set
     */
    public static  void setUserDbStatus(Context c, @BeeSyncAdapter.UserDbStatus int userDbStatus) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor spe = sp.edit();
        spe.putInt(c.getString(R.string.pref_user_db_status_key), userDbStatus);
        spe.commit();
    }
}
