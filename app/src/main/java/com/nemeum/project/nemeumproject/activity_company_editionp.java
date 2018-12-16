package com.nemeum.project.nemeumproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class activity_company_editionp extends AppCompatActivity {

    Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_editionp);

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
                        Intent intentAccount = new Intent(appContext, TrainerDetail.class);
                        appContext.startActivity(intentAccount);
                        return true;
                    default:
                        return false;
                }
            }
        });

    }

    public void save_CompanyEditionP(View view) {

        //set up the variables to validate not empty fields and strings to show as error messages
        EditText etCompanyName = findViewById(R.id.companyTitleEditText);
        String strCompanyName = etCompanyName.getText().toString();
        String company_Name_Error = getString(R.string.name_Company_Empty);

        EditText etCompanyAddress = findViewById(R.id.companyPlaceEditText);
        String strCompanyAddress = etCompanyAddress.getText().toString();
        String company_Address_Error = getString(R.string.company_Address_Empty);

        EditText etCompanyPhone = findViewById(R.id.companyPhoneEditText);
        String strCompanyPhone = etCompanyPhone.getText().toString();
        String company_Phone_Error = getString(R.string.company_Phone_Empty);


        if(TextUtils.isEmpty(strCompanyName)) {
            etCompanyName.setError(company_Name_Error);
            return;
        }
        else  if(TextUtils.isEmpty(strCompanyAddress)) {
            etCompanyAddress.setError(company_Address_Error);
            return;
        }
        else  if(TextUtils.isEmpty(strCompanyPhone)) {
            etCompanyPhone.setError(company_Phone_Error);
            return;
        }


    }
}
