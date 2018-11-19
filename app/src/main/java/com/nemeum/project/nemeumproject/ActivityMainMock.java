package com.nemeum.project.nemeumproject;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ActivityMainMock extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mock);

        ImageButton findevent_btn = (ImageButton) findViewById(R.id.findeventicon);
        ImageButton findfacilities_btn = (ImageButton) findViewById(R.id.findfacilitiesicon);
        ImageButton findscenario_btn = (ImageButton) findViewById(R.id.findscrenarioicon);
        ImageButton findtrainer_btn = (ImageButton) findViewById(R.id.findtrainericon);

        ImageButton home_btn = (ImageButton) findViewById(R.id.homeicon);
        ImageButton settings_btn = (ImageButton)  findViewById(R.id.settingsicon);
        ImageButton login_btn = (ImageButton) findViewById(R.id.loginicon);
        ImageButton myaccount_btn = (ImageButton) findViewById(R.id.myaccounticon);

        findevent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findEventOnclick();
            }
        });

        findfacilities_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findFacilitiesOnclick();
            }
        });

        findscenario_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findScenarioOnclick();
            }
        });

        findtrainer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findTrainerOnclick();
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
        Intent intent1 = new Intent(this, ActivityMainMock.class);
        startActivity(intent1);
    }

    public void loginOnclick() {
        Intent intent2 = new Intent(this, Login.class);
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

    public void findFacilitiesOnclick() {
        Intent intent5 = new Intent(this,FacilityFinder.class);
        startActivity(intent5);
    }

    public void findScenarioOnclick() {
        Intent intent6 = new Intent(this,NearScenarios.class);
        startActivity(intent6);
    }

    public void findTrainerOnclick() {
        Intent intent7 = new Intent(this,SearchTrainer.class);
        startActivity(intent7);
    }
    public void findEventOnclick() {
        Intent intent8 = new Intent(this, EventFinder.class);
        startActivity(intent8);
    }

}
