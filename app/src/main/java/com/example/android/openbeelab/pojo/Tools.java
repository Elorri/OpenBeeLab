package com.example.android.openbeelab.pojo;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Elorri on 25/11/2015.
 */
public class Tools {




    public static List<PointValue> convertToHelloChartData(List<Measure> measures){

        List<PointValue> values = new ArrayList<>();
        List<AxisValue> axisValues = new ArrayList<>();
        int i=0;
        for (Measure measure : measures) {
            values.add(new PointValue(i, (float)measure.weightPerWeek));
            axisValues.add(new AxisValue(i).setLabel(measure.weekId));
            i++;
        }
        return values;
    }



}
