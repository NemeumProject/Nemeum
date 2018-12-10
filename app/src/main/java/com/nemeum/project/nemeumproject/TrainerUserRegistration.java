package com.nemeum.project.nemeumproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import url.UrlServer;

public class TrainerUserRegistration extends AppCompatActivity {

    private EditText TrainerName;
    private EditText TrainerEmail;
    private EditText TrainerTelephone;
    private EditText TrainerPassword;
    private EditText TrainerPasswordVal;
    private EditText TrainerUsername;
    private EditText TrainerUserSurname;
    private EditText TrainerUserAddress;
    private EditText TrainerUserCity;
    private EditText TrainerUserPostalCode;
    private String Premium;
    private CheckBox TrainerUserPremium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_register);

        Button submittrainerdata_btn = (Button) findViewById(R.id.submitdatatrainer);
        TrainerName = (EditText)findViewById(R.id.TrainerNameRegister);
        TrainerEmail = (EditText)findViewById(R.id.TrainerEmailAddressRegister);
        TrainerTelephone = (EditText)findViewById(R.id.TrainerTelephoneRegister);
        TrainerUsername = (EditText) findViewById(R.id.username);
        TrainerPassword = (EditText)findViewById(R.id.TrainerPasswordRegister);
        TrainerPasswordVal = (EditText)findViewById(R.id.TrainerPasswordValidation);
        TrainerUserSurname = (EditText) findViewById(R.id.TrainerLastNameRegister) ;
        TrainerUserAddress = (EditText) findViewById(R.id.TrainerAddressRegister);
        TrainerUserCity = (EditText) findViewById(R.id.TrainerCityRegister);
        TrainerUserPostalCode = (EditText) findViewById(R.id.TrainerPostalCodeRegister);
        TrainerUserPremium = (CheckBox) findViewById(R.id.Premium);
        if(TrainerUserPremium.isChecked()){
            Premium = "true";
        }else{
            Premium = "false";
        }

        submittrainerdata_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrainerRegisterValidation(TrainerName.getText().toString(),TrainerEmail.getText().toString(), TrainerTelephone.getText().toString(),
                        TrainerUsername.getText().toString(), TrainerPassword.getText().toString(),TrainerPasswordVal.getText().toString(), TrainerUserSurname.getText().toString(),
                        TrainerUserAddress.getText().toString(), TrainerUserCity.getText().toString(), TrainerUserPostalCode.getText().toString(),
                        Premium);
            }
        });
    }

    private void TrainerRegisterValidation(String Trainer_Name,String Trainer_Email,String Trainer_Telp, String username, String Trainer_Pass,String Trainer_Pass_Vall, String surname, String address, String city, String postalCode, String premium)
    {
        if(Trainer_Name.length()<3){
            Toast.makeText(TrainerUserRegistration.this,"Your input name less than 3 characters, please input more than 2 characters!",Toast.LENGTH_LONG).show();
        }else if(Trainer_Name.length()>22){
            Toast.makeText(TrainerUserRegistration.this,"Your input name more than 22 characters, please input less than 23 characters!",Toast.LENGTH_LONG).show();
        }else if(Trainer_Email.length()==0){
            Toast.makeText(TrainerUserRegistration.this,"Your input name more than 22 characters, please input less than 23 characters!",Toast.LENGTH_LONG).show();
        }else if(Trainer_Telp.length()<7){
            Toast.makeText(TrainerUserRegistration.this,"Your input telephone number less than 7 characters, please input more than 6 characters!",Toast.LENGTH_LONG).show();
        }else if(Trainer_Pass.length()<6){
            Toast.makeText(TrainerUserRegistration.this,"Your input password less than 6 characters, please input more than 5 characters!",Toast.LENGTH_LONG).show();
        }else if(!Trainer_Pass.equals(Trainer_Pass_Vall)){
            Toast.makeText(TrainerUserRegistration.this,"The passwords must be equals",Toast.LENGTH_LONG).show();
        }else if(username.length() == 0) {
            Toast.makeText(TrainerUserRegistration.this, "The Username is empty", Toast.LENGTH_LONG).show();
        }else if(surname.length() == 0) {
            Toast.makeText(TrainerUserRegistration.this, "The Surname is empty", Toast.LENGTH_LONG).show();
        }else if(address.length() == 0) {
            Toast.makeText(TrainerUserRegistration.this, "The Address is empty", Toast.LENGTH_LONG).show();
        }else if(city.length() == 0) {
            Toast.makeText(TrainerUserRegistration.this, "The City is empty", Toast.LENGTH_LONG).show();
        }else if(postalCode.length() == 0){
            Toast.makeText(TrainerUserRegistration.this, "The Postal Code is empty", Toast.LENGTH_LONG).show();
        }else{
            final String nameUser = Trainer_Name;
            final String emailUser = Trainer_Email;
            final String phone = Trainer_Telp;
            final String Username = username;
            final String pass = Trainer_Pass;
            final String Surname = surname;
            final String Address = address;
            final String City = city;
            final String postal = postalCode;
            final String isPremium = premium;

            new Thread(new Runnable() {
                public void run() {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(UrlServer.url + "/traineruser");

                    JSONObject postData = new JSONObject();
                    try {
                        postData.put("firstName", nameUser);
                        postData.put("email", emailUser);
                        postData.put("phone", Integer.parseInt(phone));
                        postData.put("username", Username);
                        postData.put("password", pass);
                        postData.put("lastSurname", Surname);
                        postData.put("address", Address);
                        postData.put("city", City);
                        postData.put("postalCode", postal);
                        if(isPremium.equals("true")){
                            postData.put("premium", true);
                        }else{
                            postData.put("premium", false);
                        }
                        StringEntity se = null;
                        se = new StringEntity(postData.toString());
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        httppost.setEntity(se);
                        HttpResponse response = httpclient.execute(httppost);
                        if(response.getStatusLine().getStatusCode() == 200){
                            Intent intent1 = new Intent(TrainerUserRegistration.this, Login.class);
                            startActivity(intent1);
                        }else if(response.getStatusLine().getStatusCode() == 401){
                            TrainerUserRegistration.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(TrainerUserRegistration.this,"Email already exists!",Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            TrainerUserRegistration.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(TrainerUserRegistration.this,"Something was wrong. Please try again!",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
