package com.nemeum.project.nemeumproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NormalUserRegistration extends AppCompatActivity {

    private EditText NormalUserName;
    private EditText NormalUserEmail;
    private EditText NormalUserTelephone;
    private EditText NormalUserPassword;
    private EditText NormalUserPasswordVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_user_register);

        Button submituserdata_btn = (Button) findViewById(R.id.submitdatauser);
        NormalUserName = (EditText)findViewById(R.id.UserNameRegister);
        NormalUserEmail = (EditText) findViewById(R.id.UserEmailAddressRegister);
        NormalUserTelephone = (EditText) findViewById(R.id.UserTelephoneRegister);
        NormalUserPassword = (EditText) findViewById(R.id.UserPasswordRegister);
        NormalUserPasswordVal = (EditText) findViewById(R.id.UserPasswordValidation);

        submituserdata_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalUserRegisterValidation(NormalUserName.getText().toString(),NormalUserEmail.getText().toString(),NormalUserTelephone.getText().toString(),
                        NormalUserPassword.getText().toString(),NormalUserPasswordVal.getText().toString());
            }
        });
    }

    private void NormalUserRegisterValidation(String name,String email,String telephone,String password,String password_validation)
    {
        if(name.length()<3)
        {
            Toast.makeText(NormalUserRegistration.this,"Your input name less than 3 characters, please input more than 2 characters!",Toast.LENGTH_LONG).show();
        }
        if(name.length()>22)
        {
            Toast.makeText(NormalUserRegistration.this,"Your input name more than 22 characters, please input less than 23 characters!",Toast.LENGTH_LONG).show();
        }
        if(email.length()==0)
        {
            Toast.makeText(NormalUserRegistration.this,"Your input name more than 22 characters, please input less than 23 characters!",Toast.LENGTH_LONG).show();
        }
        if(telephone.length()<7)
        {
            Toast.makeText(NormalUserRegistration.this,"Your input telephone number less than 7 characters, please input more than 6 characters!",Toast.LENGTH_LONG).show();
        }
        if(password.length()<6)
        {
            Toast.makeText(NormalUserRegistration.this,"Your input password less than 6 characters, please input more than 5 characters!",Toast.LENGTH_LONG).show();
        }
    }
}
