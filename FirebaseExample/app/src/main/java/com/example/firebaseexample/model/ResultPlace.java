package com.example.firebaseexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ResultPlace implements Serializable {
    @SerializedName("formatted_address")
    @Expose
    public String formattedAddress;

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("place_id")
    @Expose
    public String placeId;

    @SerializedName("geometry")
    @Expose
    public Geometry geometry;

    @SerializedName("rating")
    @Expose
    public Double rating;

    @SerializedName("types")
    @Expose
    public List<String> types = null;


    public class Geometry implements Serializable {
        @SerializedName("location")
        @Expose
        public Location location;

    }

    public class Location implements Serializable {
        @SerializedName("lat")
        @Expose
        public Double lat;
        @SerializedName("lng")
        @Expose
        public Double lng;

    }
}
