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
import android.widget.Toast;

import com.example.android.openbeelab.hellocharts.HelloCharts;
import com.example.android.openbeelab.pojo.Measure;

import java.util.List;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Elorri on 03/12/2015.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String DETAIL_URI = "URI";
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private static final int BEEHOUSE_LOADER = 0;

    private List<Measure> mMeasures;
    private LineChartView mLineChartView;
    private static Uri mUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        View view = inflater.inflate(R.layout.weight_per_period, container, false);
        mLineChartView = (LineChartView) view.findViewById(R.id.chart);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        if (savedInstanceState == null)
            getLoaderManager().initLoader(BEEHOUSE_LOADER, null, this);
        else
            getLoaderManager().restartLoader(BEEHOUSE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        CursorLoader cursorLoader = null;

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DETAIL_URI);
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");

        if (cursor != null) {
            Log.e("hh", "are you here ?");
            mMeasures = Measure.getMeasures(cursor);
            for (Measure m : mMeasures) {
                Log.e("hh", m.getShortDesc());
            }

            mLineChartView.setOnValueTouchListener(new ValueTouchListener());
            HelloCharts helloCharts = new HelloCharts(getContext());
            mLineChartView.setLineChartData(helloCharts.getLineChartData(mMeasures));
            helloCharts.setViewport(mLineChartView, -5, 65, 0, mMeasures.size() - 1);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
    }



    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(getContext(),
                    "" + HelloCharts.getPointDesc(mMeasures, pointIndex),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub
        }
    }





}
