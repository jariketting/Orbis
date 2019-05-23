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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class
MemoryFragment extends Fragment implements OnMapReadyCallback {
    MapView mapView;
    View view;
    Toolbar toolbar;
    GoogleMap map;
    MainActivity main;
    Context context;

    ImageView imageView;
    int imageGalleryIndex;
    List<Drawable> imageGallery;
    ViewGroup.LayoutParams imageViewLayoutParams;
    boolean isImageFitToScreen;

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
        view = inflater.inflate(R.layout.fragment_memory, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        main = ((MainActivity) getActivity());
        mapView = view.findViewById(R.id.mapView);
        context = getContext();

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
        toolbar.setSubtitle("This is a memory"); //set title of memory
        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material); //set back arrow
        toolbar.inflateMenu(R.menu.memory_menu); //setup menu

        //menu items clicked listener
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //check what item was clicked
                switch (item.getItemId()) {
                    case R.id.share:
                        //do something
                        break;
                    case R.id.edit:
                        main.goToFragment(new NewFragment(), 2);
                        break;
                    case R.id.delete:
                        //do something
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

        Context context = this.getContext();

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
