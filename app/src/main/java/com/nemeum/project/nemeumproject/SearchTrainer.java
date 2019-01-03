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
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.codec.binary.StringUtils;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import models.Sport;
import models.TrainerService;

public class SearchTrainer extends AppCompatActivity {

    int[] scenarioPicture = {R.drawable.scenario_nophoto};
    int[] trainerPoints = {1, 2, 3, 4 ,5 ,6 ,7 ,8 ,9, 10 ,11};

    Context appContext;

    List<TrainerService> listTrainerService = new ArrayList<>();
    List<Sport> listSport = new ArrayList<>();
    List<String> nameSport = new ArrayList<>();
    List<String> namesOfTrainer = new ArrayList<>();

    private ListView resultList;
    private String city;
    private Integer idSport;
    private Double price;
    String idUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_trainer);

        appContext = getApplicationContext();
        SharedPreferences shared = getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);
        idUser = (shared.getString("idUser", ""));

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
                        Intent intentAccount = new Intent(appContext, TrainerDetail.class);
                        appContext.startActivity(intentAccount);
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
                if(item.toString().equals("Sport Type") || item.toString().equals("Tipo de deporte")){
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

    public synchronized void filterServices(View view){
        String name = null;
        final EditText nameTrainer = findViewById(R.id.trainerNameSrch);
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
            numResults++;
            namesOfTrainer.add(name);

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

            trainerImg.setImageResource(R.drawable.bicycle_rider);
            trainerImg.setImageResource(scenarioPicture[0]);
            trainerName.setText(namesOfTrainer.get(position));
            trainerSport.setText(nameSport.get(position));
            trainerAddress.setText(listTrainerService.get(position).getTraining_address());
            trainerDescription.setText(listTrainerService.get(position).getTraining_desc());

            bookBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentBookTrainer = new Intent(appContext, BookTrainer.class);
                    intentBookTrainer.putExtra(getResources().getString(R.string.trainerid),idUser);
                    intentBookTrainer.putExtra(getResources().getString(R.string.trainerserviceid),Integer.toString(listTrainerService.get(position).getId_training_service_post()));
                    intentBookTrainer.putExtra(getResources().getString(R.string.trainerNameExtra),namesOfTrainer.get(position));
                    intentBookTrainer.putExtra(getResources().getString(R.string.trainersporttype),nameSport.get(position));
                    intentBookTrainer.putExtra(getResources().getString(R.string.trainerPriceExtra),Double.toString(listTrainerService.get(position).getTraining_price()));
                    intentBookTrainer.putExtra(getResources().getString(R.string.trainerDescrExtra),listTrainerService.get(position).getTraining_desc());
                    appContext.startActivity(intentBookTrainer);

                }
            });

            return convertView;
        }
    }
}
