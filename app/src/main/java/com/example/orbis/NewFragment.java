package com.example.orbis;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = NewFragment.class.getSimpleName();

    View view; //store view
    Toolbar toolbar; //store view
    MainActivity main; //store main activity

    Integer id; //fragment ID for API

    private GoogleMap mMap;
    SupportMapFragment mapFragment;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_ACCESS_CAMERA = 2;
    private static final int PERMISSIONS_REQUEST_ACCESS_WRITE_EXTERNAL_STORAGE = 3;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    private LatLng mLastClickedCords;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    EditText title; //edit text for title
    EditText date; //edit text for date
    EditText time; //edit text for time
    EditText description; //edit text for description

    API api; //api

    Bundle arguments;

    /**
     * Setup when view is created
     *
     * @param inflater idk what this is
     * @param container same
     * @param savedInstanceState saved instance state
     * @return created view
     */
    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new, container, false); //assign view
        toolbar = view.findViewById(R.id.toolbar); //assign toolbar (green bar on top)
        main = ((MainActivity) getActivity()); //assign main activity
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map); //assign map
        api = new API(main.getApplicationContext()); //create api

        //hide content panel, to show loading icon
        view.findViewById(R.id.contentPanel).setVisibility(View.INVISIBLE);

        //assign title, date, time and description
        title = view.findViewById(R.id.editTextTItle);
        date = view.findViewById(R.id.editTextDate);
        time = view.findViewById(R.id.editTextTime);
        description = view.findViewById(R.id.editTextDescription);

        //create date and time format
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        //set the default values for the date and time to the current datetime
        date.setText(dateFormat.format(new Date()));
        time.setText(timeFormat.format(new Date()));

        //get bundle arguments to extract id
        arguments = getArguments();

        //check if arguments are given, if so also if the id is given
        if (arguments != null && arguments.containsKey("id")) {
            id = getArguments().getInt("id"); //set id to given argument id

            //change text in newButton
            Button newButton = view.findViewById(R.id.buttonAddMemory);
            newButton.setText(R.string.new_fragment_save_memory);
        }

        //hide navigation
        main.hideNav();

        //set stuff up
        setupToolbar();
        setupCancelButton();
        setupImageButton();
        setupAddMemoryButton();

        //TODO setup image/video selector

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null)
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(main);

        //assign map
        mapFragment.getMapAsync(this);

        return view;
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }


    /**
     * Setup toolbar
     */
    @SuppressLint("PrivateResource")
    public void setupToolbar() {
        //set title based on edit or new
        if(id != null)
            toolbar.setTitle(R.string.new_fragment_toolbar_title_edit);
        else
            toolbar.setTitle(R.string.new_fragment_toolbar_title);

        //set back button
        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);

        //set onclick listener to go back when back button pressed
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.goToLastFragment(); //go back to the last fragment
            }
        });
    }


    /**
     * On add media button
     */
    public void setupImageButton() {
        Button ImageButton = view.findViewById(R.id.buttonAddMedia);

        //create listener
        ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ImageSelector();

                if(arguments == null)
                    arguments = new Bundle();

                arguments.putString("title", title.getText().toString());
                arguments.putString("date", date.getText().toString());
                arguments.putString("time", time.getText().toString());
                arguments.putString("description", description.getText().toString());

                //check if location was clicked, else use current location
                if(mLastClickedCords != null) {
                    arguments.putDouble("lat", mLastClickedCords.latitude);
                    arguments.putDouble("long", mLastClickedCords.longitude);
                } else {
                    arguments.putDouble("lat", mLastKnownLocation.getLatitude());
                    arguments.putDouble("long", mLastKnownLocation.getLongitude());
                }

                fragment.setArguments(arguments);

                main.getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit(); //start and commit transaction to new fragment
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
                main.goToLastFragment(); //go back to the last fragment
            }
        });
    }

    /**
     * Retrieve memory from API and assign to fields
     */
    public void setUpFields() {
        String url = "memory/get/" + id; //build url with memory ID

        JSONObject jsonBody = new JSONObject(); //store parameters
        //no parameters needed for this request

        //make request
        api.request(url, jsonBody, new APICallback() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                try {
                    onMemoryResponse(response); //handle response
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * Handle on memory response
     *
     * @param response
     * @throws JSONException
     */
    public void onMemoryResponse(JSONObject response) throws JSONException {
        JSONObject error = response.getJSONObject("error"); //get error
        JSONObject data = response.getJSONObject("data"); //get data

        //check if error
        if(!error.getBoolean("error")) {
            toolbar.setSubtitle(data.getString("title")); //set title of memory

            List<ImageItem> imageItemList = new ArrayList<>();

            JSONObject images = data.getJSONObject("images");
            JSONArray key = images.names();
            if(key != null) {
                for (int i = 0; i < key.length (); ++i) {
                    String keys = key.getString(i);
                    JSONObject image = images.getJSONObject(keys);

                    imageItemList.add(new ImageItem(image.getInt("id"), image.getString("uri")));
                }

                ArrayList<ImageItem> imageItemsArray = new ArrayList<>(imageItemList.size());
                imageItemsArray.addAll(imageItemList);
                arguments.putSerializable("images", imageItemsArray);
            }

            //create cords from lat and long
            LatLng cords = new LatLng(data.getDouble("latitude"), data.getDouble("longitude"));

            setFields(
                    data.getString("title"),
                    data.getString("datetime").substring(0, 10),
                    data.getString("datetime").substring(11),
                    data.getString("description"),
                    cords);
        } else
            toolbar.setSubtitle(main.getResources().getString(R.string.memory_not_found)); //set title of memory

        //loading is done. Hide loader and show content
        loaded();
    }

    /**
     * Set fields
     *
     * @param dataTitle
     * @param dataDate
     * @param dataTime
     * @param dataDescription
     * @param dataCords
     */
    private void setFields(String dataTitle, String dataDate, String dataTime, String dataDescription, LatLng dataCords) {
        //set date and time
        date.setText(dataDate);
        time.setText(dataTime);

        //set title and description
        title.setText(dataTitle);
        description.setText(dataDescription);

        //set last clicked cords to the memory one
        mLastClickedCords = dataCords;

        //create marker to existing position
        MarkerOptions marker = new MarkerOptions();
        marker.position(dataCords);
        marker.title(dataTitle);

        mMap.addMarker(marker).showInfoWindow(); //show info window

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(dataCords, 15);
        mMap.moveCamera(cameraUpdate);
    }

    /**
     * Set up memory button
     *
     * When clicked, add a new memory and handle request
     */
    public void setupAddMemoryButton() {
        Button AddMemoryButton = view.findViewById(R.id.buttonAddMemory); //add memory button

        //add listener for clicked
        AddMemoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url; //stores request url

                //block request if title, date or text are not set
                if(title.getText().length() < 1 || date.getText().length() < 1 || time.getText().length() < 1)
                    return;

                //if id is given update, else add new
                if(id != null)
                    url = "memory/update/"+id;
                else
                    url = "memory/add/";

                //assign values to json body
                JSONObject jsonBody = new JSONObject();
                JSONArray jsonImages = new JSONArray();

                if(arguments != null && arguments.containsKey("images")) {
                    List<ImageItem> images = (ArrayList<ImageItem>)arguments.getSerializable("images");

                    for(int i = 0; i < images.size(); i++) {
                        jsonImages.put(images.get(i).id);
                    }
                }

                try {
                    jsonBody.put("title", title.getText()); //title
                    jsonBody.put("description", description.getText()); //description
                    jsonBody.put("datetime", date.getText() + " " + time.getText()); //date and time

                    //check if location was clicked, else use current location
                    if(mLastClickedCords != null) {
                        jsonBody.put("longitude", mLastClickedCords.longitude);
                        jsonBody.put("latitude", mLastClickedCords.latitude);
                    } else {
                        jsonBody.put("longitude", mLastKnownLocation.getLongitude());
                        jsonBody.put("latitude", mLastKnownLocation.getLatitude());
                    }

                    jsonBody.put("images", jsonImages);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //make request
                api.request(url, jsonBody, new APICallback() {
                    @Override
                    public void onSuccessResponse(JSONObject response) {
                        try {
                            onAddMemoryResponse(response); //handle response
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    /**
     * Handles on add/update memory response
     *
     * @param response
     * @throws JSONException
     */
    public void onAddMemoryResponse(JSONObject response) throws JSONException {
        JSONObject error = response.getJSONObject("error"); //get erro
        JSONObject data = response.getJSONObject("data"); //get data

        //only do stuff when no error
        if(!error.getBoolean("error")) {
            Fragment memoryFragment = new MemoryFragment(); //create new memory fragment

            //Pass the ID to the memory
            Bundle bundle = new Bundle(); //bundle stores stuff we want to give to memory
            bundle.putInt("id", data.getInt("id")); //the id of the memory
            memoryFragment.setArguments(bundle); //set the bundle to the arguments of the memory so we can access it from there

            //go to memory fragment with new id assigned
            main.goToFragment(memoryFragment, 1);
            main.showNav(); //show navigation
        }
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        if(arguments != null &&
            arguments.containsKey("title") &&
            arguments.containsKey("date") &&
            arguments.containsKey("time") &&
            arguments.containsKey("description") &&
            arguments.containsKey("lat") &&
            arguments.containsKey("long")) {

            LatLng cords = new LatLng(arguments.getDouble("lat"), arguments.getDouble("long"));

            setFields(
                getArguments().getString("title"),
                getArguments().getString("date"),
                getArguments().getString("time"),
                getArguments().getString("description"),
                cords
            );

            loaded();
        } else if(id != null)
            setUpFields();
        else {
            getDeviceLocation();
            loaded();
        }


        //create onClick listener
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mLastClickedCords = latLng; //set last clicked cords

                mMap.clear(); //remove any existing markers

                //create new marker with position that's clicked
                MarkerOptions marker = new MarkerOptions();
                marker.position(latLng);

                mMap.addMarker(marker).showInfoWindow(); //show info window
            }
        });
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(main, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(main.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(main,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }

        updateLocationUI(); //update UI
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        //check if mMap is set.
        if (mMap == null)
            return; //do nothing

        //tru to get location permission
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void loaded() {
        view.findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.contentPanel).setVisibility(View.VISIBLE);
    }
}
