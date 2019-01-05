package com.nemeum.project.nemeumproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class UserLoginActivity extends AppCompatActivity {

    private Context appContext;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences shared;
        TextView set_user_name;
        ImageButton findevent_btn;
        ImageButton findfacilities_btn;
        ImageButton findscenario_btn;
        ImageButton findtrainer_btn;
        ImageButton createevent_btn;
        ImageButton mybookings_btn;
        BottomNavigationView menu;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_normal_user);

        appContext = getApplicationContext();
        shared = getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);

        findevent_btn = findViewById(R.id.findeventicon);
        findfacilities_btn = findViewById(R.id.findfacilitiesicon);
        findscenario_btn = findViewById(R.id.findscrenarioicon);
        findtrainer_btn = findViewById(R.id.findtrainericon);
        createevent_btn = findViewById(R.id.myevent);
        mybookings_btn = findViewById(R.id.mybooking);
        progressBar = findViewById(R.id.progressbar);
        menu = findViewById(R.id.navigation);
        set_user_name = findViewById(R.id.user_names);

        menu.getMenu().getItem(2).setVisible(false);
        set_user_name.setText("Hello " + shared.getString("userName", "") + " !");

        findevent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                findEventOnclick();
            }
        });

        findfacilities_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                findFacilitiesOnclick();
            }
        });

        findscenario_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                findScenarioOnclick();
            }
        });

        findtrainer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                findTrainerOnclick();
            }
        });

        createevent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvntOnclick();
            }
        });

        mybookings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBookingsOnclick();
            }
        });

        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.homeButton:
                        Toast.makeText(appContext, R.string.alreadyOnHomeErr, Toast.LENGTH_LONG).show();
                        return true;
                    case R.id.settingsButton:
                        Intent intentSettings = new Intent(appContext, Settings.class);
                        appContext.startActivity(intentSettings);
                        return true;
                    case R.id.accountButton:
                        Toast.makeText(appContext, R.string.newFuncionalityErr, Toast.LENGTH_LONG).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public  void  onResume(){
        super.onResume();
        if(progressBar.getVisibility()==View.VISIBLE){
            progressBar.setVisibility(View.GONE);
        }
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

    public void createEvntOnclick() {
        Intent intent9 = new Intent(this, PostEvent.class);
        startActivity(intent9);
    }

    public void myBookingsOnclick() {
        Intent intent10 = new Intent(this, MyBookings.class);
        startActivity(intent10);
    }
}
