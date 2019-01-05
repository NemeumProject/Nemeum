package com.nemeum.project.nemeumproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class BookTrainer extends AppCompatActivity {

    Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_trainer_begin);

        appContext = getApplicationContext();

        ImageView trainerPicture = findViewById(R.id.bookTrainerImg);
        TextView trainerName = findViewById(R.id.TrainerNamesBook);
        TextView trainerSport = findViewById(R.id.TrainerBookSportText);
        TextView trainerPrice = findViewById(R.id.TrainerBookPriceText);
        TextView trainerDescription = findViewById(R.id.TrainerBookDescriptionText);

        trainerPicture.setImageResource(getIntent().getIntExtra(getResources().getString(R.string.trainerImgExtra), R.drawable.bicycle_rider));
        trainerName.setText(getIntent().getStringExtra(getResources().getString(R.string.trainerNameExtra)));
        trainerSport.setText((getIntent().getStringExtra(getResources().getString(R.string.trainersporttype))+" - "+getIntent().getStringExtra(getResources().getString(R.string.trainerAddressExtra))+", "+
        getIntent().getStringExtra(getResources().getString(R.string.trainerCityExtra))));
        trainerPrice.setText("Price: "+getIntent().getStringExtra(getResources().getString(R.string.trainerPriceExtra))+"€/Hour");
        trainerDescription.setText(getIntent().getStringExtra(getResources().getString(R.string.trainerDescrExtra)));

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

    public void scenarioBookPayment(View view) {
        Intent intentPayment = new Intent(appContext, BookTrainerPayment.class);
        intentPayment.putExtra(getResources().getString(R.string.trainerNameExtra), getIntent().getStringExtra(getResources().getString(R.string.trainerNameExtra)));
        intentPayment.putExtra(getResources().getString(R.string.trainersporttype),getIntent().getStringExtra(getResources().getString(R.string.trainersporttype)));
        intentPayment.putExtra(getResources().getString(R.string.trainerPriceExtra),getIntent().getStringExtra(getResources().getString(R.string.trainerPriceExtra)));
        intentPayment.putExtra(getResources().getString(R.string.trainerid),getIntent().getStringExtra(getResources().getString(R.string.trainerid)));
        intentPayment.putExtra(getResources().getString(R.string.trainerserviceid),getIntent().getStringExtra(getResources().getString(R.string.trainerserviceid)));
        appContext.startActivity(intentPayment);
    }

}
