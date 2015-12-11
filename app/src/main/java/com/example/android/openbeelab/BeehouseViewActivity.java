package com.example.android.openbeelab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Elorri on 11/12/2015.
 */
public class BeehouseViewActivity extends AppCompatActivity implements BeehouseViewFragment.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
        setContentView(R.layout.beehouse_view_activity);

        if (savedInstanceState == null) {
            Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
            Bundle arguments = new Bundle();
            arguments.putParcelable(BeehouseViewFragment.BEEHOUSE_VIEW_URI, getIntent().getData());

            BeehouseViewFragment fragment = new BeehouseViewFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.beehouse_view_fragment_container, fragment)
                    .commit();
        }

    }

    @Override
    public void onItemSelected(Uri uri) {
        Intent intent = new Intent(this, DatawizOverLast30DaysActivity.class);
        intent.setData(uri);
        startActivity(intent);
    }
}

