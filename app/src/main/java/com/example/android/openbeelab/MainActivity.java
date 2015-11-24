package com.example.android.openbeelab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

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

        // Create a call instance for looking up Retrofit contributors.
        Call<List<NetworkService.Contributor>> call = github.contributors("square", "retrofit");

        call.enqueue(new Callback<List<NetworkService.Contributor>>() {
            @Override
            public void onResponse(Response<List<NetworkService.Contributor>> response, Retrofit retrofit) {
                List<NetworkService.Contributor> contributors = response.body();
                for (NetworkService.Contributor contributor : contributors) {
                    Log.e("qqc", contributor.login + " (" + contributor.contributions + ")");
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
