package com.nemeum.project.nemeumproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class PostScenario extends AppCompatActivity {
    private EditText ScenarioTitle;
    private EditText ScenarioLocation;
    private EditText ScenarioDesc;
    private EditText ScenarioCapacity;
    private EditText ScenarioPrice;
    String idUser;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_scenario);
        Button submit_scenario_btn = (Button)findViewById(R.id.submit_scenario_post);
        ScenarioTitle = (EditText)findViewById(R.id.edit_scenario_title);
        ScenarioLocation = (EditText) findViewById(R.id.scenario_location);
        ScenarioDesc = (EditText)findViewById(R.id.edit_scenario_desc);
        ScenarioCapacity = (EditText)findViewById(R.id.scenario_capacity);
        ScenarioPrice = (EditText)findViewById(R.id.scenario_price);
        SharedPreferences shared = getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);
        idUser = (shared.getString("idUser", ""));

        submit_scenario_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitNewScenario(ScenarioTitle.getText().toString(),ScenarioLocation.getText().toString(),ScenarioDesc.getText().toString(),ScenarioCapacity.getText().toString(),ScenarioPrice.getText().toString(), idUser);
            }
        });
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
            final String dateScenario = "2018-12-11 23:00:00";
            final Integer idSport = 1;

            new Thread(new Runnable() {
                public void run() {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(getResources().getString(R.string.urlDB) + "/scenario");

                    JSONObject postData = new JSONObject();
                    try {
                        postData.put("idSport", idSport);
                        postData.put("price", priceScenario);
                        postData.put("capacity", capacityScenario);
                        postData.put("title", titleScenario);
                        postData.put("address", location);
                        postData.put("description", description);
                        postData.put("idCompany", idCompany);
                        postData.put("dateScenario", dateScenario);

                        StringEntity se = new StringEntity(postData.toString());
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        httppost.setEntity(se);
                        HttpResponse response = httpclient.execute(httppost);
                        if(response.getStatusLine().getStatusCode() == 200) {
                            Intent intent1 = new Intent(PostScenario.this, NearScenarios.class);
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
