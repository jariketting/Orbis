package com.example.orbis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Constraints;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class
MemoryFragment extends Fragment implements OnMapReadyCallback {
    View view; //stores view
    Toolbar toolbar; //stores toolbar (green thing on top of layout)
    MainActivity main; //stores our main activity
    Context context; //stores context

    API api; //store api

    //image gallery
    ImageView imageView; //stores image view (where current image is shown)
    int imageGalleryIndex; //index number of current shown image
    List<String> imageGallery;
    ViewGroup.LayoutParams imageViewLayoutParams; //stores the original layout params of the image view
    boolean isImageFitToScreen; //true if image is full screen, false if not.

    //fields
    Integer id;
    TextView textViewDate;
    TextView textViewTitle;
    TextView textViewDescription;

    private GoogleMap mMap; //stores map
    SupportMapFragment mapFragment; //stores map fragment

    /**
     * Setup fragment
     *
     * @param inflater ...
     * @param container ...
     * @param savedInstanceState ...
     * @return view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //assign all variables
        view = inflater.inflate(R.layout.fragment_memory, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        main = ((MainActivity) getActivity());
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        context = getContext();

        //hide content to show loader
        view.findViewById(R.id.contentPanel).setVisibility(View.INVISIBLE);

        api = new API(main.getApplicationContext()); //create new api

        //set stuff up
        setupToolbar();

        //assign map
        mapFragment.getMapAsync(this);

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Set up the fields by getting the memory from database
     */
    public void setUpFields() {
        id = getArguments().getInt("id");

        //set date title and description
        textViewDate = view.findViewById(R.id.textViewDate);
        textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewDescription = view.findViewById(R.id.textViewDescription);

        String url = "memory/get/" + id; //build url with id

        JSONObject jsonBody = new JSONObject(); //create parameters object
        //no parameters needed

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
            textViewDate.setText(data.getString("datetime")); //set datetime
            textViewTitle.setText(data.getString("title")); //set title
            textViewDescription.setText(data.getString("description")); //set description

            imageGallery = new ArrayList<>();

            JSONObject images = data.getJSONObject("images");
            JSONArray key = images.names();
            if(key != null) {
                for (int i = 0; i < key.length (); ++i) {
                    String keys = key.getString(i);
                    JSONObject image = images.getJSONObject(keys);

                    imageGallery.add(image.getString("uri"));
                }
            }

            setupImageGallery();

            LatLng cords = new LatLng(data.getDouble("latitude"), data.getDouble("longitude"));

            MarkerOptions marker = new MarkerOptions();
            marker.position(cords);
            marker.title(data.getString("title")); //add title to marker

            mMap.addMarker(marker).showInfoWindow(); //show marker

            // Updates the location and zoom of the MapView
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(cords, 15);
            mMap.moveCamera(cameraUpdate);
        } else
            toolbar.setSubtitle(R.string.memory_not_found); //set title of memory

        //loading done, show content
        view.findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.contentPanel).setVisibility(View.VISIBLE);
    }

    /**
     * Everything for the image gallery
     */
    public void setupImageGallery() {
        //create left and right gallery button listeners
        ImageButton leftButton = view.findViewById(R.id.imageButtonLeft);
        ImageButton rightButton = view.findViewById(R.id.imageButtonRight);

        if(imageGallery.size() < 1) {
            leftButton.setVisibility(View.INVISIBLE);
            rightButton.setVisibility(View.INVISIBLE);

            return;
        }

        imageView = view.findViewById(R.id.imageViewGallery); //get cancel button by view ID
        imageViewLayoutParams = imageView.getLayoutParams();

        //list with images in image gallery
        imageGalleryIndex = 0;

        //set default image
        String path = imageGallery.get(imageGalleryIndex);
        Picasso.get()
                .load(path)
                .into(imageView);

        //right button pressed
        if(imageGallery.size() > 1) {
            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int max = imageGallery.size(); //get size of image gallery

                    //prevent from going past amount in gallery
                    if (imageGalleryIndex < max - 1) {
                        imageGalleryIndex++; //next index item
                        String path = imageGallery.get(imageGalleryIndex);
                        Picasso.get()
                                .load(path)
                                .into(imageView); //update image

                        view.findViewById(R.id.imageButtonLeft).setVisibility(View.VISIBLE);

                        if(imageGalleryIndex >= max - 1)
                            view.findViewById(R.id.imageButtonRight).setVisibility(View.INVISIBLE);
                    }
                }
            });
        } else {
            rightButton.setVisibility(View.INVISIBLE);
        }

        leftButton.setVisibility(View.INVISIBLE);

        if(imageGallery.size() > 1) {
            //left button pressed
            leftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //prevent from going past amount in gallery
                    if(imageGalleryIndex > 0) {
                        imageGalleryIndex--; //previous image item
                        String path = imageGallery.get(imageGalleryIndex);
                        Picasso.get()
                                .load(path)
                                .into(imageView); //update image

                        view.findViewById(R.id.imageButtonRight).setVisibility(View.VISIBLE);

                        if(imageGalleryIndex < 1)
                            view.findViewById(R.id.imageButtonLeft).setVisibility(View.INVISIBLE);
                    }
                }
            });
        } else {
            leftButton.setVisibility(View.INVISIBLE);
        }


        //create listener
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if image is already sized up
                if(isImageFitToScreen) {
                    isImageFitToScreen = false; //image is not fit to screen
                    imageView.setLayoutParams(imageViewLayoutParams);
                    imageView.setAdjustViewBounds(true);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setBackgroundColor(Color.rgb(255, 255, 255));
                } else {
                    isImageFitToScreen = true; //image is fit to screen
                    imageView.setLayoutParams(new Constraints.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setBackgroundColor(Color.rgb(0, 0, 0));
                }
            }
        });
    }

    /**
     * Set up toolbar
     */
    @SuppressLint("PrivateResource") //stop stupid error
    public void setupToolbar() {
        toolbar.setTitle(context.getResources().getString(R.string.memory_toolbar_title)); //set toolbar title
        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material); //set back arrow
        toolbar.inflateMenu(R.menu.memory_menu); //setup menu

        //menu items clicked listener
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //check what item was clicked
                switch (item.getItemId()) {
                    case R.id.share:
                        //TODO implement share feature
                        break;
                    case R.id.edit:
                        Fragment newFragment = new NewFragment();

                        //Pass the ID to the memory
                        Bundle bundle = new Bundle(); //bundle stores stuff we want to give to memory
                        bundle.putInt("id", id); //the id of the memory
                        newFragment.setArguments(bundle); //set the bundle to the arguments of the memory so we can access it from there

                        main.goToFragment(newFragment, 2);
                        break;
                    case R.id.delete:
                        delete();
                        break;
                }

                return true;
            }
        });

        //setup back button in toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.goToLastFragment();
            }
        });
    }

    /**
     * Delete button pressed
     */
    public void delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", deleteDialogClickListener).setNegativeButton("No", deleteDialogClickListener).show();
    }

    DialogInterface.OnClickListener deleteDialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    String url = "memory/delete/" + id;

                    JSONObject jsonBody = new JSONObject();

                    api.request(url, jsonBody, new APICallback() {
                        @Override
                        public void onSuccessResponse(JSONObject response) {
                            try {
                                onDeleteResponse(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    public void onDeleteResponse(JSONObject response) throws JSONException {
        JSONObject error = response.getJSONObject("error");

        if(!error.getBoolean("error")) {
            Fragment fragment = new GoogleMapsFragment();

            main.goToFragment(fragment, 0);
        }
    }

    /**
     * Create map and update on ready
     * @param map
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        setUpFields();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}
