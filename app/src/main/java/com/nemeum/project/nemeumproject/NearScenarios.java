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
import android.widget.Spinner;
import android.widget.TextView;

public class NearScenarios extends AppCompatActivity {

    int[] scenarioPicture = {R.drawable.stadium, R.drawable.stadium, R.drawable.stadium};
    String[] scenarioName = {"Lleida Gym Center", "Soccer center of Lleida", "Boxing ring of Lleida"};
    String[] scenarioPrice = {"39€ / month", "14€ / month", "60€ / month"};
    String[] scenarioSubtitle = {"Scenario 1", "Scenario 2", "Scenario 3"};
    Context appContext;

    //MapView nearMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_scenarios);

        appContext = getApplicationContext();
        ListView resultList = findViewById(R.id.scenariosList);

        CustomAdapter customResult = new CustomAdapter();

        resultList.setAdapter(customResult);

        Spinner sportSpinner = findViewById(R.id.scenarioBySport);
        Spinner locationSpinner = findViewById(R.id.scenarioByCity);
        Spinner priceSpinner = findViewById(R.id.scenarioByPrice);

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

            convertView = getLayoutInflater().inflate(R.layout.scenario_result_layout, null);

            ImageView scenarioImg = convertView.findViewById(R.id.companyResImg);
            TextView scenarioValue = convertView.findViewById(R.id.companyScenarioPrice);
            Button bookBtn = convertView.findViewById(R.id.bookCompany);
            TextView scenarioTitle = convertView.findViewById(R.id.companyResTitle);
            TextView scenarioTitleDescr = convertView.findViewById(R.id.companyResScenarioText);
            TextView scenarioDescription = convertView.findViewById(R.id.companyResDescription);

            scenarioImg.setImageResource(scenarioPicture[position]);
            scenarioValue.setText(scenarioPrice[position]);
            scenarioTitle.setText(scenarioName[position]);
            scenarioTitleDescr.setText(scenarioSubtitle[position]);
            scenarioDescription.setText(R.string.scenarioFindDescr);

            return convertView;
        }
    }
}
