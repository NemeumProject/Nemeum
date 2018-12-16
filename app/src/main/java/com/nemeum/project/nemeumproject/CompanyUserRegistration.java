package com.nemeum.project.nemeumproject;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
    private boolean Premium;
    private CheckBox CompanyUserPremium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_register);

        final View activityRootView = findViewById(R.id.companyRegisterActivity);
        final Resources r = getResources();
        final ScrollView scroll = findViewById(R.id.scrollCompanyRegister);
        int keyboardPx = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 315, r.getDisplayMetrics()));
        final RelativeLayout.LayoutParams paramWithKeyboard = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, keyboardPx);
        paramWithKeyboard.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        paramWithKeyboard.addRule(RelativeLayout.CENTER_HORIZONTAL);
        int foldPx = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 343, r.getDisplayMetrics()));
        final RelativeLayout.LayoutParams paramFoldedKeyboard = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, foldPx);
        paramFoldedKeyboard.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        paramFoldedKeyboard.addRule(RelativeLayout.CENTER_HORIZONTAL);

        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > dpToPx(getApplicationContext(), 200)) {
                    scroll.setLayoutParams(paramWithKeyboard);
                } else {
                    scroll.setLayoutParams(paramFoldedKeyboard);
                }
            }
        });

        Button submitdatacompany_btn = findViewById(R.id.submitdatacompany);
        CompanyUserName = findViewById(R.id.CompanyNameRegister);
        CompanyUserComercialName = findViewById(R.id.CompanyComercialNameRegister);
        CompanyUserContactPerson = findViewById(R.id.CompanyContactPersonRegister);
        CompanyUserSsn = findViewById(R.id.CompanySsnRegister);
        CompanyUserEmail = findViewById(R.id.CompanyEmailAddressRegister);
        CompanyUserAddress = findViewById(R.id.CompanyAddressRegister);
        CompanyUserCity = findViewById(R.id.CompanyCityRegister);
        CompanyUserPostalCode = findViewById((R.id.CompanyPostalCodeRegister));
        CompanyUserTelephone = findViewById(R.id.CompanyTelephoneRegister);
        CompanyUserUsername = findViewById(R.id.username);
        CompanyUserPassword = findViewById(R.id.CompanyPasswordRegister);
        CompanyUserPasswordVal = findViewById(R.id.CompanyPasswordValidation);
        CompanyUserPremium = findViewById(R.id.Premium);
        Premium = CompanyUserPremium.isChecked();

        submitdatacompany_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompanyUserRegisterValidation(CompanyUserName.getText().toString(), CompanyUserComercialName.getText().toString(), CompanyUserContactPerson.getText().toString(), CompanyUserSsn.getText().toString(),
                        CompanyUserEmail.getText().toString(),CompanyUserAddress.getText().toString(), CompanyUserCity.getText().toString(), CompanyUserPostalCode.getText().toString(),
                        CompanyUserTelephone.getText().toString(), CompanyUserUsername.getText().toString() ,CompanyUserPassword.getText().toString(),CompanyUserPasswordVal.getText().toString(), Premium);
            }
        });
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    private void CompanyUserRegisterValidation(String com_name, String comercialName, String ContactPerson, String ssn, String com_email,String com_address,
                                               String city, String postalCode, String com_telephone, String username, String com_password,String com_password_validation, boolean premium)
    {
        if(com_name.length()<3){
            Toast.makeText(CompanyUserRegistration.this, getResources().getString(R.string.company_name_short), Toast.LENGTH_LONG).show();
        }else if(com_name.length()>22){
            Toast.makeText(CompanyUserRegistration.this, getResources().getString(R.string.company_name_long), Toast.LENGTH_LONG).show();
        }else if(com_email.length()==0){
            Toast.makeText(CompanyUserRegistration.this, getResources().getString(R.string.company_email_empty), Toast.LENGTH_LONG).show();
        }else if(com_telephone.length()<7){
            Toast.makeText(CompanyUserRegistration.this, getResources().getString(R.string.company_phone_invalid), Toast.LENGTH_LONG).show();
        }else if(com_password.length()<6){
            Toast.makeText(CompanyUserRegistration.this, getResources().getString(R.string.company_password_short), Toast.LENGTH_LONG).show();
        }else if(!com_password.equals(com_password_validation)) {
            Toast.makeText(CompanyUserRegistration.this, getResources().getString(R.string.company_password_mismatch), Toast.LENGTH_LONG).show();
        }else if(ssn.length() == 0) {
            Toast.makeText(CompanyUserRegistration.this, getResources().getString(R.string.company_ssn_empty), Toast.LENGTH_LONG).show();
        }else if(comercialName.length() == 0) {
            Toast.makeText(CompanyUserRegistration.this, getResources().getString(R.string.company_commercial_name_empty) , Toast.LENGTH_LONG).show();
        }else if(ContactPerson.length() == 0) {
            Toast.makeText(CompanyUserRegistration.this, getResources().getString(R.string.company_contact_person_empty), Toast.LENGTH_LONG).show();
        }else if(city.length() == 0) {
            Toast.makeText(CompanyUserRegistration.this, getResources().getString(R.string.company_city_empty), Toast.LENGTH_LONG).show();
        }else if(postalCode.length() == 0) {
            Toast.makeText(CompanyUserRegistration.this, getResources().getString(R.string.company_postal_code_empty), Toast.LENGTH_LONG).show();
        }else if(username.length() == 0){
            Toast.makeText(CompanyUserRegistration.this, getResources().getString(R.string.company_username_empty), Toast.LENGTH_LONG).show();
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
            final boolean isPremium = premium;

            new Thread(new Runnable() {
                public void run() {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(getResources().getString(R.string.urlDB) + getResources().getString(R.string.companyUserDB));

                    JSONObject postData = new JSONObject();
                    try {
                        postData.put(getResources().getString(R.string.companyNameJson), nameUser);
                        postData.put(getResources().getString(R.string.companyEmailJson), emailUser);
                        postData.put(getResources().getString(R.string.companyPhoneJson), Integer.parseInt(phone));
                        postData.put(getResources().getString(R.string.companyUsernameJson), Username);
                        postData.put(getResources().getString(R.string.companyPasswordJson), pass);
                        postData.put(getResources().getString(R.string.companyCommercialNameJson), ComercialName);
                        postData.put(getResources().getString(R.string.companyAddressJson), address);
                        postData.put(getResources().getString(R.string.companyCityJson), City);
                        postData.put(getResources().getString(R.string.companyPostalCodeJson), PostalCode);
                        postData.put(getResources().getString(R.string.companyContactPersonJson), contactPerson);
                        postData.put(getResources().getString(R.string.companySSNJson), Ssn);
                        postData.put(getResources().getString(R.string.companyPremiumJson), isPremium);
                        StringEntity se = new StringEntity(postData.toString());
                        httppost.setHeader(getResources().getString(R.string.dbAccessAccept), getResources().getString(R.string.dbAccessAppJson));
                        httppost.setHeader(getResources().getString(R.string.dbAccessContentType), getResources().getString(R.string.dbAccessAppJson));
                        httppost.setEntity(se);
                        HttpResponse response = httpclient.execute(httppost);
                        if(response.getStatusLine().getStatusCode() == 200){
                            Intent intent1 = new Intent(CompanyUserRegistration.this, Login.class);
                            startActivity(intent1);
                        }else if(response.getStatusLine().getStatusCode() == 401){
                            CompanyUserRegistration.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(CompanyUserRegistration.this,getResources().getString(R.string.company_email_exists),Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            CompanyUserRegistration.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(CompanyUserRegistration.this,getResources().getString(R.string.requisitionError),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } catch (IOException e) {
                        CompanyUserRegistration.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(CompanyUserRegistration.this, getResources().getString(R.string.noconnection), Toast.LENGTH_LONG).show();
                            }
                        });
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

}
