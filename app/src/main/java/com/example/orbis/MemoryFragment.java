package com.example.orbis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Constraints;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class
MemoryFragment extends Fragment implements OnMapReadyCallback {
    MapView mapView; //stores map view from layout
    View view; //stores view
    Toolbar toolbar; //stores toolbar (green thing on top of layout)
    GoogleMap map; //stores map stuff
    MainActivity main; //stores our main activity
    Context context; //stores contect

    API api;

    //image gallery
    ImageView imageView; //stores image view (where current image is shown)
    int imageGalleryIndex; //index number of current shown image
    List<Drawable> imageGallery; //stores all images in the gallery
    ViewGroup.LayoutParams imageViewLayoutParams; //stores the original layout params of the image view
    boolean isImageFitToScreen; //true if image is full screen, false if not.

    //fields
    Integer id;
    TextView textViewDate;
    TextView textViewTitle;
    TextView textViewDescription;

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
        mapView = view.findViewById(R.id.mapView);
        context = getContext();

        api = new API(main.getApplicationContext());

        //set stuff up
        setupImageGallery();
        setupToolbar();

        // Gets the MapView from the XML layout and creates it
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Inflate the layout for this fragment
        return view;
    }

    /**
     *
     */
    public void setUpFields() {
        id = getArguments().getInt("id");

        textViewDate = view.findViewById(R.id.textViewDate);
        textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewDescription = view.findViewById(R.id.textViewDescription);

        String url = "memory/get/" + id;

        JSONObject jsonBody = new JSONObject();

        api.request(url, jsonBody, new APICallback() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                try {
                    onMemoryResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void onMemoryResponse(JSONObject response) throws JSONException {
        JSONObject error = response.getJSONObject("error");
        JSONObject data = response.getJSONObject("data");

        if(!error.getBoolean("error")) {
            toolbar.setSubtitle(data.getString("title")); //set title of memory
            textViewDate.setText(data.getString("datetime"));
            textViewTitle.setText(data.getString("title"));
            textViewDescription.setText(data.getString("description"));

            LatLng cords = new LatLng(data.getDouble("latitude"), data.getDouble("longitude"));

            // Updates the location and zoom of the MapView
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(cords, 12);
            map.animateCamera(cameraUpdate);
        } else
            toolbar.setSubtitle(main.getResources().getString(R.string.memory_not_found)); //set title of memory
    }

    /**
     * Everything for the image gallery
     */
    public void setupImageGallery() {
        imageView = view.findViewById(R.id.imageViewGallery); //get cancel button by view ID
        imageViewLayoutParams = imageView.getLayoutParams();

        //list with images in image gallery
        imageGallery = new ArrayList<>();
        imageGallery.add(ContextCompat.getDrawable(main, R.drawable.placeholder_cats)); //add placeholder cats
        imageGallery.add(ContextCompat.getDrawable(main, R.drawable.placeholder_kitten)); //add placeholder cats
        imageGallery.add(ContextCompat.getDrawable(main, R.drawable.placeholder_kitten_lick)); //add placeholder cats

        imageGalleryIndex = 0;

        //set default image
        imageView.setImageDrawable(imageGallery.get(imageGalleryIndex));

        //create left and right gallery button listeners
        ImageButton leftButton = view.findViewById(R.id.imageButtonLeft);
        ImageButton rightButton = view.findViewById(R.id.imageButtonRight);

        //right button pressed
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int max = imageGallery.size(); //get size of image gallery

                //prevent from going past amount in gallery
                if(imageGalleryIndex < max-1) {
                    imageGalleryIndex++; //next index item
                    imageView.setImageDrawable(imageGallery.get(imageGalleryIndex)); //update image
                }
            }
        });

        //left button pressed
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //prevent from going past amount in gallery
                if(imageGalleryIndex != 0) {
                    imageGalleryIndex--; //previous image item
                    imageView.setImageDrawable(imageGallery.get(imageGalleryIndex)); //update image
                }
            }
        });


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
                } else {
                    isImageFitToScreen = true; //image is fit to screen
                    imageView.setLayoutParams(new Constraints.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
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
                        main.goToFragment(new NewFragment(), 2);
                        break;
                    case R.id.delete:
                        //TODO implement delete future
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);

        setUpFields();
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
