package com.example.android.openbeelab;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private LineChart[] mCharts = new LineChart[4];
    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    private Typeface mTf;

    private int[] mColors = new int[] {
            Color.rgb(137, 230, 81),
            Color.rgb(240, 240, 30),
            Color.rgb(89, 199, 250),
            Color.rgb(250, 104, 104)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OpenbeelabService.getMesures();


        LineData data = getData(36, 100);
        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Bold.ttf");
        data.setValueTypeface(mTf);

        mCharts[0] = (LineChart) findViewById(R.id.chart1);
        mCharts[1] = (LineChart) findViewById(R.id.chart2);
        mCharts[2] = (LineChart) findViewById(R.id.chart3);
        mCharts[3] = (LineChart) findViewById(R.id.chart4);

        for (int i = 0; i < mCharts.length; i++)
            // add some transparency to the color with "& 0x90FFFFFF"
            setupChart(mCharts[i], data, mColors[i % mColors.length]);

    }


    private void setupChart(LineChart chart, LineData data, int color) {

        // no description text
        chart.setDescription("");
        chart.setNoDataTextDescription("You need to provide data for the chart.");

        // mChart.setDrawHorizontalGrid(false);
        //
        // enable / disable grid background
        chart.setDrawGridBackground(false);
//        chart.getRenderer().getGridPaint().setGridColor(Color.WHITE & 0x70FFFFFF);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setBackgroundColor(color);

        // set custom chart offsets (automatic offset calculation is hereby disabled)
        chart.setViewPortOffsets(10, 0, 10, 0);

        // add data
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(false);

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);

        chart.getXAxis().setEnabled(false);

        // animate calls invalidate()...
        chart.animateX(2500);
    }


    private LineData getData(int count, float range) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add(mMonths[i % 12]);
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range) + 3;
            yVals.add(new Entry(val, i));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        set1.setLineWidth(1.75f);
        set1.setCircleSize(3f);
        set1.setColor(Color.WHITE);
        set1.setCircleColor(Color.WHITE);
        set1.setHighLightColor(Color.WHITE);
        set1.setDrawValues(false);

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        return data;
    }

}
