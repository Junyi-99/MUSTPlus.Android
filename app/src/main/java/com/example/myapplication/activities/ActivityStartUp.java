package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.myapplication.DBHelper;
import com.example.myapplication.models.ModelResponseLogin;

public class ActivityStartUp extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBHelper db = new DBHelper(getApplicationContext());
        ModelResponseLogin login = db.getLoginRecord();
        String timetableRaw = db.getAPIRecord(APICONSTANT.TIMETABLE);
        if ((login == null && !ActivityWelcome.active) || timetableRaw.isEmpty()) {
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
