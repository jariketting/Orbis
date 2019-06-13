package com.example.orbis;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    public ArrayList<SearchItems> exampleList;

    private RecyclerView mRecyclerView;
    private SearchAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private MainActivity main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        main = ((MainActivity) getActivity());

        //toolbar
        Toolbar toolbar = view.findViewById(R.id.toolSearch);
        toolbar.inflateMenu(R.menu.search_menu); //setup menu
        toolbar.setTitle(R.string.search_screen_toolbar_title);

        exampleList = new ArrayList<>();
        exampleList.add(new SearchItems(R.drawable.ic_person_search, "username 1","fullname 1"));
        exampleList.add(new SearchItems(R.drawable.ic_person_search, "username 2","fullname 2"));
        exampleList.add(new SearchItems(R.drawable.ic_person_search, "username 3","fullname 3"));
        exampleList.add(new SearchItems(R.drawable.ic_person_search, "username 4","fullname 4"));
        exampleList.add(new SearchItems(R.drawable.ic_person_search, "username 5","fullname 5"));
        exampleList.add(new SearchItems(R.drawable.ic_person_search, "username 6","fullname 6"));
        exampleList.add(new SearchItems(R.drawable.ic_person_search, "username 7","fullname 7"));
        exampleList.add(new SearchItems(R.drawable.ic_person_search, "username 8","fullname 8"));
        exampleList.add(new SearchItems(R.drawable.ic_person_search, "username 9","fullname 9"));
        exampleList.add(new SearchItems(R.drawable.ic_person_search, "username 10","fullname 10"));


        mRecyclerView = view.findViewById(R.id.recyclerViewSearch);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new SearchAdapter(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                exampleList.get(position);
                Fragment mFragment = new SearchingAccountFragment();

                //Pass the ID to the memory
                Bundle bundle = new Bundle(); //bundle stores stuff we want to give to memory
                bundle.putInt("id", 1); //the id of the memory
                mFragment.setArguments(bundle); //set the bundle to the arguments of the memory so we can access it from there

                main.goToFragment(mFragment, 1);
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.searchSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return false;
            }
        });


    }
}