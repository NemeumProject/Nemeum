package com.nemeum.project.nemeumproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.sql.Time;

public class TrainerDetail extends AppCompatActivity {

    GridView weekSchedule;
    Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_detail);

        appContext = getApplicationContext();
        weekSchedule = findViewById(R.id.scheduleGrid);

        ArrayAdapter<String> scheduleAdapter = new ArrayAdapter<>(appContext, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.weekDaysTrainer));
        weekSchedule.setAdapter(scheduleAdapter);

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
                        Toast toast = Toast.makeText(appContext, R.string.alreadyOnProfileErr, Toast.LENGTH_LONG);
                        toast.show();
                        return true;
                    default:
                        return false;
                }
            }
        });


    }

    public void editPersonalInfo(View view) {
        Intent intentEditPersonalInfo = new Intent(getApplicationContext(), activity_user_editionp.class);
        startActivity(intentEditPersonalInfo);


    }
}
