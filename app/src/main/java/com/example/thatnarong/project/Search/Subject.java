package com.example.thatnarong.project.Search;

/**
 * Created by Juned on 2/1/2017.
 */

public class Subject {
    int id;
    String SubName = null;
    String SubFullForm = null;
    String imageUrl;


    public Subject(int id, String Sname, String SFullForm, String imageUrl) {

        super();
        this.id = id;
        this.SubName = Sname;

        this.SubFullForm = SFullForm;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubName() {

        return SubName;

    }

    public void setSubName(String code) {

        this.SubName = code;

    }

    public String getSubFullForm() {

        return SubFullForm;

    }

    public void setSubFullForm(String name) {

        this.SubFullForm = name;

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {

        return SubName + " " + SubFullForm;

    }

}
