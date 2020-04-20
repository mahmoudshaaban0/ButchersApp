package com.mahmoudshaaban.butchers.pojo;

public class Posts {
    private String Description , Image , Pid , UserImage , Username , publisher;

    public Posts() {
    }

    public Posts(String description, String image, String pid, String userImage, String username, String publisher) {
        Description = description;
        Image = image;
        Pid = pid;
        UserImage = userImage;
        Username = username;
        this.publisher = publisher;
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

    public String getPublisher() {
        return publisher;
    }
}
