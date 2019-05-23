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

    ImageView imageView;
    int imageGalleryIndex;
    List<Drawable> imageGallery;
    ViewGroup.LayoutParams imageViewLayoutParams;
    boolean isImageFitToScreen;

    @SuppressLint("PrivateResource")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_memory, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        main = ((MainActivity) getActivity());

        //set stuff up
        setupImageGallery();
        setupToolbar();

        // Gets the MapView from the XML layout and creates it
        mapView = view.findViewById(R.id.mapView);
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

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int max = imageGallery.size();

                if(imageGalleryIndex < max-1) {
                    imageGalleryIndex++;
                    imageView.setImageDrawable(imageGallery.get(imageGalleryIndex));
                }
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageGalleryIndex != 0) {
                    imageGalleryIndex--;
                    imageView.setImageDrawable(imageGallery.get(imageGalleryIndex));
                }
            }
        });


        //create listener
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isImageFitToScreen) {
                    isImageFitToScreen = false;
                    imageView.setLayoutParams(imageViewLayoutParams);
                    imageView.setAdjustViewBounds(true);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {
                    isImageFitToScreen = true;
                    imageView.setLayoutParams(new Constraints.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                }
            }
        });
    }

    /**
     * Set up toolbar
     */
    public void setupToolbar() {
        toolbar.setTitle(getContext().getResources().getString(R.string.memory_toolbar_title) + ": " + "This is a memory");
        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.inflateMenu(R.menu.memory_menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
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
