package com.example.thatnarong.project;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jame on 9/4/2017 AD.
 */

public class EventList extends BaseAdapter  {


    private Activity context;
    ArrayList<EventModel> events;

    public EventList(Activity context, ArrayList<EventModel> events) {
        //   super(context, R.layout.row_item, countries);
        this.context = context;
        this.events = events;

    }
    public static class ViewHolder
    {
        TextView txtdate;
        TextView txtvenue;
        TextView txtlocation;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;

        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder vh;
        ViewHolder holder;
        if(convertView==null) {
            //MainActivity
            vh=new ViewHolder();
            row = inflater.inflate(R.layout.event_item, null, true);
            vh.txtdate = (TextView) row.findViewById(R.id.eventDateTextView);
            vh.txtvenue = (TextView) row.findViewById(R.id.venueEventTextView);
            vh.txtlocation = (TextView) row.findViewById(R.id.locationEventTextView);


            row.setTag(vh);
        }
        else {
            vh = (EventList.ViewHolder) convertView.getTag();



        }
        ;
        vh.txtdate.setText(events.get(position).getdate());
        vh.txtvenue.setText(""+events.get(position).getvenue());
        vh.txtlocation.setText(events.get(position).getlocation());
        return row;
    }
    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        return position;
    }

    public int getCount() {

        if(events.size()<=0)
            return 1;
        return events.size();
    }
}
