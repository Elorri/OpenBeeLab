package com.example.android.openbeelab.retrofit;

import java.util.List;

/**
 * Created by Elorri on 24/11/2015.
 */
public class MeasureResults {
    public final List<MesureRowObject> rows;

    public MeasureResults(List<MesureRowObject> rows) {
        this.rows = rows;
    }
}
