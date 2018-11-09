package com.nemeum.project.nemeumproject;
import
android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class NearScenarios extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_scenarios);

        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> cities = new ArrayList<String>();
        String city;

        for(Locale loc : locale) {
            city = loc.getDisplayCountry();
            if(city.length() > 0 && !cities.contains(city)) {
                cities.add(city);
            }
        }
        Collections.sort(cities, String.CASE_INSENSITIVE_ORDER);

        SpinnerAdapter citiesAdapter = new ArrayAdapter(
                getBaseContext(),
                android.R.layout.simple_spinner_dropdown_item,
                cities);

        //ListView listView = (ListView) findViewById(R.id.listview);
        //listView.setAdapter(countriesAdapter);

        //listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //    @Override
        //    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //        Toast.makeText(getBaseContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
        //    }
        //});
    }
}
