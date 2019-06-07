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

public class DiaryFragment extends Fragment {
    public ArrayList<DiaryItems> exampleList;

    private RecyclerView mRecyclerView;
    private DiaryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private MainActivity main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        main = ((MainActivity) getActivity());

        //toolbar
        Toolbar toolbar = view.findViewById(R.id.toolDiary);
        toolbar.inflateMenu(R.menu.diary_menu); //setup menu
        toolbar.setTitle(R.string.diary_screen_toolbar_title);

        exampleList = new ArrayList<>();
        exampleList.add(new DiaryItems(R.drawable.placeholder_diary_android, "I'm going on an adventure!", "Finally! We are going to Hobbiton this afternoon. I've been waiting for this moment for years ...", "24-04-2019"));
        exampleList.add(new DiaryItems(R.drawable.placeholder_diary_ac_unit, "Auckland", "So today we travelled to Auckland, we're stayng in a hotel . We are very lucky because the weather ...", "24-04-2019"));
        exampleList.add(new DiaryItems(R.drawable.placeholder_diary_account_circle, "New-Zealand", "Im very excited, my partner and I are flying to New-Zealand today, so right now we are at the airport waiting ...", "24-04-2019"));
        exampleList.add(new DiaryItems(R.drawable.placeholder_diary_android, "London", "So my mother surprised me with a city trip to London for the weekend. I've packed my bags and can't wait to ...", "24-04-2019"));
        exampleList.add(new DiaryItems(R.drawable.placeholder_diary_android, "Extra 5", "Finally! We are going to Hobbiton this afternoon. I've been waiting for this moment for years ...", "24-04-2019"));
        exampleList.add(new DiaryItems(R.drawable.placeholder_diary_ac_unit, "Extra 6", "So today we travelled to Auckland, we're stayng in a hotel . We are very lucky because the weather ...", "24-04-2019"));
        exampleList.add(new DiaryItems(R.drawable.placeholder_diary_account_circle, "Extra 7", "Im very excited, my partner and I are flying to New-Zealand today, so right now we are at the airport waiting ...", "24-04-2019"));
        exampleList.add(new DiaryItems(R.drawable.placeholder_diary_android, "Extra 8", "So my mother surprised me with a city trip to London for the weekend. I've packed my bags and can't wait to ...", "24-04-2019"));
        exampleList.add(new DiaryItems(R.drawable.placeholder_diary_android, "Extra 9", "Finally! We are going to Hobbiton this afternoon. I've been waiting for this moment for years ...", "24-04-2019"));
        exampleList.add(new DiaryItems(R.drawable.placeholder_diary_ac_unit, "Extra 10", "So today we travelled to Auckland, we're stayng in a hotel . We are very lucky because the weather ...", "24-04-2019"));
        exampleList.add(new DiaryItems(R.drawable.placeholder_diary_account_circle, "Extra 11", "Im very excited, my partner and I are flying to New-Zealand today, so right now we are at the airport waiting ...", "24-04-2019"));
        exampleList.add(new DiaryItems(R.drawable.placeholder_diary_android, "Extra 12", "So my mother surprised me with a city trip to London for the weekend. I've packed my bags and can't wait to ...", "24-04-2019"));

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new DiaryAdapter(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new DiaryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                exampleList.get(position);
                Fragment mFragment = new MemoryFragment();
                main.getSupportFragmentManager().beginTransaction().replace(R.id.container, mFragment).addToBackStack(null).commit();
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
        inflater.inflate(R.menu.diary_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.searchDiary);
        SearchView searchView = (SearchView) searchItem.getActionView();

        //SearchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                mAdapter.getFilter().filter(s);
//                return false;
    }
}




