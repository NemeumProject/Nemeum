package com.nemeum.project.nemeumproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import models.Booking;
import models.Scenario;
import models.TrainerService;

public class MyBookings extends AppCompatActivity {

    private List<Scenario> listScenario;
    private List<String> listTrainerName;
    private List<TrainerService> listServices;
    private List<Booking> listBookings;
    private ListView resultList;
    private CustomAdapter customResult;
    private Context appContext;
    private SharedPreferences SP;
    private BottomNavigationView menu;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        listScenario = new ArrayList<>();
        listServices = new ArrayList<>();
        listBookings = new ArrayList<>();
        listTrainerName = new ArrayList<>();

        appContext = getApplicationContext();
        SP = appContext.getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);

        getMyBookings();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        checkRegisteredUser();

        resultList = findViewById(R.id.bookingsList);

        customResult = new CustomAdapter();
        resultList.setAdapter(customResult);

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

    public synchronized void getMyBookings() {
        getScenarioBookings();
        getServiceBookings();
    }

    private void getScenarioBookings() {
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

                    URI website = new URI(getResources().getString(R.string.urlDB) + getResources().getString(R.string.scenarioJoinDB) + "/" + SP.getString(getResources().getString(R.string.idUserSP), ""));
                    request.setURI(website);
                    HttpResponse response = httpclient.execute(request);
                    in = new BufferedReader(new InputStreamReader(
                            response.getEntity().getContent()));

                    while((line = in.readLine()) != null)
                        data += line;

                    data = data.replaceFirst("null", "");
                    parserList = new JSONArray(data);

                    while(numResults < parserList.length()) {
                        Booking booking = new Booking();

                        parser = (JSONObject) parserList.get(numResults);
                        booking.setIdBooking(parser.getInt(getResources().getString(R.string.userScenario_userscenarioJson)));
                        booking.setIdScenario(parser.getInt(getResources().getString(R.string.userScenario_idScenarioJson)));
                        String dateBookStart = parser.getString(getResources().getString(R.string.userScenario_startScenarioJson));
                        String dateBookEnd = parser.getString(getResources().getString(R.string.userScenario_endScenarioJson));
                        SimpleDateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.pattern_date_format));
                        booking.setDateSchedule(dateFormat.parse(dateBookStart));
                        booking.setTimeStartScheduled(dateFormat.parse(dateBookStart));
                        booking.setTimeEndScheduled(dateFormat.parse(dateBookEnd));

                            getScenario(booking.getIdScenario());

                            Thread.sleep(200);

                            booking.setAddress(listScenario.get(numResults).getAddress());
                            booking.setTitle(listScenario.get(numResults).getTitle());
                            int hourStart = booking.getTimeStartScheduled().getHours();
                            int hourEnd = booking.getTimeEndScheduled().getHours();

                            if(hourEnd < hourStart)
                                hourEnd += 12;

                            booking.setPrice(listScenario.get(numResults).getPrice() * (hourEnd - hourStart));
                            numResults++;
                            listBookings.add(booking);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getScenario(final int scenarioId) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                BufferedReader in;
                String data = null;
                String line;
                JSONObject parser;

                HttpClient httpclient = new DefaultHttpClient();
                HttpGet request = new HttpGet();

                try{

                    URI website = new URI(getResources().getString(R.string.urlDB) + getResources().getString(R.string.scenariosDB) + "/" + scenarioId);
                    request.setURI(website);
                    HttpResponse response = httpclient.execute(request);
                    in = new BufferedReader(new InputStreamReader(
                            response.getEntity().getContent()));

                    while((line = in.readLine()) != null)
                        data += line;

                    data = data.replaceFirst("null", "");
                    parser = new JSONObject(data);
                    Scenario scenario = new Scenario();

                    scenario.setIdScenario(parser.getInt(getResources().getString(R.string.scenarioIdJson)));
                    scenario.setIdSport(parser.getInt(getResources().getString(R.string.scenarioSportIdJson)));
                    scenario.setPrice(parser.getDouble(getResources().getString(R.string.scenarioPriceJson)));
                    scenario.setIdCompany(parser.getInt(getResources().getString(R.string.scenarioCompanyIdJson)));
                    scenario.setDescription(parser.getString(getResources().getString(R.string.scenarioDescriptionJson)));
                    scenario.setCapacity(parser.getInt(getResources().getString(R.string.scenarioCapacityJson)));
                    scenario.setAddress(parser.getString(getResources().getString(R.string.scenarioAddressJson)));
                    String dateStr = parser.getString(getResources().getString(R.string.scenarioDateJson));
                    SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.pattern_date_format));
                    scenario.setDateScenario(sdf.parse(dateStr));
                    scenario.setTitle(parser.getString(getResources().getString(R.string.scenarioTitleJson)));
                    scenario.setImage(parser.getString(getResources().getString(R.string.scenarioImageJson)));

                    if(!parser.isNull(getResources().getString(R.string.scenarioIndoorJson)))
                        scenario.setIndoor(parser.getBoolean(getResources().getString(R.string.scenarioIndoorJson)));

                    listScenario.add(scenario);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getServiceBookings() {
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

                    URI website = new URI(getResources().getString(R.string.urlDB) + getResources().getString(R.string.trainerJoinDB) + "/" + SP.getString(getResources().getString(R.string.idUserSP), ""));
                    request.setURI(website);
                    HttpResponse response = httpclient.execute(request);
                    in = new BufferedReader(new InputStreamReader(
                            response.getEntity().getContent()));

                    while((line = in.readLine()) != null)
                        data += line;

                    data = data.replaceFirst("null", "");
                    parserList = new JSONArray(data);

                    while(numResults < parserList.length()) {
                        Booking booking = new Booking();

                        parser = (JSONObject) parserList.get(numResults);
                        booking.setIdBooking(parser.getInt(getResources().getString(R.string.bookingTrainer_bookingTrainerIDJson)));
                        booking.setIdService(parser.getInt(getResources().getString(R.string.bookingTrainer_idServiceJson)));
                        booking.setPrice(parser.getDouble(getResources().getString(R.string.bookingTrainer_priceJson)));
                        String dateBookStart = parser.getString(getResources().getString(R.string.bookingTrainer_startServiceJson));
                        String dateBookEnd = parser.getString(getResources().getString(R.string.bookingTrainer_endServiceJson));
                        SimpleDateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.pattern_date_format));
                        booking.setDateSchedule(dateFormat.parse(dateBookStart));
                        booking.setTimeStartScheduled(dateFormat.parse(dateBookStart));
                        booking.setTimeEndScheduled(dateFormat.parse(dateBookEnd));

                        getService(booking.getIdService());

                        Thread.sleep(200);

                        getTrainerName(listServices.get(numResults).getId_trainer_user());
                        Thread.sleep(200);

                        booking.setAddress(listServices.get(numResults).getTraining_address());
                        booking.setTitle(listTrainerName.get(numResults));
                        numResults++;
                        listBookings.add(booking);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getService(final int serviceId) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                BufferedReader in;
                String data = null;
                String line;
                JSONArray parserArray;
                JSONObject parser;

                HttpClient httpclient = new DefaultHttpClient();
                HttpGet request = new HttpGet();

                try{

                    URI website = new URI(getResources().getString(R.string.urlDB) + getResources().getString(R.string.trainerSportDB) + getResources().getString(R.string.serviceDB) + "/" + serviceId);
                    request.setURI(website);
                    HttpResponse response = httpclient.execute(request);
                    in = new BufferedReader(new InputStreamReader(
                            response.getEntity().getContent()));

                    while((line = in.readLine()) != null)
                        data += line;

                    data = data.replaceFirst("null", "");
                    parserArray = new JSONArray(data);

                    parser = (JSONObject) parserArray.get(0);

                    TrainerService trainerService = new TrainerService();
                    trainerService.setId_training_service_post(parser.getInt(getResources().getString(R.string.trainerService_idPost)));
                    trainerService.setId_sport_training_type(parser.getInt(getResources().getString(R.string.trainerService_idSport)));
                    trainerService.setId_trainer_user(parser.getInt(getResources().getString(R.string.trainerService_idTrainer)));
                    trainerService.setTraining_address(parser.getString(getResources().getString(R.string.trainerService_trainingAddress)));
                    trainerService.setTraining_city(parser.getString(getResources().getString(R.string.trainerService_trainingCity)));
                    trainerService.setTraining_desc(parser.getString(getResources().getString(R.string.trainerService_trainingDescription)));
                    trainerService.setTraining_price(parser.getDouble(getResources().getString(R.string.trainerService_trainingPrice)));
                    String startTime = parser.getString(getResources().getString(R.string.trainerService_trainingStart));
                    trainerService.setTraining_start(Time.valueOf(startTime));
                    String endTime = parser.getString(getResources().getString(R.string.trainerService_trainingEnd));
                    trainerService.setTraining_end(Time.valueOf(endTime));

                    listServices.add(trainerService);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getTrainerName(final Integer id_trainer_user) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                BufferedReader in;
                String data = null;
                String line;
                JSONObject parser;

                HttpClient httpclient = new DefaultHttpClient();
                HttpGet request = new HttpGet();

                try {
                    URI website = new URI(getResources().getString(R.string.urlDB) + getResources().getString(R.string.trainerUserDB) + "/" + id_trainer_user);
                    request.setURI(website);
                    HttpResponse response = httpclient.execute(request);
                    in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                    while((line = in.readLine()) != null)
                        data += line;

                    data = data.replaceFirst("null", "");

                    parser = (JSONObject)  new JSONArray(data).get(0);
                    listTrainerName.add(parser.getString(getResources().getString(R.string.individualNameJson)));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getBack(View view) {
        finish();
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listBookings.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return listBookings.get(position).getIdScenario();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.booking_result_layout, null);

            Button cancelBookBtn = convertView.findViewById(R.id.cancelBookResult);
            TextView bookTitle = convertView.findViewById(R.id.myBookResultTitleText);
            TextView bookHours = convertView.findViewById(R.id.myBookResultHoursText);
            TextView bookDate = convertView.findViewById(R.id.myBookResultDateText);
            TextView bookPlace = convertView.findViewById(R.id.myBookResultPlaceText);
            TextView bookValue = convertView.findViewById(R.id.myBookResultPriceText);

            bookTitle.setText(listBookings.get(position).getTitle());
            SimpleDateFormat date = new SimpleDateFormat(getResources().getString(R.string.date_format), Locale.getDefault());
            bookDate.setText(getResources().getString(R.string.bookingResDate) + " " + date.format(listBookings.get(position).getTimeStartScheduled()));
            SimpleDateFormat hours = new SimpleDateFormat(getResources().getString(R.string.hour_format), Locale.getDefault());
            bookHours.setText(getResources().getString(R.string.bookingResTime) + " " + hours.format(listBookings.get(position).getTimeStartScheduled()) + " - " + hours.format(listBookings.get(position).getTimeEndScheduled()));
            bookPlace.setText(getResources().getString(R.string.bookingResAddress) + " " + listBookings.get(position).getAddress());
            if(!listBookings.get(position).getPrice().toString().equals("null"))
                bookValue.setText(getResources().getString(R.string.bookingResPrice) + " " + listBookings.get(position).getPrice().toString() + getResources().getString(R.string.bookingPriceMarkEuro));

            cancelBookBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder cancelAlertBuilder = new AlertDialog.Builder(new ContextThemeWrapper(MyBookings.this, R.style.nemeumDialog));
                    cancelAlertBuilder.setCancelable(true);
                    cancelAlertBuilder.setMessage(R.string.bookingCancelSubtitle);
                    cancelAlertBuilder.setPositiveButton(R.string.buttonYes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(new Runnable() {
                                public void run() {
                                    HttpClient httpclient = new DefaultHttpClient();

                                    HttpDelete httpDelete;
                                    if(listBookings.get(position).getIdScenario() != null)
                                        httpDelete = new HttpDelete(getResources().getString(R.string.urlDB) + getResources().getString(R.string.scenarioJoinDB) + "/" + listBookings.get(position).getIdBooking());
                                    else
                                        httpDelete = new HttpDelete(getResources().getString(R.string.urlDB) + getResources().getString(R.string.trainerJoinDB) + "/" + listBookings.get(position).getIdBooking());

                                    httpDelete.setHeader(getResources().getString(R.string.dbAccessAccept), getResources().getString(R.string.dbAccessAppJson));
                                    httpDelete.setHeader(getResources().getString(R.string.dbAccessContentType), getResources().getString(R.string.dbAccessAppJson));
                                    HttpResponse response = null;
                                    try {
                                        response = httpclient.execute(httpDelete);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if (response.getStatusLine().getStatusCode() == 200) {
                                        MyBookings.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(appContext, getResources().getString(R.string.booking_deleted), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        Intent intent1 = new Intent(appContext, UserLoginActivity.class);
                                        startActivity(intent1);
                                    }else{
                                        MyBookings.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(appContext, getResources().getString(R.string.requisitionError), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }
                            }).start();
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
