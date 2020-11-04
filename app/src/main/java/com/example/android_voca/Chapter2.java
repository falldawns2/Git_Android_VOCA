package com.example.android_voca;

import com.google.gson.annotations.SerializedName;

public class Chapter2 {

    //챕터

    @SerializedName("Page_NO")
    int Page_NO;

    @SerializedName("Page_SIZE")
    int Page_SIZE;

    @SerializedName("OrderBy")
    String OrderBy;

    @SerializedName("ChapterName")
    String ChapterName;

    @SerializedName("VocaCount")
    String VocaCount;

    @SerializedName("userid")
    String userid;

    @SerializedName("VocaNoteName")
    String VocaNoteName;

    // 단어장 체크박스 유무 //

    boolean isSelected;

    public Chapter2(int Page_NO, int Page_SIZE, String userid, String VocaNoteName, String orderBy) {
        this.Page_NO = Page_NO;
        this.Page_SIZE = Page_SIZE;
        this.userid = userid;
        this.VocaNoteName = VocaNoteName;
        this.OrderBy = orderBy;
    }

    public Chapter2(String ChapterName, String VocaCount) {
        this.ChapterName = ChapterName;
        this.VocaCount = VocaCount;
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getVocaNoteName() {
        return VocaNoteName;
    }

    public void setVocaNoteName(String vocaNoteName) {
        VocaNoteName = vocaNoteName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
