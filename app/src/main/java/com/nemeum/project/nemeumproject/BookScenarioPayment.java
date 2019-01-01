package com.nemeum.project.nemeumproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class    BookScenarioPayment extends AppCompatActivity {

    Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_scenario_payment);

        appContext = getApplicationContext();

        TextView scenarioName = findViewById(R.id.scenarioBookTitleText);
        scenarioName.setText(getIntent().getStringExtra(getResources().getString(R.string.scenarioNameExtra)));

        CalendarView bookDay = findViewById(R.id.bookDayCalendar);

        Spinner startingRent = findViewById(R.id.startTime);
        ArrayAdapter<CharSequence> startRentAdapter = ArrayAdapter.createFromResource(this, R.array.hourStartFilter,R.layout.spinner_layout);
        startRentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startingRent.setAdapter(startRentAdapter);

        Spinner endingRent = findViewById(R.id.endTime);
        ArrayAdapter<CharSequence> endRentAdapter = ArrayAdapter.createFromResource(this, R.array.hourEndFilter,R.layout.spinner_layout);
        endRentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endingRent.setAdapter(endRentAdapter);

        Spinner bookPaymentMode = findViewById(R.id.paymentMethod);
        ArrayAdapter<CharSequence> bookPaymentAdapter = ArrayAdapter.createFromResource(this, R.array.paymentMethodsFilter,R.layout.spinner_layout);
        bookPaymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookPaymentMode.setAdapter(bookPaymentAdapter);

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

}
