package com.example.android.openbeelab.retrofit;

import java.util.List;

/**
 * Created by Elorri on 03/01/2016.
 */

public class BeehouseResults {
    public final int total_rows;
    public final int offset;
    public final List<BeehouseRowObject> rows;

    public BeehouseResults(int total_rows, int offset, List<BeehouseRowObject> rows) {
        this.total_rows = total_rows;
        this.offset = offset;
        this.rows = rows;
    }
}