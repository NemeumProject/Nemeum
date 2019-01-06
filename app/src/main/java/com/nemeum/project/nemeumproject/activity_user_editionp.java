package com.nemeum.project.nemeumproject;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import models.TrainerUser;

public class activity_user_editionp extends AppCompatActivity  {
    String idUser;
    private Context appContext;
    private SharedPreferences SP;
    private String userType;
    private List<TrainerUser> listTrainer = new ArrayList<>();
    private BottomNavigationView menu;
    private ImageView trainerImage;
    private StorageReference imageStorageReference;
    private FirebaseStorage imageStorage;
    private Uri imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_editionp);
        appContext = getApplicationContext();
        SP = appContext.getSharedPreferences(getResources().getString(R.string.userTypeSP), MODE_PRIVATE);
        idUser = (SP.getString("idUser", ""));

        checkRegisteredUser();
        getTrainerUser();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTextInScreen();

        trainerImage = findViewById(R.id.userPicture);
        imageStorage = FirebaseStorage.getInstance();
        imageStorageReference = imageStorage.getReference();

        trainerImage.setOnClickListener(new View.OnClickListener() {
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
                trainerImage.setImageBitmap(eventImg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setTextInScreen(){
        EditText trainerName = findViewById(R.id.user_Name_Edit);
        EditText trainerLastName = findViewById(R.id.user_LastName_Edit);
        EditText trainerDni = findViewById(R.id.user_DniPassport_Edit);
        EditText trainerTitle = findViewById(R.id.user_text_edit);
        EditText trainerDescription = findViewById(R.id.user_description_edit);
        EditText trainerEmail = findViewById(R.id.email_User_Edit);
        EditText trainerPhone = findViewById(R.id.phone_User_Edit);
        EditText trainerAddress = findViewById(R.id.user_address_Edit);
        EditText trainerPostalCode = findViewById(R.id.user_PostalCode_Edit);
        EditText trainerCity = findViewById(R.id.user_City_Edit);

        trainerName.setText(listTrainer.get(0).getFirstName());

        if(!listTrainer.get(0).getTitle().equals("null")){
            trainerTitle.setText(listTrainer.get(0).getTitle());
        }

        trainerLastName.setText(listTrainer.get(0).getLastSurname());
        trainerDni.setText(listTrainer.get(0).getSsn());
        trainerEmail.setText(listTrainer.get(0).getEmail());
        trainerPostalCode.setText(listTrainer.get(0).getPostalCode());

        trainerAddress.setText(listTrainer.get(0).getAddress());
        trainerCity.setText(listTrainer.get(0).getCity());

        String phone = listTrainer.get(0).getPhone().toString();

        trainerPhone.setText(phone);

        if(!listTrainer.get(0).getDescription().equals("null")){
            trainerDescription.setText(listTrainer.get(0).getDescription());
        }


    }

    private void getTrainerUser(){
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

                    URI website = new URI(getResources().getString(R.string.urlDB) + "/traineruser/" + idUser);
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
                        TrainerUser user = new TrainerUser();
                        user.setIdTrainerUser(parser.getInt("idTrainerUser"));
                        user.setAddress(parser.getString("address"));
                        user.setCity(parser.getString("city"));
                        user.setFirstName(parser.getString("firstName"));
                        user.setMiddleSurname(parser.getString("middleSurname"));
                        user.setLastSurname(parser.getString("lastSurname"));
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
                        listTrainer.add(user);

                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                Thread.yield();
            }
        }).start();
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

    public void save_Button_UserEditionP(View view) {
        EditText trainerName = findViewById(R.id.user_Name_Edit);
        EditText trainerLastName = findViewById(R.id.user_LastName_Edit);
        EditText trainerDni = findViewById(R.id.user_DniPassport_Edit);
        EditText trainerTitle = findViewById(R.id.user_text_edit);
        EditText trainerDescription = findViewById(R.id.user_description_edit);
        EditText trainerEmail = findViewById(R.id.email_User_Edit);
        EditText trainerPhone = findViewById(R.id.phone_User_Edit);
        EditText trainerAddress = findViewById(R.id.user_address_Edit);
        EditText trainerPostalCode = findViewById(R.id.user_PostalCode_Edit);
        EditText trainerCity = findViewById(R.id.user_City_Edit);

        final String name = trainerName.getText().toString();
        final String lastName = trainerLastName.getText().toString();
        final String ssn = trainerDni.getText().toString();
        final String title = trainerTitle.getText().toString();
        final String desc = trainerDescription.getText().toString();
        final String email = trainerEmail.getText().toString();
        final String phone = trainerPhone.getText().toString();
        final String address = trainerAddress.getText().toString();
        final String postalCode = trainerPostalCode.getText().toString();
        final String city = trainerCity.getText().toString();
        final Integer id_user = Integer.parseInt(idUser);
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
                HttpPut httpPut = new HttpPut(getResources().getString(R.string.urlDB) + "/traineruser");

                JSONObject postData = new JSONObject();
                try {
                    postData.put("idTrainerUser", id_user);
                    postData.put("firstName", name);
                    postData.put("title", title);
                    postData.put("address", address);
                    postData.put("city", city);
                    postData.put("phone", phone);
                    postData.put("description", desc);
                    postData.put("image", finalImage);
                    postData.put("email", email);
                    postData.put("postalCode", postalCode);
                    postData.put("lastName", lastName);
                    postData.put("ssn", ssn);

                    StringEntity se = new StringEntity(postData.toString(), "UTF-8");
                    httpPut.setHeader("Accept", "application/json");
                    httpPut.setHeader("Content-Type", "application/json; charset=utf-8");
                    httpPut.setEntity(se);
                    HttpResponse response = httpclient.execute(httpPut);
                    if(response.getStatusLine().getStatusCode() == 200) {
                        Intent intent1 = new Intent(activity_user_editionp.this, UserTrainerLoginActivity.class);
                        startActivity(intent1);
                    }else{
                        activity_user_editionp.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(activity_user_editionp.this, "Something was wrong! Try to repeat", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    activity_user_editionp.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity_user_editionp.this, "Something was wrong! Try to repeat", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }
}
