package com.nemeum.project.nemeumproject;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
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
    private boolean Premium;
    private CheckBox NormalUserPremium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_user_register);

        final View activityRootView = findViewById(R.id.userRegisterActivity);
        final Resources r = getResources();
        final ScrollView scroll = findViewById(R.id.scrollUserRegister);
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

        Button submituserdata_btn = findViewById(R.id.submitdatauser);
        NormalName = findViewById(R.id.UserNameRegister);
        NormalUserEmail = findViewById(R.id.UserEmailAddressRegister);
        NormalUserTelephone = findViewById(R.id.UserTelephoneRegister);
        NormalUserUsername = findViewById(R.id.username);
        NormalUserPassword = findViewById(R.id.UserPasswordRegister);
        NormalUserPasswordVal = findViewById(R.id.UserPasswordValidation);
        NormalUserSurname = findViewById(R.id.UserLastNameRegister);
        NormalUserAddress = findViewById(R.id.UserAddressRegister);
        NormalUserCity = findViewById(R.id.UserCityRegister);
        NormalUserPostalCode = findViewById(R.id.UserPostalCodeRegister);
        NormalUserPremium = findViewById(R.id.Premium);
        Premium = NormalUserPremium.isChecked();

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

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    private void NormalUserRegisterValidation(String name,String email,String telephone, String username, String password,String password_validation, String surname, String address, String city, String postalCode, boolean premium)
    {
        if(name.length()<3){
            Toast.makeText(NormalUserRegistration.this, getResources().getString(R.string.user_name_short),Toast.LENGTH_LONG).show();
        }else if(name.length()>22){
            Toast.makeText(NormalUserRegistration.this, getResources().getString(R.string.user_name_long),Toast.LENGTH_LONG).show();
        }else if(email.length()==0){
            Toast.makeText(NormalUserRegistration.this, getResources().getString(R.string.name_UserEdition_Empty),Toast.LENGTH_LONG).show();
        }else if(telephone.length()<7){
            Toast.makeText(NormalUserRegistration.this, getResources().getString(R.string.user_phone_invalid),Toast.LENGTH_LONG).show();
        }else if(password.length()<6){
            Toast.makeText(NormalUserRegistration.this, getResources().getString(R.string.user_password_short),Toast.LENGTH_LONG).show();
        }else if(!password.equals(password_validation)) {
            Toast.makeText(NormalUserRegistration.this, getResources().getString(R.string.user_password_mismatch),Toast.LENGTH_LONG).show();
        }else if(username.length() == 0) {
            Toast.makeText(NormalUserRegistration.this, getResources().getString(R.string.user_username_empty),Toast.LENGTH_LONG).show();
        }else if(surname.length() == 0) {
            Toast.makeText(NormalUserRegistration.this, getResources().getString(R.string.user_surname_empty),Toast.LENGTH_LONG).show();
        }else if(address.length() == 0) {
            Toast.makeText(NormalUserRegistration.this, getResources().getString(R.string.user_address_Empty),Toast.LENGTH_LONG).show();
        }else if(city.length() == 0) {
            Toast.makeText(NormalUserRegistration.this, getResources().getString(R.string.user_city_empty),Toast.LENGTH_LONG).show();
        }else if(postalCode.length() == 0){
            Toast.makeText(NormalUserRegistration.this, getResources().getString(R.string.user_postal_code_empty),Toast.LENGTH_LONG).show();
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
            final boolean isPremium = premium;

            new Thread(new Runnable() {
                public void run() {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(getResources().getString(R.string.urlDB) + getResources().getString(R.string.userDB));

                    JSONObject postData = new JSONObject();
                    try {
                        postData.put(getResources().getString(R.string.individualNameJson), nameUser);
                        postData.put(getResources().getString(R.string.individualEmailJson), emailUser);
                        postData.put(getResources().getString(R.string.individualPhoneJson), Integer.parseInt(phone));
                        postData.put(getResources().getString(R.string.individualUsernameJson), Username);
                        postData.put(getResources().getString(R.string.individualPasswordJson), pass);
                        postData.put(getResources().getString(R.string.individualSurnameJson), Surname);
                        postData.put(getResources().getString(R.string.individualAddressJson), Address);
                        postData.put(getResources().getString(R.string.individualCityJson), City);
                        postData.put(getResources().getString(R.string.individualPostalCodeJson), postal);
                        postData.put(getResources().getString(R.string.individualPremiumJson), isPremium);
                        StringEntity se = new StringEntity(postData.toString());
                        httppost.setHeader(getResources().getString(R.string.dbAccessAccept), getResources().getString(R.string.dbAccessAppJson));
                        httppost.setHeader(getResources().getString(R.string.dbAccessContentType), getResources().getString(R.string.dbAccessAppJson));
                        httppost.setEntity(se);
                        HttpResponse response = httpclient.execute(httppost);
                        if(response.getStatusLine().getStatusCode() == 200){
                            Intent intent1 = new Intent(NormalUserRegistration.this, Login.class);
                            startActivity(intent1);
                        }else if(response.getStatusLine().getStatusCode() == 401){
                            NormalUserRegistration.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(NormalUserRegistration.this,getResources().getString(R.string.user_email_exists),Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            NormalUserRegistration.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(NormalUserRegistration.this,getResources().getString(R.string.requisitionError),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } catch (IOException e) {
                        NormalUserRegistration.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(NormalUserRegistration.this, getResources().getString(R.string.noconnection), Toast.LENGTH_LONG).show();
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
