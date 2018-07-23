package com.bytelogs.riopayments;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class UserModel {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String uid;
    private String name;
    private String mobile;
    private String profile_url;


    public UserModel(String uid, String name, String mobile, String profile_url) {
        this.uid = uid;
        this.name = name;
        this.mobile = mobile;
        this.profile_url = profile_url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }
}
