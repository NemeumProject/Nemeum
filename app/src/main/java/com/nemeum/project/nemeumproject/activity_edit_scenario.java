package com.nemeum.project.nemeumproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.Scenario;
import models.Sport;

public class activity_edit_scenario extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Context appContext;
    private SharedPreferences SP;
    private BottomNavigationView menu;
    private String userType;

    ImageButton changePicture;
    Button back_btn;
    private static final int PICK_IMAGE=100;
    Uri imageUri;
    String idUser;
    String idScenario;
    Integer idSport;
    List<Scenario> listScenario = new ArrayList<>();
    SharedPreferences registeredUserPref = null;
    SharedPreferences shared = null;
    List<Sport> listSport = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_scenario);

        appContext = getApplicationContext();
        SP = appContext.getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);

        checkRegisteredUser();

        registeredUserPref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.userTypeSP), getApplicationContext().MODE_PRIVATE);
        shared = getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);
        idUser = (shared.getString("idUser", ""));

        getScenarios();
        getAllSports();
        List<String> listTitle = new ArrayList<>();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(Scenario scenario : listScenario){
            listTitle.add(scenario.getTitle());
        }
        List<String> sportName = new ArrayList<>();

        for(Sport sport : listSport){
            sportName.add(sport.getName());
        }

        Spinner spinner = findViewById(R.id.scenarios_Spinner_EditScenario);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_layout, listTitle);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final Spinner sportSpinner = findViewById(R.id.scenario_edit_sport);
        ArrayAdapter<String> sportAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_layout, sportName);
        sportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportSpinner.setAdapter(sportAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Object item = parentView.getItemAtPosition(position);
                Scenario scenarioSelected = listScenario.get(position);
                idScenario = listScenario.get(position).getIdScenario().toString();

                EditText title = (EditText)findViewById(R.id.title_Edit_Scenario);
                title.setText(scenarioSelected.getTitle());
                EditText location = (EditText)findViewById(R.id.location_Edit_Scenario);
                location.setText(scenarioSelected.getAddress());
                EditText description = (EditText)findViewById(R.id.description_Edit_Scenario);
                description.setText(scenarioSelected.getDescription());
                EditText price = (EditText)findViewById(R.id.price_Edit_Scenario);
                price.setText(scenarioSelected.getPrice().toString());
                EditText capacity = (EditText)findViewById(R.id.capacity_Edit_Scenarios);
                capacity.setText(scenarioSelected.getCapacity().toString());
                int b = 1;
                for(int i = 0; i<listSport.size(); i++){
                    if(listSport.get(i).getIdSport().equals(scenarioSelected.getIdSport())){
                        b = i;
                    }
                }
                sportSpinner.setSelection(b);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        sportSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                idSport = listSport.get(position).getIdSport();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

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

    public synchronized void getAllSports() {
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

                    URI website = new URI(getResources().getString(R.string.urlDB) + "/sport" + getResources().getString(R.string.listDB));
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
                        Sport sport = new Sport();
                        sport.setIdSport(parser.getInt("idSport"));
                        sport.setName(parser.getString("name"));
                        numResults++;
                        listSport.add(sport);

                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                Thread.yield();
            }
        }).start();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private synchronized void getScenarios(){
        new Thread(new Runnable() {
            public void run() {
                String data = null;
                int numResults = 0;
                String line;
                JSONArray parserList;
                JSONObject parser;
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(getResources().getString(R.string.urlDB) + "/scenario/company/" + idUser);

                try {
                    httpGet.setHeader("Accept", "application/json");
                    httpGet.setHeader("Content-type", "application/json");
                    HttpResponse response = httpclient.execute(httpGet);
                    if(response.getStatusLine().getStatusCode() == 200) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                response.getEntity().getContent()));

                        while(( line = in.readLine()) != null){
                            data += line;
                        }
                        data = data.replaceFirst("null", "");
                        parserList = new JSONArray(data);

                        while(numResults < parserList.length()) {
                            parser = (JSONObject) parserList.get(numResults);
                            Scenario scenario = new Scenario();
                            scenario.setIdScenario(parser.getInt(getResources().getString(R.string.scenarioIdJson)));
                            scenario.setIdSport(parser.getInt(getResources().getString(R.string.scenarioSportIdJson)));
                            scenario.setPrice(parser.getDouble(getResources().getString(R.string.scenarioPriceJson)));
                            scenario.setIdCompany(parser.getInt(getResources().getString(R.string.scenarioCompanyIdJson)));
                            scenario.setDescription(parser.getString(getResources().getString(R.string.scenarioDescriptionJson)));
                            scenario.setCapacity(parser.getInt("capacity"));
                            scenario.setAddress(parser.getString("address"));
                            String dateStr = parser.getString("dateScenario");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            scenario.setDateScenario(sdf.parse(dateStr));
                            scenario.setTitle(parser.getString("title"));
                            scenario.setImage(parser.getString("image"));
                            if(!parser.isNull("indoor")){
                                scenario.setIndoor(parser.getBoolean("indoor"));
                            }
                            numResults++;
                            listScenario.add(scenario);

                        }
                    }

                } catch (IOException e) {
                    activity_edit_scenario.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity_edit_scenario.this, "Something was wrong! Try to repeat", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void edit_scenario(View view) {
        //set up the variables to validate not empty fields and strings to show as error messages
        EditText etTitle = findViewById(R.id.title_Edit_Scenario);
        String strTitle = etTitle.getText().toString();
        String title_Error = getString(R.string.title_Post_Empty);

        EditText etCapacity = findViewById(R.id.capacity_Edit_Scenarios);
        String strCapacity = etCapacity.getText().toString();
        String capacity_Error = getString(R.string.capacity_Post_Empty);

        EditText etLocation = findViewById(R.id.location_Edit_Scenario);
        String strLocation = etLocation.getText().toString();
        String location_Error = getString(R.string.location_Post_Empty);

        EditText etPrice = findViewById(R.id.price_Edit_Scenario);
        String strPrice = etPrice.getText().toString();
        String price_Error = getString(R.string.price_Post_Empty);

        EditText etDescription = findViewById(R.id.description_Edit_Scenario);
        String strDescription = etDescription.getText().toString();
        String description_Error = getString(R.string.description_Post_Empty);


        if(TextUtils.isEmpty(strTitle)) {
            etTitle.setError(title_Error);
            return;
        }
        else if (TextUtils.isEmpty(strCapacity)){
            etCapacity.setError(capacity_Error);
            return;
        }
        else if (TextUtils.isEmpty(strPrice)){
            etPrice.setError(price_Error);
            return;
        }
        else if (TextUtils.isEmpty(strLocation)){
            etLocation.setError(location_Error);
            return;
        }
        else if (TextUtils.isEmpty(strDescription)){
            etDescription.setError(description_Error);
            return;
        }else{
            final String titleScenario = strTitle;
            final String location = strLocation;
            final String description = strDescription;
            final Integer capacityScenario = Integer.parseInt(strCapacity);
            final Float priceScenario = Float.parseFloat(strPrice);
            final Integer idCompany = Integer.parseInt(idUser);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date today = Calendar.getInstance().getTime();
            String reportDate = df.format(today);
            final String dateScenario = reportDate;
            final Integer id_sport = idSport;

            new Thread(new Runnable() {
                public void run() {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPut httpPut = new HttpPut(getResources().getString(R.string.urlDB) + "/scenario");

                    JSONObject postData = new JSONObject();
                    try {
                        postData.put("idScenario", idScenario);
                        postData.put("idSport", id_sport);
                        postData.put("price", priceScenario);
                        postData.put("capacity", capacityScenario);
                        postData.put("title", titleScenario);
                        postData.put("address", location);
                        postData.put("description", description);
                        postData.put("idCompany", idCompany);
                        postData.put("dateScenario", dateScenario);

                        StringEntity se = new StringEntity(postData.toString(), "UTF-8");
                        httpPut.setHeader("Accept", "application/json");
                        httpPut.setHeader("Content-Type", "application/json; charset=utf-8");
                        httpPut.setEntity(se);
                        HttpResponse response = httpclient.execute(httpPut);
                        if(response.getStatusLine().getStatusCode() == 200) {
                            Intent intent1 = new Intent(activity_edit_scenario.this, UserCompanyLoginActivity.class);
                            startActivity(intent1);
                        }else{
                            activity_edit_scenario.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(activity_edit_scenario.this, "Something was wrong! Try to repeat", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } catch (IOException e) {
                        activity_edit_scenario.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(activity_edit_scenario.this, "Something was wrong! Try to repeat", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }


    }

    public void delete_scenario(View view){
        new Thread(new Runnable() {
            public void run() {
                HttpClient httpclient = new DefaultHttpClient();
                HttpDelete httpDelete = new HttpDelete(getResources().getString(R.string.urlDB) + "/scenario/" + idScenario);


                httpDelete.setHeader("Accept", "application/json");
                httpDelete.setHeader("Content-type", "application/json");
                HttpResponse response = null;
                try {
                    response = httpclient.execute(httpDelete);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response.getStatusLine().getStatusCode() == 200) {
                    activity_edit_scenario.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity_edit_scenario.this,"Scenario deleted successfully", Toast.LENGTH_LONG).show();
                        }
                    });
                    Intent intent1 = new Intent(activity_edit_scenario.this, UserCompanyLoginActivity.class);
                    startActivity(intent1);
                }else{
                    activity_edit_scenario.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity_edit_scenario.this,"Something was wrong! Try to repeat!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    public void openGallery(View view) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    public void getBack(View view) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
        imageUri= data.getData();
        changePicture.setImageURI(imageUri);

    }

    }
}
