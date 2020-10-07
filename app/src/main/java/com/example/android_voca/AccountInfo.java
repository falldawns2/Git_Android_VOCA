package com.example.android_voca;

import java.io.Serializable;

public class AccountInfo implements Serializable {

    //Members 테이블
    String Session_ID; //userid
    String Name; //name
    String Nickcname; //nickname

    public AccountInfo()
    {

    }

    public AccountInfo(String Session_ID, String Name, String Nickname)
    {
        this.Session_ID = Session_ID;
        this.Name = Name;
        this.Nickcname = Nickname;
    }

    public String getSession_ID() {
        return Session_ID;
    }

    public void setSession_ID(String session_ID) {
        Session_ID = session_ID;
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
