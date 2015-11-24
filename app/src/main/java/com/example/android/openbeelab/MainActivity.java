package com.example.android.openbeelab;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BackgroundTask movieTask = new BackgroundTask();
        movieTask.execute();

    }


    public class BackgroundTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            return givesContributors();
        }

        @Override
        protected void onPostExecute(Void result) {
            if (result != null) {

            }
        }


    }

    private Void givesContributors(){
        try {
            // Create a very simple REST adapter which points the GitHub API.
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkService.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // Create an instance of our GitHub API interface.
            NetworkService.GitHub github = retrofit.create(NetworkService.GitHub.class);

            // Create a call instance for looking up Retrofit contributors.
            Call<List<NetworkService.Contributor>> call = github.contributors("square", "retrofit");

            // Fetch and print a list of the contributors to the library.
            List<NetworkService.Contributor> contributors = call.execute().body();
            for (NetworkService.Contributor contributor : contributors) {
                Log.e("qqc",contributor.login + " (" + contributor.contributions + ")");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
