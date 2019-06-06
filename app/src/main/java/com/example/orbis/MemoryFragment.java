package com.example.orbis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Constraints;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class
MemoryFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = NewFragment.class.getSimpleName();

    View view; //stores view
    Toolbar toolbar; //stores toolbar (green thing on top of layout)
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

    private GoogleMap mMap;
    SupportMapFragment mapFragment;

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

        api = new API(main.getApplicationContext());

        //set stuff up
        setupImageGallery();
        setupToolbar();

        mapFragment.getMapAsync(this);

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

            MarkerOptions marker = new MarkerOptions();
            marker.position(cords);
            marker.title(data.getString("title"));

            mMap.addMarker(marker).showInfoWindow();

            // Updates the location and zoom of the MapView
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(cords, 15);
            mMap.moveCamera(cameraUpdate);
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
