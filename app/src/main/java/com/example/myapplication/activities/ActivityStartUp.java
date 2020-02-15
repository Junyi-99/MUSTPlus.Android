package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.myapplication.DBHelper;
import com.example.myapplication.models.ModelResponseLogin;
import com.example.myapplication.models.ModelTimetable;
import com.example.myapplication.utils.API.API;

public class ActivityStartUp extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        API api = new API(getApplicationContext(), false);
        ModelTimetable timetable = api.timetable();
        if ((api.loginRecord() == null && !ActivityWelcome.active) || timetable == null) {
            Intent intent = new Intent(this, ActivityWelcome.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, ActivityMain.class);
            startActivity(intent);
            finish();
        }
    }
}
