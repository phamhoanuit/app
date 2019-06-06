package com.example.firebaseexample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.firebaseexample.Config;
import com.example.firebaseexample.Instance;
import com.example.firebaseexample.R;
import com.example.firebaseexample.adapter.PlaceSearchAdapter;
import com.example.firebaseexample.google_api.GooglePlaceSearchApiClient;
import com.example.firebaseexample.google_api.GooglePlaceSearchInterface;
import com.example.firebaseexample.model.ResultPlace;
import com.example.firebaseexample.model.TextSearch;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SearchPlaceActivity extends AppCompatActivity  {

    @BindView(R.id.edt_search_places)
    EditText edtSearchPlace;
    @BindView(R.id.rcv_find_places)
    RecyclerView recyclerView;
    @BindView(R.id.img_clear)
    ImageView imgClear;
    @BindView(R.id.progress_bar_searchs)
    ProgressBar progressBar;
    GooglePlaceSearchInterface googlePlaceSearchInterface;
    List<ResultPlace> resultPlaceList;
    PlaceSearchAdapter adapter;
    Runnable requestGeoApi;
    Handler requestHandler = new Handler();
    private static final int TIME_REQUEST = 800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_place);
        ButterKnife.bind(this);
        textSearchQuery();
    }


    private void searchNewPlace(String query) {
        Intent intent = getIntent();
        final double lat = intent.getDoubleExtra("LAT_CURRENT", 0);
        final double lng = intent.getDoubleExtra("LNG_CURRENT", 0);
        googlePlaceSearchInterface = GooglePlaceSearchApiClient.getGGMapService();
        googlePlaceSearchInterface.searchQuery(getString(R.string.google_map_api_key),
                lat + "," + lng,
                Config.RADIUS,
                Config.REGION,
                Config.LANGUAGE,
                query).enqueue(new Callback<TextSearch>() {
            @Override
            public void onResponse(Response<TextSearch> response, Retrofit retrofit) {
                   if(response.isSuccess()){
                       Log.d("LAT_CURRENT", "onResponse: google key" + getString(R.string.google_map_api_key));
                       Log.d("LAT_CURRENT", "onResponse: lat + lng" + lat + "\t" + lng);
                       resultPlaceList = new ArrayList<>();
                       resultPlaceList = response.body().results;
                       Log.d("LAT_CURRENT", "onResponse: " + resultPlaceList.size());
                       adapter = new PlaceSearchAdapter(resultPlaceList, SearchPlaceActivity.this);
                       initRecycleView(adapter);
                       progressBar.setVisibility(View.INVISIBLE);
                   }else{
                       Log.d("ERROR", "onResponse: không thể tìm thấy vị trí" );
                       progressBar.setVisibility(View.INVISIBLE);
                   }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("ERROR", "onFailure: " + t.getMessage());
            }
        });
    }

    private void initRecycleView(PlaceSearchAdapter adapter){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void textSearchQuery(){
        edtSearchPlace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (requestGeoApi != null) {
                    requestHandler.removeCallbacks(requestGeoApi);
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (edtSearchPlace.getText().length() >= 3) {
                    requestGeoApi = new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.VISIBLE);
                            searchNewPlace(s.toString());
                        }
                    };
                    requestHandler.postDelayed(requestGeoApi, TIME_REQUEST);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
