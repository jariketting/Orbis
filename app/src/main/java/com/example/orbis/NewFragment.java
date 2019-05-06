package com.example.orbis;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NewFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new, container, false);

        this.cancelOnClickListener(view);

        return view;
    }

    /**
     * Onclick cancel button listener
     *
     * When clicked will go back to map view
     *
     * @param view view to get cancel button from
     */
    public void cancelOnClickListener(View view) {
        Button CancelButton = view.findViewById(R.id.buttonCancel); //get cancel button by view ID

        //create listener
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment mapFragment = new MapFragment(); //create fragment

                //prevent errors
                if (getFragmentManager() != null)
                    getFragmentManager().beginTransaction().add(R.id.container, mapFragment).commit(); //go back to map fragment
            }
        });
    }
}
