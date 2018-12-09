package com.nemeum.project.nemeumproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CompanyUserRegistration extends AppCompatActivity {

    private EditText CompanyUserName;
    private EditText CompanyUserEmail;
    private EditText CompanyUserAddress;
    private EditText CompanyUserTelephone;
    private EditText CompanyUserPassword;
    private EditText CompanyUserPasswordVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_register);

        Button submitdatacompany_btn = (Button) findViewById(R.id.submitdatacompany);
        CompanyUserName = (EditText)findViewById(R.id.CompanyNameRegister);
        CompanyUserEmail = (EditText) findViewById(R.id.CompanyEmailAddressRegister);
        CompanyUserAddress = (EditText) findViewById(R.id.CompanyAddressRegister);
        CompanyUserTelephone = (EditText) findViewById(R.id.CompanyTelephoneRegister);
        CompanyUserPassword = (EditText) findViewById(R.id.CompanyPasswordRegister);
        CompanyUserPasswordVal = (EditText) findViewById(R.id.CompanyPasswordValidation);

        submitdatacompany_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompanyUserRegisterValidation(CompanyUserName.getText().toString(),CompanyUserEmail.getText().toString(),CompanyUserAddress.getText().toString(),CompanyUserTelephone.getText().toString(),CompanyUserPassword.getText().toString(),CompanyUserPasswordVal.getText().toString());
            }
        });
    }

    private void CompanyUserRegisterValidation(String com_name,String com_email,String com_address,String com_telephone,String com_password,String com_password_validation)
    {
        if(com_name.length()<3)
        {
            Toast.makeText(CompanyUserRegistration.this,"Your input name less than 3 characters, please input more than 2 characters!",Toast.LENGTH_LONG).show();
        }
        if(com_name.length()>22)
        {
            Toast.makeText(CompanyUserRegistration.this,"Your input name more than 22 characters, please input less than 23 characters!",Toast.LENGTH_LONG).show();
        }
        if(com_email.length()==0)
        {
            Toast.makeText(CompanyUserRegistration.this,"Your input name more than 22 characters, please input less than 23 characters!",Toast.LENGTH_LONG).show();
        }
        if(com_address.length()<5)
        {
            Toast.makeText(CompanyUserRegistration.this,"Please input your address correctly!",Toast.LENGTH_LONG).show();
        }
        if(com_telephone.length()<7)
        {
            Toast.makeText(CompanyUserRegistration.this,"Your input telephone number less than 7 characters, please input more than 6 characters!",Toast.LENGTH_LONG).show();
        }
        if(com_password.length()<6)
        {
            Toast.makeText(CompanyUserRegistration.this,"Your input password less than 6 characters, please input more than 5 characters!",Toast.LENGTH_LONG).show();
        }
    }

}
