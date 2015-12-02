package com.example.android.openbeelab.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
/**
 * Created by Elorri on 01/12/2015.
 */
public class BeeSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static BeeSyncAdapter sBeeSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sBeeSyncAdapter == null) {
                sBeeSyncAdapter = new BeeSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sBeeSyncAdapter.getSyncAdapterBinder();
    }
}
