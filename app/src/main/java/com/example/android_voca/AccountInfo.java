package com.example.android_voca;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AccountInfo { //implements Serializable

    //Members 테이블

    @SerializedName("id")
    private String Userid; //userid

    private String Passwd;
    private String Name; //name

    @SerializedName("NickName")
    private String Nickcname; //nickname

    @SerializedName("ProfileImage")
    private String ProfileImageName; //프로필 이미지 이름

    public AccountInfo(String Userid, String Name, String Nickcname)
    {
        this.Userid = Userid;
        this.Name = Name;
        this.Nickcname = Nickcname;
    }

    //닉네임, 프로필 이미지
    public AccountInfo(String Userid) {
        this.Userid = Userid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getPasswd() {
        return Passwd;
    }

    public void setPasswd(String passwd) {
        Passwd = passwd;
    }

    public String getNickcname() {
        return Nickcname;
    }

    public void setNickcname(String nickcname) {
        Nickcname = nickcname;
    }

    public String getProfileImageName() {
        return ProfileImageName;
    }

    public void setProfileImageName(String profileImageName) {
        ProfileImageName = profileImageName;
    }
}
