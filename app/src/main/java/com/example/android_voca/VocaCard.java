package com.example.android_voca;

import com.google.gson.annotations.SerializedName;

public class VocaCard {
    //도전 단어카드

    @SerializedName("userid")
    private String userid;
    @SerializedName("VocaNoteName")
    private String VocaNoteName;
    @SerializedName("ChapterName")
    private String ChapterName;
    @SerializedName("OrderBy")
    private String OrderBy;


    @SerializedName("Voca")
    private String Voca;
    @SerializedName("Mean")
    private String Mean;
    @SerializedName("Sentence")
    private String Sentence;
    @SerializedName("Interpretation")
    private String Interpretation;

    public VocaCard(String userid, String VocaNoteName, String ChapterName, String OrderBy) {
        this.userid = userid;
        this.VocaNoteName = VocaNoteName;
        this.ChapterName = ChapterName;
        this.OrderBy = OrderBy;
    }

    public VocaCard(String Voca, String Mean, String Sentence, String Interpretation, int a) {
        this.Voca = Voca;
        this.Mean = Mean;
        this.Sentence = Sentence;
        this.Interpretation = Interpretation;
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
}
