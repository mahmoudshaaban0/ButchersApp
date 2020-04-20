package com.mahmoudshaaban.butchers.pojo;

public class Guests {

    public String name , email , password , confirmpassword , id;


    public Guests() {
    }

    public Guests(String name, String email, String password, String confirmpassword, String id) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmpassword = confirmpassword;
        this.id = id;
    }
}
