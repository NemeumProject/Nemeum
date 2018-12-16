package com.nemeum.project.nemeumproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class UserCompanyLoginActivity extends AppCompatActivity {

    Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_company_user);
        appContext = getApplicationContext();

        ImageButton findevent_btn = findViewById(R.id.findeventicon);
        ImageButton findfacilities_btn = findViewById(R.id.findfacilitiesicon);
        ImageButton findscenario_btn = findViewById(R.id.findscrenarioicon);
        ImageButton findtrainer_btn = findViewById(R.id.findtrainericon);
        ImageButton postscenario_btn = findViewById(R.id.postserviceicon);
        ImageButton editscenario_btn = findViewById(R.id.editscenarioicon);
        ImageButton updateschedule_btn = findViewById(R.id.updatescheduleicon);

        Bundle bundle = getIntent().getExtras();
        String new_user_name = bundle.getString("name_of_user");
        TextView set_user_name = findViewById(R.id.user_names);
        set_user_name.setText("Hello "+ new_user_name +" !");


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

        postscenario_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postScenarioOnclick();
            }
        });

        editscenario_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editScenarioOnclick();
            }
        });

        updateschedule_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScheduleOnclick();
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

    public void  postScenarioOnclick()
    {
        Intent intent10 = new Intent(this, PostScenario.class);
        startActivity(intent10);
    }

    public void  editScenarioOnclick()
    {
        Intent intent11 = new Intent(this, activity_edit_scenario.class);
        startActivity(intent11);
    }

    public void  updateScheduleOnclick()
    {
        Intent intent13 = new Intent(this, activity_edit_daily_schedule.class);
        startActivity(intent13);
    }




}
