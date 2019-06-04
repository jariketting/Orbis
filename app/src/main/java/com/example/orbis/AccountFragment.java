package com.example.orbis;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AccountFragment extends Fragment {
    View view;
    MainActivity main; //stores our main activity

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        main = ((MainActivity) getActivity());

        Button followersButton = view.findViewById(R.id.followersButton); //get cancel button by view ID
        Button followingButton = view.findViewById(R.id.followingButton); //get cancel button by view ID

        //create listener
        followersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), FollowersActivity.class);
                startActivity(intent);
            }

        });

        //create listener
        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity().getApplication(), FollowingActivity.class);
                startActivity(intent1);
            }

        });




        return view;
    }
}
