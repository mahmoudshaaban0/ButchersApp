package com.mahmoudshaaban.butchers.pojo;

public class RetreiveGuests {

    private String Phone , Statues , confirmpassword , email , image ,name , password ;

    public RetreiveGuests() {
    }

    public RetreiveGuests(String phone, String statues, String confirmpassword, String email, String image, String name, String password) {
        Phone = phone;
        Statues = statues;
        this.confirmpassword = confirmpassword;
        this.email = email;
        this.image = image;
        this.name = name;
        this.password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public String getStatues() {
        return Statues;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
