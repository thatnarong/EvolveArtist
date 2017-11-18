package com.example.thatnarong.project;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsEvent extends FragmentActivity implements OnMapReadyCallback {
    ArrayList<EventModel> events=new ArrayList<EventModel>();
    private GoogleMap mMap;
    String convert;
    String idForEvent;
    Double lat;
    String idArtist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_event);

        Intent intent = getIntent();
        int message = intent.getIntExtra("EXTRA_MESSAGE",0);
        convert = String.valueOf(message);
        int messag = intent.getIntExtra("EXTRA_MESSAGE",0);
        idForEvent = String.valueOf(messag);
        String getIdArtist = intent.getStringExtra("getIdArtist");

        setLatLongByArtist(getIdArtist);

//        Bundle bundle = this.getIntent().getExtras();
//        double lat = bundle.getDouble("lat");
//        double lon = bundle.getDouble("lon");
//        Double lat = intent.getDoubleExtra("lat",0.0);
//        Log.d("ASL:DKKLAJSD",lat.toString());
//        Double lon = intent.getDoubleExtra("lon",0.0);
//        Log.d("ASL:DKKLAJSD",lon.toString());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    public void setLatLongByArtist(String idartist){
        this.idArtist = idartist;
    }
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        final Marker[] myMarker = new Marker[1];
        final LatLng[] sydney = new LatLng[1];
        StringRequest req = new StringRequest(Request.Method.POST,
                "http://192.168.1.18/app/postEvent.php",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ALKSDKLASKLDJKLAJSDKL",response);
                        try {
                            JSONArray jarr = new JSONArray(response);
                            for (int i = 0; i < jarr.length(); i++) {
                                JSONObject jsonobject = jarr.getJSONObject(i);

                                int eventId = jsonobject.getInt("event_ID");
                                String date = jsonobject.getString("date");
                                String venue = jsonobject.getString("venue");
                                String location = jsonobject.getString("location");
                                Double event_latitude = jsonobject.getDouble("event_latitude");
                                Double event_longitude = jsonobject.getDouble("event_longitude");

                                EventModel eventModel = new EventModel(date,
                                        eventId, venue,location,event_latitude,event_longitude);
                                events.add(eventModel);

                                sydney[0] = new LatLng(event_latitude, event_longitude);
                                LatLng latLng = new LatLng(event_latitude, event_longitude);

                                myMarker[0] = mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(venue)
                                        .snippet(location)
                                );


                            }
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney[0], 6));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapsEvent.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("idartist",idArtist);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(MapsEvent.this.getApplicationContext());
        requestQueue.add(req);
    }




}
