package com.example.orbis;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SearchFragment extends Fragment {

    MainActivity main;
    ListView search_people;
    ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        main = ((MainActivity) getActivity());

        //Toolbar
        Toolbar toolbar = view.findViewById(R.id.toolSearch);
        toolbar.inflateMenu(R.menu.search_menu); //setup menu
        toolbar.setTitle(R.string.search_screen_toolbar_title);

        search_people = (ListView) view.findViewById(R.id.search_bar);

        List<String> arrayPeople = (Arrays.asList(getResources().getStringArray(R.array.all_people)));

        adapter = new ArrayAdapter<String >(getActivity(), R.layout.support_simple_spinner_dropdown_item, arrayPeople);

        search_people.setAdapter(adapter);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.findItem(R.id.search_people);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


    }
}
