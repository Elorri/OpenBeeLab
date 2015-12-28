package com.example.android.openbeelab.retrofit;

/**
 * Created by Elorri on 20/12/2015.
 */
public class ApiaryJson {
    public final String _id;
    public final String _rev;
    public final String name;
    public final String type;
    public final String location_id;
    public final String[] beehouses;
    public final String[] beekeepers;

    public ApiaryJson(String _id, String _rev, String name, String type, String location_id, String[] beehouses, String[] beekeepers) {
        this._id = _id;
        this._rev = _rev;
        this.name = name;
        this.type = type;
        this.location_id = location_id;
        this.beehouses = beehouses;
        this.beekeepers = beekeepers;
    }
}
