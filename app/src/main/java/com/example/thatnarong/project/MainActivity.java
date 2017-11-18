package com.example.thatnarong.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.example.thatnarong.project.Search.HttpServiceClass;
import com.example.thatnarong.project.Search.ListAdapter;
import com.example.thatnarong.project.Search.Subject;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout DrawerLayout;
    private ActionBarDrawerToggle Toggle;
    ImageView imageView;
    int idid;
    Activity activity;
    ArrayList<Country> countries=new ArrayList<Country>();



    ListView listView;
    ArrayList<Subject> SubjectList = new ArrayList<Subject>();
    String HttpURL = "http://192.168.1.18/app/project.php";
    ListAdapter listAdapter;
    ProgressBar progressBar ;


    ArrayList<HashMap<String, String>> arraylist;
    static String RANK = "rank";

    //Direct Web services URL
    private String path = "http://192.168.1.18/app/project.php";
    String ServerURL = "http://192.168.1.18/app/project.php";

    EditTextWithDel mEtSearchName;
    RoundedImageView mRoundedImageView;
    CircleImageView circleImageView;

    private ShareDialog shareDialog;
    JSONObject response, profile_pic_data, profile_pic_url;
    //search
    List<String> listString = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;

        listView = (ListView) findViewById(android.R.id.list);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        mEtSearchName = (EditTextWithDel)findViewById(R.id.et_search);

        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Subject ListViewClickData = (Subject)parent.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("EXTRA_MESSAGE", ListViewClickData.getId());
                startActivity(intent);

            }
        });


        //                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> adapterView, View view,
//                                                    int position, long l) {
//                                Toast.makeText(getApplicationContext(), "You Selected " +
//                                                countries.get(position).getId() + " as Country",
//                                        Toast.LENGTH_SHORT).show();
//                                idid = countries.get(position).getId();
//                                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
//                                intent.putExtra("EXTRA_MESSAGE", countries.get(position).getId());
//                                startActivity(intent);
//
//                            }
//                        });




        mEtSearchName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence stringVar, int start, int before, int count) {

                listAdapter.getFilter().filter(stringVar.toString());
            }
        });

        new ParseJSonDataClass(this).execute();

        countries.clear();
