package com.example.orbis;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ImageSelector extends Fragment {
    View view; //stores view

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //assign all variables
        view = inflater.inflate(R.layout.fragment_image_selector, container, false);

        // Inflate the layout for this fragment
        return view;
    }
}
