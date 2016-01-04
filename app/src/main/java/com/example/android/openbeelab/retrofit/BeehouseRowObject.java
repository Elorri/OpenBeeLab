package com.example.android.openbeelab.retrofit;

/**
 * Created by Elorri on 03/01/2016.
 */
public class BeehouseRowObject {
    public final String id;
    public final String key;
    public final BeehouseJson value;

    public BeehouseRowObject(String id, String key, BeehouseJson value) {
        this.id = id;
        this.key = key;
        this.value = value;
    }
}
