package com.nemeum.project.nemeumproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PostScenario extends AppCompatActivity {
    private EditText ScenarioTitle;
    private EditText ScenarioDesc;
    private EditText ScenarioCapacity;
    private EditText ScenarioPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_scenario);
        Button submit_scenario_btn = (Button)findViewById(R.id.submit_scenario_post);
        ScenarioTitle = (EditText)findViewById(R.id.edit_scenario_title);
        ScenarioDesc = (EditText)findViewById(R.id.edit_scenario_desc);
        ScenarioCapacity = (EditText)findViewById(R.id.scenario_capacity);
        ScenarioPrice = (EditText)findViewById(R.id.scenario_price);

        submit_scenario_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitNewScenario(ScenarioTitle.getText().toString(),ScenarioDesc.getText().toString(),ScenarioCapacity.getText().toString(),ScenarioPrice.getText().toString());
            }
        });
    }

    private void SubmitNewScenario(String title,String desc,String capacity,String price){

    }
}
