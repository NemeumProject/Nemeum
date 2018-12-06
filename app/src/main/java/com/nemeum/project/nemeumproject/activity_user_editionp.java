package com.nemeum.project.nemeumproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class activity_user_editionp extends AppCompatActivity  {
    EditText birthdt_EditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_editionp);
        birthdt_EditText= (EditText) findViewById(R.id.user_birthday_Edit);
        birthdt_EditText.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {

                new DatePickerDialog(activity_user_editionp.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
        String myFormat = "dd/mm/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("es", "ES"));

        birthdt_EditText.setText(sdf.format(myCalendar.getTime()));
    }



    public void save_Button_UserEditionP(View view) {
//set up the variables to validate not empty fields and strings to show as error messages
        EditText etName = (EditText) findViewById(R.id.user_Name_Edit);
        String strName = etName.getText().toString();
        String name_Error = getString(R.string.name_UserEdition_Empty);

        EditText etLastName = (EditText) findViewById(R.id.user_LastName_Edit);
        String strLastName = etLastName.getText().toString();
        String lastName_Error = getString(R.string.lastname_UserEdition_Empty);

        EditText etEmail = (EditText) findViewById(R.id.email_User_Edit);
        String strEmail = etEmail.getText().toString();
        String email_Error = getString(R.string.email_UserEdition_Empty);

        if(TextUtils.isEmpty(strName)) {
            etName.setError(name_Error);
            return;
        }
        else  if(TextUtils.isEmpty(strLastName)) {
            etLastName.setError(lastName_Error);
            return;
        }
        else  if(TextUtils.isEmpty(strEmail)) {
            etEmail.setError(email_Error);
            return;
        }
// Validate if gender radio button is selected
        RadioGroup rgGender =(RadioGroup) findViewById(R.id.user_RbtG_Gender);
        String gender_Error = getString(R.string.gender_UserEdition_Empty);

        if (rgGender.getCheckedRadioButtonId() == -1)
        {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, gender_Error, duration);
            toast.show();
        }





    }
}
