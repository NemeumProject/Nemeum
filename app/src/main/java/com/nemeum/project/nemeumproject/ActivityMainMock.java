package com.nemeum.project.nemeumproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ActivityMainMock extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mock);

        ImageButton findevent_btn = findViewById(R.id.findeventicon);
        ImageButton findfacilities_btn = findViewById(R.id.findfacilitiesicon);
        ImageButton findscenario_btn = findViewById(R.id.findscrenarioicon);
        ImageButton findtrainer_btn = findViewById(R.id.findtrainericon);

        ImageButton home_btn = findViewById(R.id.homeicon);
        ImageButton settings_btn =  findViewById(R.id.settingsicon);
        ImageButton login_btn = findViewById(R.id.loginicon);
        ImageButton myaccount_btn = findViewById(R.id.myaccounticon);

        findevent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findfacilities_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findscenario_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findtrainer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeOnclick();
            }
        });

        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsOnclick();
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOnclick();
            }
        });

        myaccount_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myaccountOnclick();
            }
        });

    }

    public void homeOnclick() {
        Intent intent1 = new Intent(ActivityMainMock.this, ActivityMainMock.class);
        startActivity(intent1);
    }

    public void loginOnclick() {
        Intent intent2 = new Intent(ActivityMainMock.this, Login.class);
        startActivity(intent2);
    }

    public void settingsOnclick() {
        //Intent intent3 = new Intent(ActivityMainMock.this,.class);
        //startActivity(intent3);
    }

    public void myaccountOnclick() {
        //Intent intent4 = new Intent(ActivityMainMock.this, .class);
        //startActivity(intent4);
    }

    public void findfacilitiesOnclick() {
        Intent intent = new Intent(ActivityMainMock.this, FacilitiesMock.class);
        startActivity(intent);
    }

    public void findscenarioOnclick() {
        Intent intent = new Intent(ActivityMainMock.this, ScenarioMock.class);
        startActivity(intent);
    }

    public void findtrainerOnclick() {
        Intent intent = new Intent(ActivityMainMock.this, TrainerMock.class);
        startActivity(intent);
    }
    public void findEventOnclick() {
        Intent intent = new Intent(ActivityMainMock.this, EventFinder.class);
        startActivity(intent);
    }

}
