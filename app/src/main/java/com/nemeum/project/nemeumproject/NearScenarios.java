package com.nemeum.project.nemeumproject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import url.UrlServer;

public class NearScenarios extends AppCompatActivity implements OnMapReadyCallback {

    int[] scenarioPicture = {R.drawable.swimming_silhouette, R.drawable.stadium, R.drawable.decathlon_logo};
    String[] scenarioName = {"Lleida Gym Center", "Soccer center of Lleida", "Boxing ring of Lleida"};
    String[] scenarioSubtitle = {"Scenario 1", "Scenario 2", "Scenario 3"};

    ArrayList<Integer> scenarioIdArray = new ArrayList();
    ArrayList<Integer> scenarioSportIdArray = new ArrayList();
    ArrayList<Double> scenarioPriceArray = new ArrayList();
    ArrayList<Integer> scenarioCompanyIdArray = new ArrayList();
    ArrayList<String> scenarioDescriptionArray = new ArrayList();

    Context appContext;

    MapView nearMap;
    GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_scenarios);

        appContext = getApplicationContext();

        getAllScenarios();

        nearMap = findViewById(R.id.mapView);
        nearMap.onCreate(savedInstanceState);
        if (nearMap != null) {
            nearMap.getMapAsync(this);
        }
        if(!playServicesAvailable())
            nearMap.setVisibility(View.GONE);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ListView resultList = findViewById(R.id.scenariosList);
        CustomAdapter customResult = new CustomAdapter();
        resultList.setAdapter(customResult);

        Spinner sportSpinner = findViewById(R.id.scenarioBySport);
        ArrayAdapter<CharSequence> sportAdapter = ArrayAdapter.createFromResource(this, R.array.sportFilter, android.R.layout.simple_spinner_dropdown_item);
        sportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportSpinner.setAdapter(sportAdapter);

        Spinner locationSpinner = findViewById(R.id.scenarioByCity);
        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(this, R.array.cityFilter, android.R.layout.simple_spinner_dropdown_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

        Spinner priceSpinner = findViewById(R.id.scenarioByPrice);
        ArrayAdapter<CharSequence> priceAdapter = ArrayAdapter.createFromResource(this, R.array.priceFilter, android.R.layout.simple_spinner_dropdown_item);
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
                        Intent intentAccount = new Intent(getApplicationContext(), TrainerDetail.class);
                        getApplicationContext().startActivity(intentAccount);
                        return true;
                    default:
                        return false;
                }
            }
        });

    }

    public synchronized void getAllScenarios() {
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

                    URI website = new URI(UrlServer.url + "/scenario/list");
                    request.setURI(website);
                    HttpResponse response = httpclient.execute(request);
                    in = new BufferedReader(new InputStreamReader(
                            response.getEntity().getContent()));

                    while((line = in.readLine()) != null)
                        data += line;

                    data = data.replaceFirst("null", "");
                    parserList = new JSONArray(data);

                    while((parser = (JSONObject) parserList.get(numResults)) != null) {

                        scenarioIdArray.add(parser.getInt("idScenario"));
                        scenarioSportIdArray.add(parser.getInt("idSport"));
                        scenarioPriceArray.add(parser.getDouble("price"));
                        scenarioCompanyIdArray.add(parser.getInt("idCompany"));
                        scenarioDescriptionArray.add(parser.getString("description"));
                        numResults++;

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

            Toast.makeText(appContext, "ERROR PERMISSION", Toast.LENGTH_LONG ).show();
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
            return scenarioIdArray.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return scenarioIdArray.get(position);
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

            scenarioImg.setImageResource(scenarioPicture[position]);

            if(!scenarioPriceArray.get(position).toString().equals("null"))
                scenarioValue.setText(scenarioPriceArray.get(position).toString() + "€ / hour");

            scenarioTitle.setText(scenarioName[position]);
            scenarioTitleDescr.setText(scenarioSubtitle[position]);

            if(!scenarioDescriptionArray.get(position).equals("null"))
            scenarioDescription.setText(scenarioDescriptionArray.get(position));

            bookBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentBook = new Intent(appContext, BookScenarios.class);
                    intentBook.putExtra("chooseImg", scenarioPicture[position]);
                    intentBook.putExtra("chooseRating", 2);
                    intentBook.putExtra("chooseName", scenarioName[position]);

                    if(!scenarioDescriptionArray.get(position).equals("null"))
                        intentBook.putExtra("chooseDescr", scenarioDescriptionArray.get(position));
                    else
                        intentBook.putExtra("chooseDescr", "");

                    appContext.startActivity(intentBook);
                }
            });
            return convertView;
        }
    }
}