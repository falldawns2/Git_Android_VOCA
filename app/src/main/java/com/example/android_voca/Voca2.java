package com.example.android_voca;

import com.google.gson.annotations.SerializedName;

public class Voca2 {

    // 단어 테이블 //
    @SerializedName("Page_NO")
    int Page_NO;

    @SerializedName("Page_SIZE")
    int Page_SIZE;

    @SerializedName("userid")
    String userid; // 아이디 //

    @SerializedName("VocaNoteName")
    String VocaNoteName; // 단어장 명 //

    @SerializedName("ChapterName")
    String ChapterName; // 챕터 명 //

    @SerializedName("OrderBy")
    String OrderBy;

    // ---------------------------- //

    @SerializedName("Voca")
    String Voca; // 단어 //

    @SerializedName("Mean")
    String Mean; // 뜻 //

    @SerializedName("Sentence")
    String Sentence; // 예문 //

    @SerializedName("Interpretation")
    String Interpretation; // 해석 //

    boolean isComplete;

    public Voca2(int page_NO, int page_SIZE, String userid, String vocaNoteName, String chapterName, String orderBy) {
        Page_NO = page_NO;
        Page_SIZE = page_SIZE;
        this.userid = userid;
        VocaNoteName = vocaNoteName;
        ChapterName = chapterName;
        OrderBy = orderBy;
    }

    public Voca2(String voca, String mean, String sentence, String interpretation) {
        Voca = voca;
        Mean = mean;
        Sentence = sentence;
        Interpretation = interpretation;
    }

    // get set //

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

    public String getChapterName() {
        return ChapterName;
    }

    public void setChapterName(String chapterName) {
        ChapterName = chapterName;
    }

    public String getOrderBy() {
        return OrderBy;
    }

    public void setOrderBy(String orderBy) {
        OrderBy = orderBy;
    }

    public String getVoca() {
        return Voca;
    }

    public void setVoca(String voca) {
        Voca = voca;
    }

    public String getMean() {
        return Mean;
    }

    public void setMean(String mean) {
        Mean = mean;
    }

    public String getSentence() {
        return Sentence;
    }

    public void setSentence(String sentence) {
        Sentence = sentence;
    }

    public String getInterpretation() {
        return Interpretation;
    }

    public void setInterpretation(String interpretation) {
        Interpretation = interpretation;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
