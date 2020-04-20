package com.mahmoudshaaban.butchers.pojo;

public class StoryRv {

    private String imageuri;
    private long timestart;
    private long timeend;
    private String storyid;
    private String  userid;


    public StoryRv() {
    }

    public StoryRv(String imageuri, long timestart, long timeend, String storyid, String userid) {
        this.imageuri = imageuri;
        this.timestart = timestart;
        this.timeend = timeend;
        this.storyid = storyid;
        this.userid = userid;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public long getTimestart() {
        return timestart;
    }

    public void setTimestart(long timestart) {
        this.timestart = timestart;
    }

    public long getTimeend() {
        return timeend;
    }

    public void setTimeend(long timeend) {
        this.timeend = timeend;
    }

    public String getStoryid() {
        return storyid;
    }

    public void setStoryid(String storyid) {
        this.storyid = storyid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
