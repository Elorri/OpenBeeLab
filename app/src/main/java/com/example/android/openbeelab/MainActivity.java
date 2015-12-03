package com.example.android.openbeelab;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.openbeelab.db.BeeContract;
import com.example.android.openbeelab.pojo.Measure;
import com.example.android.openbeelab.retrofit.OpenBeelabNetworkJson;

import java.util.List;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;


public class MainActivity extends AppCompatActivity {


    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private List<Measure> mMeasures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BackgroundTask backgroundTask = new BackgroundTask();
        backgroundTask.execute();



       // BeeSyncAdapter.initializeSyncAdapter(this);
        //TODO add this whenever we need a manual sync  BeeSyncAdapter.syncImmediately()
    }


    private void syncDB(List<Measure> measures) {
        ContentValues[] measuresContentValues = Measure.getContentValuesArray(measures);
        int inserted = 0;
        if (measuresContentValues.length > 0)
            inserted = this.getContentResolver().bulkInsert(BeeContract.MeasureEntry
                    .CONTENT_URI, measuresContentValues);
        Log.d(LOG_TAG, "SyncDB Complete. " + inserted + " Inserted");
    }


    public class BackgroundTask extends AsyncTask<Void, Void, List<Measure>> {

        @Override
        protected List<Measure> doInBackground(Void... params) {
            List<Measure> measures = OpenBeelabNetworkJson.getMeasures(MainActivity.this);
            syncDB(measures);
            return measures;
        }

        @Override
        protected void onPostExecute(List<Measure> measures) {
            if (measures != null) {
                mMeasures=measures;
                LineChartView lineChartView = (LineChartView) findViewById(R.id.chart);
                lineChartView.setOnValueTouchListener(new ValueTouchListener());
                HelloCharts helloCharts=new HelloCharts(MainActivity.this);
                lineChartView.setLineChartData(helloCharts.getLineChartData(measures));
                helloCharts.setViewport(lineChartView, -5, 65, 0, measures.size() - 1);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
