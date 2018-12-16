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
    private boolean Premium;
    private CheckBox TrainerUserPremium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_register);

        final View activityRootView = findViewById(R.id.trainerRegisterActivity);
        final Resources r = getResources();
        final ScrollView scroll = findViewById(R.id.scrollTrainerRegister);
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

        Button submittrainerdata_btn = findViewById(R.id.submitdatatrainer);
        TrainerName = findViewById(R.id.TrainerNameRegister);
        TrainerEmail = findViewById(R.id.TrainerEmailAddressRegister);
        TrainerTelephone = findViewById(R.id.TrainerTelephoneRegister);
        TrainerUsername = findViewById(R.id.username);
        TrainerPassword = findViewById(R.id.TrainerPasswordRegister);
        TrainerPasswordVal = findViewById(R.id.TrainerPasswordValidation);
        TrainerUserSurname = findViewById(R.id.TrainerLastNameRegister);
        TrainerUserAddress = findViewById(R.id.TrainerAddressRegister);
        TrainerUserCity = findViewById(R.id.TrainerCityRegister);
        TrainerUserPostalCode = findViewById(R.id.TrainerPostalCodeRegister);
        TrainerUserPremium = findViewById(R.id.Premium);
        Premium = TrainerUserPremium.isChecked();

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

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    private void TrainerRegisterValidation(String Trainer_Name,String Trainer_Email,String Trainer_Telp, String username, String Trainer_Pass,String Trainer_Pass_Vall, String surname, String address, String city, String postalCode, boolean premium)
    {
        if(Trainer_Name.length()<3){
            Toast.makeText(TrainerUserRegistration.this, getResources().getString(R.string.trainer_name_short), Toast.LENGTH_LONG).show();
        }else if(Trainer_Name.length()>22){
            Toast.makeText(TrainerUserRegistration.this, getResources().getString(R.string.trainer_name_long), Toast.LENGTH_LONG).show();
        }else if(Trainer_Email.length()==0){
            Toast.makeText(TrainerUserRegistration.this, getResources().getString(R.string.trainer_email_empty),Toast.LENGTH_LONG).show();
        }else if(Trainer_Telp.length()<7){
            Toast.makeText(TrainerUserRegistration.this, getResources().getString(R.string.trainer_phone_invalid),Toast.LENGTH_LONG).show();
        }else if(Trainer_Pass.length()<6){
            Toast.makeText(TrainerUserRegistration.this, getResources().getString(R.string.trainer_password_short), Toast.LENGTH_LONG).show();
        }else if(!Trainer_Pass.equals(Trainer_Pass_Vall)){
            Toast.makeText(TrainerUserRegistration.this, getResources().getString(R.string.trainer_password_mismatch), Toast.LENGTH_LONG).show();
        }else if(username.length() == 0) {
            Toast.makeText(TrainerUserRegistration.this, getResources().getString(R.string.trainer_username_empty), Toast.LENGTH_LONG).show();
        }else if(surname.length() == 0) {
            Toast.makeText(TrainerUserRegistration.this, getResources().getString(R.string.trainer_surname_empty), Toast.LENGTH_LONG).show();
        }else if(address.length() == 0) {
            Toast.makeText(TrainerUserRegistration.this, getResources().getString(R.string.trainer_address_Empty), Toast.LENGTH_LONG).show();
        }else if(city.length() == 0) {
            Toast.makeText(TrainerUserRegistration.this, getResources().getString(R.string.trainer_city_empty), Toast.LENGTH_LONG).show();
        }else if(postalCode.length() == 0){
            Toast.makeText(TrainerUserRegistration.this, getResources().getString(R.string.trainer_postal_code_empty), Toast.LENGTH_LONG).show();
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
            final boolean isPremium = premium;

            new Thread(new Runnable() {
                public void run() {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(getResources().getString(R.string.urlDB) + getResources().getString(R.string.trainerDB));

                    JSONObject postData = new JSONObject();
                    try {
                        postData.put(getResources().getString(R.string.trainerNameJson), nameUser);
                        postData.put(getResources().getString(R.string.trainerEmailJson), emailUser);
                        postData.put(getResources().getString(R.string.trainerPhoneJson), Integer.parseInt(phone));
                        postData.put(getResources().getString(R.string.trainerUsernameJson), Username);
                        postData.put(getResources().getString(R.string.trainerPasswordJson), pass);
                        postData.put(getResources().getString(R.string.trainerSurnameJson), Surname);
                        postData.put(getResources().getString(R.string.trainerAddressJson), Address);
                        postData.put(getResources().getString(R.string.trainerCityJson), City);
                        postData.put(getResources().getString(R.string.trainerPostalCodeJson), postal);
                        postData.put(getResources().getString(R.string.trainerPremiumJson), isPremium);
                        StringEntity se = new StringEntity(postData.toString());
                        httppost.setHeader(getResources().getString(R.string.dbAccessAccept), getResources().getString(R.string.dbAccessAppJson));
                        httppost.setHeader(getResources().getString(R.string.dbAccessContentType), getResources().getString(R.string.dbAccessAppJson));
                        httppost.setEntity(se);
                        HttpResponse response = httpclient.execute(httppost);
                        if(response.getStatusLine().getStatusCode() == 200){
                            Intent intent1 = new Intent(TrainerUserRegistration.this, Login.class);
                            startActivity(intent1);
                        }else if(response.getStatusLine().getStatusCode() == 401){
                            TrainerUserRegistration.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(TrainerUserRegistration.this,getResources().getString(R.string.trainer_email_exists), Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            TrainerUserRegistration.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(TrainerUserRegistration.this, getResources().getString(R.string.requisitionError), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } catch (IOException e) {
                        TrainerUserRegistration.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(TrainerUserRegistration.this, getResources().getString(R.string.noconnection), Toast.LENGTH_LONG).show();
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
