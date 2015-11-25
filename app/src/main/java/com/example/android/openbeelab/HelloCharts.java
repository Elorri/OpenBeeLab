package com.example.android.openbeelab;

import android.content.Context;
import android.graphics.Color;

import com.example.android.openbeelab.pojo.Measure;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Elorri on 25/11/2015.
 */
public class HelloCharts {


    private Context context;

    public HelloCharts(Context context) {
        this.context = context;
    }

    public  List<PointValue> getPointValues(List<Measure> measures){

        List<PointValue> values = new ArrayList<>();
        int i=0;
        for (Measure measure : measures) {
            values.add(new PointValue(i, (float)measure.weightPerWeek));
            i++;
        }
        return values;
    }

    public  List<AxisValue> getAxisValues(List<Measure> measures){
        List<AxisValue> axisValues = new ArrayList<>();
        int i=0;
        for (Measure measure : measures) {
             axisValues.add(new AxisValue(i).setLabel(measure.weekId));
            i++;
        }
        return axisValues;
    }


    public  Line createLine(List<PointValue> values) {
        Line line = new Line(values);
        line.setColor(Color.parseColor(getContext().getString(R.string.primary)));
        line.setShape(ValueShape.CIRCLE);
        line.setCubic(true);
        line.setFilled(true);
        line.setHasLabels(false);
        line.setHasLabelsOnlyForSelected(false);
        line.setHasLines(true);
        line.setHasPoints(true);
        return line;
    }

    public  LineChartData createLinesData(List<Line> lines, List<AxisValue> axisValues) {
        LineChartData linesData = new LineChartData(lines);
        Axis axisX = new Axis(axisValues);
        Axis axisY = new Axis().setHasLines(true);
        linesData.setAxisXBottom(axisX);
        linesData.setAxisYLeft(axisY);
        linesData.setBaseValue(Float.NEGATIVE_INFINITY);
        return linesData;
    }

    public  LineChartData getLineChartData(List<Measure> measures) {
        List<PointValue> values = getPointValues(measures);
        List<AxisValue> axisValues= getAxisValues(measures);
        Line line = createLine(values);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);
        return createLinesData(lines, axisValues);
    }

    public  void setViewport(LineChartView chart, int bottom, int top, int left, int right) {
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = bottom;
        v.top = top;
        v.left = left;
        v.right = right;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }

    public static String getPointDesc(List<Measure> measures, int pointIndex) {
        return measures.get(pointIndex).toString();
    }

    public Context getContext() {
        return context;
    }
}
