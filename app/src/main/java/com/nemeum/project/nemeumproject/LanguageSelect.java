package com.nemeum.project.nemeumproject;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class LanguageSelect extends AppCompatActivity {

    Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_select);

            appContext = getApplicationContext();

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

    public void getBack(View view){

        finish();

    }

    public void changeLangEs(View view){

        if(LocaleManager.getLanguage(appContext).equals("es")) {

            Toast toast = Toast.makeText(appContext, R.string.changeLanguageErr, Toast.LENGTH_LONG);
            toast.show();

        } else {
            Toast toast = Toast.makeText(appContext, R.string.selectedSpanishToast, Toast.LENGTH_SHORT);
            toast.show();

            LocaleManager.setNewLocale(appContext, "es");

            restart();
        }
    }

    public void changeLangEn(View view){

        if(LocaleManager.getLanguage(appContext).equals("en")) {

            Toast toast = Toast.makeText(appContext, R.string.changeLanguageErr, Toast.LENGTH_LONG);
            toast.show();

        } else {
            Toast toast = Toast.makeText(appContext, R.string.selectedEnglishToast, Toast.LENGTH_SHORT);
            toast.show();

            LocaleManager.setNewLocale(appContext, "en");

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
    }

}
