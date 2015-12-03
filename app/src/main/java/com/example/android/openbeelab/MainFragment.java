package com.example.android.openbeelab;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.openbeelab.db.BeeContract;

/**
 * Created by Elorri on 03/12/2015.
 */
public class MainFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String SELECTED_KEY = "selected_position";
    private static final String MAIN_URI = "main_uri";
    private GridView mGridView;
    private Uri mMainUri;
    private MainAdapter mMainAdapter;
    private static final int APIARIES_LOADER = 0;
    private int mPosition = GridView.INVALID_POSITION;



    private static final String[] APIARIES_COLUMNS = {
            BeeContract.ApiaryEntry._ID,
            BeeContract.BeehouseEntry._ID,
            BeeContract.BeehouseEntry.COLUMN_CURRENT_WEIGHT
    };

    // These indices are tied to MOVIE_COLUMNS.  If MOVIE_COLUMNS changes, these
    // must change.
    static final int COL_APIARY_ID = 1;
    static final int COL_BEHOUSE_ID = 2;
    static final int COL_BEHOUSE_WEIGHT = 3;





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

    private void onBehouseClicked(AdapterView<?> parent, int position) {
        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
        if (cursor != null) {
            String userId = BeeContract.ApiaryEntry.getUserIdFromApiariesViewUri(mMainUri);
            String apiaryId=cursor.getString(COL_APIARY_ID);
            String beehouseId=cursor.getString(COL_BEHOUSE_ID);
            Uri uri = BeeContract.MeasureEntry.buildWeightOverPeriodViewUri(userId, apiaryId,
                    beehouseId);
            ((Callback) getActivity()).onItemSelected(uri, false);
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
        if (mPosition != GridView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mGridView.smoothScrollToPosition(mPosition);
        }
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
}