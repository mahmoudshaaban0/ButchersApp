package com.mahmoudshaaban.butchers.pojo;

public class Clients {

    private String ConfirmPassword, Email, Password, Username, image, Phone , Statues;

    public Clients() {
    }

    public Clients(String confirmPassword, String email, String password, String username, String image, String phone, String statues) {
        ConfirmPassword = confirmPassword;
        Email = email;
        Password = password;
        Username = username;
        this.image = image;
        Phone = phone;
        Statues = statues;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public String getUsername() {
        return Username;
    }

    public String getImage() {
        return image;
    }

    public String getPhone() {
        return Phone;
    }

    public String getStatues() {
        return Statues;
    }
}