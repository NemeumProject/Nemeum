package com.nemeum.project.nemeumproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CompanyDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_detail);
    }

    public void editCompanyInfo(View view) {
        Intent intentEditCompanyInfo = new Intent(getApplicationContext(), activity_company_editionp.class);
        startActivity(intentEditCompanyInfo);
    }
}
