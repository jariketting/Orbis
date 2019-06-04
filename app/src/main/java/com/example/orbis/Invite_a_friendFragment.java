package com.example.orbis;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;

//TODO rename class to InviteAFriendFragment plz
public class Invite_a_friendFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invite_a_friend, container, false);

        //toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbarInvite);
        toolbar.setTitle(R.string.settings_invite);

        // Inflate the layout for this fragment
        return view;

    }
}
