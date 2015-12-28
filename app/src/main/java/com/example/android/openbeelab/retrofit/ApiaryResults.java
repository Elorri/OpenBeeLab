package com.example.android.openbeelab.retrofit;

import java.util.List;

/**
 * Created by Elorri on 20/12/2015.
 */

public class ApiaryResults {
    public final int total_rows;
    public final int offset;
    public final List<ApiaryRowObject> rows;

    public ApiaryResults(int total_rows, int offset, List<ApiaryRowObject> rows) {
        this.total_rows = total_rows;
        this.offset = offset;
        this.rows = rows;
    }
}