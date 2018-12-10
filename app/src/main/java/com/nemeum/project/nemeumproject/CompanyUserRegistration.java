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

public class CompanyUserRegistration extends AppCompatActivity {

    private EditText CompanyUserName;
    private EditText CompanyUserComercialName;
    private EditText CompanyUserContactPerson;
    private EditText CompanyUserSsn;
    private EditText CompanyUserEmail;
    private EditText CompanyUserTelephone;
    private EditText CompanyUserCity;
    private EditText CompanyUserAddress;
    private EditText CompanyUserPostalCode;
    private EditText CompanyUserUsername;
    private EditText CompanyUserPassword;
    private EditText CompanyUserPasswordVal;
    private String Premium;
    private CheckBox CompanyUserPremium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_register);

        Button submitdatacompany_btn = (Button) findViewById(R.id.submitdatacompany);
        CompanyUserName = (EditText)findViewById(R.id.CompanyNameRegister);
        CompanyUserComercialName = (EditText) findViewById(R.id.CompanyComercialNameRegister);
        CompanyUserContactPerson = (EditText) findViewById(R.id.CompanyContactPersonRegister);
        CompanyUserSsn = (EditText) findViewById(R.id.CompanySsnRegister);
        CompanyUserEmail = (EditText) findViewById(R.id.CompanyEmailAddressRegister);
        CompanyUserAddress = (EditText) findViewById(R.id.CompanyAddressRegister);
        CompanyUserCity = (EditText) findViewById(R.id.CompanyCityRegister);
        CompanyUserPostalCode = (EditText) findViewById((R.id.CompanyPostalCodeRegister));
        CompanyUserTelephone = (EditText) findViewById(R.id.CompanyTelephoneRegister);
        CompanyUserUsername = (EditText) findViewById(R.id.username);
        CompanyUserPassword = (EditText) findViewById(R.id.CompanyPasswordRegister);
        CompanyUserPasswordVal = (EditText) findViewById(R.id.CompanyPasswordValidation);
        CompanyUserPremium = (CheckBox) findViewById(R.id.Premium);
        if(CompanyUserPremium.isChecked()){
            Premium = "true";
        }else{
            Premium = "false";
        }

        submitdatacompany_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompanyUserRegisterValidation(CompanyUserName.getText().toString(), CompanyUserComercialName.getText().toString(), CompanyUserContactPerson.getText().toString(), CompanyUserSsn.getText().toString(),
                        CompanyUserEmail.getText().toString(),CompanyUserAddress.getText().toString(), CompanyUserCity.getText().toString(), CompanyUserPostalCode.getText().toString(),
                        CompanyUserTelephone.getText().toString(), CompanyUserUsername.getText().toString() ,CompanyUserPassword.getText().toString(),CompanyUserPasswordVal.getText().toString(), Premium);
            }
        });
    }

    private void CompanyUserRegisterValidation(String com_name, String comercialName, String ContactPerson, String ssn, String com_email,String com_address,
                                               String city, String postalCode, String com_telephone, String username, String com_password,String com_password_validation, String premium)
    {
        if(com_name.length()<3){
            Toast.makeText(CompanyUserRegistration.this,"Your input name less than 3 characters, please input more than 2 characters!",Toast.LENGTH_LONG).show();
        }else if(com_name.length()>22){
            Toast.makeText(CompanyUserRegistration.this,"Your input name more than 22 characters, please input less than 23 characters!",Toast.LENGTH_LONG).show();
        }else if(com_email.length()==0){
            Toast.makeText(CompanyUserRegistration.this,"Your input name more than 22 characters, please input less than 23 characters!",Toast.LENGTH_LONG).show();
        }else if(com_telephone.length()<7){
            Toast.makeText(CompanyUserRegistration.this,"Your input telephone number less than 7 characters, please input more than 6 characters!",Toast.LENGTH_LONG).show();
        }else if(com_password.length()<6){
            Toast.makeText(CompanyUserRegistration.this,"Your input password less than 6 characters, please input more than 5 characters!",Toast.LENGTH_LONG).show();
        }else if(!com_password.equals(com_password_validation)) {
            Toast.makeText(CompanyUserRegistration.this, "The passwords must be equals", Toast.LENGTH_LONG).show();
        }else if(ssn.length() == 0) {
            Toast.makeText(CompanyUserRegistration.this, "The Ssn is empty", Toast.LENGTH_LONG).show();
        }else if(comercialName.length() == 0) {
            Toast.makeText(CompanyUserRegistration.this, "The Comercial Name is empty", Toast.LENGTH_LONG).show();
        }else if(ContactPerson.length() == 0) {
            Toast.makeText(CompanyUserRegistration.this, "The Contact Person is empty", Toast.LENGTH_LONG).show();
        }else if(city.length() == 0) {
            Toast.makeText(CompanyUserRegistration.this, "The City is empty", Toast.LENGTH_LONG).show();
        }else if(postalCode.length() == 0) {
            Toast.makeText(CompanyUserRegistration.this, "The Postal Code is empty", Toast.LENGTH_LONG).show();
        }else if(username.length() == 0){
            Toast.makeText(CompanyUserRegistration.this, "The Username is empty", Toast.LENGTH_LONG).show();
        }else{
            final String nameUser = com_name;
            final String ComercialName = comercialName;
            final String contactPerson = ContactPerson;
            final String Ssn = ssn;
            final String emailUser = com_email;
            final String address = com_address;
            final String City = city;
            final String PostalCode = postalCode;
            final String phone = com_telephone;
            final String Username = username;
            final String pass = com_password;
            final String isPremium = premium;

            new Thread(new Runnable() {
                public void run() {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(UrlServer.url + "/companyuser");

                    JSONObject postData = new JSONObject();
                    try {
                        postData.put("companyName", nameUser);
                        postData.put("email", emailUser);
                        postData.put("phone", Integer.parseInt(phone));
                        postData.put("username", Username);
                        postData.put("password", pass);
                        postData.put("comercialName", ComercialName);
                        postData.put("address", address);
                        postData.put("city", City);
                        postData.put("postalCode", PostalCode);
                        postData.put("contactPerson", contactPerson);
                        postData.put("ssn", Ssn);
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
                            Intent intent1 = new Intent(CompanyUserRegistration.this, Login.class);
                            startActivity(intent1);
                        }else if(response.getStatusLine().getStatusCode() == 401){
                            CompanyUserRegistration.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(CompanyUserRegistration.this,"Email already exists!",Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            CompanyUserRegistration.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(CompanyUserRegistration.this,"Something was wrong. Please try again!",Toast.LENGTH_LONG).show();
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
