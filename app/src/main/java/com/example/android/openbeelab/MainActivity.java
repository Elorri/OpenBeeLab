package com.example.android.openbeelab;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.android.openbeelab.pojo.Measure;

import java.util.List;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;


public class MainActivity extends AppCompatActivity {


    private List<Measure> mMeasures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BackgroundTask backgroundTask = new BackgroundTask();
        backgroundTask.execute();
    }


    public class BackgroundTask extends AsyncTask<Void, Void, List<Measure>> {

        @Override
        protected List<Measure> doInBackground(Void... params) {
            return OpenBeelabService.getMeasures();
        }

        @Override
        protected void onPostExecute(List<Measure> measures) {
            if (measures != null) {
                mMeasures=measures;
                LineChartView lineChartView = (LineChartView) findViewById(R.id.chart);
                lineChartView.setOnValueTouchListener(new ValueTouchListener());
                lineChartView.setLineChartData(HelloCharts.getLineChartData(measures));
                HelloCharts.setViewport(lineChartView, -5, 65, 0, measures.size() - 1);
            }
        }
    }


    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(MainActivity.this,
                    ""+HelloCharts.getPointDesc(mMeasures,pointIndex),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub
        }
    }

}
