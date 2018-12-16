package com.nemeum.project.nemeumproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

// Author: Antony Martinez

public class activity_login_edition extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_edition);
    }

    public void save_Edited_Data(View view) {
        //set up the variables to validate not empty fields and strings to show as error messages
        EditText etUserName = findViewById(R.id.EmailRegisterEdition);
        String strUserName = etUserName.getText().toString();
        String username_Error = getString(R.string.username_Empty);

        EditText etPassword= findViewById(R.id.password_Edition);
        String strPassword = etPassword.getText().toString();
        String password_Error= getString(R.string.password_Empty);

        EditText etNewPassword= findViewById(R.id.new_password_Edition);
        String strNew_password = etNewPassword.getText().toString();
        String new_password_Error= getString(R.string.new_Password_Empty);

        EditText etRp_newPassword= findViewById(R.id.password_Confirmation_edition);
        String strRp_new_Password = etRp_newPassword.getText().toString();
        String Rp_new_password_Error= getString(R.string.new_Password_Empty);

        //Validation of empty fields

        if(TextUtils.isEmpty(strUserName)) {
            etUserName.setError(username_Error);
            return;
        }
        else if (TextUtils.isEmpty(strPassword)){
            etPassword.setError(password_Error);
            return;

        }
        else if (TextUtils.isEmpty(strNew_password)){
            etNewPassword.setError(new_password_Error);
            return;

        }
        else if (TextUtils.isEmpty(strRp_new_Password)){
            etRp_newPassword.setError(Rp_new_password_Error);
            return;

        }




    }
}
