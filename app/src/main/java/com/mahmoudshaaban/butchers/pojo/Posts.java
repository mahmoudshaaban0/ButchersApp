package com.mahmoudshaaban.butchers.pojo;

public class Posts {
    private String Description , Image , Pid , UserImage , Username;

    public Posts() {
    }

    public Posts(String description, String image, String pid, String userImage, String username) {
        Description = description;
        Image = image;
        Pid = pid;
        UserImage = userImage;
        Username = username;
    }

    public String getDescription() {
        return Description;
    }

    public String getImage() {
        return Image;
    }

    public String getPid() {
        return Pid;
    }

    public String getUserImage() {
        return UserImage;
    }

    public String getUsername() {
        return Username;
    }
}
