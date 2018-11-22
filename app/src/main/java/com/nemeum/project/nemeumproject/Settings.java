package com.nemeum.project.nemeumproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
                        Toast toast = Toast.makeText(appContext, R.string.alreadyOnSettingsErr, Toast.LENGTH_LONG);
                        toast.show();
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

    public void getBack(View view) {
        finish();
    }

    public void seeAbout(View view) {
        Intent intentAbout = new Intent(appContext, About.class);
        appContext.startActivity(intentAbout);
    }

    public void seeVersion(View view) {
        Intent intentVersion = new Intent(appContext, Version.class);
        appContext.startActivity(intentVersion);
    }

    public void changeLogin(View view) {
        Intent intentCurrency = new Intent(appContext, activity_login_edition.class);
        appContext.startActivity(intentCurrency);
    }

    public void changeCurrency(View view) {
        Intent intentLogEdiion = new Intent(appContext, Currency.class);
        appContext.startActivity(intentLogEdiion);
    }

    public void changeNotification(View view) {
        Intent intentNotification = new Intent(appContext, Notification.class);
        appContext.startActivity(intentNotification);
    }

    public void changeLanguage(View view) {
        Intent intentLanguage = new Intent(appContext, LanguageSelect.class);
        appContext.startActivity(intentLanguage);
    }
}
