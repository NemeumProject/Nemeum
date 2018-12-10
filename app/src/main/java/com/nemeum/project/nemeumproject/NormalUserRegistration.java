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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import url.UrlServer;

public class NormalUserRegistration extends AppCompatActivity {

    private EditText NormalName;
    private EditText NormalUserEmail;
    private EditText NormalUserTelephone;
    private EditText NormalUserUsername;
    private EditText NormalUserPassword;
    private EditText NormalUserPasswordVal;
    private EditText NormalUserSurname;
    private EditText NormalUserAddress;
    private EditText NormalUserCity;
    private EditText NormalUserPostalCode;
    private String Premium;
    private CheckBox NormalUserPremium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_user_register);

        Button submituserdata_btn = (Button) findViewById(R.id.submitdatauser);
        NormalName = (EditText)findViewById(R.id.UserNameRegister);
        NormalUserEmail = (EditText) findViewById(R.id.UserEmailAddressRegister);
        NormalUserTelephone = (EditText) findViewById(R.id.UserTelephoneRegister);
        NormalUserUsername = (EditText)  findViewById(R.id.username);
        NormalUserPassword = (EditText) findViewById(R.id.UserPasswordRegister);
        NormalUserPasswordVal = (EditText) findViewById(R.id.UserPasswordValidation);
        NormalUserSurname = (EditText) findViewById(R.id.UserLastNameRegister) ;
        NormalUserAddress = (EditText) findViewById(R.id.UserAddressRegister);
        NormalUserCity = (EditText) findViewById(R.id.UserCityRegister);
        NormalUserPostalCode = (EditText) findViewById(R.id.UserPostalCodeRegister);
        NormalUserPremium = (CheckBox) findViewById(R.id.Premium);
        if(NormalUserPremium.isChecked()){
            Premium = "true";
        }else{
            Premium = "false";
        }

        submituserdata_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalUserRegisterValidation(NormalName.getText().toString(),NormalUserEmail.getText().toString(),NormalUserTelephone.getText().toString(),
                        NormalUserUsername.getText().toString() ,NormalUserPassword.getText().toString(),NormalUserPasswordVal.getText().toString(),
                        NormalUserSurname.getText().toString(), NormalUserAddress.getText().toString(), NormalUserCity.getText().toString(),
                NormalUserPostalCode.getText().toString(), Premium);
            }
        });
    }

    private void NormalUserRegisterValidation(String name,String email,String telephone, String username, String password,String password_validation, String surname, String address, String city, String postalCode, String premium)
    {
        if(name.length()<3){
            Toast.makeText(NormalUserRegistration.this,"Your input name less than 3 characters, please input more than 2 characters!",Toast.LENGTH_LONG).show();
        }else if(name.length()>22){
            Toast.makeText(NormalUserRegistration.this,"Your input name more than 22 characters, please input less than 23 characters!",Toast.LENGTH_LONG).show();
        }else if(email.length()==0){
            Toast.makeText(NormalUserRegistration.this,"Your input name more than 22 characters, please input less than 23 characters!",Toast.LENGTH_LONG).show();
        }else if(telephone.length()<7){
            Toast.makeText(NormalUserRegistration.this,"Your input telephone number less than 7 characters, please input more than 6 characters!",Toast.LENGTH_LONG).show();
        }else if(password.length()<6){
            Toast.makeText(NormalUserRegistration.this,"Your input password less than 6 characters, please input more than 5 characters!",Toast.LENGTH_LONG).show();
        }else if(!password.equals(password_validation)) {
            Toast.makeText(NormalUserRegistration.this, "The passwords must be equals", Toast.LENGTH_LONG).show();
        }else if(username.length() == 0) {
            Toast.makeText(NormalUserRegistration.this, "The Username is empty", Toast.LENGTH_LONG).show();
        }else if(surname.length() == 0) {
            Toast.makeText(NormalUserRegistration.this, "The Surname is empty", Toast.LENGTH_LONG).show();
        }else if(address.length() == 0) {
            Toast.makeText(NormalUserRegistration.this, "The Address is empty", Toast.LENGTH_LONG).show();
        }else if(city.length() == 0) {
            Toast.makeText(NormalUserRegistration.this, "The City is empty", Toast.LENGTH_LONG).show();
        }else if(postalCode.length() == 0){
            Toast.makeText(NormalUserRegistration.this, "The Postal Code is empty", Toast.LENGTH_LONG).show();
        }else{
            final String nameUser = name;
            final String emailUser = email;
            final String phone = telephone;
            final String Username = username;
            final String pass = password;
            final String Surname = surname;
            final String Address = address;
            final String City = city;
            final String postal = postalCode;
            final String isPremium = premium;

            new Thread(new Runnable() {
                public void run() {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(UrlServer.url + "/user");

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
                            Intent intent1 = new Intent(NormalUserRegistration.this, Login.class);
                            startActivity(intent1);
                        }else if(response.getStatusLine().getStatusCode() == 401){
                            NormalUserRegistration.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(NormalUserRegistration.this,"Email already exists!",Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            NormalUserRegistration.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(NormalUserRegistration.this,"Something was wrong. Please try again!",Toast.LENGTH_LONG).show();
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
