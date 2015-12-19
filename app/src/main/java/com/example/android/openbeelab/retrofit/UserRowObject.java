package com.example.android.openbeelab.retrofit;

/**
 * Created by Elorri on 19/12/2015.
 */
public class UserRowObject {
    public final String id;
    public final String key;
    public final UserJson value;

    public UserRowObject(String id, String key, UserJson value) {
        this.id = id;
        this.key = key;
        this.value = value;
    }
}
