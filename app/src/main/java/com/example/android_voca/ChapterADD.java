package com.example.android_voca;

import com.google.gson.annotations.SerializedName;

public class ChapterADD {

    //챕터 추가 이벤트

    @SerializedName("Userid")
    private String Userid;
    @SerializedName("NickName")
    private String NickName;
    @SerializedName("ChapterName")
    private String ChapterName;
    @SerializedName("VocaNoteName")
    private String VocaNoteName;

    //반환 값 0 : 성공, 1 : 두 글자 이상, 2 : 중복
    @SerializedName("Check")
    private int value;

    public ChapterADD(String userid, String nickName, String chapterName, String vocaNoteName) {
        Userid = userid;
        NickName = nickName;
        ChapterName = chapterName;
        VocaNoteName = vocaNoteName;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getChapterName() {
        return ChapterName;
    }

    public void setChapterName(String chapterName) {
        ChapterName = chapterName;
    }

    public String getVocaNoteName() {
        return VocaNoteName;
    }

    public void setVocaNoteName(String vocaNoteName) {
        VocaNoteName = vocaNoteName;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
