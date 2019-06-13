package com.example.orbis;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class GoogleMapsFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        View.OnClickListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code = 99;

    View view; //stores view
    MainActivity main; //stores our main activity
    Context context; //stores context
    MarkerOptions currentLocationMarker; // stores the marker of current location

    API api;

    /**
     * This method creates the view for the map
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //assign all variables
        view = inflater.inflate(R.layout.activity_google_maps, container, false);
        main = ((MainActivity) getActivity());
        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        context = getContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission();
        }

        ImageButton searchAddressButton = view.findViewById(R.id.search_address);
        searchAddressButton.setOnClickListener(this);

        // Get the data of the current user
        api = new API(main.getApplicationContext());
        this.getUserData();

        // Gets the MapView from the XML layout and creates it
        mapFrag.onCreate(savedInstanceState);
        mapFrag.getMapAsync(this);

        EditText addressField = (EditText) view.findViewById(R.id.location_search);
        addressField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * This OnClick method is used to search for places in the search bar
     *
     * @param v
     */
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.search_address:
                performSearch();

                break;


        }

    }

    public void performSearch() {
        EditText addressField = (EditText) this.getActivity().findViewById(R.id.location_search);
        String address = addressField.getText().toString();

        List<Address> addressList = null;
//                MarkerOptions userMarkerOptions = new MarkerOptions();

        if (!TextUtils.isEmpty(address)) {
            Geocoder geocoder = new Geocoder(this.getContext());

            try {
                addressList = geocoder.getFromLocationName(address, 6);

                if (addressList != null) {
                    for (int i = 0; i < addressList.size(); i++) {
                        Address userAddress = addressList.get(i);
                        LatLng latLng = new LatLng(userAddress.getLatitude(), userAddress.getLongitude());

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
                    }
                }
                else {
                    Toast.makeText(this.getContext(), "Location not found...", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this.getContext(), "Search for any location...", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method handles location permissions on the startup of the map
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);

        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            buildGoogleApiClient();

            mMap.setMyLocationEnabled(true);

        }


    }

    /**
     * This method checks if the user has given permission to use the device's location
     *
     * @return
     */

    public boolean checkUserLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * This method creates the pop up that lets the user give or deny permission
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Request_User_Location_Code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this.getContext(), "Permission Denied...", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    /**
     * This method makes the connection with the google api
     */

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }

    /**
     * This method updates the current location of the user and  moves the camera in the direction which you are heading, it also creates a marker on the current location
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;


        if (currentUserLocationMarker != null) {
            currentUserLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 3);
        mMap.moveCamera(cameraUpdate);


        currentLocationMarker = new MarkerOptions();
        currentLocationMarker.position(latLng);
        currentLocationMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        currentUserLocationMarker = mMap.addMarker(currentLocationMarker);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(11));

        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

        }
    }

    /**
     * This method gets the current location and sets an interval for how long it waits to get it.
     *
     * @param bundle
     */

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(200);
        locationRequest.setFastestInterval(200);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }


    }

    /**
     * This method connects our api so that we get the "map" data from our current user, which is the information needed to create markers on the map
     *
     * @throws org.json.JSONException
     */
    public void getUserData() {
        String url = "map/";

        JSONObject jsonBody = new JSONObject();

        api.request(url, jsonBody, new APICallback() {
            @SuppressLint("NewApi")
            @Override
            public void onSuccessResponse (JSONObject response){
                try {
                    outputData(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * This method actually creates the markers with the data we collected in the method above
     *
     * @param object
     * @throws JSONException
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void outputData(JSONObject object) throws JSONException {
        JSONObject error = object.getJSONObject("error");
        JSONObject data = object.getJSONObject("data");


        if(!error.getBoolean("error")){

            JSONArray key = data.names();
            for (int i = 0; i < key.length(); ++i) {
                String keys = key.getString(i);
                JSONObject memory = data.getJSONObject(keys);

                LatLng cords = new LatLng(memory.getDouble("latitude"), memory.getDouble("longitude"));

                mMap.addMarker(new MarkerOptions()
                        .position(cords)
                        .title(memory.getString("title"))).setTag(memory.getInt("id"));

            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * This method makes sure that when we click on a marker, the information window shows up
     *
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(final Marker marker)
    {
        if (!marker.equals(currentUserLocationMarker))
            marker.showInfoWindow();

        return true;
    }

    /**
     * THis method makes sure that when the information window is clicked, it leads you to the memory
     *
     * @param marker
     */

    @Override
    public void onInfoWindowClick(final Marker marker) {
        Log.i("MAP", "Info window clicked");

        if (!marker.equals(currentUserLocationMarker))
            main.switchToMemory((Integer)marker.getTag());
    }
}



