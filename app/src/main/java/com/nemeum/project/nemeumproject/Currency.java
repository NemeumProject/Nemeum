package com.nemeum.project.nemeumproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Currency extends AppCompatActivity {

    Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        appContext = getApplicationContext();

        BottomNavigationView menu = findViewById(R.id.navigation);
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.homeButton:
                        Intent intentMain = new Intent(appContext, ActivityMainMock.class);
                        appContext.startActivity(intentMain);
                        return true;
                    case R.id.settingsButton:
                        Intent intentSettings = new Intent(appContext, Settings.class);
                        appContext.startActivity(intentSettings);
                        return true;
                    case R.id.loginButton:
                        Intent intentLogin = new Intent(appContext, Login.class);
                        appContext.startActivity(intentLogin);
                        return true;
                    case R.id.accountButton:
                        Intent intentAccount = new Intent(appContext, TrainerDetail.class);
                        appContext.startActivity(intentAccount);
                        return true;
                    default:
                        return false;
                }
            }
        });

    }

    public void getBack(View view) {

        finish();

    }

    public void setExchange(View view) {
        if(view.getId() == R.id.euroButton){
            Toast toast = Toast.makeText(appContext, R.string.selectedEuroToast, Toast.LENGTH_SHORT);
            toast.show();
        } else{
            Toast toast = Toast.makeText(appContext, R.string.selectedDollarToast, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
