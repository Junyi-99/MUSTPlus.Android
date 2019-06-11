package com.example.myapplication.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Timetable extends Response {

    private List<TimetableCell> timetable = new ArrayList<TimetableCell>();

    public List<TimetableCell> getTimetable() {
        return timetable;
    }

    public void setTimetable(List<TimetableCell> timetable) {
        this.timetable = timetable;
    }
}
