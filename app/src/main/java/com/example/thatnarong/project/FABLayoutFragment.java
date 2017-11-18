package com.example.thatnarong.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thatnarong on 6/28/2017.
 */

public class FABLayoutFragment extends Fragment  {

    ArrayList<EventModel> events=new ArrayList<EventModel>();
    ListView listView;
    String convert;
    int idi;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.event_item, container, false);
        listView = (ListView) rootView.findViewById(android.R.id.cut);
        events.clear();

        Intent intent = getActivity().getIntent();
        int message = intent.getIntExtra("EXTRA_MESSAGE",0);
        convert = String.valueOf(message);

        query();

        return rootView;
    }

    public void query(){
        StringRequest req = new StringRequest(Request.Method.POST,
                "http://192.168.1.18/app/postEvent.php",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {




                        try {
                            JSONArray jarr = new JSONArray(response);
                            for (int i = 0; i < jarr.length(); i++) {
                                JSONObject jsonobject = jarr.getJSONObject(i);

                                //final JSONObject jsonObj = new JSONObject(response);

                                int eventId = jsonobject.getInt("event_ID");
                                String date = jsonobject.getString("date");
                                String venue = jsonobject.getString("venue");
                                String location = jsonobject.getString("location");
                                Double event_latitude = jsonobject.getDouble("event_latitude");
                                Double event_longitude = jsonobject.getDouble("event_longitude");



                                EventModel eventModel = new EventModel(date,
                                        eventId, venue,location,event_latitude,event_longitude);
                                events.add(eventModel);

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        EventList evenlist = new
                                EventList(getActivity(), events);
                        listView.setAdapter(evenlist);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view,
                                                    int position, long l) {

                                Toast.makeText(getActivity(), "You Selected " +
                                                events.get(position).getevent_id() + " as Country",
                                        Toast.LENGTH_SHORT).show();

                                idi = events.get(position).getevent_id();
                                Intent intent = new Intent(getActivity(), MapsEvent.class);
                                intent.putExtra("EXTRA_MESSAGE", events.get(position).getevent_id());
                                startActivity(intent);
                            }
                        });

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
                params.put("idevent",convert);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(req);
    }



}
