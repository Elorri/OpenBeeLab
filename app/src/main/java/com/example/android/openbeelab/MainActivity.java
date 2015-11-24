package com.example.android.openbeelab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.openbeelab.Retrofit.WeeksShotResults;
import com.example.android.openbeelab.Retrofit.WeeksShotRowObject;
import com.example.android.openbeelab.pojo.Beehive;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        givesContributors();

    }


    private Void givesContributors() {
        // Create a very simple REST adapter which points the GitHub API.

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of our GitHub API interface.
        NetworkService.GitHub github = retrofit.create(NetworkService.GitHub.class);

        // Create a call instance for looking up Retrofit weeksShot.
        //https://api.github.com/repos/square/retrofit/contributors
        Call<WeeksShotResults> call = github.weeksShot("true", "30");

        call.enqueue(new Callback<WeeksShotResults>() {
            @Override
            public void onResponse(Response<WeeksShotResults> response, Retrofit retrofit) {
                WeeksShotResults retrofitResults = response.body();
                for (WeeksShotRowObject row : retrofitResults.rows) {
                    Log.e("qqc", "row.key " + row.key);
                    Log.e("qqc", "row.value[0] " + row.value[0]);
                    new Beehive(row.key, row.value[0]);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("qqc", "onFailure");
            }
        });

        return null;
    }

}
