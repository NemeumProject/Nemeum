package com.nemeum.project.nemeumproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button normal_user_reg_btn = (Button) findViewById(R.id.normaluserregister);
        Button trainer_user_reg_btn =(Button) findViewById(R.id.traineruserregister);
        Button company_user_reg_btn = (Button) findViewById(R.id.companyuserregister);

        normal_user_reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalUserRegActivity();
            }
        });

        trainer_user_reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrainerUserRegActivity();
            }
        });

        company_user_reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompanyUserRegActivity();
            }
        });
    }

    private void NormalUserRegActivity(){
        Intent intent4 = new Intent(this,NormalUserRegistration.class);
        startActivity(intent4);
    }

    private void TrainerUserRegActivity(){
        Intent intent5 = new Intent(this,TrainerUserRegistration.class);
        startActivity(intent5);
    }

    private void CompanyUserRegActivity(){
        Intent intent6 = new Intent(this,CompanyUserRegistration.class);
        startActivity(intent6);
    }

}
