package com.example.orbis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ChangepasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);


        Toolbar toolbar = findViewById(R.id.toolbar);

        //setup toolbar
        toolbar.setTitle(R.string.memory_toolbar_title + ": " + "This is a memory");
        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);

    }
}
