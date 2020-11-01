package com.example.android_voca;

import com.google.gson.annotations.SerializedName;

public class VocaNote2 {
    @SerializedName("userid")
    String userid; // 아이디 //

    @SerializedName("NickName")
    String NickName; // 닉네임 //

    @SerializedName("VocaNoteName")
    String VocaNoteName; // 단어장 명 // 전체

    @SerializedName("Page_NO")
    int Page_NO;
    @SerializedName("Page_SIZE")
    int Page_SIZE;
    @SerializedName("OrderBy")
    String OrderBy;
    @SerializedName("TotalVocaCount")
    String TotalVocaCount; //총 단어 수

    String CrDateNote; // 단어장 생성 날짜 //


    //보낼 값
    public VocaNote2(int Page_NO, int Page_SIZE, String userid, String OrderBy) {
        this.Page_NO = Page_NO;
        this.Page_SIZE = Page_SIZE;
        this.userid = userid;
        this.OrderBy = OrderBy;
    }

    //받는 값
    public VocaNote2(String VocaNoteName, String NickName, String CrDateNote, String TotalVocaCount) {
        this.VocaNoteName = VocaNoteName;
        this.NickName = NickName;
        this.CrDateNote = CrDateNote;
        this.TotalVocaCount = TotalVocaCount;
    }

    public VocaNote2(String VocaNoteName, String TotalVocaCount) {
        this.VocaNoteName = VocaNoteName;
        this.TotalVocaCount = TotalVocaCount;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getVocaNoteName() {
        return VocaNoteName;
    }

    public void setVocaNoteName(String vocaNoteName) {
        VocaNoteName = vocaNoteName;
    }

    public int getPage_NO() {
        return Page_NO;
    }

    public void setPage_NO(int page_NO) {
        Page_NO = page_NO;
    }

    public int getPage_SIZE() {
        return Page_SIZE;
    }

    public void setPage_SIZE(int page_SIZE) {
        Page_SIZE = page_SIZE;
    }

    public String getOrderBy() {
        return OrderBy;
    }

    public void setOrderBy(String orderBy) {
        OrderBy = orderBy;
    }

    public String getTotalVocaCount() {
        return TotalVocaCount;
    }

    public void setTotalVocaCount(String totalVocaCount) {
        TotalVocaCount = totalVocaCount;
    }

    public String getCrDateNote() {
        return CrDateNote;
    }

    public void setCrDateNote(String crDateNote) {
        CrDateNote = crDateNote;
    }
}
