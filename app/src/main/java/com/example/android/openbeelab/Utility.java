package com.example.android.openbeelab;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

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

    public static  void setPreferredUserId(Context c, String userId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor spe = sp.edit();
        spe.putString(c.getString(R.string.pref_userId_key), userId);
        spe.commit();
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
    //Should be called on main thread
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
    @BeeSyncAdapter.UserStatus
    public static int getUserStatus(Context c) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        return sp.getInt(c.getString(R.string.pref_user_status_key), BeeSyncAdapter.STATUS_USERS_UNKNOWN);
    }

    /**
     * Sets the location status into shared preference.  This function should not be called from
     * the UI thread because it uses commit to write to the shared preferences. Nb:if call from
     * UI thread use, apply instead.
     *
     * @param c              Context to get the PreferenceManager from.
     * @param userStatus The IntDef value to set
     */
    public static  void setUserStatus(Context c, @BeeSyncAdapter.UserStatus int userStatus) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + ""+userStatus);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor spe = sp.edit();
        spe.putInt(c.getString(R.string.pref_user_status_key), userStatus);
        spe.commit();
    }




    /**
     *
     * @param c Context used to get the SharedPreferences
     * @return the location status integer type
     */
    @SuppressWarnings("ResourceType")
    @BeeSyncAdapter.ServeurStatus
    public static int getServeurStatus(Context c) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        return sp.getInt(c.getString(R.string.pref_serveur_status_key), BeeSyncAdapter
                .STATUS_SERVEUR_UNKNOWN);
    }

    /**
     * Sets the location status into shared preference.  This function should not be called from
     * the UI thread because it uses commit to write to the shared preferences. Nb:if call from
     * UI thread use, apply instead.
     *
     * @param c              Context to get the PreferenceManager from.
     * @param serveurStatus The IntDef value to set
     */
    public static  void setServeurStatus(Context c, @BeeSyncAdapter.ServeurStatus int
            serveurStatus) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor spe = sp.edit();
        spe.putInt(c.getString(R.string.pref_serveur_status_key), serveurStatus);
        spe.commit();
    }


    public static String getStringSharedPreference(Context context, String key, String
            defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key, defaultValue);
    }

    /**
     * This function should not be called from the UI thread because it uses commit to write
     * to the shared preferences. Nb:if call from
     * UI thread use, apply instead.
     */
    public static void setStringSharedPreference(Context context, String key, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spe = sp.edit();
        spe.putString(key, value);
        spe.commit();
    }

    //TODO should have an int parameter representing the date
    public static String getFriendlyDayName(String beehouseLastUpdate) {
        return "Aujourd'hui";
    }
}
