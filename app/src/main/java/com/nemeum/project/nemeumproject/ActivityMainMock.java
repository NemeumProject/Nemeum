package com.nemeum.project.nemeumproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.content.ContextCompat;
import android.Manifest;

import com.nemeum.project.nemeumproject.Entities.SQLiteConnectionHelper;
import com.nemeum.project.nemeumproject.Utilities.Utilities;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Arrays;

import models.Scenario;

public class ActivityMainMock extends AppCompatActivity {
    List<Scenario> listScenario = new ArrayList<>();
    Context appContext;
    //Creating a connection for local storage
    SQLiteConnectionHelper localconn= new SQLiteConnectionHelper(this, "bd_scenarios", null,1);

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mock);

        appContext = getApplicationContext();

       /* TextView testing = findViewById(R.id.tV_test);
        testing.setText("testing");*/

        getAllScenarios();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fill_Localdb_escenarios();
        checkPermissions();




        if(!LocaleManager.getLanguage(appContext).equals(Locale.getDefault().getLanguage())) {
            if(LocaleManager.getLanguage(appContext) == null)
                LocaleManager.setNewLocale(appContext, Locale.getDefault().getLanguage());
            else
                LocaleManager.setLocale(appContext);
        }

        SharedPreferences SP = appContext.getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);

        if(SP.getString(getResources().getString(R.string.userTypeSP), "").equals(getResources().getString(R.string.individualUserSP)))
        {
            Intent intent1 = new Intent(appContext, UserLoginActivity.class);
            startActivity(intent1);
            finish();
        }
        else if(SP.getString(getResources().getString(R.string.userTypeSP), "").equals(getResources().getString(R.string.trainerUserSP)))
        {
            Intent intent2 = new Intent(appContext, UserTrainerLoginActivity.class);
            startActivity(intent2);
            finish();
        }
        else if (SP.getString(getResources().getString(R.string.userTypeSP), "").equals(getResources().getString(R.string.companyUserSP)))
        {
            Intent intent3 = new Intent(appContext, UserCompanyLoginActivity.class);
            startActivity(intent3);
            finish();
        }

        final ProgressBar progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        ImageButton findevent_btn = findViewById(R.id.findeventicon);
        findevent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                findEventOnclick();
            }
        });

        ImageButton findfacilities_btn = findViewById(R.id.findfacilitiesicon);
        findfacilities_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                findFacilitiesOnclick();
            }
        });

        ImageButton findscenario_btn = findViewById(R.id.findscrenarioicon);
        findscenario_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                findScenarioOnclick();
            }
        });

        ImageButton findtrainer_btn = findViewById(R.id.findtrainericon);
        findtrainer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                findTrainerOnclick();
            }
        });

        BottomNavigationView menu = findViewById(R.id.navigation);
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.homeButton:
                        Toast toast = Toast.makeText(appContext, R.string.alreadyOnHomeErr, Toast.LENGTH_LONG);
                        toast.show();
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

    @Override
    public  void  onResume(){
        super.onResume();
        ProgressBar progressBar = findViewById(R.id.progressbar);
        if(progressBar.getVisibility()==View.VISIBLE)
        {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void findFacilitiesOnclick() {
        Intent intent5 = new Intent(this,FacilityFinder.class);
        startActivity(intent5);
    }

    public void findScenarioOnclick() {
        Intent intent6 = new Intent(this,NearScenarios.class);
        startActivity(intent6);
    }

    public void findTrainerOnclick() {
        Intent intent7 = new Intent(this,SearchTrainer.class);
        startActivity(intent7);
    }
    public void findEventOnclick() {
        Intent intent8 = new Intent(this, EventFinder.class);
        startActivity(intent8);
    }

    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(this, getResources().getString(R.string.requirePermissionErr) + permissions[index]
                                + getResources().getString(R.string.permissionNotGrantedErr), Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                break;
        }
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

                    URI website = new URI(getResources().getString(R.string.urlDB) + getResources().getString(R.string.scenariosDB) + getResources().getString(R.string.listDB));
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


    private void fill_Localdb_escenarios(){
        Long idResult;
        SQLiteConnectionHelper localconn= new SQLiteConnectionHelper(this, "bd_scenarios", null,1);
        SQLiteDatabase db = localconn.getWritableDatabase();
       //Delete old records and after this, fill-in with the new data.
        db.execSQL("DELETE FROM " + Utilities.SCENARIO_TABLE+ " ");
        ContentValues values = new ContentValues();
        DateFormat df = new SimpleDateFormat(getString(R.string.pattern_date_format));

for(Scenario scenario : listScenario){

    values.put(Utilities.field_IDscenario, scenario.getIdScenario());
    values.put(Utilities.field_SportID,scenario.getIdSport());
    values.put(Utilities.field_Price,scenario.getPrice());
    values.put(Utilities.field_Isindoor,scenario.getIndoor());
    values.put(Utilities.field_Capacity,scenario.getCapacity());
    values.put(Utilities.field_CompanyID,scenario.getIdCompany());
    values.put(Utilities.field_DateScenario,df.format(scenario.getDateScenario()));
    values.put(Utilities.field_Description, scenario.getDescription());
    values.put(Utilities.field_Title,scenario.getTitle());
    values.put(Utilities.field_Image,scenario.getImage());
    values.put(Utilities.field_address,scenario.getAddress());

     idResult=db.insert(Utilities.SCENARIO_TABLE, Utilities.field_IDscenario, values);

    System.out.println("Description  "+ scenario.getDescription());
    System.out.println("Id Record: "+ idResult);
}




        //String numberasString = new Long (idResult).toString();

        //Toast.makeText(getApplicationContext(),"Id Record:"+idResult, Toast.LENGTH_SHORT).show();


        db.close();
    }
}
