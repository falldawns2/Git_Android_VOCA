package com.example.android_voca;

import com.google.gson.annotations.SerializedName;

public class ADD {
    //단어장 관련 추가 이벤트

    @SerializedName("VocaTitle")
    private String VocaNoteName;
    @SerializedName("Userid")
    private String Userid;
    @SerializedName("Nickname")
    private String Nickname;
    @SerializedName("In_Group") //그룹 단어장인지 아닌지 판단
    private String In_Group;

    private String ChapterName;
    private String VocaName;

    //반환 값 0 : 성공, 1 : 한 글자 이상, 2 : 중복 존재
    private int value;

    public ADD(String VocaNoteName, String Userid, String NickName, String In_Group) {
        this.VocaNoteName = VocaNoteName;
        this.Userid = Userid;
        this.Nickname = NickName;
        this.In_Group = In_Group;
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

    public String getVocaName() {
        return VocaName;
    }

    public void setVocaName(String vocaName) {
        VocaName = vocaName;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getIn_Group() {
        return In_Group;
    }

    public void setIn_Group(String in_Group) {
        In_Group = in_Group;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
