package com.nemeum.project.nemeumproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class activity_Trainer_Posting extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__trainer__posting);

        Spinner spinner = findViewById(R.id.sports_Spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this,R.array.sports_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void posting_Trainer(View view) {
        //set up the variables to validate not empty fields and strings to show as error messages
        EditText etTitle = (EditText) findViewById(R.id.title_Edit);
        String strTitle = etTitle.getText().toString();
        String title_Error = getString(R.string.title_Post_Empty);

        EditText etCapacity = (EditText) findViewById(R.id.capacity_Edit);
        String strCapacity = etCapacity.getText().toString();
        String capacity_Error = getString(R.string.capacity_Post_Empty);

        EditText etLocation = (EditText) findViewById(R.id.location_Edit);
        String strLocation = etLocation.getText().toString();
        String location_Error = getString(R.string.location_Post_Empty);

        EditText etPrice = (EditText) findViewById(R.id.price_Edit);
        String strPrice = etPrice.getText().toString();
        String price_Error = getString(R.string.price_Post_Empty);

        EditText etDescription = (EditText) findViewById(R.id.description_Edit);
        String strDescription = etDescription.getText().toString();
        String description_Error = getString(R.string.description_Post_Empty);

        EditText etDate =(EditText) findViewById(R.id.date_Edit);
        String strDate = etDate.getText().toString();
        String date_Error = getString(R.string.date_Post_Empty);



        if(TextUtils.isEmpty(strTitle)) {
            etTitle.setError(title_Error);
            return;
        }
        else if (TextUtils.isEmpty(strCapacity)){
            etCapacity.setError(capacity_Error);
            return;
        }
        else if (TextUtils.isEmpty(strLocation)){
            etLocation.setError(location_Error);
            return;
        }
        else if (TextUtils.isEmpty(strPrice)){
            etPrice.setError(price_Error);
            return;
        }
        else if (TextUtils.isEmpty(strDescription)){
            etDescription.setError(description_Error);
            return;
        }
        else if (TextUtils.isEmpty(strDate)){
            etDate.setError(date_Error);
            return;
        }


    }
}
