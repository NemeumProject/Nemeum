package com.nemeum.project.nemeumproject;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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

public class MyBookings extends AppCompatActivity {

    List<Scenario> listScenario = new ArrayList<>();
    Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        appContext = getApplicationContext();

        getMyBookings();

        ListView resultList = findViewById(R.id.bookingsList);
        CustomAdapter customResult = new CustomAdapter();
        resultList.setAdapter(customResult);

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

    public synchronized void getMyBookings() {
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

    public void getBack(View view) {
        finish();
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

            convertView = getLayoutInflater().inflate(R.layout.booking_result_layout, null);


            Button editBookBtn = convertView.findViewById(R.id.editBookResult);
            Button cancelBookBtn = convertView.findViewById(R.id.cancelBookResult);
            TextView bookTitle = convertView.findViewById(R.id.myBookResultTitleText);
            TextView bookHours = convertView.findViewById(R.id.myBookResultHoursText);
            TextView bookDate = convertView.findViewById(R.id.myBookResultDateText);
            TextView bookPlace = convertView.findViewById(R.id.myBookResultPlaceText);
            TextView bookValue = convertView.findViewById(R.id.myBookResultPriceText);

            bookTitle.setText(listScenario.get(position).getTitle());
            bookHours.setText(getResources().getString(R.string.bookingResTime) /*+ listScenario.get(position)*/);
            bookDate.setText(getResources().getString(R.string.bookingResDate) /*+ listScenario.get(position)*/);
            bookPlace.setText(getResources().getString(R.string.bookingResAddress) + " " + listScenario.get(position).getAddress());

            if(!listScenario.get(position).getPrice().toString().equals("null"))
                bookValue.setText(getResources().getString(R.string.bookingResPrice) + " " + listScenario.get(position).getPrice().toString() + "€ / hour");

            editBookBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentPayment = new Intent(appContext, EditBook.class);
                    intentPayment.putExtra(getResources().getString(R.string.scenarioNameExtra), listScenario.get(position).getTitle());
                    appContext.startActivity(intentPayment);
                }
            });

            cancelBookBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder cancelAlertBuilder = new AlertDialog.Builder(new ContextThemeWrapper(MyBookings.this, R.style.nemeumDialog));
                    cancelAlertBuilder.setCancelable(true);
                    cancelAlertBuilder.setMessage(R.string.bookingCancelSubtitle);
                    cancelAlertBuilder.setPositiveButton(R.string.buttonYes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intentBook = new Intent(appContext, BookScenarios.class);
                            intentBook.putExtra(getResources().getString(R.string.scenarioRatingExtra), 2);
                            intentBook.putExtra(getResources().getString(R.string.scenarioNameExtra), listScenario.get(position).getTitle());

                            if(!listScenario.get(position).getDescription().equals("null"))
                                intentBook.putExtra(getResources().getString(R.string.scenarioDescrExtra), listScenario.get(position).getDescription());
                            else
                                intentBook.putExtra(getResources().getString(R.string.scenarioDescrExtra), "");

                            appContext.startActivity(intentBook);

                        }
                    });
                    cancelAlertBuilder.setNegativeButton(R.string.buttonNo, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog cancelAlert = cancelAlertBuilder.create();
                    cancelAlert.setTitle(R.string.bookingCancelTitle);
                    cancelAlert.show();
                }
            });
            return convertView;
        }
    }
}
