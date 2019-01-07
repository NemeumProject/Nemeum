package com.nemeum.project.nemeumproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import models.Sport;

public class PostScenario extends AppCompatActivity {
    private Context appContext;
    private EditText ScenarioTitle;
    private EditText ScenarioLocation;
    private EditText ScenarioDesc;
    private EditText ScenarioCapacity;
    private EditText ScenarioPrice;
    private Spinner sportSpinner;
    private Spinner citySpinner;
    private Uri imagePath;
    private ImageView eventImage;
    private StorageReference imageStorageReference;
    private FirebaseStorage imageStorage;
    private BottomNavigationView menu;
    String idUser;
    Integer idSport;
    String cityScenario;
    private String userType;
    private SharedPreferences SP;
    private List<Sport> listSport = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_scenario);
        appContext = getApplicationContext();
        SP = appContext.getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);
        checkRegisteredUser();
        getAllSports();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<String> sportName = new ArrayList<>();

        for(Sport sport : listSport){
            sportName.add(sport.getName());
        }
        sportSpinner = findViewById(R.id.sportScenario);
        ArrayAdapter<String> sportAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_layout, sportName);
        sportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportSpinner.setAdapter(sportAdapter);

        citySpinner = findViewById(R.id.cityScenario);
        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(this, R.array.cityFilter, R.layout.spinner_layout);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(locationAdapter);

        Button submit_scenario_btn = (Button)findViewById(R.id.submit_scenario_post);
        ScenarioTitle = (EditText)findViewById(R.id.edit_scenario_title);
        ScenarioLocation = (EditText) findViewById(R.id.scenario_location);
        ScenarioDesc = (EditText)findViewById(R.id.edit_scenario_desc);
        ScenarioCapacity = (EditText)findViewById(R.id.scenario_capacity);
        ScenarioPrice = (EditText)findViewById(R.id.scenario_price);
        eventImage = findViewById(R.id.eventImg);

        SharedPreferences shared = getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);
        idUser = (shared.getString("idUser", ""));

        imageStorage = FirebaseStorage.getInstance();
        imageStorageReference = imageStorage.getReference();

        eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
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

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Object item = parentView.getItemAtPosition(position);
                if(item.toString().equals("City") || item.toString().equals("Ciudad")){
                    cityScenario = null;
                }else{
                    cityScenario = item.toString();
                }
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

        submit_scenario_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitNewScenario(ScenarioTitle.getText().toString(),ScenarioLocation.getText().toString(),ScenarioDesc.getText().toString(),ScenarioCapacity.getText().toString(),ScenarioPrice.getText().toString(), idUser);
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

    private void chooseImage(){
        Intent chooseImg = new Intent();
        chooseImg.setType("image/*");
        chooseImg.setAction(Intent.ACTION_GET_CONTENT);
        chooseImg.createChooser(chooseImg, "Select Picture");
        startActivityForResult(chooseImg, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            try {
                imagePath = data.getData();
                Bitmap eventImg = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                eventImage.setImageBitmap(eventImg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    public void getBack(View view) {
        finish();
    }

    private void SubmitNewScenario(String title,String loc,String desc,String capacity,String price, String idUser){
        if(title.length()==0)
        {
            Toast.makeText(PostScenario.this, getResources().getString(R.string.scenario_post_title_empty), Toast.LENGTH_LONG).show();
        }
        else if(loc.length()==0)
        {
            Toast.makeText(PostScenario.this, getResources().getString(R.string.scenario_post_location_empty), Toast.LENGTH_LONG).show();
        }
        else if(desc.length()==0)
        {
            Toast.makeText(PostScenario.this, getResources().getString(R.string.scenario_post_description_empty), Toast.LENGTH_LONG).show();
        }
        else if (capacity.equals(0) || capacity.length()==0)
        {
            Toast.makeText(PostScenario.this, getResources().getString(R.string.scenario_post_capacity_empty), Toast.LENGTH_LONG).show();
        }
        else if (price.equals(0) || price.length()==0)
        {
            Toast.makeText(PostScenario.this, getResources().getString(R.string.scenario_post_price_empty), Toast.LENGTH_LONG).show();
        }else{
            final String titleScenario = title;
            final String location = loc;
            final String description = desc;
            final Integer capacityScenario = Integer.parseInt(capacity);
            final Float priceScenario = Float.parseFloat(price);
            final Integer idCompany = Integer.parseInt(idUser);
            final String city = cityScenario;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date today = Calendar.getInstance().getTime();
            String reportDate = df.format(today);

            final String dateScenario = reportDate;
            final Integer id_sport = idSport;
            String image = null;
            if(imagePath != null){
                final ProgressDialog progress = new ProgressDialog(this);
                progress.setTitle("Uploading...");
                progress.show();

                StorageReference reference = imageStorageReference.child("images/" + UUID.randomUUID().toString());
                reference.putFile(imagePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progress.dismiss();
                                Toast.makeText(appContext, "Uploaded", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progress.dismiss();
                                Toast.makeText(appContext, "Failed " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double percentage = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                progress.setMessage("Uploaded " + percentage + "%");
                            }
                        });
                image = "https://firebasestorage.googleapis.com/v0/b" + imageStorageReference.toString().substring(4) + "o/" + reference.getPath().substring(1).replace("/", "%2F") + "?alt=media";
            }
            final String finalImage = image;
            new Thread(new Runnable() {
                public void run() {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(getResources().getString(R.string.urlDB) + "/scenario");

                    JSONObject postData = new JSONObject();
                    try {
                        postData.put("idSport", id_sport);
                        postData.put("price", priceScenario);
                        postData.put("capacity", capacityScenario);
                        postData.put("title", titleScenario);
                        postData.put("address", location);
                        postData.put("description", description);
                        postData.put("idCompany", idCompany);
                        postData.put("dateScenario", dateScenario);
                        postData.put("image", finalImage);
                        postData.put("city", city);

                        StringEntity se = new StringEntity(postData.toString(), "UTF-8");
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-Type", "application/json; charset=utf-8");
                        httppost.setEntity(se);
                        HttpResponse response = httpclient.execute(httppost);
                        if(response.getStatusLine().getStatusCode() == 200) {
                            Intent intent1 = new Intent(PostScenario.this, UserCompanyLoginActivity.class);
                            startActivity(intent1);
                        }else{
                            PostScenario.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(PostScenario.this, "Something was wrong! Try to repeat", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } catch (IOException e) {
                        PostScenario.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(PostScenario.this, "Something was wrong! Try to repeat", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
