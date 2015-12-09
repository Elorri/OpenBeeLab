package com.example.android.openbeelab.retrofit;

/**
 * Created by Elorri on 09/12/2015.
 */
public class ErrorObject {
    public final String error;
    public final String reason;

    public ErrorObject(String error, String reason) {
        this.error=error;
        this.reason=reason;
    }
}
