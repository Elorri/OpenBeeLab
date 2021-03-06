package com.example.android.openbeelab;

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

import com.example.android.openbeelab.db.BeeContract;

/**
 * Created by Elorri on 11/12/2015.
 */
public class BeehouseMasterViewFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String BEEHOUSE_VIEW_URI = "beehouse_view_uri";
    private BeehouseMasterViewAdapter mAdapter;
    private ListView mListView;
    private Uri mUri;

    private static final int BEEHOUSE_LOADER = 0;


    public interface Callback {
        void onItemSelected(Uri uri, int position);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        mAdapter = new BeehouseMasterViewAdapter(getActivity(), null, 0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        View view = inflater.inflate(R.layout.beehouse_view_fragment, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) view.findViewById(R.id.listview);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemClicked(parent, position);
            }


        });

        return view;
    }

    private void onItemClicked(AdapterView<?> parent, int position) {
        String database = Utility.getPreferredDatabase(getContext());
        String userId = Utility.getPreferredUserId(getContext());
        String beehouseId = BeeContract.BeehouseEntry
                .getBeehouseIdFromBeehouseDetailViewUri(mUri);
        Uri uri = null;
        switch (position) {
            case BeehouseMasterViewAdapter.VIEW_TYPE_INFO:
                uri = BeeContract.BeehouseEntry.buildBeehouseViewUri(database, userId,
                        beehouseId);
                break;
            case BeehouseMasterViewAdapter.VIEW_TYPE_DATAWIZ_OVER_LAST_30_DAYS:

                uri = BeeContract.MeasureEntry.buildWeightOverPeriodViewUri(database, userId,
                        beehouseId);
                break;
        }
        ((Callback) getActivity()).onItemSelected(uri, position);
    }

    @Override
    public void onStart() {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        super.onStart();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        getLoaderManager().initLoader(BEEHOUSE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = null;
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(BEEHOUSE_VIEW_URI);
            if (mUri != null) {
                cursorLoader = new CursorLoader(getActivity(),
                        mUri,
                        null,
                        null,
                        null,
                        null);
            }
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
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
