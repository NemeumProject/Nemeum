package com.nemeum.project.nemeumproject;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import models.Event;

public class PostEvent extends AppCompatActivity {

    private Context appContext;

    private EditText eventTitle;
    private EditText eventDesc;
    private EditText eventLocation;
    private EditText eventCity;
    private EditText eventDate;
    private ImageView eventImage;
    private Button submitEventBtn;
    private Uri imagePath;
    private FirebaseStorage imageStorage;
    private StorageReference imageStorageReference;
    private Calendar eventCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_event);

        appContext = getApplicationContext();

        eventTitle = findViewById(R.id.eventTitle);
        eventDesc = findViewById(R.id.eventDescription);
        eventLocation = findViewById(R.id.eventLocation);
        eventCity = findViewById(R.id.eventCity);
        eventDate = findViewById(R.id.eventDate);
        eventImage = findViewById(R.id.eventImg);
        submitEventBtn = findViewById(R.id.eventPost);
        eventCalendar = Calendar.getInstance();

        imageStorage = FirebaseStorage.getInstance();
        imageStorageReference = imageStorage.getReference();

        eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        eventDate.setShowSoftInputOnFocus(false);
        eventDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    new DatePickerDialog(PostEvent.this, date, eventCalendar
                            .get(Calendar.YEAR), eventCalendar.get(Calendar.MONTH),
                            eventCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });
        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(PostEvent.this, date, eventCalendar
                        .get(Calendar.YEAR), eventCalendar.get(Calendar.MONTH),
                        eventCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        submitEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String databaseFormat = getResources().getString(R.string.pattern_date_format);
                SimpleDateFormat dbSdf = new SimpleDateFormat(databaseFormat, Locale.getDefault());
                SubmitEvent();
            }
        });
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String exhibitFormat = getResources().getString(R.string.date_format);
            SimpleDateFormat sdf = new SimpleDateFormat(exhibitFormat, Locale.getDefault());

            eventCalendar.set(Calendar.YEAR, year);
            eventCalendar.set(Calendar.MONTH, monthOfYear);
            eventCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            eventDate.setText(sdf.format(eventCalendar.getTime()));
        }
    };

    private void chooseImage(){
        Intent chooseImg = new Intent();
        chooseImg.setType("image/*");
        chooseImg.setAction(Intent.ACTION_GET_CONTENT);
        chooseImg.createChooser(chooseImg, "Select Picture");
        startActivityForResult(chooseImg, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            try {
                imagePath = data.getData();
                Bitmap eventImg = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                eventImage.setImageBitmap(eventImg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void getBack(View view) {
        finish();
    }

    private void SubmitEvent(){
        boolean error = false;
        if(eventTitle.getText().toString().isEmpty()){
            Toast.makeText(appContext, getResources().getString(R.string.field_title_empty), Toast.LENGTH_LONG).show();
            error = true;
        }
        if(eventDesc.getText().toString().isEmpty()) {
            Toast.makeText(appContext, getResources().getString(R.string.field_description_empty), Toast.LENGTH_LONG).show();
            error = true;
        }
        if(eventLocation.getText().toString().isEmpty()) {
            Toast.makeText(appContext, getResources().getString(R.string.field_location_empty), Toast.LENGTH_LONG).show();
            error = true;
        }
        if (eventCity.getText().toString().isEmpty()) {
            Toast.makeText(appContext, getResources().getString(R.string.field_city_empty), Toast.LENGTH_LONG).show();
            error = true;
        }
        if (eventDate.getText().toString().isEmpty()) {
            Toast.makeText(appContext, getResources().getString(R.string.field_date_empty), Toast.LENGTH_LONG).show();
            error = true;
        }
        if(!error){

            final Event event = new Event();

            if(imagePath != null){
                final ProgressDialog progress = new ProgressDialog(this);
                progress.setTitle("Uploading...");
                progress.show();

                StorageReference reference = imageStorageReference.child("images/" + UUID.randomUUID().toString());
                reference.putFile(imagePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progress.dismiss();
                                Toast.makeText(appContext, "Uploaded", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progress.dismiss();
                                Toast.makeText(appContext, "Failed " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double percentage = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                progress.setMessage("Uploaded " + percentage + "%");
                            }
                        });
                event.setImage("https://firebasestorage.googleapis.com/v0/b" + imageStorageReference.toString().substring(4) + "o/" + reference.getPath().substring(1).replace("/", "%2F") + "?alt=media");
            }

            event.setTitle(eventTitle.getText().toString());
            event.setDescription(eventDesc.getText().toString());
            event.setAddress(eventLocation.getText().toString());
            event.setCity(eventCity.getText().toString());

            new Thread(new Runnable() {
                @SuppressLint("StringFormatInvalid")
                public void run() {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(getResources().getString(R.string.urlDB) + getResources().getString(R.string.eventDB));

                    DateFormat df = new SimpleDateFormat(getResources().getString(R.string.pattern_date_format));

                    JSONObject postData = new JSONObject();
                    try {
                        postData.put(getResources().getString(R.string.eventSportIdJson), 1);
                        postData.put(getResources().getString(R.string.eventIndoorJson), false);
                        postData.put(getResources().getString(R.string.eventCapacityJson), 0);
                        postData.put(getResources().getString(R.string.eventCityJson), event.getCity());
                        postData.put(getResources().getString(R.string.eventAddressJson), event.getAddress());
                        postData.put(getResources().getString(R.string.eventPostalCodeJson), 25001);
                        postData.put(getResources().getString(R.string.eventPhoneJson), 111111111);
                        postData.put(getResources().getString(R.string.eventDateEventJson), df.format(eventCalendar.getTime()));
                        postData.put(getResources().getString(R.string.eventDescriptionJson), event.getDescription());
                        postData.put(getResources().getString(R.string.eventImageJson), event.getImage());
                        postData.put(getResources().getString(R.string.eventTitleJson), event.getTitle());
                        postData.put(getResources().getString(R.string.eventPriceJson), 0);

                        StringEntity se = new StringEntity(postData.toString(), "UTF-8");
                        httppost.setHeader(getResources().getString(R.string.dbAccessAccept), getResources().getString(R.string.dbAccessAppJson));
                        httppost.setHeader(getResources().getString(R.string.dbAccessContentType), getResources().getString(R.string.dbAccessAppJson));
                        httppost.setEntity(se);
                        HttpResponse response = httpclient.execute(httppost);
                        if(response.getStatusLine().getStatusCode() == 200) {
                            Intent intent1 = new Intent(appContext, UserCompanyLoginActivity.class);
                            startActivity(intent1);
                        }else{
                            PostEvent.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(appContext, getResources().getString(R.string.requisitionError), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } catch (IOException e) {
                        PostEvent.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(appContext, getResources().getString(R.string.requisitionError), Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
