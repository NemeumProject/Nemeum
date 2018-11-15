package com.nemeum.project.nemeumproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class TrainerDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_detail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.homeButton:
                Intent intentMain = new Intent(this, MainActivity.class);
                this.startActivity(intentMain);
                return true;
            case R.id.settingsButton:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                this.startActivity(intentSettings);
                return true;
            case R.id.loginButton:
                Intent intentLogin = new Intent(this, Login.class);
                this.startActivity(intentLogin);
                return true;
            case R.id.accountButton:
                //Intent intentAccount = new Intent(this, .class);
                //this.startActivity(intentAccount);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
