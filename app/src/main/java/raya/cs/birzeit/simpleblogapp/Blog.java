package raya.cs.birzeit.simpleblogapp;

import java.util.Date;

/**
 * Created by Sandy on 3/7/2018.
 */

public class Blog {

    private String title;
    private String desc;
    private String image;
    private   String Type;
    public   String uid;
    private String username;
    public Date timeStamp;

    public Blog(){

    }


    public Blog(String title, String desc, String image, String type, String uid, String username, Date timeStamp) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this. Type = type;
        this.uid = uid;
        this.username = username;
        this.timeStamp = timeStamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {

        return title;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }





    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
