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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    public ArrayList<SearchItems> exampleList;

    private String lastSearch;

    private RecyclerView mRecyclerView;
    private SearchAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private MainActivity main;
    View view;
    API api;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        main = ((MainActivity) getActivity());
        api = new API(main);

        //toolbar
        Toolbar toolbar = view.findViewById(R.id.toolSearch);
        toolbar.inflateMenu(R.menu.search_menu); //setup menu
        toolbar.setTitle(R.string.search_screen_toolbar_title);
        main.setSupportActionBar(toolbar);

        setHasOptionsMenu(true);

        search("");
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

        Log.i("test", "huts");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.i("Search", s);
                search(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                search("");
                return false;
            }
        });
    }

    private void search(String search){
        if(lastSearch != null && lastSearch.equals(search))
            return;

        lastSearch = search;

        String url = "search_user";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("search", search);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        api.request(url, jsonBody, new APICallback() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                try {
                    onSearchResult(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void onSearchResult(JSONObject object) throws JSONException {
        JSONObject error = object.getJSONObject("error");
        JSONObject data = object.getJSONObject("data");

        if(!error.getBoolean("error")) {
            exampleList = new ArrayList<>();

            JSONArray key = data.names();
            if(key != null) {
                for (int i = 0; i < key.length(); ++i) {
                    String keys = key.getString(i);
                    JSONObject user = data.getJSONObject(keys);
                    JSONObject image = user.getJSONObject("image");

                    exampleList.add(new SearchItems(
                            user.getInt("id"),
                            image.getString("uri"),
                            user.getString("username"),
                            user.getString("name")
                    ));
                }
            }

            mRecyclerView = view.findViewById(R.id.recyclerViewSearch);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mAdapter = new SearchAdapter(exampleList);

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);


            mAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    SearchItems searchItems = exampleList.get(position);
                    Fragment mFragment = new SearchingAccountFragment();

                    //Pass the ID to the memory
                    Bundle bundle = new Bundle(); //bundle stores stuff we want to give to memory
                    bundle.putInt("id", searchItems.getId()); //the id of the memory
                    mFragment.setArguments(bundle); //set the bundle to the arguments of the memory so we can access it from there

                    main.goToFragment(mFragment, 4);
                }
            });
        }

    }
}