package com.nemeum.project.nemeumproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import models.Event;

public class EventFinder extends AppCompatActivity {

    List<Event> listEvent = new ArrayList<>();
    List<Event> filteredEvent = new ArrayList<>();
    List<Button> listMonths= new ArrayList<>();
    TextView yearFilter;
    String[] monthsArray;
    Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getEvents();

        setContentView(R.layout.activity_event_finder);

        appContext = getApplicationContext();
        final ImageView backYearBtn = findViewById(R.id.backYear);
        ImageView nextYearBtn = findViewById(R.id.nextYear);
        yearFilter = findViewById(R.id.yearText);
        final ListView resultList = findViewById(R.id.eventsList);
        final GridLayout resultFilter = findViewById(R.id.monthSelect);
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(dpToPx(appContext, 160), dpToPx(appContext, 20));

        monthsArray = getResources().getStringArray(R.array.monthsEvents);
        layout.setMargins(5,5,5,5);
        yearFilter.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        backYearBtn.setVisibility(View.INVISIBLE);
        backYearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String year = yearFilter.getText().toString();
                yearFilter.setText(String.valueOf(Integer.parseInt(year) - 1));
                if(yearFilter.getText().equals(String.valueOf(Calendar.getInstance().get(Calendar.YEAR))))
                    backYearBtn.setVisibility(View.INVISIBLE);
                resetTexts();
                resetColors();
            }
        });
        nextYearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String year = yearFilter.getText().toString();
                yearFilter.setText(String.valueOf(Integer.parseInt(year) + 1));
                backYearBtn.setVisibility(View.VISIBLE);
                resetTexts();
                resetColors();
            }
        });

        CustomAdapter customResult = new CustomAdapter();

        for(int i = 0; i < 12; i++){
            final Button btnMonth = new Button(appContext);
            btnMonth.setAllCaps(false);
            btnMonth.setLayoutParams(layout);
            btnMonth.setBackground(getResources().getDrawable(R.drawable.months_rounded));
            btnMonth.setTextColor(getResources().getColor(R.color.colorWhite));
            btnMonth.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            btnMonth.setTextAppearance(Typeface.BOLD);
            btnMonth.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
            btnMonth.setId(i + 1);
            btnMonth.setText(Arrays.asList(monthsArray).get(i) + " " + yearFilter.getText());

            btnMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetColors();
                    btnMonth.setBackground(getResources().getDrawable(R.drawable.selected_month));
                    btnMonth.setTextColor(getResources().getColor(R.color.colorFont));
                    resultList.clearChoices();
                    setFilteredEvents(btnMonth.getId());
                    resultList.setAdapter(new CustomAdapter());
                }
            });
            listMonths.add(btnMonth);
            resultFilter.addView(listMonths.get(i));
        }

        ArrayAdapter<Button> filterResult = new ArrayAdapter<Button>(appContext, R.layout.support_simple_spinner_dropdown_item, listMonths);

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

    public void setFilteredEvents(int month){
        filteredEvent.clear();
        for(int i = 0; i < listEvent.size(); i++){
            if((listEvent.get(i).getDateEvent().getMonth() + 1) == month){
                if((listEvent.get(i).getDateEvent().getYear() + 1900) == Integer.parseInt(yearFilter.getText().toString())){
                    filteredEvent.add(listEvent.get(i));
                }
            }
        }
    }

    private void resetColors(){
        for(int i = 0; i < 12; i++) {
            listMonths.get(i).setBackground(getResources().getDrawable(R.drawable.months_rounded));
            listMonths.get(i).setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    private void resetTexts(){
        for(int i = 0; i < 12; i++) {
            listMonths.get(i).setText(Arrays.asList(monthsArray).get(i) + " " + yearFilter.getText());
        }
    }

    public void getBack(View view) {
        finish();
    }

    private void getEvents(){
        new Thread(new Runnable() {

            int numResults = 0;
            BufferedReader in;
            String data = null;
            String line;
            JSONArray parserList;
            JSONObject parser;

            public void run() {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(getResources().getString(R.string.urlDB) + getResources().getString(R.string.eventDB) + getResources().getString(R.string.listDB));

                try {
                    HttpResponse response = httpclient.execute(httpget);
                    in = new BufferedReader(new InputStreamReader(
                            response.getEntity().getContent()));

                    while((line = in.readLine()) != null)
                        data += line;

                    data = data.replaceFirst("null", "");
                    parserList = new JSONArray(data);

                    while(numResults < parserList.length()) {
                        parser = (JSONObject) parserList.get(numResults);
                        Event event = new Event();
                        event.setIdEvent(parser.getInt(getResources().getString(R.string.eventIdJson)));
                        event.setIdSport(parser.getInt(getResources().getString(R.string.eventSportIdJson)));
                        //event.setIdCompany(parser.getInt(getResources().getString(R.string.eventCompanyIdJson)));
                        //event.setIdUser(parser.getInt(getResources().getString(R.string.eventUserIdJson)));
                        //event.setIdTrainer(parser.getInt(getResources().getString(R.string.eventTrainerIdJson)));
                        event.setIndoor(parser.getBoolean(getResources().getString(R.string.eventIndoorJson)));
                        event.setCapacity(parser.getInt(getResources().getString(R.string.eventCapacityJson)));
                        event.setPrice(parser.getDouble(getResources().getString(R.string.eventPriceJson)));
                        event.setCity(parser.getString(getResources().getString(R.string.eventCityJson)));
                        event.setAddress(parser.getString(getResources().getString(R.string.eventAddressJson)));
                        event.setPostalCode(parser.getString(getResources().getString(R.string.eventPostalCodeJson)));
                        event.setPhone(parser.getInt(getResources().getString(R.string.eventPhoneJson)));
                        String dateStr = parser.getString(getResources().getString(R.string.eventDateEventJson));
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        event.setDateEvent(sdf.parse(dateStr));
                        event.setDescription(parser.getString(getResources().getString(R.string.eventDescriptionJson)));
                        event.setImage(parser.getString(getResources().getString(R.string.eventImageJson)));
                        event.setTitle(parser.getString(getResources().getString(R.string.eventTitleJson)));
                        numResults++;
                        listEvent.add(event);
                    }
                    filteredEvent.addAll(listEvent);
                } catch (IOException e) {
                    EventFinder.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(appContext, getResources().getString(R.string.noconnection), Toast.LENGTH_LONG).show();
                        }
                    });
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static int dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics));
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return filteredEvent.size();
        }

        @Override
        public Object getItem(int position) {
            return filteredEvent.get(position);
        }

        @Override
        public long getItemId(int position) {
            return filteredEvent.get(position).getIdEvent();
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

            if(!filteredEvent.get(position).getImage().equals("null"))
                Picasso.get().load(filteredEvent.get(position).getImage()).into(eventImg);
            else
                Picasso.get().load(R.drawable.scenario_nophoto).into(eventImg);

            //if(eventRecommend[position])
            //    eventRecommendedTitle.setVisibility(View.VISIBLE);

            eventTitle.setText(filteredEvent.get(position).getTitle());
            eventTitle.setTypeface(null, Typeface.BOLD);
            eventDescription.setText(getResources().getString(R.string.eventResDescr) + " " + filteredEvent.get(position).getDescription());

            String exhibitFormat = getResources().getString(R.string.date_format);
            SimpleDateFormat sdf = new SimpleDateFormat(exhibitFormat, Locale.getDefault());
            eventDate.setText(String.format(getResources().getString(R.string.eventResDate) + " " + sdf.format(filteredEvent.get(position).getDateEvent())));
            eventAddress.setText(getResources().getString(R.string.eventResAddress) + " " + filteredEvent.get(position).getAddress() + " - " + filteredEvent.get(position).getCity());

            return convertView;
        }
    }
}
