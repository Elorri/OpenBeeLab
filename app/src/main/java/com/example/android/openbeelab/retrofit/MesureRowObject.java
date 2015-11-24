package com.example.android.openbeelab.retrofit;

/**
 * Created by Elorri on 24/11/2015.
 */
public class MesureRowObject {
    public final String key;
    public final double[] value;

    public MesureRowObject(String key, double[] value) {
        this.key = key;
        this.value = value;
    }
}
