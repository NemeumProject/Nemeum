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

public class NearScenarios extends AppCompatActivity implements OnMapReadyCallback {

    int[] scenarioPicture = {R.drawable.stadium, R.drawable.stadium, R.drawable.stadium};
    String[] scenarioName = {"Lleida Gym Center", "Soccer center of Lleida", "Boxing ring of Lleida"};
    String[] scenarioPrice = {"39€ / month", "14€ / month", "60€ / month"};
    String[] scenarioSubtitle = {"Scenario 1", "Scenario 2", "Scenario 3"};
    Context appContext;

    MapView nearMap;
    GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_scenarios);

        appContext = getApplicationContext();

        nearMap = findViewById(R.id.mapView);
        nearMap.onCreate(savedInstanceState);

        if (nearMap != null) {
            nearMap.getMapAsync(this);
        }

        if(!playServicesAvailable())
            nearMap.setVisibility(View.GONE);

        ListView resultList = findViewById(R.id.scenariosList);

        CustomAdapter customResult = new CustomAdapter();

        resultList.setAdapter(customResult);

        Spinner sportSpinner = findViewById(R.id.scenarioBySport);
        Spinner locationSpinner = findViewById(R.id.scenarioByCity);
        Spinner priceSpinner = findViewById(R.id.scenarioByPrice);

        ArrayAdapter<CharSequence> sportAdapter = ArrayAdapter.createFromResource(this, R.array.sportFilter, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(this, R.array.cityFilter, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> priceAdapter = ArrayAdapter.createFromResource(this, R.array.priceFilter, android.R.layout.simple_spinner_dropdown_item);

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
                        Intent intentAccount = new Intent(getApplicationContext(), TrainerDetail.class);
                        getApplicationContext().startActivity(intentAccount);
                        return true;
                    default:
                        return false;
                }
            }
        });

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
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        MapsInitializer.initialize(appContext);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(latitude, longitude));
        LatLngBounds bounds = builder.build();
        int padding = 10;

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 1000, 1000, padding);
        gMap.moveCamera(cameraUpdate);
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 3;
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
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.scenario_result_layout, null);

            ImageView scenarioImg = convertView.findViewById(R.id.companyResImg);
            TextView scenarioValue = convertView.findViewById(R.id.companyScenarioPrice);
            Button bookBtn = convertView.findViewById(R.id.bookCompany);
            TextView scenarioTitle = convertView.findViewById(R.id.companyResTitle);
            TextView scenarioTitleDescr = convertView.findViewById(R.id.companyResScenarioText);
            TextView scenarioDescription = convertView.findViewById(R.id.companyResDescription);

            scenarioImg.setImageResource(scenarioPicture[position]);
            scenarioValue.setText(scenarioPrice[position]);
            scenarioTitle.setText(scenarioName[position]);
            scenarioTitleDescr.setText(scenarioSubtitle[position]);
            scenarioDescription.setText(R.string.scenarioFindDescr);

            return convertView;
        }
    }
}
