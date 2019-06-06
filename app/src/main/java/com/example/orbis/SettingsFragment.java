package com.example.orbis;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

public class SettingsFragment extends Fragment {
    MainActivity main; //store main activity

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        main = ((MainActivity) getActivity());

        //toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbarSettings);
        toolbar.setTitle(R.string.toolbarSettings);
        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material); //set back arrow
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.goToLastFragment();
            }
        });


        //invite a friend button
        TextView TextInvitefriends = (TextView) view.findViewById(R.id.TextInvitefriends);
        TextInvitefriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new Invite_a_friendFragment()).addToBackStack(null).commit();
            }
        });

        //help button
        TextView TextHelp = (TextView) view.findViewById(R.id.TextHelp);
        TextHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new HelpFragment()).addToBackStack(null).commit();
            }
        });

        //about button
        TextView TextAbout = (TextView) view.findViewById(R.id.TextAbout);
        TextAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new AboutFragment()).addToBackStack(null).commit();
            }
        });

        //change password button
        TextView TextChangepassword = (TextView) view.findViewById(R.id.TextChangepassword);
        TextChangepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new Change_passwordFragment()).addToBackStack(null).commit();
            }
        });


        return view;

    }
}
