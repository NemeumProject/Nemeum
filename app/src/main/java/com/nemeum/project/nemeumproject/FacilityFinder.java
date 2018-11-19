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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class FacilityFinder extends AppCompatActivity {

    int[] facilityPicture = {R.drawable.stadium, R.drawable.stadium, R.drawable.stadium, R.drawable.stadium, R.drawable.stadium};
    String[] titleText = {"Lleida Soccer Field I", "Lleida Soccer Field II", "Lleida Soccer Field III", "Lleida Soccer Field IV", "Lleida Soccer Field V"};
    int[] facilityPoints = {1, 2, 3, 4, 5};
    String[] FacilityDirection = {"Address: Rambla Ferran no. 24", "Address: Rambla Ferran no. 24", "Address: Rambla Ferran no. 24", "Address: Rambla Ferran no. 24", "Address: Rambla Ferran no. 24"};
    String[] facilityOpenHours = {"Open - Closes: 09:00 - 23:00", "Open - Closes: 09:00 - 23:00", "Open - Closes: 09:00 - 23:00", "Open - Closes: 09:00 - 23:00", "Open - Closes: 09:00 - 23:00"};
    Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_finder);

        appContext = getApplicationContext();
        ListView resultList = findViewById(R.id.facilitiesList);

        CustomAdapter customResult = new CustomAdapter();

        resultList.setAdapter(customResult);

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
                        Intent intentSettings = new Intent(appContext, SettingsActivity.class);
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

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 5;
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

            convertView = getLayoutInflater().inflate(R.layout.facility_result_layout, null);

            ImageView facilityImg = convertView.findViewById(R.id.facilityResultImg);
            Button bookBtn = convertView.findViewById(R.id.bookFacilityResult);
            Button scheduleBtn = convertView.findViewById(R.id.scenarioFacilityResult);
            TextView facilityTitle = convertView.findViewById(R.id.facilityResultTitleText);
            RatingBar facilityRating = convertView.findViewById(R.id.starFacilityRating);
            TextView facilityAddress = convertView.findViewById(R.id.facilityResultPlaceText);
            TextView facilityHours = convertView.findViewById(R.id.facilityResultHoursText);
            TextView facilityDescription = convertView.findViewById(R.id.facilityResultDescriptionText);

            facilityImg.setImageResource(facilityPicture[position]);
            facilityTitle.setText(titleText[position]);
            facilityRating.setRating(facilityPoints[position]);
            facilityAddress.setText(FacilityDirection[position]);
            facilityHours.setText(facilityOpenHours[position]);
            facilityDescription.setText(R.string.findFacilityDescr);

            return convertView;
        }
    }
}
