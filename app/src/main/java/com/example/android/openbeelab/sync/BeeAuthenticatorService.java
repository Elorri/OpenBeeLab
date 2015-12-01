package com.example.android.openbeelab.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Elorri on 01/12/2015.
 */
public class BeeAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private BeeAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new BeeAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