//        getWebServiceResponseData();
        FacebookSdk.sdkInitialize(this);
        DrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        Toggle = new ActionBarDrawerToggle(this,DrawerLayout, R.string.open, R.string.close);
        DrawerLayout.addDrawerListener(Toggle);
        Toggle.syncState();

        mRoundedImageView = (RoundedImageView) findViewById(R.id.imagelist);



        Intent intent = getIntent();
        String jsondata = intent.getStringExtra("userProfile");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        ImageView user_picture = (ImageView) header.findViewById(R.id.imageViewfb);
        TextView nameView = (TextView) header.findViewById(R.id.textView);
        TextView emailView = (TextView) header.findViewById(R.id.email);

        try {
            JSONObject response = new JSONObject(jsondata);
            nameView.setText(response.get("name").toString());
            emailView.setText(response.get("email").toString());
            profile_pic_data = new JSONObject(response.get("picture").toString());
            profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
            Picasso.with(this).load(profile_pic_url.getString("url")).into(user_picture);




            checkLogin(response.get("id").toString(),response.get("name").toString(),response.get("email").toString());
            checkUserNow(response.get("id").toString());
        } catch(Exception e){
            e.printStackTrace();
        }
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();


        if(AccessToken.getCurrentAccessToken()== null){
            goLoginScreen();
        }

    }

    private class ParseJSonDataClass extends AsyncTask<Void, Void, Void> {
        public Context context;
        String FinalJSonResult;

        public ParseJSonDataClass(Context context) {

            this.context = context;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpServiceClass httpServiceClass = new HttpServiceClass(HttpURL);

            try {
                httpServiceClass.ExecutePostRequest();

                if (httpServiceClass.getResponseCode() == 200) {

                    FinalJSonResult = httpServiceClass.getResponse();

                    if (FinalJSonResult != null) {

                        JSONArray jsonArray = null;
                        try {

                            jsonArray = new JSONArray(FinalJSonResult);

                            JSONObject jsonObject;

                            Subject subject;

                            SubjectList = new ArrayList<Subject>();

                            for (int i = 0; i < jsonArray.length(); i++) {

                                jsonObject = jsonArray.getJSONObject(i);
                                int id = jsonObject.getInt("artist_id");

                                String country = jsonObject.getString("artist_name").toString();

                                String artisCamp = jsonObject.getString("artist_music_camp").toString();

                                String imageUrl = jsonObject.getString("artist_image");


                                subject = new Subject(id,country, artisCamp,imageUrl);

                                SubjectList.add(subject);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } else {

                    Toast.makeText(context, httpServiceClass.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.INVISIBLE);
            listAdapter = new ListAdapter(MainActivity.this, R.layout.row_item, SubjectList);
            listView.setAdapter(listAdapter);
        }
    }


    public void checkLogin(final String id, final String name, final String email){
        Log.d("idFb2",id.toString()+name.toString()+email.toString());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://192.168.1.18/app/registerFb.php",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",id.toString());
                params.put("name",name.toString());
                params.put("email",email.toString());
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void checkUserNow(final String id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://192.168.1.18/app/checkUserNow.php",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("AKSJDKJASD2",response);
                        try {
                            final JSONObject jsonObj = new JSONObject(response);
                            String idUser = jsonObj.getString("id");
                            Log.d("AKSJDKJASD",idUser);
                            SharedPreferences shared2 = getSharedPreferences("IdUser",
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = shared2.edit();
                            editor.putString("userIdNow",idUser);
                            editor.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",id.toString());
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void goLoginScreen(){
        Intent intent = new Intent(this,Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logout(MenuItem item){
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerlayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    //Menu
        @Override
       public boolean onCreateOptionsMenu(Menu menu) {
         //getMenuInflater().inflate(R.menu.menu_maps,menu);

        return super.onCreateOptionsMenu(menu);

      }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       if(Toggle.onOptionsItemSelected(item)) {
           return  true;
       }
        return super.onOptionsItemSelected(item);

    }
//    //listView
//    protected Void getWebServiceResponseData() {
//        mRoundedImageView = (RoundedImageView) findViewById(R.id.imagelist);
//        JsonArrayRequest req = new JsonArrayRequest(path,
//                new Response.Listener<JSONArray>() {
//
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d(TAG, response.toString());
//
//                        try {
//                            for (int i = 0; i < response.length(); i++) {
//                                JSONObject jsonobject = response.getJSONObject(i);
//                                int id = jsonobject.getInt("artist_id");
//                                String country = jsonobject.getString("artist_name");
//                                String artisCamp = jsonobject.getString("artist_music_camp");
//                                String imageUrl = jsonobject.getString("artist_image");
//
//                               // JSONObject jsonObj = new JSONObject(response);
//
//                                Log.d(TAG, "artist_id:" + id);
//                                Log.d(TAG, "artist_name:" + country);
//
//
//                                Country countryObj = new Country(id, country, artisCamp ,
//                                        imageUrl);
//                                countries.add(countryObj);
//
//
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(getApplicationContext(),
//                                    "Error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG).show();
//                        }
//                        CustomCountryList customCountryList = new CustomCountryList(activity, countries);
//                        listView.setAdapter(customCountryList);
//                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> adapterView, View view,
//                                                    int position, long l) {
//                                Toast.makeText(getApplicationContext(), "You Selected " +
//                                                countries.get(position).getId() + " as Country",
//                                        Toast.LENGTH_SHORT).show();
//                                idid = countries.get(position).getId();
//                                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
//                                intent.putExtra("EXTRA_MESSAGE", countries.get(position).getId());
//                                startActivity(intent);
//
//                            }
//                        });
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
//            }
//
//        });
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(req);
//        return null;
//    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.my_schedule) {
            Intent intent = new Intent(this,Schedule.class);
             this.startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawer.closeDrawer(GravityCompat.START);


        return false;
    }

    //Search
    private void initViews() {
        listView = (ListView) findViewById(android.R.id.list);
        mEtSearchName = (EditTextWithDel) findViewById(R.id.et_search);


        mEtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }



}
