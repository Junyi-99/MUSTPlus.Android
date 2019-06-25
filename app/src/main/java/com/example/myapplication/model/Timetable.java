package com.example.myapplication.model;


import java.util.ArrayList;
import java.util.List;

public class Timetable extends Response {

    private List<TimetableCell> timetable = new ArrayList<TimetableCell>();

    public List<TimetableCell> getTimetable() {
        return timetable;
    }

    public void setTimetable(List<TimetableCell> timetable) {
        this.timetable = timetable;
    }
}
