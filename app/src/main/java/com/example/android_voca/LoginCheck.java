package com.example.android_voca;

import com.google.gson.annotations.SerializedName;

public class LoginCheck {
    @SerializedName("id")
    private String id;

    @SerializedName("pwd")
    private String pwd;

    //WCF Service 에서 반환된 결과 값
    @SerializedName("Check")
    private boolean value;

    public LoginCheck(String id, String pwd)
    {
        this.id = id;
        this.pwd = pwd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
