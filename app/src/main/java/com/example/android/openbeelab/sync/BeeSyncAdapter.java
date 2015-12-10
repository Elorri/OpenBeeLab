package com.example.android.openbeelab.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.android.openbeelab.MainActivity;
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
    @IntDef({
            STATUS_SERVEUR_UNKNOWN,
            STATUS_SERVEUR_NO_INTERNET,
            STATUS_SERVEUR_DOWN,
            STATUS_SERVEUR_ERROR})
    public @interface ServeurStatus {
    }

    public static final int STATUS_SERVEUR_UNKNOWN = 0;
    public static final int STATUS_SERVEUR_NO_INTERNET = 1;
    public static final int STATUS_SERVEUR_DOWN = 2;
    public static final int STATUS_SERVEUR_ERROR = 3;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATUS_USERS_UNKNOWN,
            STATUS_USERS_LOADING,
            STATUS_USERS_SYNC_DONE})
    public @interface UserStatus {
    }

    public static final int STATUS_USERS_UNKNOWN = 0;
    public static final int STATUS_USERS_LOADING = 1;
    public static final int STATUS_USERS_SYNC_DONE = 2;


    // Interval at which to sync with the openbeelab server, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    // 60 seconds (1 minute) * 720 = 12 hours
    //public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_INTERVAL = 60 * 720;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int BEE_NOTIFICATION_ID = 3004;
    private String LOG_TAG = BeeSyncAdapter.class.getSimpleName();


    public BeeSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {

//            Utility.setUserStatus(getContext(), BeeSyncAdapter
//                    .STATUS_USERS_LOADING);

        Cursor usersCursor = null;
        Cursor beehousesCursor = null;
        Cursor measuresCursor = null;


        User.resetDB(getContext());
        Beehouse.resetDB(getContext());
        Measure.resetDB(getContext());

        Utility.setUserStatus(getContext(), BeeSyncAdapter.STATUS_USERS_LOADING);

        List<User> users = JsonCall.getUsers(getContext());
        User.syncDB(getContext(), users);
        usersCursor = getContext().getContentResolver()
                .query(BeeContract.UserEntry.CONTENT_URI, null, null, null, null);
        List<User> users_with_ids = User.getUsers(usersCursor);

        if (usersCursor.getCount() > 0)
            Utility.setUserStatus(getContext(), BeeSyncAdapter
                    .STATUS_USERS_SYNC_DONE);
        else Utility.setUserStatus(getContext(), BeeSyncAdapter
                .STATUS_USERS_UNKNOWN);


        for (User user : users_with_ids) {
            Log.e("Sync", Thread.currentThread().getStackTrace()[2] + "user.id: " + user.getId());
            List<Beehouse> beehouses = JsonCall.getBeehouses(getContext(), user.getId());
            Beehouse.syncDB(getContext(), beehouses);
            beehousesCursor = getContext().getContentResolver()
                    .query(BeeContract.BeehouseEntry.CONTENT_URI,
                            null,
                            BeeContract.BeehouseEntry.COLUMN_USER_ID + "=?",
                            new String[]{String.valueOf(user.getId())},
                            null);
            List<Beehouse> beehouses_with_ids = Beehouse.getBeehouses(beehousesCursor);

            for (Beehouse beehouse : beehouses_with_ids) {
                Log.e("Sync", Thread.currentThread().getStackTrace()[2] + "beehouse.id: " + beehouse
                        .getId());
                List<Measure> measures = JsonCall.getLast30DaysMeasures(getContext(), beehouse
                        .getId(), beehouse.getName());
                Measure.syncDB(getContext(), measures);
            }
        }

        //Send notification only if bees are in danger
        boolean areBeesInDanger = areBeesInDanger();
        if (areBeesInDanger) {
            int message = areBeesInDangerMessage(areBeesInDanger);
            notifyUserSyncDone(message);
        }
    }

    private boolean areBeesInDanger() {
        String database = Utility.getPreferredDatabase(getContext());
        String userId = Utility.getPreferredUserId(getContext());
        Cursor cursor = getContext().getContentResolver()
                .query(BeeContract.BeehouseEntry.buildBeehousesViewUri(database, userId),
                        null,
                        BeeContract.BeehouseEntry.COLUMN_CURRENT_WEIGHT + "<=?",
                        new String[]{getContext().getString(R.string.dangerous_weight)},
                        null);
        if (cursor.getCount() > 0) return true;
        else return false;
    }

    private int areBeesInDangerMessage(boolean areBeesInDanger) {
        if (areBeesInDanger) return R.string.bees_in_danger;
        else return R.string.bees_are_fine;
    }


    private void notifyUserSyncDone(int message) {
        Context context = getContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String lastNotificationKey = context.getString(R.string.pref_last_notification);
        long lastSync = prefs.getLong(lastNotificationKey, 0);

        if (System.currentTimeMillis() - lastSync >= DAY_IN_MILLIS) {
            // Last sync was more than 1 day ago, let's send a notification for today

            // NotificationCompatBuilder is a very convenient way to build backward-compatible
            // notifications.  Just throw in some data.
            Resources resources = context.getResources();
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getContext())
                            .setColor(resources.getColor(R.color.colorPrimary))
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(BitmapFactory.decodeResource(context
                                    .getResources(), R.mipmap.ic_launcher))
                            .setContentTitle(context.getString(R.string.app_name))
                            .setContentText(context.getString(message));

            // Make something interesting happen when the user clicks on the notification.
            // In this case, opening the app is sufficient.
            Intent resultIntent = new Intent(context, MainActivity.class);

            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager =
                    (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

            // To allows use to  update the notification later on.
            mNotificationManager.notify(BEE_NOTIFICATION_ID, mBuilder.build());

            //refreshing last sync
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(lastNotificationKey, System.currentTimeMillis());
            editor.commit();
        }

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
