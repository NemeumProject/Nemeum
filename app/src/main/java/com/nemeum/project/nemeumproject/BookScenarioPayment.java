package com.nemeum.project.nemeumproject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.DateInterval;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Spinner;
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
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookScenarioPayment  extends Activity {

    Context appContext;
    String start_calc,end_calc,payment_met;
    float s,e,calculation;
    int days,months,years;

    String titles = "titles";
    String dates_days = "dates_days";
    String dates_months = "dates_months";
    String dates_years = "dates_years";
    String starting = "starting";
    String finishing = "finishing";
    String payment_method = "payment_method";
    String prices = "prices";
    static final int CUSTOM_DIALOG_ID = 0;
    static final int ERROR_DIALOG = 1;
    TextView title_popup;
    TextView date_popup;
    TextView time_popup;
    TextView payment_method_popup;
    TextView total_price_popup;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_scenario_payment);
        appContext = getApplicationContext();

        Button back_btn = findViewById(R.id.backBtn);
        final TextView scenarioName = findViewById(R.id.scenarioBookTitleText);
        scenarioName.setText(getIntent().getStringExtra(getResources().getString(R.string.scenarioNameExtra)));

        TextView scenarioPrice = findViewById(R.id.totalScenarioPrice);
        scenarioPrice.setText("Price: "+getIntent().getStringExtra(getResources().getString(R.string.scenarioPriceExtra))+"€/Hour");

        final Button submit_booking_btn = findViewById(R.id.confirmBookButton);
        final CalendarView bookDay = findViewById(R.id.bookDayCalendar);

        final Spinner startingRent = findViewById(R.id.startTime);
        ArrayAdapter<CharSequence> startRentAdapter = ArrayAdapter.createFromResource(this, R.array.hourStartFilter,R.layout.spinner_layout);
        startRentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startingRent.setAdapter(startRentAdapter);

        final Spinner endingRent = findViewById(R.id.endTime);
        ArrayAdapter<CharSequence> endRentAdapter = ArrayAdapter.createFromResource(this, R.array.hourEndFilter,R.layout.spinner_layout);
        endRentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endingRent.setAdapter(endRentAdapter);

        final Spinner bookPaymentMode = findViewById(R.id.paymentMethod);
        ArrayAdapter<CharSequence> bookPaymentAdapter = ArrayAdapter.createFromResource(this, R.array.paymentMethodsFilter,R.layout.spinner_layout);
        bookPaymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookPaymentMode.setAdapter(bookPaymentAdapter);

        bookDay.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                years = year;
                months = month+1;
                days = dayOfMonth;
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                finish();
            }
        });

        submit_booking_btn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Bundle bundle = new Bundle();
                bundle.putString(titles, scenarioName.getText().toString());
                bundle.putString(dates_days,String.valueOf(days));
                bundle.putString(dates_months,String.valueOf(months));
                bundle.putString(dates_years,String.valueOf(years));
                bundle.putString(starting,startingRent.getSelectedItem().toString());
                bundle.putString(finishing,endingRent.getSelectedItem().toString());
                bundle.putString(payment_method,bookPaymentMode.getSelectedItem().toString());
                bundle.putString(prices,getIntent().getStringExtra(getResources().getString(R.string.scenarioPriceExtra)));
                if(String.valueOf(days).equals("0") || startingRent.getSelectedItem().toString().equals("Starting Time") || endingRent.getSelectedItem().toString().equals("Finish Time") || Float.parseFloat(startingRent.getSelectedItem().toString().replace(":00",""))
                        >=Float.parseFloat(endingRent.getSelectedItem().toString().replace(":00","")) ||
                        bookPaymentMode.getSelectedItem().toString().equals("Payment Method"))
                {
                    showDialog(ERROR_DIALOG);
                }
                else
                {
                    showDialog(CUSTOM_DIALOG_ID, bundle);
                }

            }});

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

    @Override
    protected Dialog onCreateDialog(int id) {

        Dialog dialog = null;

        switch(id) {
            case CUSTOM_DIALOG_ID:
                dialog = new Dialog(BookScenarioPayment .this);
                dialog.setContentView(R.layout.activity_booking_popup);
                title_popup = dialog.findViewById(R.id.scenarioTitleSubmission);
                date_popup = dialog.findViewById(R.id.DatesSubmission);
                time_popup = dialog.findViewById(R.id.TimesSubmission);
                payment_method_popup = dialog.findViewById(R.id.PaymentMethodSubmission);
                total_price_popup = dialog.findViewById(R.id.TotalPriceSubmission);

                Button dialog_OK = dialog.findViewById(R.id.yes);
                dialog_OK.setOnClickListener(new OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        submitBookingData(Integer.parseInt(getIntent().getStringExtra(getResources().getString(R.string.scenarioid))),Integer.parseInt(getIntent().getStringExtra(getResources().getString(R.string.scenarioiduser))),start_calc,end_calc,payment_met);
                    }});

                Button dialog_Cancel = dialog.findViewById(R.id.no);
                dialog_Cancel.setOnClickListener(new OnClickListener(){

                    @Override
                    public void onClick(View v) {
// TODO Auto-generated method stub
                        Toast.makeText(BookScenarioPayment .this,
                                "Cancelled",
                                Toast.LENGTH_LONG).show();
                        dismissDialog(CUSTOM_DIALOG_ID);
                    }});

                break;

            case ERROR_DIALOG:
                dialog = new Dialog(BookScenarioPayment .this);
                dialog.setContentView(R.layout.activity_error_popup);

                Button dialog_back = dialog.findViewById(R.id.ok_back);
                dialog_back.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissDialog(ERROR_DIALOG);
                    }
                });

        }



        return dialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle bundle) {
// TODO Auto-generated method stub
        super.onPrepareDialog(id, dialog, bundle);

        switch(id) {
            case CUSTOM_DIALOG_ID:
                start_calc = bundle.getString(starting);
                end_calc = bundle.getString(finishing);
                payment_met = bundle.getString(payment_method);
                s = Float.parseFloat(start_calc.replace(":00",""));
                e = Float.parseFloat(end_calc.replace(":00",""));

                calculation = Float.parseFloat(bundle.getString(prices))*(e-s);
                title_popup.setText(bundle.getString(titles));
                date_popup.setText("Date: "+bundle.getString(dates_days)+"-"+bundle.getString(dates_months)+"-"+bundle.getString(dates_years));
                time_popup.setText("Start: "+bundle.getString(starting)+"  "+"End: "+bundle.getString(finishing));
                payment_method_popup.setText("Payment Method: "+payment_met);
                total_price_popup.setText("Total Price: "+calculation+"€");
                break;
            case ERROR_DIALOG:
                break;
        }
    }

    public void submitBookingData(Integer id_scenario, Integer id_user, String starting,String ending,String payment_method)
    {
        final Integer scenario_id = id_scenario;
        final Integer user_id = id_user;
        String month;
        String day;
        if(months < 10){
            month = "0" + String.valueOf(months);
        }else{
            month = String.valueOf(months);
        }
        if(days < 10){
            day = "0" + String.valueOf(days);
        }else{
            day = String.valueOf(days);
        }
        final String startDate = String.valueOf(years) + "-" + month + "-" + day + " " + Time.valueOf(starting+":00");
        final String endDate =String.valueOf(years) + "-" + month + "-" + day + " " + Time.valueOf(ending+":00");
        final String payment_m = payment_method;

        new Thread(new Runnable() {
            public void run() {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(getResources().getString(R.string.urlDB) + "/joinScenario");
                JSONObject postData = new JSONObject();
                try {
                    Date date = new Date();
                    String currentDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
                    postData.put("idScenario",scenario_id);
                    postData.put("idUser",user_id);
                    postData.put("startScenario",startDate);
                    postData.put("endScenario",endDate);
                    postData.put("dateBooking",currentDate);
                    StringEntity se = new StringEntity(postData.toString(), "UTF-8");
                    httppost.setHeader("Accept", "application/json");
                    httppost.setHeader("Content-Type", "application/json; charset=utf-8");
                    httppost.setEntity(se);
                    HttpResponse response = httpclient.execute(httppost);
                    if(response.getStatusLine().getStatusCode() == 200){
                        BookScenarioPayment.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(BookScenarioPayment.this, getResources().getString(R.string.BookingSuccess), Toast.LENGTH_LONG).show();
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Intent intent2 = new Intent(BookScenarioPayment.this, NearScenarios.class);
                                startActivity(intent2);
                            }
                        });
                    }
                }
                catch (IOException e){
                    BookScenarioPayment.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(BookScenarioPayment.this, getResources().getString(R.string.noconnection), Toast.LENGTH_LONG).show();
                        }
                    });
                    e.printStackTrace();
                }
                catch (JSONException e){
                    BookScenarioPayment.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(BookScenarioPayment.this, getResources().getString(R.string.noconnection), Toast.LENGTH_LONG).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        }
        ).start();
    }
}