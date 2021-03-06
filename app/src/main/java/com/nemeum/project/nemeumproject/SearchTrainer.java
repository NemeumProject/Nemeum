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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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

public class SearchTrainer extends AppCompatActivity {

    private Context appContext;

    private List<TrainerService> listTrainerService = new ArrayList<>();
    private List<Sport> listSport = new ArrayList<>();
    private List<String> nameSport = new ArrayList<>();
    private List<String> namesOfTrainer = new ArrayList<>();
    private List<String> listImages = new ArrayList<>();

    private ListView resultList;
    private String city;
    private Integer idSport;
    private Double price;
    private String idUser;
    private SharedPreferences SP;
    private BottomNavigationView menu;
    private String userType;
    private EditText nameTrainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_trainer);

        appContext = getApplicationContext();
        SP = appContext.getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);

        checkRegisteredUser();
        idUser = (SP.getString("idUser", ""));

        getAllSports();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getAllServices();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        nameTrainer = findViewById(R.id.trainerNameSrch);
        resultList = findViewById(R.id.trainersList);
        CustomAdapter customResult = new CustomAdapter();

        resultList.setAdapter(customResult);

        List<String> sportName = new ArrayList<>();
        sportName.add("Sport type");

        for(Sport sport : listSport){
            sportName.add(sport.getName());
        }

        Spinner sportSpinner = findViewById(R.id.trainerBySport);
        Spinner locationSpinner = findViewById(R.id.trainerByCity);
        Spinner priceSpinner = findViewById(R.id.trainerByPrice);

        ArrayAdapter<String> sportAdapter = new ArrayAdapter<String>(this,R.layout.spinner_layout, sportName);
        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(this, R.array.cityFilter, R.layout.spinner_layout);
        ArrayAdapter<CharSequence> priceAdapter = ArrayAdapter.createFromResource(this, R.array.priceFilter, R.layout.spinner_layout);

        sportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sportSpinner.setAdapter(sportAdapter);
        locationSpinner.setAdapter(locationAdapter);
        priceSpinner.setAdapter(priceAdapter);


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

        sportSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Object item = parentView.getItemAtPosition(position);
                if(item.toString().equals("Sport type") || item.toString().equals("Tipo de deporte")){
                    idSport = null;
                }else{
                    for(Sport sport : listSport){
                        if(item.toString().equals(sport.getName())){
                            idSport = sport.getIdSport();
                        }
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        priceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Object item = parentView.getItemAtPosition(position);
                if(item.toString().equals("Price range") || item.toString().equals("Gama de precios")){
                    price = null;
                }else{
                    String euros = item.toString().split(" ")[1];
                    String finalPrice = euros.split("€")[0];
                    price = Double.parseDouble(finalPrice);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Object item = parentView.getItemAtPosition(position);
                if(item.toString().equals("City") || item.toString().equals("Ciudad")){
                    city = null;
                }else{
                    city = item.toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
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

    public synchronized void filterServices(View view){
        String name = null;
        String inputUser = nameTrainer.getText().toString();
        if(!inputUser.equals("") && inputUser != null){
            name = nameTrainer.getText().toString();
        }
        final String trainerName = name;
        new Thread(new Runnable() {
            @Override
            public void run() {
                listTrainerService.clear();
                int numResults = 0;
                BufferedReader in;
                String data = null;
                String line;
                JSONArray parserList;
                JSONObject parser;

                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(getResources().getString(R.string.urlDB) + "/trainersport/search");

                JSONObject postData = new JSONObject();
                try {
                    postData.put("name", trainerName);
                    postData.put("price", price);
                    postData.put("city", city);
                    postData.put("sport_id", idSport);
                    String result = "";
                    StringEntity se = new StringEntity(postData.toString(), "UTF-8");
                    httppost.setHeader(getResources().getString(R.string.dbAccessAccept), getResources().getString(R.string.dbAccessAppJson));
                    httppost.setHeader(getResources().getString(R.string.dbAccessContentType), getResources().getString(R.string.dbAccessAppJson));
                    httppost.setEntity(se);
                    HttpResponse response = httpclient.execute(httppost);
                    in = new BufferedReader(new InputStreamReader(
                            response.getEntity().getContent()));

                    while((line = in.readLine()) != null)
                        data += line;

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
                    URI website = null;
                    HttpGet request = new HttpGet();
                    nameSport.clear();
                    namesOfTrainer.clear();
                    for(TrainerService trainerService : listTrainerService){
                        website = new URI(getResources().getString(R.string.urlDB) + "/sport/" + trainerService.getId_sport_training_type());
                        request.setURI(website);
                        response = httpclient.execute(request);
                        in = new BufferedReader(new InputStreamReader(
                                response.getEntity().getContent()));

                        getNameSport(in);
                        website = new URI(getResources().getString(R.string.urlDB) + "/traineruser/" + trainerService.getId_trainer_user());
                        request.setURI(website);
                        response = httpclient.execute(request);
                        in = new BufferedReader(new InputStreamReader(
                                response.getEntity().getContent()));
                        getTrainerName(in);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                Thread.yield();
            }
        }).start();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ((BaseAdapter) resultList.getAdapter()).notifyDataSetChanged();
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

    public void getAllServices(){
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

                    URI website = new URI(getResources().getString(R.string.urlDB) + "/trainersport");
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
                        TrainerService trainerService = new TrainerService();
                        trainerService.setId_training_service_post(parser.getInt("id_training_service_post"));
                        trainerService.setId_sport_training_type(parser.getInt("id_sport_training_type"));
                        trainerService.setId_trainer_user(parser.getInt("id_trainer_user"));
                        trainerService.setTraining_address(parser.getString("training_address"));
                        trainerService.setTraining_city(parser.getString("training_city"));
                        trainerService.setTraining_desc(parser.getString("training_desc"));
                        trainerService.setTraining_price(parser.getDouble("training_price"));
                        String startTime = parser.getString("training_start");
                        trainerService.setTraining_start(java.sql.Time.valueOf(startTime));
                        String endTime = parser.getString("training_end");
                        trainerService.setTraining_end(java.sql.Time.valueOf(endTime));
                        numResults++;
                        listTrainerService.add(trainerService);

                    }
                    for(TrainerService trainerService : listTrainerService){
                        website = new URI(getResources().getString(R.string.urlDB) + "/sport/" + trainerService.getId_sport_training_type());
                        request.setURI(website);
                        response = httpclient.execute(request);
                        in = new BufferedReader(new InputStreamReader(
                                response.getEntity().getContent()));

                        getNameSport(in);
                        website = new URI(getResources().getString(R.string.urlDB) + "/traineruser/" + trainerService.getId_trainer_user());
                        request.setURI(website);
                        response = httpclient.execute(request);
                        in = new BufferedReader(new InputStreamReader(
                                response.getEntity().getContent()));
                        getTrainerName(in);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                Thread.yield();
            }
        }).start();
    }

    private void getNameSport(BufferedReader in) throws IOException, JSONException {
        String line = null;
        JSONObject objectSport;
        JSONArray arraySport;
        String dataSport = null;
        int numResults = 0;
        while((line = in.readLine()) != null)
            dataSport += line;

        dataSport = dataSport.replaceFirst("null", "");
        arraySport = new JSONArray(dataSport);
        while(numResults < arraySport.length()) {
            objectSport = (JSONObject) arraySport.get(numResults);
            String sport = objectSport.getString("name");
            numResults++;
            nameSport.add(sport);

        }
    }

    private void getTrainerName(BufferedReader in) throws JSONException, IOException {
        String line = null;
        JSONObject objectTrainer;
        JSONArray arrayTrainer;
        String dataTrainer = null;
        int numResults = 0;
        while((line = in.readLine()) != null)
            dataTrainer += line;

        dataTrainer = dataTrainer.replaceFirst("null", "");
        arrayTrainer = new JSONArray(dataTrainer);

        numResults = 0;
        while(numResults < arrayTrainer.length()) {
            objectTrainer = (JSONObject) arrayTrainer.get(numResults);
            String name = objectTrainer.getString("firstName");
            String image = objectTrainer.getString("image");
            numResults++;
            namesOfTrainer.add(name);
            listImages.add(image);

        }
    }

    public void getBack(View view) {
        finish();
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listTrainerService.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.trainer_result_layout, null);

            ImageView trainerImg = convertView.findViewById(R.id.trainerResultImg);
            Button bookBtn = convertView.findViewById(R.id.bookTrainerResult);
            TextView trainerName = convertView.findViewById(R.id.trainerResultName);
            TextView trainerSport = convertView.findViewById(R.id.trainerResultSportText);
            TextView trainerAddress = convertView.findViewById(R.id.trainerResultPlaceText);
            TextView trainerDescription = convertView.findViewById(R.id.trainerResultDescriptionText);

            if(!listImages.get(position).equals("null"))
                Picasso.get().load(listImages.get(position)).into(trainerImg);
            else
                Picasso.get().load(R.drawable.scenario_nophoto).into(trainerImg);

            trainerName.setText(namesOfTrainer.get(position));
            trainerSport.setText(nameSport.get(position));
            trainerAddress.setText(listTrainerService.get(position).getTraining_address()+", "+listTrainerService.get(position).getTraining_city());
            trainerDescription.setText(listTrainerService.get(position).getTraining_desc());

            bookBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!(userType.equals(getResources().getString(R.string.individualUserSP)) ||
                            userType.equals(getResources().getString(R.string.trainerUserSP))))
                    {
                        if(userType.equals(getResources().getString(R.string.companyUserSP)))
                        {
                            SearchTrainer.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(SearchTrainer.this, "Company user can't use this feature", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else
                        {
                            SearchTrainer.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(SearchTrainer.this, "Please Login to Use This Feature", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                    else
                    {
                        Intent intentBookTrainer = new Intent(appContext, BookTrainer.class);
                        if(!listImages.get(position).equals("null")) {
                            intentBookTrainer.putExtra(getResources().getString(R.string.scenarioImgExtra), listImages.get(position));
                            intentBookTrainer.putExtra("withImage", "Yes");

                        }else {
                            intentBookTrainer.putExtra("withImage", "No");
                        }
                        intentBookTrainer.putExtra(getResources().getString(R.string.trainerid),idUser);
                        intentBookTrainer.putExtra(getResources().getString(R.string.trainerserviceid),Integer.toString(listTrainerService.get(position).getId_training_service_post()));
                        intentBookTrainer.putExtra(getResources().getString(R.string.trainerNameExtra),namesOfTrainer.get(position));
                        intentBookTrainer.putExtra(getResources().getString(R.string.trainersporttype),nameSport.get(position));
                        intentBookTrainer.putExtra(getResources().getString(R.string.trainerPriceExtra),Double.toString(listTrainerService.get(position).getTraining_price()));
                        intentBookTrainer.putExtra(getResources().getString(R.string.trainerDescrExtra),listTrainerService.get(position).getTraining_desc());
                        intentBookTrainer.putExtra(getResources().getString(R.string.trainerAddressExtra),listTrainerService.get(position).getTraining_address());
                        intentBookTrainer.putExtra(getResources().getString(R.string.trainerCityExtra),listTrainerService.get(position).getTraining_city());
                        appContext.startActivity(intentBookTrainer);
                    }
                }
            });

            return convertView;
        }
    }
}
