package com.nemeum.project.nemeumproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import models.CompanyUser;
import models.TrainerUser;

public class TrainerDetail extends AppCompatActivity {

    private GridView weekSchedule;
    private Context appContext;
    private SharedPreferences SP;
    private BottomNavigationView menu;
    private String userType;
    private String idUser;
    private List<TrainerUser> listTrainer = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_detail);

        appContext = getApplicationContext();
        SP = appContext.getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);
        idUser = (SP.getString("idUser", ""));

        checkRegisteredUser();
        getTrainerUser();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTextInScreen();

        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.homeButton:
                        if(userType.equals(getResources().getString(R.string.individualUserSP))){
                            Intent intentMainInd = new Intent(appContext, UserLoginActivity.class);
                            appContext.startActivity(intentMainInd);
                        } else if(userType.equals(getResources().getString(R.string.trainerUserSP))){
                            Intent intentMainTrainer = new Intent(appContext, UserTrainerLoginActivity.class);
                            appContext.startActivity(intentMainTrainer);
                        } else if(userType.equals(getResources().getString(R.string.companyUserSP))){
                            Intent intentMainCompany = new Intent(appContext, UserCompanyLoginActivity.class);
                            appContext.startActivity(intentMainCompany);
                        } else {
                            Intent intentMain = new Intent(appContext, ActivityMainMock.class);
                            appContext.startActivity(intentMain);
                        }
                        return true;
                    case R.id.settingsButton:
                        Intent intentSettings = new Intent(appContext, Settings.class);
                        appContext.startActivity(intentSettings);
                        return true;
                    case R.id.loginButton:
                        Intent intentLogin = new Intent(appContext, Login.class);
                        appContext.startActivity(intentLogin);
                        return true;
                    case R.id.accountButton:
                        if(userType.equals(getResources().getString(R.string.individualUserSP))){
                            Intent intentAccount = new Intent(appContext, IndividualUserDetail.class);
                            appContext.startActivity(intentAccount);
                        } else if(userType.equals(getResources().getString(R.string.trainerUserSP))){
                            Intent intentAccount = new Intent(appContext, TrainerDetail.class);
                            appContext.startActivity(intentAccount);
                        } else {
                            Intent intentAccount = new Intent(appContext, CompanyDetail.class);
                            appContext.startActivity(intentAccount);
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void getTrainerUser(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                int numResults = 0;
                BufferedReader in;
                String data = null;
                String line;
                JSONArray parserList;
                JSONObject parser;

                HttpClient httpclient = new DefaultHttpClient();
                HttpGet request = new HttpGet();

                try{

                    URI website = new URI(getResources().getString(R.string.urlDB) + "/traineruser/" + idUser);
                    request.setURI(website);
                    HttpResponse response = httpclient.execute(request);
                    in = new BufferedReader(new InputStreamReader(
                            response.getEntity().getContent()));

                    while((line = in.readLine()) != null)
                        data += line;

                    data = data.replaceFirst("null", "");
                    parserList = new JSONArray(data);

                    while(numResults < parserList.length()) {
                        parser = (JSONObject) parserList.get(numResults);
                        TrainerUser user = new TrainerUser();
                        user.setIdTrainerUser(parser.getInt("idTrainerUser"));
                        user.setAddress(parser.getString("address"));
                        user.setCity(parser.getString("city"));
                        user.setFirstName(parser.getString("firstName"));
                        user.setMiddleSurname(parser.getString("middleSurname"));
                        user.setLastSurname(parser.getString("lastSurname"));
                        user.setDescription(parser.getString("description"));
                        user.setEmail(parser.getString("email"));
                        user.setImage(parser.getString("image"));
                        user.setPassword(parser.getString("password"));
                        user.setPhone(parser.getInt("phone"));
                        user.setPostalCode(parser.getString("postalCode"));
                        if(!parser.isNull("premium")){
                            user.setPremium(parser.getBoolean("premium"));
                        }
                        user.setSsn(parser.getString("ssn"));
                        user.setTitle(parser.getString("title"));
                        user.setUsername(parser.getString("username"));
                        numResults++;
                        listTrainer.add(user);

                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                Thread.yield();
            }
        }).start();
    }

    private void setTextInScreen(){
        TextView companyName = findViewById(R.id.companyTitleText);
        TextView sloganName = findViewById(R.id.companySloganText);
        TextView companyAddress = findViewById(R.id.companyPlaceText);
        TextView companyCity = findViewById(R.id.companyCityText);
        ImageView companyImage = findViewById(R.id.companyLogoImg);
        TextView companyDescription = findViewById(R.id.scenarioDescriptionText);
        TextView companyPhone = findViewById(R.id.trainerIIIText);

        companyName.setText(listTrainer.get(0).getFirstName());

        if(listTrainer.get(0).getTitle().equals("null")){
            sloganName.setText("Without slogan. Edit your profile.");
        }else{
            sloganName.setText(listTrainer.get(0).getTitle());
        }

        companyAddress.setText(listTrainer.get(0).getAddress());
        companyCity.setText(listTrainer.get(0).getCity());

        if(listTrainer.get(0).getImage().equals("null")){
            Picasso.get().load(R.drawable.scenario_nophoto).into(companyImage);
        }else{
            Picasso.get().load(listTrainer.get(0).getImage()).into(companyImage);
        }

        String phone = listTrainer.get(0).getPhone().toString();

        companyPhone.setText(phone);

        if(listTrainer.get(0).getDescription().equals("null")){
            companyDescription.setText("Without description. Edit your profile.");
        }else{
            companyDescription.setText(listTrainer.get(0).getDescription());
        }


    }

    private void checkRegisteredUser() {
        menu = findViewById(R.id.navigation);
        userType = SP.getString(getResources().getString(R.string.userTypeSP), "");

        if(userType.equals(getResources().getString(R.string.individualUserSP)) ||
                userType.equals(getResources().getString(R.string.trainerUserSP)) ||
                userType.equals(getResources().getString(R.string.companyUserSP))){
            menu.getMenu().getItem(2).setVisible(false);
        } else {
            menu.getMenu().getItem(3).setVisible(false);
        }
    }

    public void editPersonalInfo(View view) {
        Intent intentEditPersonalInfo = new Intent(getApplicationContext(), activity_user_editionp.class);
        startActivity(intentEditPersonalInfo);
    }
}
