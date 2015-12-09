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
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.android.openbeelab.db.BeeContract;
import com.example.android.openbeelab.pojo.User;
import com.example.android.openbeelab.sync.BeeSyncAdapter;

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
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_database_key)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_userId_key)));
        }

        // Registers a shared preference change listener that gets notified when preferences change
        @Override
        public void onResume() {
            Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            sp.registerOnSharedPreferenceChangeListener(this);

            BeeSyncAdapter.initializeSyncAdapter(getActivity());
            //TODO add this whenever we need a manual sync  BeeSyncAdapter.syncImmediately()
            super.onResume();
        }

        // Unregisters a shared preference change listener
        @Override
        public void onPause() {
            Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            sp.unregisterOnSharedPreferenceChangeListener(this);
            super.onPause();
        }

        /**
         * Attaches a listener so the summary is always updated with the preference value.
         * Also fires the listener once, to initialize the summary (so it shows up before the value
         * is changed.)
         */
        private void bindPreferenceSummaryToValue(Preference preference) {
            // Set the listener to watch for value changes.
            preference.setOnPreferenceChangeListener(this);

            // Trigger the listener immediately with the preference's
            // current value.
            onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            setPreferenceSummary(preference, value);
            return true;
        }

        private void setPreferenceSummary(Preference preference, Object value) {
            Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
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
            Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
            if (key.equals(getString(R.string.pref_user_db_status_key))) {
                @BeeSyncAdapter.UserDbStatus int userDbStatus = Utility.getUserDbStatus(getActivity());
                switch (userDbStatus) {
                    case BeeSyncAdapter.USER_DB_STATUS_USERS_LOADING:
//                        preference.setSummary(getString(R.string.pref_location_error_description, value.toString()));
                        break;
                    case BeeSyncAdapter.USER_DB_STATUS_USERS_SYNC_DONE: {
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

                        ListPreference userPref = (ListPreference) findPreference(getString(R.string.pref_userId_key));
                        userPref.setEntries(pref_userName_options_label);
                        userPref.setEntryValues(pref_userId_options_values);

                        //Set a new userPref default value
                        String userPrefDefault=(String)pref_userId_options_values[0];
      
                        // Update the preference summary with new default value.
                         setPreferenceSummary(userPref, userPrefDefault);

                        //Open alert dialog asking the user to change the value. For now nothing
                        // done
                        break;
                    }
                    default:
                        // Note --- if the server is down we still assume the value
                        // is valid
//                        preference.setSummary(stringValue);

                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Intent getParentActivityIntent() {
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
}