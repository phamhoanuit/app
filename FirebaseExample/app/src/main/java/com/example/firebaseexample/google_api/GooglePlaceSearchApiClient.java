package com.example.firebaseexample.google_api;

import com.example.firebaseexample.Config;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class GooglePlaceSearchApiClient {
    private static Retrofit retrofit = null;

    private static Retrofit getClient(String url) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static GooglePlaceSearchInterface getGGMapService() {
        return getClient(Config.GOOGLE_API_PLACE_BASE_TEXT_SEARCH_URL).create(GooglePlaceSearchInterface.class);
    }
}
