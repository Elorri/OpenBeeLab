package com.example.android.openbeelab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Elorri on 03/01/2016.
 */
public class BeehousesActivity extends AppCompatActivity implements BeehousesFragment.Callback {

    private Uri mUri;
    static final String URI = "mUri";
    private BeehousesFragment mBeehousesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beehouses_activity);

        if (savedInstanceState == null)
            mUri = getIntent().getData();
        else{
            mUri = savedInstanceState.getParcelable(URI);
        }
        mBeehousesFragment = (BeehousesFragment) getSupportFragmentManager().findFragmentById(R.id
                .beehouses_fragment);
        mBeehousesFragment.setMainUri(mUri);


    }


    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    public void onItemSelected(Uri uri) {
        Intent intent = new Intent(this, BeehouseOverviewActivity.class);
        intent.setData(uri);
        startActivity(intent);
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(URI, mUri);
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
