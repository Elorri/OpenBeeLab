package com.example.android.openbeelab;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.openbeelab.db.BeeContract;
import com.example.android.openbeelab.sync.BeeSyncAdapter;

/**
 * Created by Elorri on 03/12/2015.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String SELECTED_KEY = "selected_position";
    private static final String MAIN_URI = "main_uri";
    private ListView mListView;
    private Uri mMainUri;
    private MainAdapter mMainAdapter;
    private static final int APIARIES_LOADER = 0;
    private int mPosition = ListView.INVALID_POSITION;


    private static final String[] APIARIES_COLUMNS = {
            BeeContract.ApiaryEntry.TABLE_NAME+"."+BeeContract.ApiaryEntry._ID,
            BeeContract.ApiaryEntry.COLUMN_NAME
    };

    // These indices are tied to MOVIE_COLUMNS.  If MOVIE_COLUMNS changes, these
    // must change.
    static final int COL_APIARY_ID = 0;
    static final int COL_APIARY_NAME = 1;


    public interface Callback {
        void onItemSelected(Uri uri);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // The CursorAdapter will take data from our cursor and populate the ListView
        // However, we cannot use FLAG_AUTO_REQUERY since it is deprecated, so we will end
        // up with an empty list the first time we run.
        mMainAdapter = new MainAdapter(getActivity(), null, 0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) rootView.findViewById(R.id.listView);
        mListView.setAdapter(mMainAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onApiaryClicked(parent, position);
                setPosition(position);
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        return rootView;
    }

    public void onMainUriChange() {
        if (mMainUri != null) {
            Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
            Log.e("h", "" + mMainUri.toString());
            getLoaderManager().restartLoader(APIARIES_LOADER, null, this);
        }
    }

    private void onApiaryClicked(AdapterView<?> parent, int position) {
        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
        if (cursor != null) {
            String database = Utility.getPreferredDatabase(getContext());
            String userId = Utility.getPreferredUserId(getContext());
            String apiaryId = cursor.getString(COL_APIARY_ID);
            Uri uri = BeeContract.BeehouseEntry.buildBeehousesByApiaryViewUri(database, userId,
                    apiaryId);
            ((Callback) getActivity()).onItemSelected(uri);
        }
    }

    @Override
    public void onResume() {
        getLoaderManager().initLoader(APIARIES_LOADER, null, this);
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                mMainUri,
                APIARIES_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMainAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }
        updateEmptyView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMainAdapter.swapCursor(null);
    }

    public void setMainUri(Uri mMainUri) {
        this.mMainUri = mMainUri;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MAIN_URI, mMainUri);
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to ListView.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }


    /*
 Updates the empty list view with contextually relevant information that the user can
 use to determine why they aren't seeing weather.
*/
    private void updateEmptyView() {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        TextView emptyTextView = (TextView) getView().findViewById(R.id.listview_apiaries_empty);
        if (mMainAdapter.getCount() == 0) {
            @BeeSyncAdapter.UserStatus int userStatus = Utility.getUserStatus(getActivity());
            @BeeSyncAdapter.ServeurStatus int serverStatus = Utility.getServeurStatus(getActivity());
            if (null != emptyTextView) {
                // if cursor is empty, why? do we have an invalid location
                int message = R.string.empty_beehouse_list;
                if (userStatus == BeeSyncAdapter.STATUS_USERS_UNKNOWN) {
                    message = R.string.empty_beehouse_list_user_unknown;
                    if (serverStatus == BeeSyncAdapter.STATUS_SERVEUR_NO_INTERNET)
                        message = R.string.empty_beehouse_list_user_unknown_no_internet;
                    else if (serverStatus == BeeSyncAdapter.STATUS_SERVEUR_ERROR)
                        message = R.string.empty_beehouse_list_user_unknown_serveur_error;
                } else if (userStatus == BeeSyncAdapter.STATUS_USERS_LOADING)
                    message = R.string.empty_beehouse_list_user_unknown;
                emptyTextView.setText(message);
            }
        }else emptyTextView.setText("");
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_user_status_key))) {
            updateEmptyView();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
    }


}
