package com.example.android.openbeelab.Retrofit;

import java.util.List;

/**
 * Created by Elorri on 24/11/2015.
 */
public class WeeksShotResults {
    public final List<WeeksShotRowObject> rows;

    public WeeksShotResults(List<WeeksShotRowObject> rows) {
        this.rows = rows;
    }
}
