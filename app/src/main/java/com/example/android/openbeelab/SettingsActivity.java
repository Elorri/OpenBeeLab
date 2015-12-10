package com.example.android.openbeelab;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MergeCursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.example.android.openbeelab.db.BeeContract;
import com.example.android.openbeelab.pojo.User;

/**
 * Created by Elorri on 03/12/2015.
 */
public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();


    }


    public static class SettingsFragment extends PreferenceFragment implements Preference
            .OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {

        ListPreference mDatabasePref;
        ListPreference mUserPref;


        private static final String[] USERS_COLUMNS = {
                BeeContract.UserEntry._ID,
                BeeContract.UserEntry.COLUMN_NAME
        };

        private static final int COL_USER_ID = 0;
        private static final int COL_USER_NAME = 1;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Add 'general' preferences, defined in the XML file
            addPreferencesFromResource(R.xml.pref_general);

            // For all preferences, attach an OnPreferenceChangeListener so the UI summary can be
            // updated when the preference changes.
            mDatabasePref = (ListPreference) findPreference(getString(R.string.pref_database_key));
            String defaultDatabasePref=getPrefStoredValue(mDatabasePref);
            onPreferenceChange(mDatabasePref, defaultDatabasePref);
            mDatabasePref.setOnPreferenceChangeListener(this);


            mUserPref = (ListPreference) findPreference(getString(R.string.pref_userId_key));
            String defaultUserPref=getPrefStoredValue(mUserPref);
            onPreferenceChange(mUserPref, defaultUserPref);
            mUserPref.setOnPreferenceChangeListener(this);

//            @BeeSyncAdapter.UserStatus int userDbStatus = Utility.getUserStatus(getActivity());
//            switch (userDbStatus) {
//                case BeeSyncAdapter.STATUS_USERS_LOADING:
//                    userPref.setSummary(getString(R.string.pref_userName_option_label_loading));
//                    break;
//                case BeeSyncAdapter.STATUS_USERS_SYNC_DONE:
//                    bindPreferenceSummaryToNewValue(userPref);
//                    break;
//                default:
//                bindPreferenceSummaryToValue(userPref);
//            }
        }

        private String getPrefStoredValue(Preference pref) {
            String defaultPref =
                    Utility.getStringSharedPreference(getActivity(),
                            pref.getKey(), "");
            return defaultPref;
        }

        // Registers a shared preference change listener that gets notified when preferences change
        @Override
        public void onResume() {
            Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
//            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
//            sp.registerOnSharedPreferenceChangeListener(this);
//
//            //We only want to call initializeSyncAdapter the very firsttime the app is launch,
//            // this means when userStatus = STATUS_USER_UNKNOWN
//            @BeeSyncAdapter.UserStatus int userStatus = Utility.getUserStatus(getActivity());
//            if (userStatus == BeeSyncAdapter.STATUS_USER_UNKNOWN) {
//                if (!Utility.isNetworkAvailable(getActivity()))
//                    Utility.setServeurStatus(getActivity(), BeeSyncAdapter
//                            .STATUS_SERVEUR_NO_INTERNET);
//                else {
//                    BeeSyncAdapter.initializeSyncAdapter(getActivity());
//                }
//            }
            super.onResume();
        }

        // Unregisters a shared preference change listener
        @Override
        public void onPause() {
//            Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
//            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
//            sp.unregisterOnSharedPreferenceChangeListener(this);
            super.onPause();
        }


        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "" + value);

            Utility.setStringSharedPreference(getActivity(), preference.getKey(), value
                    .toString());

            String defaultPref = getPrefStoredValue(preference);
            Log.e("Lifecycle", "" + defaultPref);

            int prefIndex = ((ListPreference)preference).findIndexOfValue(value.toString());
            if (prefIndex >= 0) {
                preference.setSummary(((ListPreference)preference).getEntries()[prefIndex]);
                ((ListPreference)preference).setValueIndex(prefIndex);
            }
            return true;
        }

        private void setPreferenceSummary(Preference preference, Object value) {
            Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
            String stringValue = value.toString();
            String key = preference.getKey();

            if (preference instanceof ListPreference) {
                if (key.equals(getString(R.string.pref_userId_key))) {
                    Utility.setPreferredUserId(getActivity(), stringValue);
                }
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list (since they have separate labels/values).
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    preference.setSummary(listPreference.getEntries()[prefIndex]);
                    ((ListPreference) preference).setValueIndex(prefIndex);
                }
            } else {
                // For other preferences, set the summary to the value's simple string representation.
                preference.setSummary(stringValue);
            }
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//            Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
//            if (key.equals(getString(R.string.pref_user_status_key))) {
//                ListPreference userPref = (ListPreference) findPreference(getString(R.string.pref_userId_key));
//                @BeeSyncAdapter.UserStatus int userDbStatus = Utility.getUserStatus(getActivity());
//                switch (userDbStatus) {
//                    case BeeSyncAdapter.STATUS_USERS_LOADING:
//                        userPref.setSummary(getString(R.string.pref_userName_option_label_loading));
//                        break;
//                    case BeeSyncAdapter.STATUS_USERS_SYNC_DONE: {
//                       bindPreferenceSummaryToNewValue(userPref);
//                        break;
//                    }
//                    default:
//                        // Note --- if the server is down we still assume the value
//                        // is valid
////                        preference.setSummary(stringValue);
//
//                }
//            }
        }

        private void bindPreferenceSummaryToNewValue(ListPreference userPref) {
            String database = Utility.getPreferredDatabase(getActivity());
            Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "" + database);
            Cursor cursor = getActivity().getContentResolver().query(
                    BeeContract.UserEntry.CONTENT_URI,
                    USERS_COLUMNS,
                    BeeContract.UserEntry.COLUMN_DATABASE + "=?",
                    new String[]{database},
                    BeeContract.UserEntry.COLUMN_NAME + " ASC");

            //Make a copy of the cursor
            Cursor[] cursors = new Cursor[1];
            cursors[0] = cursor;
            Cursor cursor2 = new MergeCursor(cursors);


            CharSequence[] pref_userId_options_values = User.toStringOptionValue(cursor);
            CharSequence[] pref_userName_options_label = User.toStringOptionLabel(cursor2);

            userPref = (ListPreference) findPreference(getString(R.string.pref_userId_key));
            userPref.setEntries(pref_userName_options_label);
            userPref.setEntryValues(pref_userId_options_values);

            //Set a new userPref default value
            String userPrefDefault = (String) pref_userId_options_values[0];
            Utility.setPreferredUserId(getActivity(), userPrefDefault);

            // Update the preference summary with new default value.
            setPreferenceSummary(userPref, userPrefDefault);

            //Open alert dialog asking the user to change the value. For now nothing
            // done
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Intent getParentActivityIntent() {
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
}