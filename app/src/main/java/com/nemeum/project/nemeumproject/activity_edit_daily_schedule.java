package com.nemeum.project.nemeumproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class activity_edit_daily_schedule extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText date_EditText;
    EditText startingTime_EditText;
    EditText endingTime_EditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_daily_schedule);

        Spinner spinner = findViewById(R.id.scenarios_Spinner_Daily_Schedule);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this,R.array.scenarios_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        date_EditText= (EditText) findViewById(R.id.et_Date_Daily_Schedule);
        startingTime_EditText=(EditText) findViewById(R.id.et_StartingTime_Daily_Schedule);
        endingTime_EditText=(EditText) findViewById(R.id.et_EndingTime_Daily_Schedule);
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

        EditText etDate = (EditText)findViewById(R.id.et_Date_Daily_Schedule);
        String str_date = etDate.getText().toString();
        String date_Error= getString(R.string.date_Post_Empty);

        EditText etStartingTime = (EditText)findViewById(R.id.et_StartingTime_Daily_Schedule);
        String stStartingTime = etStartingTime.getText().toString();
        String stStartingTime_Error= getString(R.string.stime_error);


        EditText etEndingTime = (EditText)findViewById(R.id.et_EndingTime_Daily_Schedule);
        String strEndTime = etEndingTime.getText().toString();
        String strEndingtime_Error = getString(R.string.etime_error) ;

        EditText etCustomerName = (EditText) findViewById(R.id.et_customerName);
        String strName = etCustomerName.getText().toString();
        String name_Error = getString(R.string.name_UserEdition_Empty);

        EditText etCustomerPhone = (EditText) findViewById(R.id.et_customerPhone);
        String strPhone = etCustomerPhone.getText().toString();
        String phone_Error = getString(R.string.user_Phone_Empty);

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

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

        // check if spinner item is selected
        Spinner spinner =(Spinner) findViewById(R.id.scenarios_Spinner_Daily_Schedule);
        String scenarios_spinner_Error = getString(R.string.scenarios_spinner_Error);

        if (spinner.getSelectedItemId() == -1)
        {


            Toast toast = Toast.makeText(context, scenarios_spinner_Error, duration);
            toast.show();
        }

    }
}
