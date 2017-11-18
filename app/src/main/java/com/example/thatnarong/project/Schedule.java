package com.example.thatnarong.project;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

public class Schedule extends AppCompatActivity  {


    String userId ;
    String artistId;
    ListView listView;
    ImageView imageView;
    Activity activity;
    ArrayList<Country> countries=new ArrayList<Country>();
    private String path = "http://192.168.1.18/app/getFollow.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        listView = (ListView) findViewById(R.id.listSc);

        Intent intent = getIntent();
        String getUserId = intent.getStringExtra("userId");
        String getArtistId = intent.getStringExtra("userArtist");
        userId = String.valueOf(getUserId);
        artistId = String.valueOf(getArtistId);

        activity = this;
        countries.clear();
        queryGetEventDate();
        query2();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }







    public void query2(){
        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST,
                "http://192.168.1.18/app/getFollow.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("test");
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject curr = jsonArray.getJSONObject(i);

                                int id = curr.getInt("artist_id");
                                String country = curr.getString("artist_name");
                                String artisCamp = curr.getString("artist_music_camp");
                                String imageUrl = curr.getString("artist_image");





                                Country countryObj = new Country(id, country, artisCamp , imageUrl);
                                countries.add(countryObj);

                            }
                        } catch (JSONException e) {
                            listView = null;
                            e.printStackTrace();
                        }

                        CustomCountryList customCountryList = new
                                CustomCountryList(activity, countries);

                        if(customCountryList.getCount() > 0){
                            TextView empty = (TextView) findViewById(android.R.id.empty);
                            listView.setEmptyView(empty);
                            listView.setAdapter(customCountryList);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view,
                                                        int position, long l) {
                                    Toast.makeText(getApplicationContext(), "You Selected " +
                                                    countries.get(position).getId() + " as Country",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Schedule.this, Main2Activity.class);
                                    intent.putExtra("EXTRA_MESSAGE", countries.get(position).getId());
                                    startActivity(intent);

                                }
                            });

                        }else{
                            listView.setAdapter(customCountryList);


                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Schedule.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences("IdUser", Context.MODE_PRIVATE);
                String id = prefs.getString("userIdNow","3");
                params.put("userId",id);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
        requestQueue.start();
    }





    public void queryGetEventDate(){
        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST,
                "http://192.168.1.18/app/getEventDate.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("getEventDateJson");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject curr = jsonArray.getJSONObject(i);

                        String id = curr.getString("artist_id");
                        String date = curr.getString("date");
                        String venue = curr.getString("venue");
                        String location = curr.getString("location");


                        SharedPreferences shared = getSharedPreferences("MyPref",
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = shared.edit();



                        editor.putString("DateArtist" + "_" + i, date + venue + location);
                        editor.putInt("DateArtist" +"_size", jsonArray.length());
                        editor.commit();
                        Log.d("DATEDATE",shared.getString("DateArtist" + "_" + i,
                                date + venue + location));



                    }
                } catch (JSONException e) {
                    listView = null;
                    e.printStackTrace();
                }



            }
        },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Schedule.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("userId","1");
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
        requestQueue.start();
    }




}
