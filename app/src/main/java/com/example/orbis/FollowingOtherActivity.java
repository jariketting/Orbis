package com.example.orbis;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class FollowingOtherActivity extends AppCompatActivity {

    public ImageButton backfollowingButton_other;

    public void BackToOther2(){
        backfollowingButton_other = (ImageButton) findViewById(R.id.backfollowingButtonOther);
        backfollowingButton_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtoother2 = new Intent(FollowingOtherActivity.this,OtherAccountActivity.class);
                startActivity(backtoother2);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_other);
        BackToOther2();
    }
}
