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

    @SerializedName("ChapterName")
    String ChapterName;

    @SerializedName("VocaCount")
    String VocaCount;


    //보낼 값
    public VocaNote2(int Page_NO, int Page_SIZE, String userid, String OrderBy) {
        this.Page_NO = Page_NO;
        this.Page_SIZE = Page_SIZE;
        this.userid = userid;
        this.OrderBy = OrderBy;
    }

    //단어장//
    //챕터//
    //받는 값//
    public VocaNote2(int VocaNote_Chapter, String VocaNote_ChapterName, String TotalVoca_VocaCount) {
        if(VocaNote_Chapter == 0) {
            this.VocaNoteName = VocaNote_ChapterName;
            this.TotalVocaCount = TotalVoca_VocaCount;
        } else {
            this.ChapterName = VocaNote_ChapterName;
            this.VocaCount = TotalVoca_VocaCount;
        }
    }
    //받는 값
    public VocaNote2(String VocaNoteName, String NickName, String CrDateNote, String TotalVocaCount) {
        this.VocaNoteName = VocaNoteName;
        this.NickName = NickName;
        this.CrDateNote = CrDateNote;
        this.TotalVocaCount = TotalVocaCount;
    }

    //챕터

    //보낼 값
    public VocaNote2(int Page_No, int Page_Size, String userid, String VocaNoteName, String Orderby) {
        this.Page_NO = Page_No;
        this.Page_SIZE = Page_Size;
        this.userid = userid;
        this.VocaNoteName= VocaNoteName;
        this.OrderBy = Orderby;
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

    public String getChapterName() {
        return ChapterName;
    }

    public void setChapterName(String chapterName) {
        ChapterName = chapterName;
    }

    public String getVocaCount() {
        return VocaCount;
    }

    public void setVocaCount(String vocaCount) {
        VocaCount = vocaCount;
    }
}
