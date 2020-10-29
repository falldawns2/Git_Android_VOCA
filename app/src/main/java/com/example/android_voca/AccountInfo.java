package com.example.android_voca;

import java.io.Serializable;

public class AccountInfo implements Serializable {

    //Members 테이블
    String Userid; //userid
    String Passwd;
    String Name; //name
    String Nickcname; //nickname

    public AccountInfo()
    {

    }

    public AccountInfo(String Userid, String Name, String Nickcname)
    {
        this.Userid = Userid;
        this.Name = Name;
        this.Nickcname = Nickcname;
    }

    public String getSession_ID() {
        return Userid;
    }

    public void setSession_ID(String userid) {
        Userid = userid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNikcname() {
        return Nickcname;
    }

    public void setNikcname(String nickcname) {
        Nickcname = nickcname;
    }
}
