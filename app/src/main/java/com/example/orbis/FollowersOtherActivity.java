package com.example.orbis;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class FollowersOtherActivity extends AppCompatActivity {

    public ImageButton backfollowersButton_other;

    public void BackToOther1(){
        backfollowersButton_other = (ImageButton) findViewById(R.id.backfollowersButtonOther);
        backfollowersButton_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtoother1 = new Intent(FollowersOtherActivity.this,OtherAccountActivity.class);
                startActivity(backtoother1);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers_other);
        BackToOther1();
    }
}
