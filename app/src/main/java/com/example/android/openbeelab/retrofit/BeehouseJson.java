package com.example.android.openbeelab.retrofit;

/**
 * Created by Elorri on 03/01/2016.
 */
public class BeehouseJson {
    public final String _id;
    public final String _rev;
    public final String name;
    public final String apiary_id;


    public BeehouseJson(String _id, String _rev, String name, String apiary_id) {
        this._id = _id;
        this._rev = _rev;
        this.name = name;
        this.apiary_id=apiary_id;
    }
}
