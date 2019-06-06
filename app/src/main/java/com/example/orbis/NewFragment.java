package com.example.orbis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

public class NewFragment extends Fragment implements OnMapReadyCallback {
    MapView mapView; //store map view
    GoogleMap map; //store gmap stuff
    View view; //store view
    Toolbar toolbar; //store view
    MainActivity main; //store main activity

    EditText title;
    EditText date;
    EditText time;
    EditText description;

    API api;

    /**
     * Setup when view is created
     *
     * @param inflater idk what this is
     * @param container same
     * @param savedInstanceState saved instance state
     * @return created view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        main = ((MainActivity) getActivity());
        api = new API(main.getApplicationContext());

        title = view.findViewById(R.id.editTextTItle);
        date = view.findViewById(R.id.editTextDate);
        time = view.findViewById(R.id.editTextTime);
        description = view.findViewById(R.id.editTextDescription);

        assert main != null;
        main.hideNav();

        //set stuff up
        setupToolbar();
        setupCancelButton();
        setupAddMemoryButton();

        //TODO setup image/video selector
        //TODO allow user to search and pick a location

        // Gets the MapView from the XML layout and creates it
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    /**
     * Setup toolbar
     */
    @SuppressLint("PrivateResource")
    public void setupToolbar() {
        toolbar.setTitle(R.string.new_fragment_toolbar_title);
        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.goToLastFragment();
            }
        });
    }

    /**
     * Onclick cancel button listener
     *
     * When clicked will go back to map view
     */
    public void setupCancelButton() {
        Button CancelButton = view.findViewById(R.id.buttonCancel); //get cancel button by view ID

        //create listener
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.goToLastFragment();
            }
        });
    }

    /**
     * Set up memory button
     *
     * When clicked, add a new memory and handle request
     */
    public void setupAddMemoryButton() {
        Button AddMemoryButton = view.findViewById(R.id.buttonAddMemory);

        AddMemoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "memory/add/";

                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("title", title.getText());
                    jsonBody.put("description", description.getText());
                    jsonBody.put("datetime", date.getText() + " " + time.getText());
                    jsonBody.put("longitude", 0);
                    jsonBody.put("latitude", 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                api.request(url, jsonBody, new APICallback() {
                    @Override
                    public void onSuccessResponse(JSONObject response) {
                        Log.i("Memory", response.toString());
                    }
                });
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);

        Context context = this.getContext();

        assert context != null;
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
        map.animateCamera(cameraUpdate);
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
