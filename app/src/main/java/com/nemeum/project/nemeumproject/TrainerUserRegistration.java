package com.nemeum.project.nemeumproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        if(Trainer_Pass != Trainer_Pass_Vall)
        {

        }
    }
}
