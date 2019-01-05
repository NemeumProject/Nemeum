package com.nemeum.project.nemeumproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import models.Sport;
import models.TrainerService;

public class activity_edit_trainer_service extends AppCompatActivity {

    Context appContext;
    private EditText training_address;
    private EditText training_price;
    private EditText training_desc;
    private Spinner trainingService;
    String idUser;
    Integer idService;
    Integer idSport;
    private List<TrainerService> listTrainerService = new ArrayList<>();
    private List<Sport> listSport = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trainer_service);

        appContext = getApplicationContext();
        SharedPreferences shared = getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);
        idUser = (shared.getString("idUser", ""));

        getServices();
        getAllSports();
        List<String> listTitle = new ArrayList<>();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(TrainerService service : listTrainerService){
            listTitle.add(service.getTraining_desc());
        }
        List<String> sportName = new ArrayList<>();
        for(Sport sport : listSport){
            sportName.add(sport.getName());
        }
        trainingService = findViewById(R.id.training_service);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_layout_special, listTitle);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        trainingService.setAdapter(adapter);
        training_address = findViewById(R.id.location_Edit_Training); //Edit Text for edit training adress
        training_price = findViewById(R.id.training_price_edit); //Edit training price
        training_desc = findViewById(R.id.description_Edit_Training); //Edit training desc

        final Spinner SportTrainingCategory = findViewById(R.id.sport_type); //Spinner for sport type
        final ArrayAdapter<String> SportCategoryAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_layout, sportName);
        SportCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SportTrainingCategory.setAdapter(SportCategoryAdapter);

        final Spinner SportTrainingCity = findViewById(R.id.trainer_city); //Spinner for city list
        final ArrayAdapter<CharSequence> SportCityAdapter = ArrayAdapter.createFromResource(this, R.array.cityFilter, R.layout.spinner_layout);
        SportCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SportTrainingCity.setAdapter(SportCityAdapter);

        trainingService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Object item = parentView.getItemAtPosition(position);
                TrainerService trainingSelected = null;
                for(TrainerService service : listTrainerService){
                    if(item.toString().equals(service.getTraining_desc())){
                        idService = service.getId_training_service_post();
                        trainingSelected = service;
                    }
                }
                training_address.setText(trainingSelected.getTraining_address());
                training_price.setText(trainingSelected.getTraining_price().toString());
                training_desc.setText(trainingSelected.getTraining_desc());
                int j = 0;
                for(int i = 0; i < SportCityAdapter.getCount(); i++){
                    String city = (String) SportCityAdapter.getItem(i);
                    if(city.equals(trainingSelected.getTraining_city())){
                        j = i;
                        break;
                    }
                }
                SportTrainingCity.setSelection(j);
                int b = 0;
                for(int h = 0; h<listSport.size(); h++){
                    if(listSport.get(h).getIdSport().equals(trainingSelected.getId_sport_training_type())){
                        b = h;
                    }
                }
                SportTrainingCategory.setSelection(b);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        SportTrainingCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                for(Sport sport : listSport){
                    if(item.toString().equals(sport.getName())){
                        idSport = sport.getIdSport();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Button submit_training_service = findViewById(R.id.btn_finish_posting_EditTrainer); //Submit Button
        final Button delete_training_service = findViewById(R.id.delete_posting_EditTrainer); //Delete Button

        submit_training_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitTrainerService(idSport, training_address.getText().toString(), training_price.getText().toString(), SportTrainingCity.getSelectedItem().toString(), training_desc.getText().toString()); //A void to submit the posting after edit
            }
        });

        delete_training_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTrainerService(); //A void to delete a posting
            }
        });


        BottomNavigationView menu = findViewById(R.id.navigation);
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.homeButton:
                        Intent intentMain = new Intent(appContext, ActivityMainMock.class);
                        appContext.startActivity(intentMain);
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
                        Intent intentAccount = new Intent(getApplicationContext(), TrainerDetail.class);
                        getApplicationContext().startActivity(intentAccount);
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
    private synchronized void getServices(){
        new Thread(new Runnable() {
            public void run() {
                String data = null;
                int numResults = 0;
                String line;
                JSONArray parserList;
                JSONObject parser;
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(getResources().getString(R.string.urlDB) + "/trainersport/" + idUser);

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
                            TrainerService trainerService = new TrainerService();
                            trainerService.setId_training_service_post(parser.getInt("id_training_service_post"));
                            trainerService.setId_sport_training_type(parser.getInt("id_sport_training_type"));
                            trainerService.setId_trainer_user(parser.getInt("id_trainer_user"));
                            trainerService.setTraining_address(parser.getString("training_address"));
                            trainerService.setTraining_city(parser.getString("training_city"));
                            trainerService.setTraining_desc(parser.getString("training_desc"));
                            trainerService.setTraining_price(parser.getDouble("training_price"));
                            String startTime = parser.getString("training_start");
                            trainerService.setTraining_start(Time.valueOf(startTime));
                            String endTime = parser.getString("training_end");
                            trainerService.setTraining_end(Time.valueOf(endTime));
                            numResults++;
                            listTrainerService.add(trainerService);

                        }
                    }

                } catch (IOException e) {
                    activity_edit_trainer_service.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity_edit_trainer_service.this, "Something was wrong! Try to repeat", Toast.LENGTH_LONG).show();
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

    public void submitTrainerService(Integer sportID, String sportaddress, String sportprice, String sportcity, String sportdesc )
    {
        final Integer SportTypeID = sportID;
        final String SportAddress = sportaddress;
        final Double SportPrice = Double.parseDouble(sportprice);
        final String SportCity = sportcity;
        final String SportDesc = sportdesc;

        new Thread(new Runnable() {
            public void run() {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPut httpput = new HttpPut(getResources().getString(R.string.urlDB) + "/trainersport");
                JSONObject postData = new JSONObject();
                try {

                    postData.put("id_training_service_post", idService);
                    postData.put(getResources().getString(R.string.SportTypeTraining),SportTypeID);
                    postData.put(getResources().getString(R.string.SportAddressTraining),SportAddress);
                    postData.put(getResources().getString(R.string.SportPriceTraining),SportPrice);
                    postData.put(getResources().getString(R.string.SportCityTraining),SportCity);
                    postData.put(getResources().getString(R.string.SportDescTraining),SportDesc);
                    StringEntity se = new StringEntity(postData.toString(), "UTF-8");
                    httpput.setHeader("Accept", "application/json");
                    httpput.setHeader("Content-Type", "application/json; charset=utf-8");
                    httpput.setEntity(se);
                    HttpResponse response = httpclient.execute(httpput);
                    if(response.getStatusLine().getStatusCode() == 200){
                        activity_edit_trainer_service.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(activity_edit_trainer_service.this, getResources().getString(R.string.SportServiceSuccess), Toast.LENGTH_LONG).show();
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent intent2 = new Intent(activity_edit_trainer_service.this, UserTrainerLoginActivity.class);
                        startActivity(intent2);
                    }
                }
                catch (IOException e){
                    activity_edit_trainer_service.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity_edit_trainer_service.this, getResources().getString(R.string.noconnection), Toast.LENGTH_LONG).show();
                        }
                    });
                    e.printStackTrace();
                }
                catch (JSONException e){
                    activity_edit_trainer_service.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity_edit_trainer_service.this, getResources().getString(R.string.noconnection), Toast.LENGTH_LONG).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        }
        ).start();
    }

    public void deleteTrainerService()
    {
        new Thread(new Runnable() {
            public void run() {
                HttpClient httpclient = new DefaultHttpClient();
                HttpDelete httpDelete = new HttpDelete(getResources().getString(R.string.urlDB) + "/trainersport/" + idService);


                httpDelete.setHeader("Accept", "application/json");
                httpDelete.setHeader("Content-type", "application/json");
                HttpResponse response = null;
                try {
                    response = httpclient.execute(httpDelete);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response.getStatusLine().getStatusCode() == 200) {
                    activity_edit_trainer_service.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity_edit_trainer_service.this,"Service deleted successfully", Toast.LENGTH_LONG).show();
                        }
                    });
                    Intent intent1 = new Intent(activity_edit_trainer_service.this, UserTrainerLoginActivity.class);
                    startActivity(intent1);
                }else{
                    activity_edit_trainer_service.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity_edit_trainer_service.this,"Something was wrong! Try to repeat!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    public void getBack(View view) {
        finish();
    }
}
