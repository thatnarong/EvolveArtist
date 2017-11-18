package com.example.thatnarong.project;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Thatnarong on 6/28/2017.
 */

public class CoordinatorFragment extends Fragment implements
        DatePickerDialog.OnDateSetListener, View.OnClickListener {


    private String path = "http://192.168.1.18/app/postEvent.php";
    ArrayList<EventModel> events=new ArrayList<EventModel>();
    int id;


    String convert;
    String converti;
    private  static final String TAG = "Calender";
    CompactCalendarView compactCalendarView;
    Toolbar toolbar;
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat
            ("dd-M-yyyy hh:mm:ss a", Locale.getDefault());
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat
            ("MMMM- yyyy", Locale.getDefault());


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.coordinator_layout, container, false);

        events.clear();

        Intent intent = getActivity().getIntent();
        int message = intent.getIntExtra("EXTRA_MESSAGE",0);
        convert = String.valueOf(message);
        int messages = intent.getIntExtra("EXTRA_MESSAGE",0);
        converti = String.valueOf(messages);

        final List<String> mutableBookings = new ArrayList<>();
        final ListView bookingsListView = (ListView) rootView.findViewById(R.id.event);
        toolbar = (Toolbar) rootView.findViewById(R.id.eventDateTextView);

        final ArrayAdapter adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, mutableBookings);
        bookingsListView.setAdapter(adapter);

        compactCalendarView = (CompactCalendarView) rootView.findViewById
                (R.id.compactcalendar_view) ;
        final ImageView imageArtist = (ImageView) rootView.findViewById(R.id.ImageArtist);


        StringRequest req = new StringRequest(Request.Method.POST,
                "http://192.168.1.18/app/postEvent.php",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jarr = new JSONArray(response);
                            for (int i = 0; i < jarr.length(); i++) {
                                JSONObject jsonobject = jarr.getJSONObject(i);

                                int eventId = jsonobject.getInt("event_ID");
                                final String date = jsonobject.getString("date");
                                String venue = jsonobject.getString("venue");
                                String location = jsonobject.getString("location");
                                final Double event_latitude = jsonobject.getDouble("event_latitude");
                                final Double event_longitude = jsonobject.getDouble("event_longitude");

                                String str = date + " 00:00:00.00";
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                Date datep = null;
                                try {
                                    datep = df.parse(str);
                                } catch (ParseException e) {
                                    e.printStackTrace();

                                }

                                long epoch = datep.getTime();
                                Event ev1 = new Event(Color.GREEN, epoch, "DATE :" +'\b' + date +'\n' +
                                        "VENUE :" +'\b'+ venue + '\n'+
                                        "LOCATION :" +'\b' + location
                                       );



                                compactCalendarView.addEvent(ev1);
                                compactCalendarView.setListener(new CompactCalendarView.
                                        CompactCalendarViewListener() {
                                    @Override
                                    public void onDayClick(Date dateClicked) {
                                        List<Event> bookingsFromMap = compactCalendarView
                                                .getEvents(dateClicked);
                                        Log.d(TAG, "inside onclick " + dateFormatForDisplaying
                                                .format(dateClicked));
                                        if (bookingsFromMap != null) {
                                            Log.d(TAG, bookingsFromMap.toString());
                                            mutableBookings.clear();
                                            for (Event booking : bookingsFromMap) {
                                                mutableBookings.add((String) booking.getData());

                                            }
                                            adapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onMonthScroll(Date firstDayOfNewMonth) {
                                        toolbar.setTitle(dateFormatMonth.
                                                format(firstDayOfNewMonth));

                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("idartist",convert);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(req);
        return rootView;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
    }

    @Override
    public void onClick(View view) {
    }

}



