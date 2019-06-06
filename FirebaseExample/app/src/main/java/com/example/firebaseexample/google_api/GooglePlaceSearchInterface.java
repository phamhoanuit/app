package com.example.firebaseexample.google_api;

import com.example.firebaseexample.Config;
import com.example.firebaseexample.model.TextSearch;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface GooglePlaceSearchInterface {
    @GET(Config.GOOGLE_API_PLACE_TEXT_SEARCH_URL)
    Call<TextSearch> searchQuery(@Query("key") String key,
                                 @Query("location") String location,
                                 @Query("radius") String radius,
                                 @Query("region") String region,
                                 @Query("language") String language,
                                 @Query("query") String query);

    @GET(Config.GOOGLE_API_PLACE_TEXT_SEARCH_URL)
    Call<TextSearch> searchFollowingType(@Query("key") String key,
                                         @Query("location") String location,
                                         @Query("radius") String radius,
                                         @Query("region") String region,
                                         @Query("language") String language,
                                         @Query("query") String query,
                                         @Query("type") String type);
}
