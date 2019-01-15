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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class FacilityFinder extends AppCompatActivity {

    private Context appContext;
    private SharedPreferences SP;
    private List<CompanyUser> listCompany = new ArrayList<>();
    private BottomNavigationView menu;
    private String userType;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_finder);

        appContext = getApplicationContext();
        SP = appContext.getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);
        progressBar = findViewById(R.id.progressbar);

        getAllUsers();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        checkRegisteredUser();
        ListView resultList = findViewById(R.id.facilitiesList);

        CustomAdapter customResult = new CustomAdapter();

        resultList.setAdapter(customResult);

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

    @Override
    public  void  onResume(){
        super.onResume();
        if(progressBar.getVisibility()==View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void checkRegisteredUser() {
        menu = findViewById(R.id.navigation);
        userType = SP.getString(getResources().getString(R.string.userTypeSP), "");

        if(userType.equals(getResources().getString(R.string.companyUserSP))){
            menu.getMenu().getItem(2).setVisible(false);
        } else if(userType.equals(getResources().getString(R.string.individualUserSP)) ||
                userType.equals(getResources().getString(R.string.trainerUserSP))){
            menu.getMenu().getItem(2).setVisible(false);
            menu.getMenu().getItem(3).setVisible(false);
        } else{
            menu.getMenu().getItem(3).setVisible(false);
        }
    }

    public synchronized void getAllUsers() {
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

                    URI website = new URI(getResources().getString(R.string.urlDB) + "/companyuser");
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
                        CompanyUser user = new CompanyUser();
                        user.setIdCompanyUser(parser.getInt("idCompanyUser"));
                        user.setAddress(parser.getString("address"));
                        user.setCity(parser.getString("city"));
                        user.setComercialName(parser.getString("comercialName"));
                        user.setCompanyName(parser.getString("companyName"));
                        user.setContactPerson(parser.getString("contactPerson"));
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
                        listCompany.add(user);

                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                Thread.yield();
            }
        }).start();
    }

    public void getBack(View view) {
        finish();
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return listCompany.size();
        }

        @Override
        public Object getItem(int position) {
            return listCompany.get(position);
        }

        @Override
        public long getItemId(int position) {
            return listCompany.get(position).getIdCompanyUser();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.facility_result_layout, null);

            ImageView facilityImg = convertView.findViewById(R.id.facilityResultImg);
            Button scheduleBtn = convertView.findViewById(R.id.scenarioFacilityResult);
            TextView facilityTitle = convertView.findViewById(R.id.facilityResultTitleText);
            TextView facilityAddress = convertView.findViewById(R.id.facilityResultPlaceText);
            TextView facilityEmail = convertView.findViewById(R.id.facilityResultEmailText);
            TextView facilityDescription = convertView.findViewById(R.id.facilityResultDescriptionText);

            if(!listCompany.get(position).getImage().equals("null"))
                Picasso.get().load(listCompany.get(position).getImage()).into(facilityImg);
            else
                Picasso.get().load(R.drawable.scenario_nophoto).into(facilityImg);

            if(listCompany.get(position).getComercialName().equals("null")){
                facilityTitle.setText("Without title");
            }else{
                facilityTitle.setText(listCompany.get(position).getComercialName());
            }

            facilityAddress.setText(listCompany.get(position).getAddress());
            facilityEmail.setText(listCompany.get(position).getEmail());
            if(listCompany.get(position).getDescription().equals("null")){
                facilityDescription.setText("Without description");
            }else{
                facilityDescription.setText(listCompany.get(position).getDescription());
            }
            final int index = position;
            scheduleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    SharedPreferences.Editor registeredUserEditor = SP.edit();
                    registeredUserEditor.putString(getResources().getString(R.string.idCompanySP), listCompany.get(index).getIdCompanyUser().toString());
                    registeredUserEditor.apply();
                    Intent intentBook = new Intent(appContext, ScenariosByFacility.class);
                    appContext.startActivity(intentBook);
                }
            });
            return convertView;
        }
    }
}
