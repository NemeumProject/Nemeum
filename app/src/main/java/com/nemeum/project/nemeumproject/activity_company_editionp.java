package com.nemeum.project.nemeumproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import models.CompanyUser;

public class activity_company_editionp extends AppCompatActivity {

    private Context appContext;
    private SharedPreferences SP;
    private BottomNavigationView menu;
    private String userType;
    private List<CompanyUser> listCompany = new ArrayList<>();
    String idUser;
    private ImageView eventImage;
    private StorageReference imageStorageReference;
    private FirebaseStorage imageStorage;
    private Uri imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_editionp);

        appContext = getApplicationContext();
        SP = appContext.getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);
        idUser = (SP.getString("idUser", ""));

        checkRegisteredUser();
        getCompanyUser();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTextInScreen();

        eventImage = findViewById(R.id.eventImg);
        imageStorage = FirebaseStorage.getInstance();
        imageStorageReference = imageStorage.getReference();

        eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
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

    private void checkRegisteredUser() {
        menu = findViewById(R.id.navigation);
        userType = SP.getString(getResources().getString(R.string.userTypeSP), "");

        if(userType.equals(getResources().getString(R.string.individualUserSP)) ||
                userType.equals(getResources().getString(R.string.trainerUserSP)) ||
                userType.equals(getResources().getString(R.string.companyUserSP))){
            menu.getMenu().getItem(2).setVisible(false);
        } else {
            menu.getMenu().getItem(3).setVisible(false);
        }
    }

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

    private void setTextInScreen(){
        TextView companyName = findViewById(R.id.companyTitleEditText);
        TextView sloganName = findViewById(R.id.companySloganEditText);
        TextView companyAddress = findViewById(R.id.companyPlaceEditText);
        TextView companyCity = findViewById(R.id.companyCityEditText);
        TextView companyDescription = findViewById(R.id.scenarioDescriptionEditText);
        TextView companyPhone = findViewById(R.id.companyPhoneEditText);

        companyName.setText(listCompany.get(0).getCompanyName());

        if(!listCompany.get(0).getTitle().equals("null")){
            sloganName.setText(listCompany.get(0).getTitle());
        }

        companyAddress.setText(listCompany.get(0).getAddress());
        companyCity.setText(listCompany.get(0).getCity());

        String phone = listCompany.get(0).getPhone().toString();

        companyPhone.setText(phone);

        if(!listCompany.get(0).getDescription().equals("null")){
            companyDescription.setText(listCompany.get(0).getDescription());
        }


    }

    private void getCompanyUser(){
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

                    URI website = new URI(getResources().getString(R.string.urlDB) + "/companyuser/" + idUser);
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
                        CompanyUser user = new CompanyUser();
                        user.setIdCompanyUser(parser.getInt("idCompanyUser"));
                        user.setAddress(parser.getString("address"));
                        user.setCity(parser.getString("city"));
                        user.setComercialName(parser.getString("comercialName"));
                        user.setCompanyName(parser.getString("companyName"));
                        user.setContactPerson(parser.getString("contactPerson"));
                        user.setDescription(parser.getString("description"));
                        user.setEmail(parser.getString("email"));
                        user.setImage(parser.getString("image"));
                        user.setPassword(parser.getString("password"));
                        user.setPhone(parser.getInt("phone"));
                        user.setPostalCode(parser.getString("postalCode"));
                        if(!parser.isNull("premium")){
                            user.setPremium(parser.getBoolean("premium"));
                        }
                        user.setSsn(parser.getString("ssn"));
                        user.setTitle(parser.getString("title"));
                        user.setUsername(parser.getString("username"));
                        numResults++;
                        listCompany.add(user);

                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                Thread.yield();
            }
        }).start();
    }

    public void save_CompanyEditionP(View view) {

        //set up the variables to validate not empty fields and strings to show as error messages
        EditText etCompanyName = findViewById(R.id.companyTitleEditText);
        String strCompanyName = etCompanyName.getText().toString();
        String company_Name_Error = getString(R.string.name_Company_Empty);

        EditText etCompanySlogan = findViewById(R.id.companySloganEditText);
        String strCompanySlogan = etCompanySlogan.getText().toString();

        EditText etCompanyCity = findViewById(R.id.companyCityEditText);
        String strCompanyCity = etCompanyCity.getText().toString();

        EditText etCompanyAddress = findViewById(R.id.companyPlaceEditText);
        String strCompanyAddress = etCompanyAddress.getText().toString();
        String company_Address_Error = getString(R.string.company_Address_Empty);

        EditText etCompanyPhone = findViewById(R.id.companyPhoneEditText);
        String strCompanyPhone = etCompanyPhone.getText().toString();
        String company_Phone_Error = getString(R.string.company_Phone_Empty);

        EditText etCompanyDescription = findViewById(R.id.scenarioDescriptionEditText);
        String strCompanyDescription = etCompanyDescription.getText().toString();


        if(TextUtils.isEmpty(strCompanyName)) {
            etCompanyName.setError(company_Name_Error);
            return;
        }
        else  if(TextUtils.isEmpty(strCompanyAddress)) {
            etCompanyAddress.setError(company_Address_Error);
            return;
        }
        else  if(TextUtils.isEmpty(strCompanyPhone)) {
            etCompanyPhone.setError(company_Phone_Error);
            return;
        }else{
            final String id_user = idUser;
            final String companyName = strCompanyName;
            final String title = strCompanySlogan;
            final String address = strCompanyAddress;
            final String city = strCompanyCity;
            final Integer phone = Integer.parseInt(strCompanyPhone);
            final String description = strCompanyDescription;
            String image = null;
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
                image = "https://firebasestorage.googleapis.com/v0/b" + imageStorageReference.toString().substring(4) + "o/" + reference.getPath().substring(1).replace("/", "%2F") + "?alt=media";
            }
            final String finalImage = image;
            new Thread(new Runnable() {
                public void run() {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPut httpPut = new HttpPut(getResources().getString(R.string.urlDB) + "/companyuser");

                    JSONObject postData = new JSONObject();
                    try {
                        postData.put("idCompanyUser", id_user);
                        postData.put("companyName", companyName);
                        postData.put("title", title);
                        postData.put("address", address);
                        postData.put("city", city);
                        postData.put("phone", phone);
                        postData.put("description", description);
                        postData.put("image", finalImage);

                        StringEntity se = new StringEntity(postData.toString(), "UTF-8");
                        httpPut.setHeader("Accept", "application/json");
                        httpPut.setHeader("Content-Type", "application/json; charset=utf-8");
                        httpPut.setEntity(se);
                        HttpResponse response = httpclient.execute(httpPut);
                        if(response.getStatusLine().getStatusCode() == 200) {
                            Intent intent1 = new Intent(activity_company_editionp.this, UserCompanyLoginActivity.class);
                            startActivity(intent1);
                        }else{
                            activity_company_editionp.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(activity_company_editionp.this, "Something was wrong! Try to repeat", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } catch (IOException e) {
                        activity_company_editionp.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(activity_company_editionp.this, "Something was wrong! Try to repeat", Toast.LENGTH_LONG).show();
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
