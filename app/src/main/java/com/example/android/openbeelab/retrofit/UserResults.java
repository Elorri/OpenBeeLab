package com.example.android.openbeelab.retrofit;

import java.util.List;

/**
 * Created by Elorri on 19/12/2015.
 */
public class UserResults {
    public final int total_rows;
    public final int offset;
    public final List<UserRowObject> rows;

    public UserResults(int total_rows, int offset, List<UserRowObject> rows) {
        this.total_rows = total_rows;
        this.offset = offset;
        this.rows = rows;
    }
}
