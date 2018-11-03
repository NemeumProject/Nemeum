package com.nemeum.project.nemeumproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TrainerMock extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_mock);
    }

    public void homeOnclick(View v) {
        Intent intent = new Intent(TrainerMock.this, ActivityMainMock.class);
        startActivity(intent);
    }

}

