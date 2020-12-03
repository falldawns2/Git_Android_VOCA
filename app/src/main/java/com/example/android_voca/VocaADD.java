package com.example.android_voca;

import com.google.gson.annotations.SerializedName;

public class VocaADD {

    //단어 추가 이벤트

    @SerializedName("Userid")
    private String Userid;
    @SerializedName("VocaNoteName")
    private String VocaNoteName;
    @SerializedName("ChapterName")
    private String ChapterName;
    @SerializedName("Voca")
    private String Voca;
    @SerializedName("Mean")
    private String Mean;
    @SerializedName("Sentence")
    private String Sentence;
    @SerializedName("Interpretation")
    private String Interpretation;

    //0 : 성공, 1 : 단어 빈칸, 2 : 뜻 빈칸, 3 : 중복 존재
    @SerializedName("Check")
    private int value;

    //만약 중복 단어인 경우 - > 업데이트 하기 위해 4가지 가져온다.
    @SerializedName("Update_Voca")
    private String Update_Voca;
    @SerializedName("Update_Mean")
    private String Update_Mean;
    @SerializedName("Update_Sentence")
    private String Update_Sentence;
    @SerializedName("Update_Interpretation")
    private String Update_Interpretation;

    public VocaADD(String userid, String vocaNoteName, String chapterName, String voca, String mean, String sentence, String interpretation) {
        Userid = userid;
        VocaNoteName = vocaNoteName;
        ChapterName = chapterName;
        Voca = voca;
        Mean = mean;
        Sentence = sentence;
        Interpretation = interpretation;
    }

    public VocaADD(String userid, String voca, String mean, String sentence, String interpretation) {
        Userid = userid;
        Voca = voca;
        Mean = mean;
        Sentence = sentence;
        Interpretation = interpretation;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getUpdate_Voca() {
        return Update_Voca;
    }

    public void setUpdate_Voca(String update_Voca) {
        Update_Voca = update_Voca;
    }

    public String getUpdate_Mean() {
        return Update_Mean;
    }

    public void setUpdate_Mean(String update_Mean) {
        Update_Mean = update_Mean;
    }

    public String getUpdate_Sentence() {
        return Update_Sentence;
    }

    public void setUpdate_Sentence(String update_Sentence) {
        Update_Sentence = update_Sentence;
    }

    public String getUpdate_Interpretation() {
        return Update_Interpretation;
    }

    public void setUpdate_Interpretation(String update_Interpretation) {
        Update_Interpretation = update_Interpretation;
    }
}
