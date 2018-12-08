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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class EventFinder extends AppCompatActivity {

    int[] eventPicture = {R.drawable.swimming_silhouette, R.drawable.group_of_men_running, R.drawable.bicycle_rider};
    boolean[] eventRecommend = {false, true, false};
    String[] titleText = {"Lleida Swimming Competition", "Lleida Half Marathon", "Lleida Cycling Competition"};
    String[] eventDescr = {"Description: Swimming competition for child", "Description: Half marathon for 18-55 y.o.", "Description: Cycling competition"};
    String[] eventDirection = {"Place: Lleida Swimming Center", "Place: Lleida Running Track", "Place: Lleida Running Track"};
    String[] eventExecDate = {"Date: 1 December 2018", "Date: 16 December 2018", "Date: 21 December 2018"};
    Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_finder);

        appContext = getApplicationContext();
        ListView resultList = findViewById(R.id.eventsList);

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

            convertView = getLayoutInflater().inflate(R.layout.event_result_layout, null);

            ImageView eventImg = convertView.findViewById(R.id.eventResultImg);
            TextView eventRecommendedTitle = convertView.findViewById(R.id.eventRecommendedText);
            TextView eventTitle = convertView.findViewById(R.id.eventResultTitleText);
            TextView eventDescription = convertView.findViewById(R.id.eventResultDescriptionText);
            TextView eventDate = convertView.findViewById(R.id.eventResultDateText);
            TextView eventAddress = convertView.findViewById(R.id.eventResultPlaceText);

            eventImg.setImageResource(eventPicture[position]);

            if(eventRecommend[position])
                eventRecommendedTitle.setVisibility(View.VISIBLE);

            eventTitle.setText(titleText[position]);
            eventDescription.setText(eventDescr[position]);
            eventDate.setText(eventExecDate[position]);
            eventAddress.setText(eventDirection[position]);

            return convertView;
        }
    }
}
