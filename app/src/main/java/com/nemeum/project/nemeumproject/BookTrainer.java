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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class BookTrainer extends AppCompatActivity {

    private Context appContext;
    private SharedPreferences SP;
    private BottomNavigationView menu;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_trainer_begin);

        appContext = getApplicationContext();
        SP = appContext.getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);

        checkRegisteredUser();

        ImageView trainerPicture = findViewById(R.id.bookTrainerImg);
        TextView trainerName = findViewById(R.id.TrainerNamesBook);
        TextView trainerSport = findViewById(R.id.TrainerBookSportText);
        TextView trainerPrice = findViewById(R.id.TrainerBookPriceText);
        TextView trainerDescription = findViewById(R.id.TrainerBookDescriptionText);

        if(getIntent().getStringExtra("withImage").equals("Yes")){
            Picasso.get().load(getIntent().getStringExtra(getResources().getString(R.string.scenarioImgExtra))).into(trainerPicture);
        }else{
            trainerPicture.setImageResource(R.drawable.scenario_nophoto);
        }

        trainerName.setText(getIntent().getStringExtra(getResources().getString(R.string.trainerNameExtra)));
        trainerSport.setText((getIntent().getStringExtra(getResources().getString(R.string.trainersporttype))+" - "+getIntent().getStringExtra(getResources().getString(R.string.trainerAddressExtra))+", "+
        getIntent().getStringExtra(getResources().getString(R.string.trainerCityExtra))));
        trainerPrice.setText("Price: "+getIntent().getStringExtra(getResources().getString(R.string.trainerPriceExtra))+"€/Hour");
        trainerDescription.setText(getIntent().getStringExtra(getResources().getString(R.string.trainerDescrExtra)));

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
        Intent intentPayment = new Intent(appContext, BookTrainerPayment.class);
        intentPayment.putExtra(getResources().getString(R.string.trainerNameExtra), getIntent().getStringExtra(getResources().getString(R.string.trainerNameExtra)));
        intentPayment.putExtra(getResources().getString(R.string.trainersporttype),getIntent().getStringExtra(getResources().getString(R.string.trainersporttype)));
        intentPayment.putExtra(getResources().getString(R.string.trainerPriceExtra),getIntent().getStringExtra(getResources().getString(R.string.trainerPriceExtra)));
        intentPayment.putExtra(getResources().getString(R.string.trainerid),getIntent().getStringExtra(getResources().getString(R.string.trainerid)));
        intentPayment.putExtra(getResources().getString(R.string.trainerserviceid),getIntent().getStringExtra(getResources().getString(R.string.trainerserviceid)));
        appContext.startActivity(intentPayment);
    }

}
