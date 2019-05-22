package com.example.orbis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OtherAccountActivity extends AppCompatActivity {

    public Button followersButton_other;
    public Button followingButton_other;

    public void ToFollowersOther(){
        followersButton_other = (Button) findViewById(R.id.followersButton_other);
        followersButton_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tofollowersother = new Intent(OtherAccountActivity.this,FollowersOtherActivity.class);
                startActivity(tofollowersother);
            }
        });

    }

    public void ToFollowingOther(){
        followingButton_other = (Button) findViewById(R.id.followingButton_other);
        followingButton_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tofollowingother = new Intent(OtherAccountActivity.this,FollowingOtherActivity.class);
                startActivity(tofollowingother);
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_account);
        ToFollowersOther();
        ToFollowingOther();

    }
}
