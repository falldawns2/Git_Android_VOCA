package com.example.android_voca;

import com.google.gson.annotations.SerializedName;

public class VocaNote_Chapter_List {

    //스피너 등록을 위한 단어장, 챕터 값을 받는다.

    @SerializedName("userid")
    String userid;
    @SerializedName("OrderBy")
    String OrderBy;

    @SerializedName("VocaNoteName")
    String VocaNoteName;

    public VocaNote_Chapter_List(String userid, String OrderBy) {
        this.userid = userid;
        this.OrderBy = OrderBy;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getOrderBy() {
        return OrderBy;
    }

    public void setOrderBy(String orderBy) {
        OrderBy = orderBy;
    }

    public String getVocaNoteName() {
        return VocaNoteName;
    }

    public void setVocaNoteName(String vocaNoteName) {
        VocaNoteName = vocaNoteName;
    }
}
