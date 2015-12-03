package com.example.android.openbeelab.pojo;

import android.content.ContentValues;

import com.example.android.openbeelab.db.BeeContract;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by Elorri on 24/11/2015.
 */
public class Measure {
    public double id; //can be null
    public final String name;
    public String timestamp; //can be null
    public final String weekId;
    public final double value;
    public final String unit;

    public Measure(String name, String weekId, double value, String unit) {
        this.name = name;
        this.weekId = weekId;
        this.value = value;
        this.unit = unit;
    }

    @Override
    public String toString() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(1);
        numberFormat.setRoundingMode(RoundingMode.CEILING);
        return weekId + "  " + numberFormat.format(value);
    }


    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BeeContract.MeasureEntry.COLUMN_NAME, this.name);
        contentValues.put(BeeContract.MeasureEntry.COLUMN_TAG, this.weekId);
        contentValues.put(BeeContract.MeasureEntry.COLUMN_VALUE, this.value);
        return contentValues;
    }

    public static ContentValues[] getContentValuesArray(List<Measure> measures) {
        ContentValues[] values = new ContentValues[measures.size()];
        int i = 0;
        for (Measure measure : measures) {
            values[i] = measure.toContentValues();
            i++;
        }
        return values;
    }
}
