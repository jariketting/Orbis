package com.example.orbis;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    int menuIdCurrent;
    int menuIdLast;
    int menuId;
    Fragment selectedFragment;

    BottomNavigationView bottomNav; //stores bottom navigation

    /**
     * Setup activity
     *
     * @param savedInstanceState saved state of instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //set content view to activity main for this activity

        //get bottom navigation
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener); //setup nav listener for bottom navigation
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new GoogleMapsFragment()).commit(); //default fragment to be shown

        //store menu history
        menuIdCurrent = 0; //current menu item id (defaults to zero / map)
        menuIdLast = 0; //last menu item id (defaults to zero / map)

    }

    /**
     * Hides navigation bar
     */
    public void hideNav() {
        bottomNav.setVisibility(View.GONE); //set visibility to gone (invisible still shows background)
    }

    /**
     * Show navigation bar
     */
    public void showNav() {
        bottomNav.setVisibility(View.VISIBLE); //set visibility to visible
    }

    /**
     * Overwrite default with our own last fragment function
     */
    @Override
    public void onBackPressed() {
        goToLastFragment();
    }

    /**
     * Go to last fragment
     */
    public void goToLastFragment() {
        getSupportFragmentManager().popBackStack(); //goes to previous fragment stored in the backstack
        bottomNav.getMenu().getItem(menuIdLast).setChecked(true); //set previous fragment item checked
        showNav(); //make sure nav is shown
        menuIdCurrent = menuIdLast; //set current menu id to last menu item id
    }

    /**
     * Go to provided fragment and change menu to provided index
     *
     * @param fragment fragment to go to
     * @param menuIndex menu item index to change menu item to
     */
    public void goToFragment(Fragment fragment, int menuIndex) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit(); //start and commit transaction to new fragment
        bottomNav.getMenu().getItem(menuIndex).setChecked(true); //set previous fragment item checked

        menuIdLast = menuIdCurrent; //update last id
        menuIdCurrent = menuIndex; //set current menu id to last menu item id
    }

    /**
     * Bottom navigation listener
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        /**
         * Changes fragment based on what item the user has clicked in the bottom navigation
         *
         * @param menuItem clicked item in menu
         * @return true
         */
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            //stores default values (always defaults back to the map fragment
            selectedFragment = new GoogleMapsFragment(); //create map fragment
            menuId = 0; //menu id for map fragment

            //go trough each menu item, then set item id and set selected fragment
            switch (menuItem.getItemId()) {
                case R.id.map:
                    //default values already set
                    break;
                case R.id.books:
                    selectedFragment = new DiaryFragment();
                    menuId = 1;
                    break;
                case R.id.add:
                    selectedFragment = new NewFragment();
                    menuId = 2;
                    break;
                case R.id.search:
                    selectedFragment = new SearchFragment();
                    menuId = 3;
                    break;
                case R.id.account:
                    selectedFragment = new AccountFragment();
                    menuId = 4;
                    break;
            }

            //prevent current menu item being selected twice
            if(menuIdCurrent == menuId)
                return false;

            //set new fragment and add to backstack
            getSupportFragmentManager().beginTransaction().replace(R.id.container, selectedFragment).addToBackStack(null).commit();

            //update menu item id history
            menuIdLast = menuIdCurrent;
            menuIdCurrent = menuId;

            return true;
        }
    };

    /**
     * Switch to memory
     *
     * @param id
     */
    protected void switchToMemory(int id){
        selectedFragment = new MemoryFragment(); //id needs to be given as parameter
        menuId = 1;
        goToFragment(selectedFragment, menuId);
    }
}