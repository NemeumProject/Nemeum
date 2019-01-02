package com.nemeum.project.nemeumproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class BookScenarios extends AppCompatActivity {

    Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_scenarios);

        appContext = getApplicationContext();

        ImageView scenarioPicture = findViewById(R.id.bookScenarioImg);
        TextView scenarioName = findViewById(R.id.scenarioBookTitleText);
        TextView scenarioPrice = findViewById(R.id.scenarioBookPriceText);
        TextView scenarioDescription = findViewById(R.id.scenarioBookDescriptionText);

        scenarioPicture.setImageResource(getIntent().getIntExtra(getResources().getString(R.string.scenarioImgExtra), R.drawable.bicycle_rider));
        scenarioName.setText(getIntent().getStringExtra(getResources().getString(R.string.scenarioNameExtra)));
        scenarioPrice.setText("Price: "+getIntent().getStringExtra(getResources().getString(R.string.scenarioPriceExtra))+"€/Hour");
        scenarioDescription.setText(getIntent().getStringExtra(getResources().getString(R.string.scenarioDescrExtra)));

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
        Intent intentPayment = new Intent(appContext, BookScenarioPayment.class);
        intentPayment.putExtra(getResources().getString(R.string.scenarioNameExtra), getIntent().getStringExtra(getResources().getString(R.string.scenarioNameExtra)));
        intentPayment.putExtra(getResources().getString(R.string.scenarioPriceExtra),getIntent().getStringExtra(getResources().getString(R.string.scenarioPriceExtra)));
        appContext.startActivity(intentPayment);
    }
}
