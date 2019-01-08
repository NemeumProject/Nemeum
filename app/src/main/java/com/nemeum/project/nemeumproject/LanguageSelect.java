package com.nemeum.project.nemeumproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class LanguageSelect extends AppCompatActivity {

    private Context appContext;
    private SharedPreferences SP;
    private BottomNavigationView menu;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_select);

        appContext = getApplicationContext();
        SP = appContext.getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);

        checkRegisteredUser();

        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.homeButton:
                            if (userType.equals(getResources().getString(R.string.individualUserSP))) {
                                Intent intentMainInd = new Intent(appContext, UserLoginActivity.class);
                                appContext.startActivity(intentMainInd);
                            } else if (userType.equals(getResources().getString(R.string.trainerUserSP))) {
                                Intent intentMainTrainer = new Intent(appContext, UserTrainerLoginActivity.class);
                                appContext.startActivity(intentMainTrainer);
                            } else if (userType.equals(getResources().getString(R.string.companyUserSP))) {
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
                            if (userType.equals(getResources().getString(R.string.individualUserSP))) {
                                Intent intentAccount = new Intent(appContext, IndividualUserDetail.class);
                                appContext.startActivity(intentAccount);
                            } else if (userType.equals(getResources().getString(R.string.trainerUserSP))) {
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

    public void getBack(View view){

        finish();

    }

    /*public void changeLangEs(View view){

        if(LocaleManager.getLanguage(appContext).equals(getResources().getString(R.string.app_language_spanish))) {

            Toast toast = Toast.makeText(appContext, R.string.changeLanguageErr, Toast.LENGTH_LONG);
            toast.show();

        } else {
            Toast toast = Toast.makeText(appContext, R.string.selectedSpanishToast, Toast.LENGTH_SHORT);
            toast.show();

            LocaleManager.setNewLocale(appContext, getResources().getString(R.string.app_language_spanish));
            restart();
        }
    }

    public void changeLangEn(View view){

        if(LocaleManager.getLanguage(appContext).equals(getResources().getString(R.string.app_language_english))) {

            Toast toast = Toast.makeText(appContext, R.string.changeLanguageErr, Toast.LENGTH_LONG);
            toast.show();

        } else {
            Toast toast = Toast.makeText(appContext, R.string.selectedEnglishToast, Toast.LENGTH_SHORT);
            toast.show();

            LocaleManager.setNewLocale(appContext, getResources().getString(R.string.app_language_english));

            restart();
        }

    }

    private void restart(){

        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(i);
            }
        }.start();
    }*/

}
