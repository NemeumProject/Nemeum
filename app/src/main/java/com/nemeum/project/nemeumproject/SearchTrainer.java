package com.nemeum.project.nemeumproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

public class SearchTrainer extends AppCompatActivity {

    int[] trainerPicture = {R.drawable.ronaldo, R.drawable.cristiano, R.drawable.bean};
    String[] trainerNameText = {"Ronaldo", "Cristiano Ronaldo", "Mr. Bean"};
    int[] trainerPoints = {3, 4, 5};
    String[] trainerSportText = {"Soccer Trainer", "Soccer Trainer", "Rugby Trainer"};
    String[] trainerDirection = {"Address: Rambla Ferran no. 24", "Address: Rambla Ferran no. 24", "Address: Rambla Ferran no. 24"};

    Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_trainer);

        appContext = getApplicationContext();

        ListView resultList = findViewById(R.id.trainersList);
        CustomAdapter customResult = new CustomAdapter();

        resultList.setAdapter(customResult);

        Spinner sportSpinner = findViewById(R.id.trainerBySport);
        Spinner locationSpinner = findViewById(R.id.trainerByCity);
        Spinner priceSpinner = findViewById(R.id.trainerByPrice);

        ArrayAdapter<CharSequence> sportAdapter = ArrayAdapter.createFromResource(this, R.array.sportFilter, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(this, R.array.cityFilter, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> priceAdapter = ArrayAdapter.createFromResource(this, R.array.priceFilter, android.R.layout.simple_spinner_dropdown_item);

        sportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sportSpinner.setAdapter(sportAdapter);
        locationSpinner.setAdapter(locationAdapter);
        priceSpinner.setAdapter(priceAdapter);


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
                        Intent intentAccount = new Intent(appContext, TrainerDetail.class);
                        appContext.startActivity(intentAccount);
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

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.trainer_result_layout, null);

            ImageView trainerImg = convertView.findViewById(R.id.trainerResultImg);
            Button bookBtn = convertView.findViewById(R.id.bookTrainerResult);
            Button scheduleBtn = convertView.findViewById(R.id.scheduleTrainerResult);
            TextView trainerName = convertView.findViewById(R.id.trainerResultName);
            RatingBar trainerRating = convertView.findViewById(R.id.starTrainerRating);
            TextView trainerSport = convertView.findViewById(R.id.trainerResultSportText);
            TextView trainerAddress = convertView.findViewById(R.id.trainerResultPlaceText);
            TextView trainerDescription = convertView.findViewById(R.id.trainerResultDescriptionText);

            trainerImg.setImageResource(trainerPicture[position]);
            trainerName.setText(trainerNameText[position]);
            trainerRating.setRating(trainerPoints[position]);
            trainerSport.setText(trainerSportText[position]);
            trainerAddress.setText(trainerDirection[position]);
            trainerDescription.setText(R.string.findFacilityDescr);

            return convertView;
        }
    }
}
