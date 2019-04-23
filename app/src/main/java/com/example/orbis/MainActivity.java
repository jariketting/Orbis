package com.example.orbis;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        // StartScreen selection
        getSupportFragmentManager().beginTransaction().replace(R.id.container,          // Hier geef ik aan dat als de app opstart, hij het Kaart Fragment laat zien, als wijze van homescherm
                new MapFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =         // Hier wordt geselecteerd op welk fragment er is gedrukt
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFagment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.map:
                            selectedFagment = new MapFragment();
                            break;
                        case R.id.books:
                            selectedFagment = new DiaryFragment();
                            break;
                        case R.id.account:
                            selectedFagment = new AccountFragment();
                            break;
                        case R.id.search:
                            selectedFagment = new SearchFragment();
                            break;
                        case R.id.add:
                            selectedFagment = new NewFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,    // Nadat er op eentje is gedrukt wordt deze ge-commit, dus vertoond en hierna begint de loop weer opnieuw
                            selectedFagment).commit();

                    return true;
                }
            };
}