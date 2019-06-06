package com.example.firebaseexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TextSearch {
    @SerializedName("html_attributions")
    @Expose
    public List<String> htmlAttributions = null;
    @SerializedName("next_page_token")
    @Expose
    public String nextPageToken;
    @SerializedName("results")
    @Expose
    public List<ResultPlace> results = null;
    @SerializedName("status")
    @Expose
    public String status;
}
