package com.example.thatnarong.project;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import static com.example.thatnarong.project.R.id.imageView;

public class Main2Activity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ViewPager viewPager;

    TextView textDetails ;
    TextView textCamp;
    TextView textStyle;
    TextView textmenbers1,textmenbers2,textmenbers3,textmenbers4,textmenbers5,textmenbers6;
    ImageView imageArtist;
    Button followBtn;
    String convert;
    String name = "";
    String camp ="";
    String style ="";
    String artist_members1 ="",artist_members2 ="",artist_members3 ="",artist_members4 ="",
            artist_members5 ="",artist_members6 ="";
    Double lat,lon = 0.0;
    boolean mFollowing;
    private boolean isButtonClicked = false;
    String idForEvent;
    String idartist;
    String isActive;
    Context context;
    String idArtist;

    ArrayList<EventModel> events=new ArrayList<EventModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        int message = intent.getIntExtra("EXTRA_MESSAGE",0);
        convert = String.valueOf(message);

        int messag = intent.getIntExtra("EXTRA_MESSAGE",0);
        idForEvent = String.valueOf(messag);

        int messa = intent.getIntExtra("EXTRA_MESSAGE",0);
        idartist = String.valueOf(messa);


//        String getArtistId = intent.getStringExtra("userArtist");
//        idartist = String.valueOf(getArtistId);


        events.clear();
        query();
        queryfollow();



        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        viewPager = (ViewPager)findViewById(R.id.tab_viewpager);
        if (viewPager != null){
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FloatingLabelsFragment(), "Profile");
        //adapter.addFrag(new FABLayoutFragment(), "Event");
        adapter.addFrag(new CoordinatorFragment(), "Events");
        viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_me:
                Intent intent = new Intent(Main2Activity.this,
                        MapsEvent.class);
                intent.putExtra("getIdArtist", getIdArtist().toString());
                startActivity(intent);
                Log.d("asdsdsdsd",getIdArtist().toString());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maps,menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void query(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://192.168.1.18/app/post.php",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        followBtn = (Button) findViewById(R.id.follow);
                        textDetails = (TextView) findViewById(R.id.nameText);
                        textCamp = (TextView) findViewById(R.id.camp);
                        textStyle = (TextView) findViewById(R.id.style);
                        imageArtist = (ImageView) findViewById(R.id.ImageArtist);
                        textmenbers1 = (TextView) findViewById(R.id.textView25);
                        textmenbers2 = (TextView) findViewById(R.id.textView26);
                        textmenbers3 = (TextView) findViewById(R.id.textView27);
                        textmenbers4 = (TextView) findViewById(R.id.textView28);
                        textmenbers5 = (TextView) findViewById(R.id.textView23);
                        textmenbers6 = (TextView) findViewById(R.id.textView12);
                        try {
                            Log.d("response",response);
                            final JSONObject jsonObj = new JSONObject(response);
                            setIdArtist(jsonObj.getString("artist_id"));
                            name = jsonObj.getString("artist_name");
                            setTitle(name);
                            camp = jsonObj.getString("artist_music_camp");
                            style = jsonObj.getString("artist_music_style");
                            artist_members1= jsonObj.getString("artist_members1");
                            artist_members2= jsonObj.getString("artist_members2");
                            artist_members3= jsonObj.getString("artist_members3");
                            artist_members4= jsonObj.getString("artist_members4");
                            artist_members5= jsonObj.getString("artist_members5");
                            artist_members6= jsonObj.getString("artist_members6");
                            Glide.with(Main2Activity.this).load("http://192.168.1.18"+jsonObj.
                                    getString("artist_image")).into(imageArtist);
                            textDetails.setText(name);
                            textCamp.setText(camp);
                            textStyle.setText(style);
                            textmenbers1.setText(artist_members1);
                            textmenbers2.setText(artist_members2);
                            textmenbers3.setText(artist_members3);
                            textmenbers4.setText(artist_members4);
                            textmenbers5.setText(artist_members5);
                            textmenbers6.setText(artist_members6);

                            Drawable d1 = getResources().getDrawable(R.drawable.follows);
                            Drawable d2 = getResources().getDrawable(R.drawable.nufollow);
                            final TransitionDrawable followDrawable = new TransitionDrawable(new Drawable[] { d1, d2 });
                            final int transitionDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

                            followBtn.setBackground(followDrawable);
                            followBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(!mFollowing) {
                                     mFollowing = true;
                                     followDrawable.startTransition(transitionDuration);
                                        try {

                                            SharedPreferences prefs = getSharedPreferences("IdUser", Context.MODE_PRIVATE);
                                            String id = prefs.getString("userIdNow","3");
                                            saveFollow(id,jsonObj.getString("artist_id").toString());

                                            Intent intent = new Intent(Main2Activity.this,
                                                 Schedule.class);
                                         intent.putExtra("userId", id);
                                         intent.putExtra("userArtist", jsonObj.getString("artist_id").toString());
                                            startActivity(intent);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                 }else {
                                     mFollowing = false;
                                     followDrawable.reverseTransition(transitionDuration);

                                 }

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Main2Activity.this,error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",convert);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void setIdArtist(String id) {
        this.idArtist = id;
    }

    public String getIdArtist() {
        return idArtist;
    }

    public void queryfollow() {
        StringRequest request = new StringRequest(Request.Method.POST,
                "http://192.168.1.18/app/follow.php",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            final JSONObject jsonObj = new JSONObject(response);
                            String isActive2 = jsonObj.getString("active");
                            Log.d("activeactive",isActive2.toString());
                            if(isActive2!=null){
                                Drawable d2 = getResources().getDrawable(R.drawable.nufollow);
                                final TransitionDrawable follow = new
                                        TransitionDrawable(new Drawable[] { d2 });
                                followBtn.setBackground(follow);

                                followBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            String idartist = jsonObj.getString("idartist");
                                            String iduser = jsonObj.getString("iduser");
                                            unFollow(iduser,idartist);
                                            Intent intent = new Intent(Main2Activity.this,
                                                    Schedule.class);
                                            intent.putExtra("userId", iduser);
                                            intent.putExtra("userArtist", jsonObj.getString("idartist"));
                                            startActivity(intent);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
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
                        //Toast.makeText(Schedule.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences("IdUser", Context.MODE_PRIVATE);
                String id = prefs.getString("userIdNow","3");
                params.put("userId",id);
                params.put("artistId",idartist);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }



    public void saveFollow(final String id, final String artist_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://192.168.1.18/app/postFollow.php",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonObj = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(Schedule.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("userId",id);
                params.put("artistId",artist_id);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        requestQueue.start();

    }

    public void unFollow(final String id, final String artist_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://192.168.1.18/app/unFollow.php",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonObj = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(Schedule.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("userId",id);
                params.put("artistId",artist_id);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        requestQueue.start();

    }



}
