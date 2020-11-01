package com.example.android_voca;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VocaNote {

    // 단어장 테이블 //

    int no; // 단어장 무한 생성 번호 //
    int bbsid; // 게시판 번호 //

    @SerializedName("userid")
    String userid; // 아이디 //

    @SerializedName("NickName")
    String NickName; // 닉네임 //

    @SerializedName("VocaNoteName")
    String VocaNoteName; // 단어장 명 // 전체

    String VocaNoteName_Part; //단어장 명 일부만

    String ChapterName; // 챕터 명 //

    int VocaCount; // 총 단어 수 // // 전체
    int VocaCount_Part; //총 단어 수 일부만

    String CrDateNote; // 단어장 생성 날짜 //
    String CrDateChapter; // 챕터 생성 날짜 //
    boolean Group; // 그룹 단어장인지 아닌지 판별 //

    @SerializedName("Page_NO")
    int Page_NO;
    @SerializedName("Page_SIZE")
    int Page_SIZE;
    @SerializedName("OrderBy")
    String OrderBy;
    @SerializedName("TotalVocaCount")
    String TotalVocaCount; //총 단어 수

    // 단어장 체크박스 유무 //

    boolean isSelected;

    //보낼 값
    public VocaNote(int Page_NO, int Page_SIZE, String userid, String OrderBy) {
        this.Page_NO = Page_NO;
        this.Page_SIZE = Page_SIZE;
        this.userid = userid;
        this.OrderBy = OrderBy;
    }

    //받는 값
    public VocaNote(String VocaNoteName, String NickName, String CrDateNote, String TotalVocaCount) {
        this.VocaNoteName = VocaNoteName;
        this.NickName = NickName;
        this.CrDateNote = CrDateNote;
        this.TotalVocaCount = TotalVocaCount;
    }

    public VocaNote(String VocaNoteName, String TotalVocaCount) {
        this.VocaNoteName = VocaNoteName;
        this.TotalVocaCount = TotalVocaCount;
    }

    public VocaNote(String VocaNoteName) {
        this.VocaNoteName = VocaNoteName;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getBbsid() {
        return bbsid;
    }

    public void setBbsid(int bbsid) {
        this.bbsid = bbsid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return NickName;
    }

    public void setNickname(String nickname) {
        this.NickName = nickname;
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

    public int getVocaCount() {
        return VocaCount;
    }

    public void setVocaCount(int vocaCount) {
        VocaCount = vocaCount;
    }

    public String getCrDateNote() {
        return CrDateNote;
    }

    public void setCrDateNote(String crDateNote) {
        CrDateNote = crDateNote;
    }

    public String getCrDateChapter() {
        return CrDateChapter;
    }

    public void setCrDateChapter(String crDateChapter) {
        CrDateChapter = crDateChapter;
    }

    public boolean isGroup() {
        return Group;
    }

    public void setGroup(boolean group) {
        Group = group;
    }

    public String getVocaNoteName_Part() {
        return VocaNoteName_Part;
    }

    public void setVocaNoteName_Part(String vocaNoteName_Part) {
        VocaNoteName_Part = vocaNoteName_Part;
    }

    public int getVocaCount_Part() {
        return VocaCount_Part;
    }

    public void setVocaCount_Part(int vocaCount_Part) {
        VocaCount_Part = vocaCount_Part;
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

    // 테이블 아님 //

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
