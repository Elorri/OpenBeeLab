package com.example.android.openbeelab.retrofit;

/**
 * Created by Elorri on 24/11/2015.
 */

import com.example.android.openbeelab.pojo.Measure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Query;

public class OpenBeelabNetworkJson {
    public static final String API_URL = "http://dev.openbeelab.org:5984";


    public interface OpenBeelab {
        @GET("/la_mine/_design/ruche_01/_view/weight_by_week")
        Call<MesureResults> weeksShot(
                @Query("group") String owner,
                @Query("limit") String repo);
    }


    public static List<Measure> getMeasures() {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(OpenBeelabNetworkJson.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // Create an instance of our OpenBeelab API interface.
            OpenBeelab openBeelab = retrofit.create(OpenBeelab
                    .class);

            // Create a call instance for looking up OpenBeelab weeksShot.
            Call<MesureResults> call = openBeelab.weeksShot("true", "30");
            MesureResults retrofitResults = call.execute().body();
            final List<Measure> measures = new ArrayList<>();
            for (MesureRowObject row : retrofitResults.rows) {
                //TODO virer ce static string
                 measures.add(new Measure("global_weight",row.key, row.value[0]));
            }
            return measures;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}