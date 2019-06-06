package com.example.firebaseexample.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firebaseexample.PlaceType;
import com.example.firebaseexample.R;
import com.example.firebaseexample.activity.GoogleMapActivity;
import com.example.firebaseexample.model.ResultPlace;

import java.util.List;

public class PlaceSearchAdapter extends RecyclerView.Adapter<PlaceSearchAdapter.RecylerViewHolder> {
    private List<ResultPlace> listResultPlaces;
    Context context;
    private static final String TAG = PlaceSearchAdapter.class.getSimpleName();

    public PlaceSearchAdapter(List<ResultPlace> listResultPlaces, Context context) {
        this.listResultPlaces = listResultPlaces;
        this.context = context;
    }

    @NonNull
    @Override
    public RecylerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_place_search, null , false);
        return new RecylerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecylerViewHolder recylerViewHolder, int i) {
        final ResultPlace resultPlace = listResultPlaces.get(i);
        recylerViewHolder.txtAddress.setText(resultPlace.formattedAddress);
        recylerViewHolder.txtName.setText(resultPlace.name);
        recylerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoogleMapActivity.class);
                intent.putExtra("PlaceSelected", resultPlace.formattedAddress);
                Log.d(TAG, "onClick: " + resultPlace.formattedAddress);
                ((Activity) context).setResult(Activity.RESULT_OK, intent);
                ((Activity) context).finish();
            }
        });
        if (resultPlace.types != null && !resultPlace.types.isEmpty()) {
            recylerViewHolder.imgPlaceType.setImageResource(PlaceType.getIdImageDrawable(resultPlace.types.get(0), resultPlace.types.get(resultPlace.types.size() - 1)));
        } else {
            recylerViewHolder.imgPlaceType.setImageResource(PlaceType.DEFAULT.value);
        }
    }

    @Override
    public int getItemCount() {
        return listResultPlaces.size();
    }

    public class RecylerViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtAddress;
        ImageView imgPlaceType;

        public RecylerViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_place_return);
            txtAddress = itemView.findViewById(R.id.txt_address_return);
            imgPlaceType = itemView.findViewById(R.id.img_type_return);
        }
    }

}
