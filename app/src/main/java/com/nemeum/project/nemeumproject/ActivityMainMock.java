package com.nemeum.project.nemeumproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.support.v4.content.ContextCompat;
import android.Manifest;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import models.Scenario;

public class ActivityMainMock extends AppCompatActivity {
    private List<Scenario> listScenario = new ArrayList<>();
    private Context appContext;
    private BottomNavigationView menu;
    private SharedPreferences SP;
    private ProgressBar progressBar;

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ImageButton findevent_btn;
        ImageButton findfacilities_btn;
        ImageButton findscenario_btn;
        ImageButton findtrainer_btn;

        appContext = getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mock);

        SP = appContext.getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);

        checkPermissions();
        checkRegisteredUser();

        menu = findViewById(R.id.navigation);
        progressBar = findViewById(R.id.progressbar);
        findevent_btn = findViewById(R.id.findeventicon);
        findfacilities_btn = findViewById(R.id.findfacilitiesicon);
        findscenario_btn = findViewById(R.id.findscrenarioicon);
        findtrainer_btn = findViewById(R.id.findtrainericon);

        menu.getMenu().getItem(3).setVisible(false);

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
                    case R.id.loginButton:
                        Intent intentLogin = new Intent(appContext, Login.class);
                        appContext.startActivity(intentLogin);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void checkRegisteredUser() {
        if(SP.getString(getResources().getString(R.string.userTypeSP), "").equals(getResources().getString(R.string.individualUserSP))){
            Intent intent1 = new Intent(appContext, UserLoginActivity.class);
            startActivity(intent1);
            finish();
        } else if(SP.getString(getResources().getString(R.string.userTypeSP), "").equals(getResources().getString(R.string.trainerUserSP))){
            Intent intent2 = new Intent(appContext, UserTrainerLoginActivity.class);
            startActivity(intent2);
            finish();
        } else if(SP.getString(getResources().getString(R.string.userTypeSP), "").equals(getResources().getString(R.string.companyUserSP))){
            Intent intent3 = new Intent(appContext, UserCompanyLoginActivity.class);
            startActivity(intent3);
            finish();
        }
    }

    @Override
    public  void  onResume(){
        super.onResume();
        if(progressBar.getVisibility()==View.VISIBLE) {
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

    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(this, getResources().getString(R.string.requirePermissionErr) + permissions[index]
                                + getResources().getString(R.string.permissionNotGrantedErr), Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                break;
        }
    }
}
