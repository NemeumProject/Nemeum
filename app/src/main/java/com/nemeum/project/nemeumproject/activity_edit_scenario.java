package com.nemeum.project.nemeumproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import models.Scenario;

public class activity_edit_scenario extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageButton changePicture;
    private static final int PICK_IMAGE=100;
    Uri imageUri;
    String idUser;
    String idScenario;
    List<Scenario> listScenario = new ArrayList<>();
    SharedPreferences registeredUserPref = null;
    SharedPreferences shared = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_scenario);
        registeredUserPref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.userTypeSP), getApplicationContext().MODE_PRIVATE);
        shared = getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);
        idUser = (shared.getString("idUser", ""));

        getScenarios();
        List<String> listTitle = new ArrayList<>();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(Scenario scenario : listScenario){
            listTitle.add(scenario.getTitle());
        }

        Spinner spinner = findViewById(R.id.scenarios_Spinner_EditScenario);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listTitle);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Object item = parentView.getItemAtPosition(position);
                Scenario scenarioSelected = null;
                for(Scenario scenario : listScenario){
                    if(item.toString().equals(scenario.getTitle())){
                        idScenario = scenario.getIdScenario().toString();
                        scenarioSelected = scenario;
                    }
                }
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        changePicture = findViewById(R.id.companyLogoImg_EditScenario);

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
            final String dateScenario = "2018-12-11 23:00:00";
            final Integer idSport = 1;

            new Thread(new Runnable() {
                public void run() {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPut httpPut = new HttpPut(getResources().getString(R.string.urlDB) + "/scenario");

                    JSONObject postData = new JSONObject();
                    try {
                        postData.put("idScenario", idScenario);
                        postData.put("idSport", idSport);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
        imageUri= data.getData();
        changePicture.setImageURI(imageUri);

    }

    }
}
