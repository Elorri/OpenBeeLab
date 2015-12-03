package com.example.android.openbeelab;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.openbeelab.db.BeeContract;
import com.example.android.openbeelab.hellocharts.HelloCharts;
import com.example.android.openbeelab.pojo.Measure;
import com.example.android.openbeelab.retrofit.OpenBeelabNetworkJson;

import java.util.List;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Elorri on 03/12/2015.
 */
public class DetailFragment  extends Fragment {
    public static final String DETAIL_URI = "URI";
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private List<Measure> mMeasures;
    private LineChartView mLineChartView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weight_per_period, container, false);
        mLineChartView = (LineChartView) view.findViewById(R.id.chart);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        BackgroundTask backgroundTask = new BackgroundTask();
        backgroundTask.execute();
        super.onActivityCreated(savedInstanceState);
    }

    public class BackgroundTask extends AsyncTask<Void, Void, List<Measure>> {

        @Override
        protected List<Measure> doInBackground(Void... params) {
            List<Measure> measures = OpenBeelabNetworkJson.getMeasures(getContext());
            syncDB(measures);
            return measures;
        }

        @Override
        protected void onPostExecute(List<Measure> measures) {
            if (measures != null) {
                mMeasures=measures;
                mLineChartView.setOnValueTouchListener(new ValueTouchListener());
                HelloCharts helloCharts=new HelloCharts(getContext());
                mLineChartView.setLineChartData(helloCharts.getLineChartData(measures));
                helloCharts.setViewport(mLineChartView, -5, 65, 0, measures.size() - 1);
            }
        }
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


    private void syncDB(List<Measure> measures) {
        ContentValues[] measuresContentValues = Measure.getContentValuesArray(measures);
        int inserted = 0;
        if (measuresContentValues.length > 0)
            inserted = getContext().getContentResolver().bulkInsert(BeeContract.MeasureEntry
                    .CONTENT_URI, measuresContentValues);
        Log.d(LOG_TAG, "SyncDB Complete. " + inserted + " Inserted");
    }

}
