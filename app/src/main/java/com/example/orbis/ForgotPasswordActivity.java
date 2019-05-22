package com.example.orbis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //toolbar title
        Toolbar toolbar = findViewById(R.id.toolbarForgotPW);
        toolbar.setTitle(R.string.forgot_pw_screen_toolbar_title);
//        dit even goed uitzoeken
//        deze code zorgt voor een pijl in de title bar waardoor je terug kan naar de vorige pagina
//        actionbar heeft al bepaalde abstracte kunstjes

        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}