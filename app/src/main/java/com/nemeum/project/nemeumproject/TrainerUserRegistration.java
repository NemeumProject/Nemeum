package com.nemeum.project.nemeumproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TrainerUserRegistration extends AppCompatActivity {

    private EditText TrainerName;
    private EditText TrainerEmail;
    private EditText TrainerTelephone;
    private EditText TrainerPassword;
    private EditText TrainerPasswordVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_register);

        Button submittrainerdata_btn = (Button) findViewById(R.id.submitdatatrainer);
        TrainerName = (EditText)findViewById(R.id.TrainerNameRegister);
        TrainerEmail = (EditText)findViewById(R.id.TrainerEmailAddressRegister);
        TrainerTelephone = (EditText)findViewById(R.id.TrainerTelephoneRegister);
        TrainerPassword = (EditText)findViewById(R.id.TrainerPasswordRegister);
        TrainerPasswordVal = (EditText)findViewById(R.id.TrainerPasswordValidation);

        submittrainerdata_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrainerRegisterValidation(TrainerName.getText().toString(),TrainerEmail.getText().toString(), TrainerTelephone.getText().toString(),
                        TrainerPassword.getText().toString(),TrainerPasswordVal.getText().toString());
            }
        });
    }

    private void TrainerRegisterValidation(String Trainer_Name,String Trainer_Email,String Trainer_Telp,String Trainer_Pass,String Trainer_Pass_Vall)
    {
        if(Trainer_Name.length()<3)
        {
            Toast.makeText(TrainerUserRegistration.this,"Your input name less than 3 characters, please input more than 2 characters!",Toast.LENGTH_LONG).show();
        }
        if(Trainer_Name.length()>22)
        {
            Toast.makeText(TrainerUserRegistration.this,"Your input name more than 22 characters, please input less than 23 characters!",Toast.LENGTH_LONG).show();
        }
        if(Trainer_Email.length()==0)
        {
            Toast.makeText(TrainerUserRegistration.this,"Your input name more than 22 characters, please input less than 23 characters!",Toast.LENGTH_LONG).show();
        }
        if(Trainer_Telp.length()<7)
        {
            Toast.makeText(TrainerUserRegistration.this,"Your input telephone number less than 7 characters, please input more than 6 characters!",Toast.LENGTH_LONG).show();
        }
        if(Trainer_Pass.length()<6)
        {
            Toast.makeText(TrainerUserRegistration.this,"Your input password less than 6 characters, please input more than 5 characters!",Toast.LENGTH_LONG).show();
        }
        if(!Trainer_Pass.equals(Trainer_Pass_Vall))
        {
            Toast.makeText(TrainerUserRegistration.this,"Your password and your password validation doesn't match",Toast.LENGTH_LONG).show();
        }
    }
}
