package com.mahmoudshaaban.butchers.pojo;

public class Users {

 private String Username , Statues , image;

    public Users() {
    }

    public Users(String username, String statues, String image) {
        Username = username;
        Statues = statues;
        this.image = image;
    }

    public String getUsername() {
        return Username;
    }

    public String getStatues() {
        return Statues;
    }

    public String getImage() {
        return image;
    }
}
