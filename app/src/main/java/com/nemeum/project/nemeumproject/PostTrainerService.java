package com.nemeum.project.nemeumproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;

public class PostTrainerService extends AppCompatActivity {

    private Context appContext;
    private EditText training_address;
    private EditText training_price;
    private EditText training_desc;
    private String idUser;
    private SharedPreferences SP;
    private BottomNavigationView menu;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__trainer__posting);

        appContext = getApplicationContext();
        SP = appContext.getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);

        checkRegisteredUser();

        training_address = findViewById(R.id.trainingAdd);
        training_price = findViewById(R.id.TrainingPrice);
        training_desc = findViewById(R.id.TrainingDesc);
        final Button submit_training_service = findViewById(R.id.confirmBookButton);

        final Spinner SportTrainingCategory = findViewById(R.id.SportTypeTraining);
        ArrayAdapter<CharSequence> SportCategoryAdapter = ArrayAdapter.createFromResource(this, R.array.sportFilter, R.layout.spinner_layout);
        SportCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SportTrainingCategory.setAdapter(SportCategoryAdapter);

        final Spinner SportTrainingCity = findViewById(R.id.TrainingCity);
        ArrayAdapter<CharSequence> SportCityAdapter = ArrayAdapter.createFromResource(this, R.array.cityFilter, R.layout.spinner_layout);
        SportCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SportTrainingCity.setAdapter(SportCityAdapter);

        final Spinner TrainingStart = findViewById(R.id.startTrainingTime);
        ArrayAdapter<CharSequence> TrainingStartAdapter = ArrayAdapter.createFromResource(this,R.array.hourStartFilter, R.layout.spinner_layout);
        TrainingStartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TrainingStart.setAdapter(TrainingStartAdapter);

        final Spinner TrainingEnd = findViewById(R.id.endTrainingTime);
        ArrayAdapter<CharSequence> TrainingEndAdapter = ArrayAdapter.createFromResource(this,R.array.hourEndFilter, R.layout.spinner_layout);
        TrainingEndAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TrainingEnd.setAdapter(TrainingStartAdapter);

        idUser = (SP.getString("idUser", ""));

        submit_training_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitTrainerService(idUser,SportTrainingCategory.getSelectedItemPosition(),training_address.getText().toString(),
                        training_price.getText().toString(),SportTrainingCity.getSelectedItem().toString(),TrainingStart.getSelectedItem().toString(),
                        TrainingEnd.getSelectedItem().toString(),training_desc.getText().toString());
            }
        });


        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.homeButton:
                        if(userType.equals(getResources().getString(R.string.individualUserSP))){
                            Intent intentMainInd = new Intent(appContext, UserLoginActivity.class);
                            appContext.startActivity(intentMainInd);
                        } else if(userType.equals(getResources().getString(R.string.trainerUserSP))){
                            Intent intentMainTrainer = new Intent(appContext, UserTrainerLoginActivity.class);
                            appContext.startActivity(intentMainTrainer);
                        } else if(userType.equals(getResources().getString(R.string.companyUserSP))){
                            Intent intentMainCompany = new Intent(appContext, UserCompanyLoginActivity.class);
                            appContext.startActivity(intentMainCompany);
                        } else {
                            Intent intentMain = new Intent(appContext, ActivityMainMock.class);
                            appContext.startActivity(intentMain);
                        }
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
                        if(userType.equals(getResources().getString(R.string.individualUserSP))){
                            Intent intentAccount = new Intent(appContext, IndividualUserDetail.class);
                            appContext.startActivity(intentAccount);
                        } else if(userType.equals(getResources().getString(R.string.trainerUserSP))){
                            Intent intentAccount = new Intent(appContext, TrainerDetail.class);
                            appContext.startActivity(intentAccount);
                        } else {
                            Intent intentAccount = new Intent(appContext, CompanyDetail.class);
                            appContext.startActivity(intentAccount);
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void checkRegisteredUser() {
        menu = findViewById(R.id.navigation);
        userType = SP.getString(getResources().getString(R.string.userTypeSP), "");

        if(userType.equals(getResources().getString(R.string.individualUserSP)) ||
                userType.equals(getResources().getString(R.string.trainerUserSP)) ||
                userType.equals(getResources().getString(R.string.companyUserSP))){
            menu.getMenu().getItem(2).setVisible(false);
        } else {
            menu.getMenu().getItem(3).setVisible(false);
        }
    }

    public void submitTrainerService(String idtraineruser, Integer sportID, String sportaddress, String sportprice, String sportcity, String sportstart, String sportend, String sportdesc ){
        final Integer traineriduser = Integer.parseInt(idtraineruser);
        final Integer SportTypeID = sportID;
        final String SportAddress = sportaddress;
        final Double SportPrice = Double.parseDouble(sportprice);
        final String SportCity = sportcity;

        final Time SportStart = Time.valueOf(sportstart+":00");
        final Time SportEnd = Time.valueOf(sportend+":00");
        final String SportDesc = sportdesc;

        new Thread(new Runnable() {
            public void run() {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(getResources().getString(R.string.urlDB) + "/trainersport");
                JSONObject postData = new JSONObject();
                try {

                    postData.put(getResources().getString(R.string.idtrainersport),traineriduser);
                    postData.put(getResources().getString(R.string.SportTypeTraining),SportTypeID);
                    postData.put(getResources().getString(R.string.SportAddressTraining),SportAddress);
                    postData.put(getResources().getString(R.string.SportPriceTraining),SportPrice);
                    postData.put(getResources().getString(R.string.SportCityTraining),SportCity);
                    postData.put(getResources().getString(R.string.SportStartTraining),SportStart);
                    postData.put(getResources().getString(R.string.SportEndTraining),SportEnd);
                    postData.put(getResources().getString(R.string.SportDescTraining),SportDesc);
                    StringEntity se = new StringEntity(postData.toString(), "UTF-8");
                    httppost.setHeader("Accept", "application/json");
                    httppost.setHeader("Content-Type", "application/json; charset=utf-8");
                    httppost.setEntity(se);
                    HttpResponse response = httpclient.execute(httppost);
                    if(response.getStatusLine().getStatusCode() == 200){
                        PostTrainerService.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(PostTrainerService.this, getResources().getString(R.string.SportServiceSuccess), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
                catch (IOException e){
                    PostTrainerService.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(PostTrainerService.this, getResources().getString(R.string.noconnection), Toast.LENGTH_LONG).show();
                        }
                    });
                    e.printStackTrace();
                }
                catch (JSONException e){
                    PostTrainerService.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(PostTrainerService.this, getResources().getString(R.string.noconnection), Toast.LENGTH_LONG).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        }
        ).start();
    }

    public void getBack(View view) {
        finish();
    }
}
