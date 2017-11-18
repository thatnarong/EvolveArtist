package com.example.thatnarong.project;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Thatnarong on 6/27/2017.
 */

public class CustomCountryList extends BaseAdapter {

    private Activity context;
    ArrayList<Country> countries;



    public CustomCountryList(Activity context, ArrayList<Country> countries) {

        this.context = context;
        this.countries=countries;

    }

    public static class ViewHolder
    {
        TextView textViewname;
        TextView textViewCamp;
        ImageView imageForList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder vh;

        if(convertView==null) {
            //MainActivity
            vh=new ViewHolder();
            row = inflater.inflate(R.layout.row_item, null, true);
            vh.textViewname = (TextView) row.findViewById(R.id.artistname);
            vh.textViewCamp = (TextView) row.findViewById(R.id.artistCamp);
            vh.imageForList = (ImageView) row.findViewById(R.id.imagelist);


            row.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();


        }
        vh.textViewCamp.setText(countries.get(position).getArtisCamp());
        vh.textViewname.setText(countries.get(position).getCountryName());
        Glide.with(context).load("http://192.168.1.18" + countries.get(position).
                getImageUrl()).into(vh.imageForList);
        return  row;
    }

    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        return position;
    }

    public int getCount() {

        if(countries.size()<=0)
            return 1;
        return countries.size();
    }

}
