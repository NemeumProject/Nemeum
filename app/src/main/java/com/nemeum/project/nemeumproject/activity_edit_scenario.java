package com.nemeum.project.nemeumproject;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

public class activity_edit_scenario extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageButton changePicture;
    private static final int PICK_IMAGE=100;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_scenario);



        Spinner spinner = findViewById(R.id.scenarios_Spinner_EditScenario);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this,R.array.scenarios_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        changePicture =(ImageButton) findViewById(R.id.companyLogoImg_EditScenario);

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void edit_scenario(View view) {
        //set up the variables to validate not empty fields and strings to show as error messages
        EditText etTitle = (EditText) findViewById(R.id.title_Edit_Scenario);
        String strTitle = etTitle.getText().toString();
        String title_Error = getString(R.string.title_Post_Empty);

        EditText etCapacity = (EditText) findViewById(R.id.capacity_Edit_Scenarios);
        String strCapacity = etCapacity.getText().toString();
        String capacity_Error = getString(R.string.capacity_Post_Empty);

        EditText etLocation = (EditText) findViewById(R.id.location_Edit_Scenario);
        String strLocation = etLocation.getText().toString();
        String location_Error = getString(R.string.location_Post_Empty);

        EditText etPrice = (EditText) findViewById(R.id.price_Edit_Scenario);
        String strPrice = etPrice.getText().toString();
        String price_Error = getString(R.string.price_Post_Empty);

        EditText etDescription = (EditText) findViewById(R.id.description_Edit_Scenario);
        String strDescription = etDescription.getText().toString();
        String description_Error = getString(R.string.description_Post_Empty);





        if(TextUtils.isEmpty(strTitle)) {
            etTitle.setError(title_Error);
            return;
        }
        else if (TextUtils.isEmpty(strCapacity)){
            etCapacity.setError(capacity_Error);
            return;
        }
        else if (TextUtils.isEmpty(strPrice)){
            etPrice.setError(price_Error);
            return;
        }
        else if (TextUtils.isEmpty(strLocation)){
            etLocation.setError(location_Error);
            return;
        }
        else if (TextUtils.isEmpty(strDescription)){
            etDescription.setError(description_Error);
            return;
        }


    }

    public void openGallery(View view) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
        imageUri= data.getData();
        changePicture.setImageURI(imageUri);

    }

    }
}
