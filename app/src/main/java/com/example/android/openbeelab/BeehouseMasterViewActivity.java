package com.example.android.openbeelab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Elorri on 11/12/2015.
 */
public class BeehouseMasterViewActivity extends AppCompatActivity implements BeehouseMasterViewFragment.Callback {

    Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + ""+savedInstanceState);
        setContentView(R.layout.beehouse_view_activity);

        if (savedInstanceState == null) {
            Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
            Bundle arguments = new Bundle();
            mUri = getIntent().getData();
            arguments.putParcelable(BeehouseMasterViewFragment.BEEHOUSE_VIEW_URI, mUri);

            BeehouseMasterViewFragment fragment = new BeehouseMasterViewFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.beehouse_view_fragment_container, fragment)
                    .commit();
        } else mUri = savedInstanceState.getParcelable(BeehouseMasterViewFragment.BEEHOUSE_VIEW_URI);

    }

    @Override
    public void onItemSelected(Uri uri, int position) {
        if(position==0){
            Intent intent = new Intent(this, BeehouseDetailViewActivity.class);
            intent.setData(uri);
            startActivity(intent);
        }
        else{
        Intent intent = new Intent(this, DatawizOverLast30DaysActivity.class);
        intent.setData(uri);
        startActivity(intent);}
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + ""+outState);
        outState.putParcelable(BeehouseMasterViewFragment.BEEHOUSE_VIEW_URI, mUri);

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

