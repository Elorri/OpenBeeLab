package com.example.android.openbeelab;

/**
 * Created by Elorri on 24/11/2015.
 */
import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

public final class NetworkService {
    public static final String API_URL = "https://api.github.com";

    public static class Contributor {
        public final String login;
        public final int contributions;

        public Contributor(String login, int contributions) {
            this.login = login;
            this.contributions = contributions;
        }
    }

    public interface GitHub {
        @GET("/repos/{owner}/{repo}/contributors")
        Call<List<Contributor>> contributors(
                @Path("owner") String owner,
                @Path("repo") String repo);
    }


}