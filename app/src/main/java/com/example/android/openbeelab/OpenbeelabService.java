package com.example.android.openbeelab;

/**
 * Created by Elorri on 24/11/2015.
 */

import android.util.Log;

import com.example.android.openbeelab.retrofit.MesureResults;
import com.example.android.openbeelab.retrofit.MesureRowObject;
import com.example.android.openbeelab.pojo.Beehive;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Query;

public final class OpenbeelabService {
    public static final String API_URL = "http://dev.openbeelab.org:5984";



    public interface OpenBeeLab {
        @GET("/la_mine/_design/ruche_01/_view/weight_by_week")
        Call<MesureResults> weeksShot(
                @Query("group") String owner,
                @Query("limit") String repo);
    }


     static Void getMesures() {
        // Create a very simple REST adapter which points the OpenBeeLab API.

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OpenbeelabService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of our OpenBeeLab API interface.
        OpenbeelabService.OpenBeeLab github = retrofit.create(OpenbeelabService.OpenBeeLab.class);

        // Create a call instance for looking up Retrofit weeksShot.
        //https://api.github.com/repos/square/retrofit/contributors
        Call<MesureResults> call = github.weeksShot("true", "30");

        call.enqueue(new Callback<MesureResults>() {
            @Override
            public void onResponse(Response<MesureResults> response, Retrofit retrofit) {
                MesureResults retrofitResults = response.body();
                for (MesureRowObject row : retrofitResults.rows) {
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