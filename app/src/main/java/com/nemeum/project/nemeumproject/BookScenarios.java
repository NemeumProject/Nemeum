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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class BookScenarios extends AppCompatActivity {

    private Context appContext;
    private SharedPreferences SP;
    private BottomNavigationView menu;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_scenarios);

        appContext = getApplicationContext();
        SP = appContext.getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);

        checkRegisteredUser();

        ImageView scenarioPicture = findViewById(R.id.bookScenarioImg);
        TextView scenarioName = findViewById(R.id.scenarioBookTitleText);
        TextView scenarioPrice = findViewById(R.id.scenarioBookPriceText);
        TextView scenarioDescription = findViewById(R.id.scenarioBookDescriptionText);

        if(getIntent().getStringExtra("withImage").equals("Yes")){
            Picasso.get().load(getIntent().getStringExtra(getResources().getString(R.string.scenarioImgExtra))).into(scenarioPicture);
        }else{
            scenarioPicture.setImageResource(R.drawable.scenario_nophoto);
        }

        scenarioName.setText(getIntent().getStringExtra(getResources().getString(R.string.scenarioNameExtra)));
        scenarioPrice.setText("Price: "+getIntent().getStringExtra(getResources().getString(R.string.scenarioPriceExtra))+"€/Hour");
        scenarioDescription.setText(getIntent().getStringExtra(getResources().getString(R.string.scenarioDescrExtra)));

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

        if(userType.equals(getResources().getString(R.string.companyUserSP))){
            menu.getMenu().getItem(2).setVisible(false);
        } else if(userType.equals(getResources().getString(R.string.individualUserSP)) ||
                userType.equals(getResources().getString(R.string.trainerUserSP))){
            menu.getMenu().getItem(2).setVisible(false);
            menu.getMenu().getItem(3).setVisible(false);
        } else{
            menu.getMenu().getItem(3).setVisible(false);
        }
    }

    public void getBack(View view) {
        finish();
    }

    public void scenarioBookPayment(View view) {
        Intent intentPayment = new Intent(appContext, BookScenarioPayment.class);
        intentPayment.putExtra(getResources().getString(R.string.scenarioNameExtra), getIntent().getStringExtra(getResources().getString(R.string.scenarioNameExtra)));
        intentPayment.putExtra(getResources().getString(R.string.scenarioPriceExtra),getIntent().getStringExtra(getResources().getString(R.string.scenarioPriceExtra)));
        intentPayment.putExtra(getResources().getString(R.string.scenarioiduser),getIntent().getStringExtra(getResources().getString(R.string.scenarioiduser)));
        intentPayment.putExtra(getResources().getString(R.string.scenarioid),getIntent().getStringExtra(getResources().getString(R.string.scenarioid)));
        appContext.startActivity(intentPayment);
    }
}
