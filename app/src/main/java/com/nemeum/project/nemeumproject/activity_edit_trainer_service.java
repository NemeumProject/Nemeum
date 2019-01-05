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

public class activity_edit_trainer_service extends AppCompatActivity {

    Context appContext;
    private EditText training_address;
    private EditText training_price;
    private EditText training_desc;
    String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trainer_service);

        appContext = getApplicationContext();

        //Still doesn't have spinner for choosing training service list

        training_address = findViewById(R.id.location_Edit_Training); //Edit Text for edit training adress
        training_price = findViewById(R.id.training_price_edit); //Edit training price
        training_desc = findViewById(R.id.description_Edit_Training); //Edit training desc

        final Spinner SportTrainingCategory = findViewById(R.id.sport_type); //Spinner for sport type
        ArrayAdapter<CharSequence> SportCategoryAdapter = ArrayAdapter.createFromResource(this, R.array.sportFilter, R.layout.spinner_layout_special);
        SportCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SportTrainingCategory.setAdapter(SportCategoryAdapter);

        final Spinner SportTrainingCity = findViewById(R.id.trainer_city); //Spinner for city list
        ArrayAdapter<CharSequence> SportCityAdapter = ArrayAdapter.createFromResource(this, R.array.cityFilter, R.layout.spinner_layout);
        SportCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SportTrainingCity.setAdapter(SportCityAdapter);

        final Button submit_training_service = findViewById(R.id.btn_finish_posting_EditTrainer); //Submit Button
        final Button delete_training_service = findViewById(R.id.delete_posting_EditTrainer); //Delete Button

        SharedPreferences shared = getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);
        idUser = (shared.getString("idUser", ""));

        submit_training_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitTrainerService(); //A void to submit the posting after edit
            }
        });

        delete_training_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTrainerService(); //A void to delete a posting
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

    public void submitTrainerService()
    {

    }

    public void deleteTrainerService()
    {

    }

    public void getBack(View view) {
        finish();
    }
}
