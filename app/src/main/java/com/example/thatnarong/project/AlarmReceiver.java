package com.example.thatnarong.project;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jame on 10/23/2017 AD.
 */

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Intent notificationIntent = new Intent(context, NotificationActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NotificationActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent
                (0, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        SharedPreferences prefs = context.getSharedPreferences("MyPref", 0);
        int size = prefs.getInt("DateArtist" + "_size", 0);

        String array[] = new String[size];
        for (int i = 0; i < size; i++) {

            array[i] = prefs.getString("DateArtist" + "_" + i, null);
            Log.d("aaaaa", array[i]);


            Notification notification = builder
                    .setContentTitle("Event")
                    .setContentText(array[i])
                    .setTicker("New Message")
                    .setSmallIcon(R.drawable.ic_event)
//                    .addAction(R.drawable.ic_add, "ADD TO Calendar", pendingIntent)
//                    .addAction(R.drawable.ic_cancel, "CANCEL", pendingIntent)
//                    .addAction(R.drawable.ic_event,"EVENT",pendingIntent)
                    .setContentIntent(pendingIntent).build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);


        }


    }
}




