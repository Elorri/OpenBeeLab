package com.example.android.openbeelab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Elorri on 03/12/2015.
 */
public class DatawizOverLast30DaysActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(DatawizOverLast30DaysFragment.DETAIL_URI, getIntent().getData());

            DatawizOverLast30DaysFragment fragment = new DatawizOverLast30DaysFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_fragment_container, fragment)
                    .commit();
        }

    }
}
