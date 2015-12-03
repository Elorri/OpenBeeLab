package com.example.android.openbeelab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.openbeelab.db.BeeContract;


public class MainActivity extends AppCompatActivity implements MainFragment.Callback {


    private static final String LOG_TAG = MainActivity.class.getSimpleName();


    private Uri mMainUri;
    static final String MAIN_URI = "mMainUri";
    private MainFragment mMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        if (savedInstanceState == null)
            mMainUri = BeeContract.ApiaryEntry
                    .buildApiariesViewUri(Utility.getPreferredUserDB(this));
        else
            mMainUri = savedInstanceState.getParcelable(MAIN_URI);
        mMainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        mMainFragment.setMainUri(mMainUri);



        //BeeSyncAdapter.initializeSyncAdapter(this);
        //TODO add this whenever we need a manual sync  BeeSyncAdapter.syncImmediately()
    }




    @Override
    public void onItemSelected(Uri uri, boolean firstDisplay) {
        Intent intent = new Intent(this, DetailActivity.class);
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

}
