package com.example.orbis;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    int menuIdCurrent;
    int menuIdLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        // StartScreen selection
        getSupportFragmentManager().beginTransaction().replace(R.id.container,          // Hier geef ik aan dat als de app opstart, hij het Kaart Fragment laat zien, als wijze van homescherm
                new MapFragment()).commit();

        //store menu history
        menuIdCurrent = 0; //current menu item id (defaults to zero / map)
        menuIdLast = 0; //last menu item id (defaults to zero / map)
    }

    /**
     * Hides navigation bar
     */
    public void hideNav() {
        //find nav view
        View nav = findViewById(R.id.bottom_navigation);
        nav.setVisibility(View.GONE); //set visibility to gone (invisible still shows background)
    }

    /**
     * Show navigation bar
     */
    public void showNav() {
        //find nav view
        View nav = findViewById(R.id.bottom_navigation);
        nav.setVisibility(View.VISIBLE); //set visibility to visible
    }

    /**
     * Go to last fragment
     */
    public void goToLastFragment() {
        getSupportFragmentManager().popBackStack(); //goes to previous fragment stored in the backstack

        //change menu to fragments menu item
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation); //get bottom navigation
        bottomNav.getMenu().getItem(menuIdLast).setChecked(true); //set previous fragment item checked

        menuIdCurrent = menuIdLast; //set current menu id to last menu item id
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =         // Hier wordt geselecteerd op welk fragment er is gedrukt
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    int menuId = 0; //stores selected menu id, zero is default (map)

                    //go trough each menu item, then set item id and set selected fragment
                    switch (menuItem.getItemId()) {
                        case R.id.map:
                            selectedFragment = new MapFragment();
                            menuId = 0;
                            break;
                        case R.id.books:
                            //selectedFragment = new DiaryFragment();
                            selectedFragment = new MemoryFragment(); //temp for development purposes
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
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,    // Nadat er op eentje is gedrukt wordt deze ge-commit, dus vertoond en hierna begint de loop weer opnieuw
                            selectedFragment).addToBackStack(null).commit();


                    //update menu item id history
                    menuIdLast = menuIdCurrent;
                    menuIdCurrent = menuId;

                    return true;
                }
            };
}