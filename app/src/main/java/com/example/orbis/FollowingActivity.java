package com.example.orbis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FollowingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
    }

    public void back(View view) {
        finish();
    }
}
