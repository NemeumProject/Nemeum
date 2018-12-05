package com.nemeum.project.nemeumproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CompanyUserRegistration extends AppCompatActivity {

    private EditText CompanyName;
    private EditText CompanyEmail;
    private EditText CompanyAddress;
    private EditText CompanyTelephone;
    private EditText CompanyPassword;
    private EditText CompanyPasswordVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_register);

        Button submitcompanydata_btn = (Button) findViewById(R.id.submitdatacompany);
        CompanyName = (EditText)findViewById(R.id.CompanyNameRegister);
        CompanyEmail = (EditText)findViewById(R.id.CompanyEmailAddressRegister);
        CompanyAddress = (EditText)findViewById(R.id.CompanyAddressRegister);
        CompanyTelephone = (EditText)findViewById(R.id.CompanyTelephoneRegister);
        CompanyPassword = (EditText)findViewById(R.id.TrainerPasswordRegister);
        CompanyPasswordVal = (EditText)findViewById(R.id.TrainerPasswordValidation);

        submitcompanydata_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompanyRegisterValidation(CompanyName.getText().toString(),CompanyEmail.getText().toString(),CompanyAddress.getText().toString(), CompanyTelephone.getText().toString(),
                        CompanyPassword.getText().toString(),CompanyPasswordVal.getText().toString());
            }
        });
    }

    private void CompanyRegisterValidation(String Company_Name, String Company_Email, String Company_Address, String Company_Telephone, String Company_Pass, String Company_Pass_Val)
    {
        if(Company_Pass != Company_Pass_Val)
        {
            Toast.makeText(CompanyUserRegistration.this,"Your Password or Password Validation doesn't match, Please Try Again!",Toast.LENGTH_LONG).show();
        }
        if(Company_Pass.length()<6)
        {
            Toast.makeText(CompanyUserRegistration.this,"Your Password is too short. Minimum length of password is 6 letters! Please Try Again!",Toast.LENGTH_LONG).show();
        }
        if(Company_Pass.length()>22)
        {
            Toast.makeText(CompanyUserRegistration.this,"Your Password is too long. Maximum length of password is 22 letters!, Please Try Again!",Toast.LENGTH_LONG).show();
        }
    }


}
