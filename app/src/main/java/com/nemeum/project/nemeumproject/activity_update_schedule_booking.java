package com.nemeum.project.nemeumproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class activity_update_schedule_booking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_schedule_booking);

        Bundle my_new_Bundle =this.getIntent().getExtras();
        if (my_new_Bundle!=null){

            TextView show_scenarioName= findViewById(R.id.show_scenario_name);
            TextView show_Date = findViewById(R.id.show_Date_Daily_Schedule);
            TextView show_startingTime = findViewById(R.id.show_StartingTime_Daily_Schedule);
            TextView show_endingTime = findViewById(R.id.show_EndingTime_Daily_Schedule);
            TextView show_name = findViewById(R.id.show_customerName);
            TextView show_phone = findViewById(R.id.show_customerPhone);
            TextView show_email = findViewById(R.id.show_customerEmail);

            String scenario_name_to_show = my_new_Bundle.getString("scenarioName");
            String date_to_show= my_new_Bundle.getString("Date");
            String startingTime_to_show= my_new_Bundle.getString("starting_Time");
            String endingTime_to_show= my_new_Bundle.getString("ending_Time");
            String name_to_show= my_new_Bundle.getString("customer_Name");
            String phone_to_show= my_new_Bundle.getString("phone");
            String email_to_show= my_new_Bundle.getString("email");

            show_scenarioName.setText(scenario_name_to_show);
            show_Date.setText(date_to_show);
            show_startingTime.setText(startingTime_to_show);
            show_endingTime.setText(endingTime_to_show);
            show_name.setText(name_to_show);
            show_phone.setText(phone_to_show);
            show_email.setText(email_to_show);

        }
    }

    public void getBack(View view) {
        finish();
    }
}
