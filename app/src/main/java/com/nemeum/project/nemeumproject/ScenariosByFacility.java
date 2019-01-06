package com.nemeum.project.nemeumproject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import models.Scenario;
import models.Sport;

public class ScenariosByFacility extends AppCompatActivity implements OnMapReadyCallback {

    private List<Scenario> listScenario = new ArrayList<>();
    private List<Sport> listSport = new ArrayList<>();

    private Context appContext;

    private MapView nearMap;
    private GoogleMap gMap;

    private Spinner spinnerCity;
    private Spinner spinnerSport;
    private Spinner spinnerPrice;
    private ListView resultList;

    private String city;
    private Integer idSport;
    private Double price;
    private String idCompany;

    private SharedPreferences SP;
    private BottomNavigationView menu;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenarios_byfacility);

        appContext = getApplicationContext();
        SP = appContext.getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);

        checkRegisteredUser();
        idCompany = (SP.getString("idCompany", ""));

        getAllSports();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getAllScenarios(idCompany);


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        nearMap = findViewById(R.id.mapView);
        nearMap.onCreate(savedInstanceState);
        if (nearMap != null) {
            nearMap.getMapAsync(this);
        }
        if(!playServicesAvailable())
            nearMap.setVisibility(View.GONE);

        resultList = findViewById(R.id.scenariosList);
        CustomAdapter customResult = new CustomAdapter();
        resultList.setAdapter(customResult);

        List<String> sportName = new ArrayList<>();
        sportName.add("Sport type");

        for(Sport sport : listSport){
            sportName.add(sport.getName());
        }

        Spinner sportSpinner = findViewById(R.id.scenarioBySport);
        ArrayAdapter<String> sportAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_layout, sportName);
        sportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportSpinner.setAdapter(sportAdapter);

        Spinner locationSpinner = findViewById(R.id.scenarioByCity);
        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(this, R.array.cityFilter, R.layout.spinner_layout);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

        Spinner priceSpinner = findViewById(R.id.scenarioByPrice);
        ArrayAdapter<CharSequence> priceAdapter = ArrayAdapter.createFromResource(this, R.array.priceFilter, R.layout.spinner_layout);
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

        spinnerCity = findViewById(R.id.scenarioByCity);
        spinnerSport = findViewById(R.id.scenarioBySport);
        spinnerPrice = findViewById(R.id.scenarioByPrice);

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        spinnerSport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        spinnerPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    public synchronized void filterScenarios(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                listScenario.clear();
                int numResults = 0;
                BufferedReader in;
                String data = null;
                String line;
                JSONArray parserList;
                JSONObject parser;

                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(getResources().getString(R.string.urlDB) + "/scenario/search/facility");

                JSONObject postData = new JSONObject();
                try {
                    postData.put("idSport", idSport);
                    postData.put("price", price);
                    postData.put("city", city);
                    postData.put("idCompany", Integer.parseInt(idCompany));
                    String result = "";
                    StringEntity se = null;
                    se = new StringEntity(postData.toString());
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
                }catch(Exception e){
                    e.printStackTrace();
                }
                Thread.yield();
            }
        }).start();
        try {
            Thread.sleep(2000);
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

    public synchronized void getAllScenarios(final String idCompany) {
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

                    URI website = new URI(getResources().getString(R.string.urlDB) + getResources().getString(R.string.scenariosDB) + "/company/" + idCompany);
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

    private boolean playServicesAvailable(){
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(appContext);
        if(isAvailable == ConnectionResult.SUCCESS)
            return true;
        if(api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast toast = Toast.makeText(appContext, R.string.playServicesErr, Toast.LENGTH_LONG);
            toast.show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        double latitude, longitude;

        gMap = googleMap;

        //gMap.addMarker(new MarkerOptions()
        //        .icon(BitmapDescriptorFactory.defaultMarker())
        //        .anchor(0.0f, 1.0f)
        //        .position(new LatLng(41.616751, 0.626416)));

        if (ActivityCompat.checkSelfPermission(appContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(appContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(appContext, getResources().getString(R.string.permissionLocationErr), Toast.LENGTH_LONG ).show();
        }

        gMap.setMyLocationEnabled(true);
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.getUiSettings().setMyLocationButtonEnabled(false);
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setZoomGesturesEnabled(true);

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));
        if(location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        } else {
            latitude = 0;
            longitude = 0;
        }

        MapsInitializer.initialize(appContext);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(latitude, longitude));
        LatLngBounds bounds = builder.build();
        int padding = 0;

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 1, 1, padding);
        gMap.moveCamera(cameraUpdate);
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listScenario.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return listScenario.get(position).getIdScenario();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.scenario_result_layout, null);

            ImageView scenarioImg = convertView.findViewById(R.id.companyResImg);
            TextView scenarioValue = convertView.findViewById(R.id.companyScenarioPrice);
            Button bookBtn = convertView.findViewById(R.id.bookCompany);
            TextView scenarioTitle = convertView.findViewById(R.id.companyResTitle);
            TextView scenarioTitleDescr = convertView.findViewById(R.id.companyResScenarioText);
            TextView scenarioDescription = convertView.findViewById(R.id.companyResDescription);
            if (!listScenario.get(position).getImage().equals("null")) {
                Picasso.get().load(listScenario.get(position).getImage()).into(scenarioImg);
            } else{
                Picasso.get().load(R.drawable.scenario_nophoto).into(scenarioImg);
            }

            if(!listScenario.get(position).getPrice().toString().equals("null"))
                scenarioValue.setText(listScenario.get(position).getPrice().toString() + "€ / hour");

            scenarioTitle.setText(listScenario.get(position).getTitle());
            scenarioTitleDescr.setText("Scenario " + position);

            if(!listScenario.get(position).getDescription().equals("null"))
                scenarioDescription.setText(listScenario.get(position).getDescription());

            bookBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!(userType.equals(getResources().getString(R.string.individualUserSP)) ||
                            userType.equals(getResources().getString(R.string.trainerUserSP)) ||
                            userType.equals(getResources().getString(R.string.companyUserSP))))
                    {
                        ScenariosByFacility.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(ScenariosByFacility.this, "Please Login to Use This Feature", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else
                    {
                        Intent intentBook = new Intent(appContext, BookScenarios.class);
                        if(!listScenario.get(position).getImage().equals("null")) {
                            intentBook.putExtra(getResources().getString(R.string.scenarioImgExtra), listScenario.get(position).getImage());
                            intentBook.putExtra("withImage", "Yes");

                        }else {
                            intentBook.putExtra("withImage", "No");
                        }
                        intentBook.putExtra(getResources().getString(R.string.scenarioNameExtra), listScenario.get(position).getTitle());

                        if(!listScenario.get(position).getDescription().equals("null"))
                            intentBook.putExtra(getResources().getString(R.string.scenarioDescrExtra), listScenario.get(position).getDescription());
                        else
                            intentBook.putExtra(getResources().getString(R.string.scenarioDescrExtra), "");

                        appContext.startActivity(intentBook);
                    }

                }
            });
            return convertView;
        }
    }
}
