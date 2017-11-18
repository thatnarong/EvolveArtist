package com.example.thatnarong.project;

import java.util.ArrayList;

/**
 * Created by Thatnarong on 6/27/2017.
 */

public class Country {
    int id;
    String countryName;
    String artisCamp;
    String imageUrl;
    String sortLetters;

    public Country(int i, String countryName,String artisCamp,String imageUrl) {
        super();
        this.id = i;
        this.countryName = countryName;
        this.artisCamp = artisCamp;
        this.imageUrl = imageUrl;


    }

    public Country(MainActivity mainActivity, ArrayList<Country> countries) {

    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getArtisCamp(){return artisCamp;}
    public void  setArtisCamp(String artisCamp){
        this.artisCamp = artisCamp;
    }

    public  String getImageUrl(){return imageUrl;}
    public void  setImageUrl(String imageUrl) { this.imageUrl=imageUrl; }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

}
