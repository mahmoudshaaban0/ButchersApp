package com.mahmoudshaaban.butchers.pojo;

import com.google.firebase.database.PropertyName;

public class Users {

 private String name , Statues , image , id , checkonline , Birthday;

    public Users() {
    }


    public Users(String name, String statues, String image, String id, String checkonline, String birthday) {
        this.name = name;
        Statues = statues;
        this.image = image;
        this.id = id;
        this.checkonline = checkonline;
        Birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatues() {
        return Statues;
    }

    public void setStatues(String statues) {
        Statues = statues;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCheckonline() {
        return checkonline;
    }

    public void setCheckonline(String checkonline) {
        this.checkonline = checkonline;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }
}
