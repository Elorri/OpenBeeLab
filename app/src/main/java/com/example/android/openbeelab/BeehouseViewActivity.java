package com.example.android.openbeelab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Elorri on 11/12/2015.
 */
public class BeehouseViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beehouse_view_activity);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(BeehouseViewFragment.BEEHOUSE_VIEW_URI, getIntent().getData());

            BeehouseViewFragment fragment = new BeehouseViewFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.beehouse_view_fragment_container, fragment)
                    .commit();
        }

    }
}

