package com.example.thatnarong.project;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Date;

/**
 * Created by jame on 9/5/2017 AD.
 */

public class EventModel{
    int event_id;
    String date;
    String venue;
    String location;
    Double event_latitude;
    Double event_longitude;




    public EventModel(String date, int event_id, String venue,
                      String location, Double event_latitude, Double event_longitude) {
        super();

        this.event_id = event_id;
        this.date = date;
        this.venue = venue;
        this.location = location;
        this.event_latitude = event_latitude;
        this.event_longitude = event_longitude;

    }




    public int getevent_id() {
        return event_id;
    }
    public void setevent_id(int event_id) {

        this.event_id = event_id;
    }

    public String getdate() {
        return date;
    }
    public void setdate(String date) {

        this.date = date;
    }

    public String getvenue(){return venue;}
    public void  setvenue(String venue){
        this.venue = venue;
    }


    public String getlocation(){return location;}
    public void  setlocation(String location){

        this.location = location;
    }

    public Double getevent_latitude(){return event_latitude;}
    public void  setevent_latitude(Double event_latitude){

        this.event_latitude = event_latitude;
    }

    public Double getevent_longitude(){return event_longitude;}
    public void  setevent_longitude(Double event_longitude){
        this.event_longitude = event_longitude;
    }



}
