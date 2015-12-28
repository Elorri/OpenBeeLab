package com.example.android.openbeelab.retrofit;

/**
 * Created by Elorri on 20/12/2015.
 */
public class ApiaryRowObject {
    public final String id;
    public final String key;
    public final ApiaryJson value;

    public ApiaryRowObject(String id, String key, ApiaryJson value) {
        this.id = id;
        this.key = key;
        this.value = value;
    }
}
