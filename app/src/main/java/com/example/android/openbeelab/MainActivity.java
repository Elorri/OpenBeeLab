package com.example.android.openbeelab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.openbeelab.db.BeeContract;
import com.example.android.openbeelab.sync.BeeSyncAdapter;


public class MainActivity extends AppCompatActivity implements MainFragment.Callback {


    private static final String LOG_TAG = MainActivity.class.getSimpleName();


    private Uri mMainUri;
    static final String MAIN_URI = "mMainUri";
    private MainFragment mMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        @BeeSyncAdapter.UserStatus int userStatus =Utility.getUserStatus(this);
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "(userStatus == " +
                "BeeSyncAdapter.STATUS_USERS_UNKNOWN) "+(userStatus == BeeSyncAdapter
                .STATUS_USERS_UNKNOWN)+" "+userStatus);
        if (userStatus == BeeSyncAdapter.STATUS_USERS_UNKNOWN)
            startActivity(new Intent(this, SettingsActivity.class));

        if (savedInstanceState == null)
            mMainUri = BeeContract.ApiaryEntry
                    .buildApiariesViewUri(Utility.getPreferredDatabase(this), Utility
                            .getPreferredUserId(this));
        else{
            mMainUri = savedInstanceState.getParcelable(MAIN_URI);
        }
        mMainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        mMainFragment.setMainUri(mMainUri);


    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        Uri currentMainUri = BeeContract.ApiaryEntry
                .buildApiariesViewUri(Utility.getPreferredDatabase(this), Utility.getPreferredUserId
                        (this));
        if (!Utility.compareUris(mMainUri, currentMainUri)) {
            Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
            onMainUriChange(currentMainUri);
        }
    }

    private void onMainUriChange(Uri newUri) {
        mMainUri = newUri;
        mMainFragment.setMainUri(mMainUri);
        mMainFragment.onMainUriChange();
    }


    @Override
    public void onItemSelected(Uri uri) {
        Intent intent = new Intent(this, BeehousesActivity.class);
        intent.setData(uri);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MAIN_URI, mMainUri);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
    }

}
