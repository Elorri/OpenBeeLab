package com.example.android.openbeelab;

/**
 * Created by Elorri on 24/11/2015.
 */

import com.example.android.openbeelab.Retrofit.WeeksShotResults;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public final class NetworkService {
    public static final String API_URL = "http://dev.openbeelab.org:5984";



    public interface GitHub {
        @GET("/la_mine/_design/ruche_01/_view/weight_by_week")
        Call<WeeksShotResults> weeksShot(
                @Query("group") String owner,
                @Query("limit") String repo);
    }


}