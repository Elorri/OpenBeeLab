package com.example.android.openbeelab.retrofit;

/**
 * Created by Elorri on 24/11/2015.
 */

import android.content.Context;
import android.util.Log;

import com.example.android.openbeelab.R;
import com.example.android.openbeelab.Utility;
import com.example.android.openbeelab.pojo.Apiary;
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

        @GET("/{user_db}/_design/apiaries/_view/by_name")
        Call<ApiaryResults> getJsonApiaries(@Path("user_db") String userDB);

        @GET("/{user_db}/_design/beehouses/_view/by_apiary")
        Call<BeehouseResults> getJsonBeehouses(@Path("user_db") String userDB);

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
                    //TODO right now users without name mentionned are not added in my db.
                    if ((row.value._id != null) && (row.value.name != null))
                        users.add(new User(row.value._id, row.value.name, database));
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


    public static List<Apiary> getApiaries(Context context, String database) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(JsonCall.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // Create an instance of our OpenBeelab API interface.
            OpenBeelab openBeelab = retrofit.create(OpenBeelab.class);


            Call<ApiaryResults> call = openBeelab.getJsonApiaries(database);
            ApiaryResults apiaryResults = call.execute().body();
            final List<Apiary> apiaries = new ArrayList<>();
            if (apiaryResults != null) {
                for (ApiaryRowObject row : apiaryResults.rows) {
                    Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
                    //TODO right now apiaries without beekeepers mentionned are not added in my db. See with Remy
                    if ((row.value._id != null) && (row.value.name != null)
                            && (row.value.beekeepers != null))
                        apiaries.add(new Apiary(row.value._id, row.value.name, row.value.beekeepers));
                }
            } else {
                //TODO use ErrorObject somehow here
                //if ErrorObject not null
                Utility.setServeurStatus(context, BeeSyncAdapter.STATUS_SERVEUR_ERROR);
                // else
                //Utility.setServeurStatus(context, BeeSyncAdapter.STATUS_SERVEUR_DOWN);
            }
            return apiaries;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static List<Beehouse> getBeehouses(Context context, String database) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(JsonCall.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // Create an instance of our OpenBeelab API interface.
            OpenBeelab openBeelab = retrofit.create(OpenBeelab.class);


            Call<BeehouseResults> call = openBeelab.getJsonBeehouses(database);
            BeehouseResults beehouseResults = call.execute().body();
            final List<Beehouse> beehouses = new ArrayList<>();
            if (beehouseResults != null) {
                for (BeehouseRowObject row : beehouseResults.rows) {
                    Log.e("Lifecycle", Thread.currentThread().getStackTrace()[2] + "");
                    //TODO right now beehouses without name mentionned are not added in my db.
                    if ((row.value._id != null) && (row.value.name != null)
                            && (row.value.apiary_id != null))
                        beehouses.add(new Beehouse(row.value._id, row.value.name, row.value.apiary_id));
                }
            } else {
                //TODO use ErrorObject somehow here
                //if ErrorObject not null
                Utility.setServeurStatus(context, BeeSyncAdapter.STATUS_SERVEUR_ERROR);
                // else
                //Utility.setServeurStatus(context, BeeSyncAdapter.STATUS_SERVEUR_DOWN);
            }
            return beehouses;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
                    //TODO right now measure without key and values mentionned are not added in my db.
                    if ((row.key != null) && (row.value != null))
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