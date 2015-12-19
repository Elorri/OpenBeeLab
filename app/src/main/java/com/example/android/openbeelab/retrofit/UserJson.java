package com.example.android.openbeelab.retrofit;

/**
 * Created by Elorri on 19/12/2015.
 */
public class UserJson {
    public final String _id;
    public final String _rev;
    public final String name;
    public final String email;
    public final String type;

    public UserJson(String _id, String _rev, String name, String email, String type) {
        this._id = _id;
        this._rev = _rev;
        this.name = name;
        this.email = email;
        this.type = type;
    }
}
