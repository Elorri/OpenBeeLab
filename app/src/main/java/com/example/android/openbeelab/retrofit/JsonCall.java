package com.example.android.openbeelab.retrofit;

/**
 * Created by Elorri on 24/11/2015.
 */

import android.content.Context;
import android.util.Log;

import com.example.android.openbeelab.R;
import com.example.android.openbeelab.Utility;
import com.example.android.openbeelab.pojo.Beehouse;
import com.example.android.openbeelab.pojo.Measure;
import com.example.android.openbeelab.pojo.User;
import com.example.android.openbeelab.sync.BeeSyncAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public class JsonCall {
    public static final String API_URL = "http://dev.openbeelab.org:5984";


    public interface OpenBeelab {
        @GET("/{user_db}/_design/users/_view/all")
        Call<UserResults> getJsonUsers(@Path("user_db") String userDB);

//        @GET("/{user_db}/_design/_view/{user_name}/beehouses")
//        Call<UserResults> getJsonUsers(
//                @Path("user_name") String userName);

        @GET("/{user_db}/_design/{beehouse_name}/_view/weight_by_week")
        Call<MeasureResults> getJsonWeekAverageMeasure(
                @Path("user_db") String userDB,
                @Path("beehouse_name") String beehouseName,
                @Query("group") String owner,
                @Query("limit") String repo);


    }

    public static String[] getDatabases(Context context) {
        return new String[]{
                context.getResources().getString(R.string.pref_database_option_value_lamine),
                context.getResources().getString(R.string.pref_database_option_value_lamine_dev),
                context.getResources().getString(R.string.pref_database_option_value_fred_db)
        };
    }

    public static List<User> getUsers(Context context, String database) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(JsonCall.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // Create an instance of our OpenBeelab API interface.
            OpenBeelab openBeelab = retrofit.create(OpenBeelab.class);

            // Create a call instance for looking up OpenBeelab getJsonWeekAverageMeasure.
            Call<UserResults> call = openBeelab.getJsonUsers(database);
            UserResults userResults = call.execute().body();
            final List<User> users = new ArrayList<>();
            if (userResults != null) {
                for (UserRowObject row : userResults.rows) {
                    Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
                    users.add(new User(row.value.name, database));
                }
            } else {
                //TODO use ErrorObject somehow here
                //if ErrorObject not null
                Utility.setServeurStatus(context, BeeSyncAdapter.STATUS_SERVEUR_ERROR);
                // else
                //Utility.setServeurStatus(context, BeeSyncAdapter.STATUS_SERVEUR_DOWN);
            }
            return users;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static List<Beehouse> getBeehouses(Context context, String database, long user_id) {
        List<Beehouse> beehouses = new ArrayList<>();
        beehouses.add(new Beehouse("ruche_01", user_id, "la_mine_rucher_01", 23.5d));
        beehouses.add(new Beehouse("ruche_02", user_id, "la_mine_rucher_01", 43.6d));
        beehouses.add(new Beehouse("ruche_03", user_id, "la_mine_rucher_01", 78.5d));
        beehouses.add(new Beehouse("ruche_04", user_id, "la_mine_rucher_01", 98.2d));
        beehouses.add(new Beehouse("ruche_01", user_id, "la_mine_rucher_02", 54.5d));
        beehouses.add(new Beehouse("ruche_02", user_id, "la_mine_rucher_02", 22.8d));
        beehouses.add(new Beehouse("ruche_03", user_id, "la_mine_rucher_02", 37.5d));
        return beehouses;
    }


    public static List<Measure> getLast30DaysMeasures(Context context, String database, long beehouseId, String
            beehouseName) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(JsonCall.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // Create an instance of our OpenBeelab API interface.
            OpenBeelab openBeelab = retrofit.create(OpenBeelab
                    .class);

            // Create a call instance for looking up OpenBeelab getJsonWeekAverageMeasure.
            Call<MeasureResults> call = openBeelab.getJsonWeekAverageMeasure(database, beehouseName, "true", "30");
            MeasureResults measureResults = call.execute().body();
            final List<Measure> measures = new ArrayList<>();
            if (measureResults != null) {
                for (MesureRowObject row : measureResults.rows) {
                    //TODO virer ce static string
                    Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "beehouse.id " + beehouseId);
                    measures.add(new Measure("global_weight", row.key, row.value[0], "Kg", beehouseId));
                }
            } else {
                //TODO use ErrorObject somehow here
                //if ErrorObject not null
                Utility.setServeurStatus(context, BeeSyncAdapter.STATUS_SERVEUR_ERROR);
                // else
                //Utility.setServeurStatus(context, BeeSyncAdapter.STATUS_SERVEUR_DOWN);
            }
            return measures;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}