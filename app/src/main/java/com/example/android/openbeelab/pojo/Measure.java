package com.example.android.openbeelab.pojo;

import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * Created by Elorri on 24/11/2015.
 */
public class Measure {
    public  final String weekId;
    public  final double weightPerWeek;

    public Measure(String weekId, double weightPerWeek) {
        this.weekId=weekId;
        this.weightPerWeek=weightPerWeek;
    }

    @Override
    public String toString() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(1);
        numberFormat.setRoundingMode(RoundingMode.CEILING);
        return weekId+"  "+numberFormat.format(weightPerWeek);
    }
}
