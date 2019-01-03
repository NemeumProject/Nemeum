package com.nemeum.project.nemeumproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;



public class UserTrainerLoginActivity extends AppCompatActivity {

    Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_trainer_user);
        appContext = getApplicationContext();

        ImageButton findevent_btn = findViewById(R.id.findeventicon);
        ImageButton findfacilities_btn = findViewById(R.id.findfacilitiesicon);
        ImageButton findscenario_btn = findViewById(R.id.findscrenarioicon);
        ImageButton findtrainer_btn = findViewById(R.id.findtrainericon);
        ImageButton createevent_btn = findViewById(R.id.myevent);
        ImageButton posttrainersvc_btn = findViewById(R.id.postservice);

        final ProgressBar progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        SharedPreferences shared = getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);
        String new_user_name = (shared.getString("userName", ""));
        TextView set_user_name = findViewById(R.id.user_names);
        set_user_name.setText("Hello "+ new_user_name +" !");

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
        posttrainersvc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postTrainerOnclick();
            }
        });

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
                        Intent intentAccount = new Intent(getApplicationContext(), TrainerDetail.class);
                        getApplicationContext().startActivity(intentAccount);
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
        ProgressBar progressBar = findViewById(R.id.progressbar);
        if(progressBar.getVisibility()==View.VISIBLE)
        {
            progressBar.setVisibility(View.INVISIBLE);
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
    public void postTrainerOnclick() {
        Intent intent9 = new  Intent(this,PostTrainerService.class);
        startActivity(intent9);
    }

}




