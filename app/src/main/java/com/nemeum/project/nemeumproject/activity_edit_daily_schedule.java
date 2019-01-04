package com.nemeum.project.nemeumproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import models.Scenario;

public class activity_edit_daily_schedule extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText date_EditText;
    EditText startingTime_EditText;
    EditText endingTime_EditText;

    List<Scenario> listScenario = new ArrayList<>();
    ArrayList<String>scenarioList;

    private String idCompany;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_daily_schedule);

        SharedPreferences shared = getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);
        idCompany = (shared.getString("idCompany", ""));

        getAllScenarios(idCompany);

        Spinner spinner = findViewById(R.id.scenarios_Spinner_Daily_Schedule);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, scenarioList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        date_EditText= findViewById(R.id.et_Date_Daily_Schedule);
        startingTime_EditText= findViewById(R.id.et_StartingTime_Daily_Schedule);
        endingTime_EditText= findViewById(R.id.et_EndingTime_Daily_Schedule);
        final String select_stime_popup = getString(R.string.starting_Time);
        final String select_etime_popup = getString(R.string.ending_Time);

        //Set the current date
        String date_n = new SimpleDateFormat(getResources().getString(R.string.date_format), Locale.getDefault()).format(new Date());
        date_EditText.setText(date_n);

        //Create action OnClickListener to allow the user choose a date
        date_EditText.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {

                new DatePickerDialog(activity_edit_daily_schedule.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();



            }
        });

        startingTime_EditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Calendar calendarStartingTime = Calendar.getInstance();
                int hour_st = calendarStartingTime.get(Calendar.HOUR_OF_DAY);
                int minutest = calendarStartingTime.get(Calendar.MINUTE);
                TimePickerDialog st_TimePicker;
                st_TimePicker = new TimePickerDialog(activity_edit_daily_schedule.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHourst, int selectedMinutest) {
                        startingTime_EditText.setText( selectedHourst + ":" + selectedMinutest);
                    }
                }, hour_st, minutest, true);//Yes 24 hour time
                st_TimePicker.setTitle(select_stime_popup);
                st_TimePicker.show();

            }
        });

        endingTime_EditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Calendar calendarEndingTime = Calendar.getInstance();
                int hour = calendarEndingTime.get(Calendar.HOUR_OF_DAY);
                int minute = calendarEndingTime.get(Calendar.MINUTE);
                TimePickerDialog ending_time_TimePicker;
                ending_time_TimePicker = new TimePickerDialog(activity_edit_daily_schedule.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        endingTime_EditText.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                ending_time_TimePicker.setTitle(select_etime_popup);
                ending_time_TimePicker.show();

            }
        });
        
    }
    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {


        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };
    private void updateLabel() {
        String myFormat = getResources().getString(R.string.date_format);
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        date_EditText.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void save_Button_Daily_Schedule(View view) {

        EditText etDate = findViewById(R.id.et_Date_Daily_Schedule);
        final String str_date = etDate.getText().toString();
        String date_Error= getString(R.string.date_Post_Empty);

        EditText etStartingTime = findViewById(R.id.et_StartingTime_Daily_Schedule);
        final String stStartingTime = etStartingTime.getText().toString();
        String stStartingTime_Error= getString(R.string.stime_error);


        EditText etEndingTime = findViewById(R.id.et_EndingTime_Daily_Schedule);
        final String strEndTime = etEndingTime.getText().toString();
        String strEndingtime_Error = getString(R.string.etime_error) ;

        EditText etCustomerName = findViewById(R.id.et_customerName);
        String strName = etCustomerName.getText().toString();
        String name_Error = getString(R.string.name_UserEdition_Empty);

        EditText etCustomerPhone = findViewById(R.id.et_customerPhone);
        final String strPhone = etCustomerPhone.getText().toString();
        String phone_Error = getString(R.string.user_Phone_Empty);

        EditText etEmail = findViewById(R.id.et_customerEmail);
        final String strEmail = etEmail.getText().toString();

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Spinner spinner = findViewById(R.id.scenarios_Spinner_Daily_Schedule);
        String scenarios_spinner_Error = getString(R.string.scenarios_spinner_Error);

        // check if spinner item is selected
        if (spinner.getSelectedItemId() == -1)
        {


            Toast toast = Toast.makeText(context, scenarios_spinner_Error, duration);
            toast.show();
            return;
        }


        if(TextUtils.isEmpty(str_date)) {
            etDate.setError(date_Error);
            return;
        }
        else if(TextUtils.isEmpty(stStartingTime)) {

            Toast toast = Toast.makeText(context, stStartingTime_Error, duration);
            toast.show();
            return;
        }
        else if(TextUtils.isEmpty(strEndTime)) {
            Toast toast = Toast.makeText(context, strEndingtime_Error, duration);
            toast.show();
            return;
        }
        else if(TextUtils.isEmpty(strName)) {
            etCustomerName.setError(name_Error);
            return;
        }
        else  if(TextUtils.isEmpty(strPhone)) {
            etCustomerPhone.setError(phone_Error);
            return;
        }
        else{

      //Code to sent data to the web service and save in the database

            new Thread(new Runnable() {
                public void run() {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(getResources().getString(R.string.urlDB) + getResources().getString(R.string.joinScenarioDB));

                    JSONObject postData = new JSONObject();
                    try {
                       // postData.put(getResources().getString(R.string.userScenario_userscenarioJson), Integer.parseInt(userScenario) );
                       // postData.put(getResources().getString(R.string.userScenario_idUserJson), Integer.parseInt(iduser));
                        postData.put(getResources().getString(R.string.userScenario_idScenarioJson), Integer.parseInt(/*idScenario*/null));
                        postData.put(getResources().getString(R.string.userScenario_dateBookingJson), str_date );
                        postData.put(getResources().getString(R.string.userScenario_startScenarioJson), stStartingTime);
                        postData.put(getResources().getString(R.string.userScenario_endScenarioJson), strEndTime);
                        postData.put(getResources().getString(R.string.userScenario_phoneJson), Integer.parseInt(strPhone));
                        postData.put(getResources().getString(R.string.userScenario_emailJson), strEmail);

                        StringEntity se = new StringEntity(postData.toString());
                        httppost.setHeader(getResources().getString(R.string.dbAccessAccept), getResources().getString(R.string.dbAccessAppJson));
                        httppost.setHeader(getResources().getString(R.string.dbAccessContentType), getResources().getString(R.string.dbAccessAppJson));
                        httppost.setEntity(se);
                        HttpResponse response = httpclient.execute(httppost);
                        if(response.getStatusLine().getStatusCode() == 200){
                            activity_edit_daily_schedule.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(activity_edit_daily_schedule.this,getResources().getString(R.string.bookingOK),Toast.LENGTH_LONG).show();
                                }
                            });
                            Intent intent1 = new Intent(activity_edit_daily_schedule.this, UserCompanyLoginActivity.class);
                            startActivity(intent1);
                        }else{
                            activity_edit_daily_schedule.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(activity_edit_daily_schedule.this,getResources().getString(R.string.bookingError),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } catch (IOException e) {
                        activity_edit_daily_schedule.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(activity_edit_daily_schedule.this, getResources().getString(R.string.noconnection), Toast.LENGTH_LONG).show();
                            }
                        });
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        }

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
                        scenario.setTitle(parser.getString("title"));

                        numResults++;
                        listScenario.add(scenario);

                    }
                    getList_to_Spinner();

                }catch(Exception e){
                    e.printStackTrace();
                }
                Thread.yield();
            }
        }).start();
    }

    private void getList_to_Spinner(){
        scenarioList= new ArrayList<String>();
        scenarioList.add(getResources().getString(R.string.selectplease));

        for(Scenario scenario : listScenario){

            scenarioList.add(scenario.getTitle());


        }
    }
}
