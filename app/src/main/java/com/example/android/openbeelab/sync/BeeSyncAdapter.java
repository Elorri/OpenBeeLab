package com.example.android.openbeelab.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.util.Log;

import com.example.android.openbeelab.R;
import com.example.android.openbeelab.Utility;
import com.example.android.openbeelab.db.BeeContract;
import com.example.android.openbeelab.pojo.Beehouse;
import com.example.android.openbeelab.pojo.Measure;
import com.example.android.openbeelab.pojo.User;
import com.example.android.openbeelab.retrofit.JsonCall;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Created by Elorri on 01/12/2015.
 */
public class BeeSyncAdapter extends AbstractThreadedSyncAdapter {


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({USER_DB_STATUS_SERVER_ERROR, USER_DB_STATUS_SERVEUR_UNKNOWN, USER_DB_STATUS_SERVER_LOAD_COMPLETED})
    public @interface UserDbStatus {
    }

    public static final int USER_DB_STATUS_SERVER_ERROR = 0;
    public static final int USER_DB_STATUS_SERVEUR_UNKNOWN = 1;
    public static final int USER_DB_STATUS_SERVER_LOAD_COMPLETED = 2;


    // Interval at which to sync with the openbeelab server, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    // 60 seconds (1 minute) * 720 = 12 hours
    //public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_INTERVAL = 60 * 720;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private String LOG_TAG = BeeSyncAdapter.class.getSimpleName();


    public BeeSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");

        User.resetDB(getContext());
        Beehouse.resetDB(getContext());
        Measure.resetDB(getContext());
        List<User> users = JsonCall.getUsers(getContext());
        User.syncDB(getContext(), users);
        Cursor usersCursor = getContext().getContentResolver()
                .query(BeeContract.UserEntry.CONTENT_URI, null, null, null, null);
        List<User> users_with_ids = User.getUsers(usersCursor);

        for (User user : users_with_ids) {
            List<Beehouse> beehouses = JsonCall.getBeehouses(getContext(), user.getId());
            Beehouse.syncDB(getContext(), beehouses);
            Cursor beehousesCursor = getContext().getContentResolver()
                    .query(BeeContract.BeehouseEntry.CONTENT_URI, null, null, null, null);
            List<Beehouse> beehouses_with_ids = Beehouse.getBeehouses(beehousesCursor);

            for(Beehouse beehouse : beehouses_with_ids){
                Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
                List<Measure> measures = JsonCall.getLast30DaysMeasures(getContext(), beehouse
                        .getId(), beehouse.getName());
                Measure.syncDB(getContext(),measures);
            }
        }

        Utility.setUserDbStatus(getContext(), BeeSyncAdapter.USER_DB_STATUS_SERVER_LOAD_COMPLETED);
    }




    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
             /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        BeeSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }
}
