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
import android.widget.GridView;
import android.widget.TextView;

import com.example.android.openbeelab.db.BeeContract;
import com.example.android.openbeelab.sync.BeeSyncAdapter;

/**
 * Created by Elorri on 03/12/2015.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String SELECTED_KEY = "selected_position";
    private static final String MAIN_URI = "main_uri";
    private GridView mGridView;
    private Uri mMainUri;
    private MainAdapter mMainAdapter;
    private static final int BEEHOUSES_LOADER = 0;
    private int mPosition = GridView.INVALID_POSITION;


    private static final String[] BEEHOUSES_COLUMNS = {
            BeeContract.BeehouseEntry._ID,
            BeeContract.BeehouseEntry.COLUMN_NAME,
            BeeContract.BeehouseEntry.COLUMN_CURRENT_WEIGHT
    };

    // These indices are tied to MOVIE_COLUMNS.  If MOVIE_COLUMNS changes, these
    // must change.
    static final int COL_BEEHOUSE_ID = 0;
    static final int COL_BEEHOUSE_NAME = 1;
    static final int COL_BEEHOUSE_WEIGHT = 2;


    public interface Callback {
        void onItemSelected(Uri uri, boolean firstDisplay);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // The CursorAdapter will take data from our cursor and populate the GridView
        // However, we cannot use FLAG_AUTO_REQUERY since it is deprecated, so we will end
        // up with an empty list the first time we run.
        mMainAdapter = new MainAdapter(getActivity(), null, 0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mGridView = (GridView) rootView.findViewById(R.id.gridView);
        mGridView.setAdapter(mMainAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onBehouseClicked(parent, position);
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
            getLoaderManager().restartLoader(BEEHOUSES_LOADER, null, this);
        }
    }

    private void onBehouseClicked(AdapterView<?> parent, int position) {
        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
        if (cursor != null) {
            String database = Utility.getPreferredDatabase(getContext());
            String userId = Utility.getPreferredUserId(getContext());
            String beehouseId = cursor.getString(COL_BEEHOUSE_ID);
            Uri uri = BeeContract.MeasureEntry.buildWeightOverPeriodViewUri(database, userId,
                    beehouseId);
            ((Callback) getActivity()).onItemSelected(uri, false);
        }
    }

    @Override
    public void onResume() {
        getLoaderManager().initLoader(BEEHOUSES_LOADER, null, this);
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                mMainUri,
                BEEHOUSES_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "data : " + data + " - data" +
                ".count" + data.getCount());
        mMainAdapter.swapCursor(data);
        if (mPosition != GridView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mGridView.smoothScrollToPosition(mPosition);
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
        // When no item is selected, mPosition will be set to GridView.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != GridView.INVALID_POSITION) {
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


}
